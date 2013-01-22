package piggene.serialisation;

import piggene.serialisation.scriptcreation.PigSnippet;

public class StoreSnippet extends PigSnippet {

	public StoreSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getRelation());
		sb.append(" INTO '$");
		sb.append(comp.getName());
		sb.append("'");
		return sb.toString();
	}

}
