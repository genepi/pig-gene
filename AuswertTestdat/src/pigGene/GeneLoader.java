/**
 * PigGene - BACHELOR PROJECT
 * 
 * UDF to load the content of 
 * the gene sample file. 
 * 
 * @author: Clemens Banas
 */

package pigGene;

import java.io.IOException;

import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.pig.LoadFunc;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.Tuple;

public class GeneLoader extends LoadFunc {

	@Override
	public InputFormat getInputFormat() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple getNext() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareToRead(RecordReader arg0, PigSplit arg1)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocation(String arg0, Job arg1) throws IOException {
		// TODO Auto-generated method stub
		
	}

}