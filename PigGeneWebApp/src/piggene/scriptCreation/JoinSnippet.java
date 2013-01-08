package piggene.scriptCreation;

import piggene.serialisation.WorkflowComponent;

public class JoinSnippet extends PigSnippet {

	public JoinSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	// TODO functionality must be extended...
	// TODO null-check and abort process?
	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(comp.getRelName());
		sb.append(EQUAL_SYMBOL);
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getRelation());
		sb.append(" BY (");
		sb.append(comp.getOptions());
		sb.append("), ");
		sb.append(comp.getRelation2());
		sb.append(" BY (");
		sb.append(comp.getOptions2());
		sb.append(')');
		return sb.toString();
	}

}
