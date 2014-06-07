package piggene.serialisation.workflow;

public class RegisterOperation extends Workflow implements IWorkflowOperation {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_SINGLE_ELEM;

	private String input;
	private String operation;
	private String comment;

	public RegisterOperation() {
	}

	public RegisterOperation(final String input, final String operation, final String comment) {
		this.input = input;
		this.operation = operation;
		this.comment = comment;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		RegisterOperation.workflowType = workflowType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
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
	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) {
		StringBuilder sb = new StringBuilder();
		sb.append(parseInfo(getComment()));
		sb.append("REGISTER");
		sb.append(" ");
		sb.append(getInput());
		sb.append(";");
		return sb.toString();
	}

	@Override
	public String renameParameters(final boolean renameParam, final String wfName) {
		if (renameParam) {
			return "_" + wfName;
		}
		return "";
	}

}
