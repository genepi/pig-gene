package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

public class FilterSnippet extends PigSnippet {

	public FilterSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(comp.getName());
		sb.append(EQUAL_SYMBOL);
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getRelation());
		sb.append(" BY ");
		sb.append(comp.getOptions());
		return sb.toString();
	}

}
