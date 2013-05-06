package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

/**
 * JoinSnippet class is used to return a "JOIN" statement based on the specified
 * workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class JoinSnippet extends PigSnippet {

	public JoinSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		final String comment = comp.getComment();
		if (!comment.equals("-")) {
			sb.append(System.getProperty("line.separator"));
			sb.append("--");
			sb.append(comment);
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(comp.getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append(comp.getOperation());
		sb.append(" ");
		sb.append(comp.getInput());
		sb.append(" BY (");
		sb.append(comp.getOptions());
		sb.append("), ");
		sb.append(comp.getInput2());
		sb.append(" BY (");
		sb.append(comp.getOptions2());
		sb.append(')');
		return sb.toString();
	}

}
