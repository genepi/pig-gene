package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

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

	private static final String FASTQ_STORAGE = "fi.aalto.seqpig.io.FastqLoader()";
	private static final String BAM_STORAGE = "fi.aalto.seqpig.io.BamLoader";
	private static final String SAM_STORAGE = "fi.aalto.seqpig.io.SamLoader";

	private static final String TXT_STORAGE_TAB_SEPARATOR = "('\\t')";
	private static final String TXT_STORAGE_SPACE_SEPARATOR = "(' ')";
	private static final String TXT_STORAGE_COMMA_SEPARATOR = "(',')";

	public LoadSnippet(final SingleWorkflowElement comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(parseComment(comp.getComment()));
		sb.append(comp.getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append(comp.getOperation());
		sb.append(" '$");
		sb.append(comp.getInput());
		sb.append("' ");
		sb.append("USING ");
		String options = comp.getOptions();
		String options2 = comp.getOptions2();

		if (options.equals("fastQ")) {
			sb.append(FASTQ_STORAGE);
		} else if (options.equals("bam")) {
			sb.append(BAM_STORAGE);
			sb.append("('");
			sb.append(comp.getOptions2());
			sb.append("')");
		} else if (options.equals("sam")) {
			sb.append(SAM_STORAGE);
			sb.append("('");
			sb.append(comp.getOptions2());
			sb.append("')");
		} else if (options.equals("vcf")) {
			if (options2.equals("ref")) {
				sb.append(VCF_STORAGE_REF);
			} else {
				sb.append(VCF_STORAGE);
			}
		} else { // txt
			sb.append(TXT_STORAGE);
			if (options2.equals("tab")) {
				sb.append(TXT_STORAGE_TAB_SEPARATOR);
			} else if (options2.equals("space")) {
				sb.append(TXT_STORAGE_SPACE_SEPARATOR);
			} else { // comma
				sb.append(TXT_STORAGE_COMMA_SEPARATOR);
			}
			if (!comp.getInput2().equals("-")) {
				sb.append(" AS ");
				sb.append(comp.getInput2());
			}
		}
		return sb.toString();
	}

}