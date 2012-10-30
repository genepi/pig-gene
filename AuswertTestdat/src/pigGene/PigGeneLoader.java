package pigGene;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.pig.Expression;
import org.apache.pig.LoadFunc;
import org.apache.pig.LoadMetadata;
import org.apache.pig.ResourceSchema;
import org.apache.pig.ResourceSchema.ResourceFieldSchema;
import org.apache.pig.ResourceStatistics;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.builtin.PigStorage;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.util.UDFContext;
import org.apache.pig.impl.util.Utils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class PigGeneLoader extends LoadFunc implements LoadMetadata {
	private final Log log = LogFactory.getLog(getClass());
	private RecordReader reader = null;
	private ResourceFieldSchema[] fields = null;
	private String udfcSignature = null;

	public InputFormat getInputFormat() throws IOException {
	    return new TextInputFormat(); //default Hadoop input format
	}

    public void setLocation(String location, Job job) throws IOException {
        FileInputFormat.setInputPaths(job, location);
    }
    
	public ResourceSchema getSchema(String location, Job job) throws IOException {
	    // Open the schema file and read the schema
	    // Get an HDFS handle.
	    FileSystem fs = FileSystem.get(job.getConfiguration());
	    DataInputStream in = fs.open(new Path(location + "/_schema"));
	    String line = in.readLine();
	    in.close();
	
	    // Parse the schema
	    Schema s = Utils.getSchemaFromString(line);
	    if (s == null) {
	    	throw new IOException("Unable to parse schema found in file " + location + "/_schema");
	    }
	    ResourceSchema schema = new ResourceSchema(s);
	
	    // Now that we have determined the schema, store it in our
	    // UDFContext properties object so we have it when we need it (backend)
	    UDFContext udfc = UDFContext.getUDFContext();
	    Properties p = udfc.getUDFProperties(this.getClass(), new String[]{udfcSignature});
	    p.setProperty("pig.Geneloader.schema", line);
	    return schema;
	}

	public void setUDFContextSignature(String signature) {
	    udfcSignature = signature;
	}

    public void prepareToRead(RecordReader reader, PigSplit split) throws IOException {
        this.reader = reader;
        UDFContext udfc = UDFContext.getUDFContext();
        Properties p = udfc.getUDFProperties(this.getClass(), new String[]{udfcSignature});
        String strSchema = p.getProperty("pig.Geneloader.schema"); 
        if (strSchema == null) {
            throw new IOException("Could not find schema in UDF context");
        }

        // Parse the schema from the string stored in the properties object.
        ResourceSchema schema = new ResourceSchema(Utils.getSchemaFromString(strSchema));
        fields = schema.getFields();
    }
    
    public Tuple getNext() throws IOException {
        Text val = null;
        Text key = null;
        try {
            // Read the next key value pair from the record reader.  
        	// If it's finished, return null
            if (!reader.nextKeyValue()) {
            	return null;
            }

            key = (Text)reader.getCurrentKey();
            val = (Text)reader.getCurrentValue();
        } catch (InterruptedException ie) {
            throw new IOException(ie);
        }

        // Create a parser specific for this input line. This may not be the
        // most efficient approach.
        ByteArrayInputStream bais = new ByteArrayInputStream(val.getBytes());
        JsonParser p = jsonFactory.createJsonParser(bais);

        // Create the tuple we will be returning.  We create it with the right
        // number of fields, as the Tuple object is optimized for this case.
        Tuple t = tupleFactory.newTuple(fields.length);

        // Read the start object marker.  Throughout this file if the parsing
        // isn't what we expect we return a tuple with null fields rather than
        // throwing an exception.  That way a few mangled lines don't fail the
        // job.
        if (p.nextToken() != JsonToken.START_OBJECT) {
            log.warn("Bad record, could not find start of record " +
                val.toString());
            return t;
        }

        // Read each field in the record
        for (int i = 0; i < fields.length; i++) {
            t.set(i, readField(p, fields[i], i));
        }

        if (p.nextToken() != JsonToken.END_OBJECT) {
            log.warn("Bad record, could not find end of record " +
                val.toString());
            return t;
        }
        p.close();
        return t;
    }
    
    private Object readField(JsonParser p, ResourceFieldSchema field, int fieldnum) throws IOException {
		// Read the next token
		JsonToken tok = p.nextToken();
		if (tok == null) {
			log.warn("Early termination of record, expected " + fields.length + " fields bug found " + fieldnum);
			return null;
		}
		
		// Check to see if this value was null
		if (tok == JsonToken.VALUE_NULL) return null;
		
		// Read based on our expected type
		switch (field.getType()) {
			case DataType.INTEGER:
				// Read the field name
				p.nextToken();
				return p.getValueAsInt();

			case DataType.LONG:
				p.nextToken();
				return p.getValueAsLong();
		
			case DataType.FLOAT:
				p.nextToken();
				return (float)p.getValueAsDouble();
		
			case DataType.DOUBLE:
				p.nextToken();
				return p.getValueAsDouble();
		
			case DataType.BYTEARRAY:
				p.nextToken();
				byte[] b = p.getBinaryValue();
				// Use the DBA constructor that copies the bytes so that we own
				// the memory
				return new DataByteArray(b, 0, b.length);
		
			case DataType.CHARARRAY:
				p.nextToken();
				return p.getText();
			
			case DataType.MAP:
				// Should be a start of the map object
				if (p.nextToken() != JsonToken.START_OBJECT) {
					log.warn("Bad map field, could not find start of object, field " + fieldnum);
					return null;
				}
				Map<String, String> m = new HashMap<String, String>();
				while (p.nextToken() != JsonToken.END_OBJECT) {
					String k = p.getCurrentName();
					String v = p.getText();
					m.put(k, v);
				}
				return m;
		
			case DataType.TUPLE:
				if (p.nextToken() != JsonToken.START_OBJECT) {
					log.warn("Bad tuple field, could not find start of object, " + "field " + fieldnum);
					return null;
				}
				ResourceSchema s = field.getSchema();
				ResourceFieldSchema[] fs = s.getFields();
				Tuple t = tupleFactory.newTuple(fs.length);
				for (int j = 0; j < fs.length; j++) {
					t.set(j, readField(p, fs[j], j));
				}
				if (p.nextToken() != JsonToken.END_OBJECT) {
					log.warn("Bad tuple field, could not find end of object, " + "field " + fieldnum);
					return null;
				}
				return t;
		
			case DataType.BAG:
				if (p.nextToken() != JsonToken.START_ARRAY) {
					log.warn("Bad bag field, could not find start of array, " + "field " + fieldnum);
					return null;
				}
				s = field.getSchema();
				fs = s.getFields();
				// Drill down the next level to the tuple's schema.
				s = fs[0].getSchema();
				fs = s.getFields();
				DataBag bag = bagFactory.newDefaultBag();
				JsonToken innerTok;
				while ((innerTok = p.nextToken()) != JsonToken.END_ARRAY) {
					if (innerTok != JsonToken.START_OBJECT) {
					   log.warn("Bad bag tuple field, could not find start of " + "object, field " + fieldnum);
					   return null;
				}
				t = tupleFactory.newTuple(fs.length);
				for (int j = 0; j < fs.length; j++) {
				   t.set(j, readField(p, fs[j], j));
				}
				if (p.nextToken() != JsonToken.END_OBJECT) {
				   log.warn("Bad bag tuple field, could not find end of "
				   + "object, field " + fieldnum);
				   return null;
				}
				bag.add(t);
				}
				return bag;
		
			default:
				throw new IOException("Unknown type in input schema: " + field.getType());
		}
    }
    
    public ResourceStatistics getStatistics(String location, Job job) throws IOException {
        return null; // this method is unimplemented...
    }
    

    public String[] getPartitionKeys(String location, Job job) throws IOException {
        return null; // this method is unimplemented...
    }
    
    public void setPartitionFilter(Expression partitionFilter) throws IOException {
    	// this method is unimplemented...
    }

}