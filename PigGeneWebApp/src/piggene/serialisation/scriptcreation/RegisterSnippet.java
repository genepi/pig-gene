package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

public class RegisterSnippet extends PigSnippet {

	public RegisterSnippet(final WorkflowComponent comp) {
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
		return sb.toString();
	}

}