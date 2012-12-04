package pigGene;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class ExtractGeneInfo extends EvalFunc<String> {
	private String geneinfo = "GENEINFO=";
	private int offset = geneinfo.length();

	@Override
	public String exec(Tuple input) throws IOException {
		if (input == null || input.size() != 1) {
			return "given tuple was null";
		}

		String infoCol = (String) input.get(0);
		if (infoCol == null) {
			return "could not read given tuple";
		}

		int startOffset = infoCol.indexOf(geneinfo) + offset;
		if (startOffset < 0) {
			return "GENEINFO= could was not found";
		}

		int endOffset = infoCol.indexOf(";", startOffset);

		if (endOffset < 0) {
			return "index problem: end < 0";
		}

		if (startOffset > endOffset) {
			return "index problem: start > end";
		}

		return infoCol.substring(startOffset, endOffset);
	}

}