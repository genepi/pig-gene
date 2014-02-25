package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

/**
 * PigSnippet class is used as an abstract definition for the different possible
 * pig snippets.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public abstract class PigSnippet implements IPigSnippet {
	protected static final String EQUAL_SYMBOL = " = ";
	protected SingleWorkflowElement comp;

	public PigSnippet(final SingleWorkflowElement comp) {
		this.comp = comp;
	}

	protected String parseComment(String comment) {
		final StringBuilder sb = new StringBuilder();
		if (!comment.equals("-")) {
			sb.append("--");
			sb.append(comment);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	@Override
	public abstract String toPigScript();

}