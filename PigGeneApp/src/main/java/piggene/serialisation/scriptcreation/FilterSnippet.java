package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

/**
 * FilterSnippet class is used to return a "FILTER" statement based on the
 * specified workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class FilterSnippet extends PigSnippet {

	public FilterSnippet(final SingleWorkflowElement comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(parseComment(comp.getComment()));
		sb.append(comp.getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getInput());
		sb.append(" BY ");
		sb.append(comp.getOptions());
		return sb.toString();
	}

}