package piggene.serialisation.workflow;

public class RegisterOperation extends Workflow {
	private String input;
	private String comment;

	public RegisterOperation() {
		this.workflowType = WorkflowType.SINGLE_ELEM;
	}

	public RegisterOperation(final String input, final String comment) {
		this();
		this.input = input;
		this.comment = comment;
	}

	public String getInput() {
		return input;
	}

	public void setInput(final String input) {
		this.input = input;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@Override
	public String getPigScriptRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append(parseComment(getComment()));
		sb.append("REGISTER");
		sb.append(" ");
		sb.append(getInput());
		return sb.toString();
	}

}
