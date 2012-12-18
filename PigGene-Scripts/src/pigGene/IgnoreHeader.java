/**
 * PigGene - BACHELOR PROJECT
 * 
 * UDF to remove unwanted header information 
 * in the pig relation to speed up execution.
 * 
 * @author: Clemens Banas
 */

package pigGene;

import java.io.IOException;
import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class IgnoreHeader extends FilterFunc {

	@Override
	public Boolean exec(Tuple input) throws IOException {
		if(input == null || input.size() == 0) {
			return false;
		}
		String value = (String) input.get(0);
		String headerSymbol = "#";
		return !(value.substring(0, 1).equals(headerSymbol)); //filter header information
	}

}