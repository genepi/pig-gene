/**
 * PigGene - BACHELOR PROJECT
 * 
 * UDF to compare two filenames and find
 * out which filename comes lexicographically
 * first. 
 * 
 * @author: Clemens Banas
 */

package pigGene;

import java.io.IOException;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class CompareFilenames extends FilterFunc {

	@Override
	public Boolean exec(Tuple input) throws IOException {
		if(input == null || input.size() != 2) {
			return false;
		}
		String a = (String)input.get(0);
		String b = (String)input.get(1);
		return (a.compareTo(b) < 0);
	}

}