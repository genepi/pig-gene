package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

/**
 * StoreSnippet class is used to return a "STORE" statement based on the
 * specified workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class StoreSnippet extends PigSnippet {

	public StoreSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		final String comment = comp.getComment();
		if (!comment.equals("-")) {
			sb.append(System.getProperty("line.separator"));
			sb.append("//");
			sb.append(comment);
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getInput());
		sb.append(" INTO '$");
		sb.append(comp.getRelation());
		sb.append("'");
		return sb.toString();
	}

}