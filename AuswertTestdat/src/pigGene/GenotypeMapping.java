package pigGene;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class GenotypeMapping extends EvalFunc<String> {
	private final String oneOne = "1/1";
	private final String zeroOne = "0/1";
	private final String oneZero = "1/0";
	private final String zeroZero = "0/0";

	@Override
	public String exec(Tuple input) throws IOException {
		if (input == null || input.size() != 3) {
			return "error";
		}

		String ref = (String) input.get(0);
		String alt = (String) input.get(1);
		String genotype = (String) input.get(2);

		if (genotype.equals(oneOne)) {
			return alt.concat(alt);
		}

		if (genotype.equals(zeroOne)) {
			return ref.concat(alt);
		}

		if (genotype.equals(oneZero)) {
			return ref.concat(alt);
		}

		if (genotype.equals(zeroZero)) {
			return ref.concat(ref);
		}

		return "error";
	}
}