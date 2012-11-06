package pigGene;

import java.io.IOException;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class FilterRange extends FilterFunc {

	@Override
	public Boolean exec(Tuple input) throws IOException {
		if (input == null || input.size() != 4) {
			return false;
		}
		int position = (Integer) input.get(0);
		int accuracy = (Integer) input.get(1);

		int start = ((Integer) input.get(2)) - accuracy;
		int end = ((Integer) input.get(3)) + accuracy;
		return (position >= start && position <= end);
	}

}