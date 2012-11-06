package pigGene;

import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.pig.builtin.PigStorage;

public class PigGeneStorage extends PigStorage {

	@SuppressWarnings("rawtypes")
	@Override
	public InputFormat getInputFormat() {
		return new PigGeneInputFormat();
	}

}