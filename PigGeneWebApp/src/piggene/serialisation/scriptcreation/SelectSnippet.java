package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

/**
 * SelectSnippet class is used to return a "FOREACH - GENERATE" statement based
 * on the specified workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class SelectSnippet extends PigSnippet {

	public SelectSnippet(final WorkflowComponent comp) {
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
		sb.append("FOREACH ");
		sb.append(comp.getInput());
		sb.append(" GENERATE ");
		sb.append(comp.getOptions());
		return sb.toString();
	}

}