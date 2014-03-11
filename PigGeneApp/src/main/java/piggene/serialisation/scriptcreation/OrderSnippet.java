package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

public class OrderSnippet extends PigSnippet {

	public OrderSnippet(SingleWorkflowElement comp) {
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