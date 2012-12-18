package pigGene;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class ExtractGeneInfo extends EvalFunc<String> {
	private final String geneinfo = "GENEINFO=";
	private final int offset = geneinfo.length();

	@Override
	public String exec(final Tuple input) throws IOException {
		if (input == null || input.size() != 1) {
			return "the given tuple was null";
		}

		final String infoCol = (String) input.get(0);
		if (infoCol == null) {
			return "could not read given tuple";
		}

		final int startOffset = infoCol.indexOf(geneinfo) + offset;
		if (startOffset < 0) {
			return "GENEINFO= could was not found";
		}

		final int endOffset = infoCol.indexOf(";", startOffset);

		if (endOffset < 0) {
			return "index problem: end < 0";
		}

		if (startOffset > endOffset) {
			return "index problem: start > end";
		}

		return infoCol.substring(startOffset, endOffset);
	}

}