package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

/**
 * SelectSnippet class is used to return a "FOREACH - GENERATE" statement based
 * on the specified workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class SelectSnippet extends PigSnippet {

	public SelectSnippet(final SingleWorkflowElement comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(parseComment(comp.getComment()));
		sb.append(comp.getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append("FOREACH ");
		sb.append(comp.getInput());
		sb.append(" GENERATE ");
		sb.append(comp.getOptions());
		return sb.toString();
	}

}