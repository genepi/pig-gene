package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

public class GroupSnippet extends PigSnippet {

	public GroupSnippet(SingleWorkflowElement comp) {
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
		sb.append(" BY ");
		sb.append(comp.getOptions());
		return sb.toString();
	}

}