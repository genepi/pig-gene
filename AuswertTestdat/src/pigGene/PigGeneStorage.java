package pigGene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.pig.ResourceSchema;
import org.apache.pig.builtin.PigStorage;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;
import org.apache.pig.impl.util.UDFContext;

public class PigGeneStorage extends PigStorage {
	static ResourceSchema schema;

	static {
		ArrayList<FieldSchema> fieldSchemaList = new ArrayList<FieldSchema>();
		fieldSchemaList.add(new FieldSchema("chrom", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("pos", org.apache.pig.data.DataType.LONG));
		fieldSchemaList.add(new FieldSchema("id", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("ref", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("alt", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("qual", org.apache.pig.data.DataType.DOUBLE));
		fieldSchemaList.add(new FieldSchema("filter", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("info", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("format", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("genotype", org.apache.pig.data.DataType.CHARARRAY));
		fieldSchemaList.add(new FieldSchema("persID", org.apache.pig.data.DataType.INTEGER));
		schema = new ResourceSchema(new Schema(fieldSchemaList));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public InputFormat getInputFormat() {
		return new PigGeneInputFormat();
	}

	@Override
	public ResourceSchema getSchema(final String location, final Job job) throws IOException {
		Properties p = UDFContext.getUDFContext().getUDFProperties(this.getClass(), new String[] { signature });
		p.setProperty(signature + ".schema", schema.toString());
		return schema;
	}

}