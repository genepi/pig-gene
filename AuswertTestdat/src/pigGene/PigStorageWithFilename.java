/**
 * PigGene - BACHELOR PROJECT
 * 
 * UDF extension of the PigStorage functionality that
 * adds the filename as last parameter to the tuple. 
 * Needed to be able to find out to which file each
 * line in a relation corresponds when multiple files
 * are loaded from the same directory.
 * 
 * @author: Clemens Banas
 */

package pigGene;

import java.io.IOException;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.builtin.PigStorage;
import org.apache.pig.data.Tuple;

public class PigStorageWithFilename extends PigStorage {
	private String filename = null;
	
	public PigStorageWithFilename() {
		super();
	}

	@Override
	public void prepareToRead(RecordReader reader, PigSplit split) {
		super.prepareToRead(reader, split);
		String path = ((FileSplit)split.getWrappedSplit()).getPath().toString();
		int offset = path.lastIndexOf('/');
		this.filename = path.substring(offset+1, path.length());
	}

	@Override
	public Tuple getNext() throws IOException {
		Tuple myTuple = super.getNext();
		if (myTuple != null) {
			myTuple.append(filename);
		}
		return myTuple;
	}

}