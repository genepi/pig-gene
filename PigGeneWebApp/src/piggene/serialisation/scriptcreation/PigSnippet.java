package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

public abstract class PigSnippet implements IPigSnippet {
	protected static final String EQUAL_SYMBOL = " = ";
	protected WorkflowComponent comp;

	public PigSnippet(final WorkflowComponent comp) {
		this.comp = comp;
	}

	@Override
	public abstract String toPigScript();

}