package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

public class RegisterSnippet extends PigSnippet {

	public RegisterSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getRelation());
		return sb.toString();
	}

}