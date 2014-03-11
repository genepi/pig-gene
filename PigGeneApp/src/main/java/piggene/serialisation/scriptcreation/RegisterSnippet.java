package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

/**
 * RegisterSnippet class is used to return a "REGISTER" statement based on the
 * specified workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class RegisterSnippet extends PigSnippet {

	public RegisterSnippet(final SingleWorkflowElement comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(parseComment(comp.getComment()));
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getInput());
		return sb.toString();
	}

}