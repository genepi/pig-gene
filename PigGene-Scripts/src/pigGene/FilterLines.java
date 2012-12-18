/**
 * PigGene - BACHELOR PROJECT
 * 
 * UDF to filter lines above a 
 * specified threshold.
 * 
 * @author: Clemens Banas
 */

package pigGene;

import java.io.IOException;
import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class FilterLines extends FilterFunc {
	private final static int noOfLines = 15;
	private static int countLines = 0;

	@Override
	public Boolean exec(Tuple input) throws IOException {
		if(countLines >= noOfLines) {
			return false;
		}
		countLines++;
		return true;
	}

}