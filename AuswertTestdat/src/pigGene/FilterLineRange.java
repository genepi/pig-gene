package pigGene;

import java.io.IOException;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class FilterLineRange extends FilterFunc {
	private static int countLines = 0;

	@Override
	public Boolean exec(Tuple input) throws IOException {
		if(input == null || input.size() != 2) {
			return false;
		}
		int start = (Integer)input.get(0);
		int end = (Integer)input.get(1);
		countLines++;
		return (countLines >= start && countLines <= end);
	}

}