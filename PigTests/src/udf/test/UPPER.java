package udf.test;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;

public class UPPER extends EvalFunc<String> {

	public String exec(Tuple input) {
		if(input == null || input.size() == 0) {
			return null;
		}
		String str = null;
		try {
			str = (String) input.get(0);
		} catch (ExecException e) {
			e.printStackTrace();
		}
		return str.toUpperCase();
	}
}