package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

/**
 * LoadSnippet class is used to return a "LOAD" statement based on the specified
 * workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class LoadSnippet extends PigSnippet {
	private static final String TXT_STORAGE = "PigStorage";
	private static final String VCF_STORAGE = "pigGene.storage.merged.PigGeneStorage()";
	private static final String VCF_STORAGE_REF = "pigGene.storage.reference.PigGeneStorageReferenceFile()";
	private static final String BAM_STORAGE = "fi.aalto.seqpig.BamUDFLoader('yes')";
	private static final String FASTQ_STORAGE = "fi.aalto.seqpig.FastqUDFLoader()";

	private static final String TXT_STORAGE_TAB_SEPARATOR = "('\\t')";
	private static final String TXT_STORAGE_SPACE_SEPARATOR = "(' ')";
	private static final String TXT_STORAGE_COMMA_SEPARATOR = "(',')";

	public LoadSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		final String comment = comp.getComment();
		if (!comment.equals("-")) {
			sb.append(System.getProperty("line.separator"));
			sb.append("--");
			sb.append(comment);
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(comp.getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append(comp.getOperation());
		sb.append(" '$");
		sb.append(comp.getInput());
		sb.append("' ");
		sb.append("USING ");
		if (comp.getOptions().equals("txt")) {
			sb.append(TXT_STORAGE);
			if (comp.getOptions2().equals("tab")) {
				sb.append(TXT_STORAGE_TAB_SEPARATOR);
			} else if (comp.getOptions2().equals("space")) {
				sb.append(TXT_STORAGE_SPACE_SEPARATOR);
			} else {
				sb.append(TXT_STORAGE_COMMA_SEPARATOR);
			}
			if (!comp.getInput2().equals("-")) {
				sb.append(" AS ");
				sb.append(comp.getInput2());
			}
		} else {
			if (comp.getOptions2().equals("ref")) {
				sb.append(VCF_STORAGE_REF);
			} else if (comp.getOptions2().equals("seqpig")) {
				// sb.append(BAM_STORAGE);
				sb.append(FASTQ_STORAGE);
			} else {
				sb.append(VCF_STORAGE);
			}
		}
		return sb.toString();
	}

}