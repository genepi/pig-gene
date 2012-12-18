/**
 * PigGene - BACHELOR PROJECT
 * 
 * UDF to filter all lines  
 * containing an rs-Number.
 * 
 * @author: Clemens Banas
 */


package pigGene;

import java.io.IOException;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;



public class RemoveRsNumber extends FilterFunc {
	private static String rsNumber = "rs";

	@Override
	public Boolean exec(Tuple input) throws IOException {
		if(input == null || input.size() == 0) {
			return false;
		}
		String rs;
		try {
		rs = ((String)input.get(0)).substring(0, 2);
		} catch(IndexOutOfBoundsException e) {
			return true;
		}
		return !rsNumber.equals(rs);
	}

}