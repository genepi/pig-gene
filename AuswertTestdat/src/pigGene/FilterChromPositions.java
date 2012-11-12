package pigGene;

import java.io.IOException;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class FilterChromPositions extends FilterFunc {

	@Override
	public Boolean exec(final Tuple input) throws IOException {
		if (input == null || input.size() != 6) {
			return false;
		}

		final String chrom = (String) input.get(0);
		final String chromSelected = (String) input.get(1);
		if (!chrom.equals(chromSelected)) {
			return false;
		}

		final long pos = (Long) input.get(2);
		final long accuracy = (Long) input.get(5);
		final long start = (Long) input.get(3) - accuracy;
		final long end = (Long) input.get(4) + accuracy;

		return (pos >= start && pos <= end);
	}
}
