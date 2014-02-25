package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

/**
 * StoreSnippet class is used to return a "STORE" statement based on the
 * specified workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class StoreSnippet extends PigSnippet {
	private static final String FASTQ_STORAGE = "fi.aalto.seqpig.io.FastqStorer()";
	private static final String BAM_STORAGE = "fi.aalto.seqpig.io.BamStorer('input.bam.asciiheader')";
	private static final String SAM_STORAGE = "fi.aalto.seqpig.io.SamStorer('input.sam.asciiheader')";

	public StoreSnippet(final SingleWorkflowElement comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(parseComment(comp.getComment()));
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getInput());
		sb.append(" INTO '$");
		sb.append(comp.getRelation());
		sb.append("'");

		String storage = comp.getOptions();
		if (storage.equals("-")) {
			// nothing to do
		} else if (storage.equals("fastQ")) {
			sb.append(" USING ");
			sb.append(FASTQ_STORAGE);
		} else if (storage.equals("bam")) {
			sb.append(" USING ");
			sb.append(BAM_STORAGE);
		} else if (storage.equals("sam")) {
			sb.append(" USING ");
			sb.append(SAM_STORAGE);
		}

		return sb.toString();
	}

}
