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

	@Override
	public abstract String toPigScript();

}