package pigGene;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class ExtractGeneInfo extends EvalFunc<String> {

	@Override
	public String exec(Tuple input) throws IOException {
		if (input == null || input.size() != 1) {
			return "given tuple was null";
		}

		String infoCol = (String) input.get(0);
		if (infoCol == null) {
			return "null";
		}

		int startOffset = infoCol.indexOf("GENEINFO=") + 9;
		if (startOffset < 0) {
			return "geneinfo missing";
		}

		int endOffset = infoCol.indexOf(";", startOffset);
		// if (endOffset < 0) { // GENEINFO is last info in the string
		// endOffset = infoCol.length();
		// }

		if (endOffset < 0) {
			return "end < 0 !!!!!";
		}

		if (startOffset > endOffset) {
			return "start > end";
		}

		return infoCol.substring(startOffset, endOffset);
	}

}