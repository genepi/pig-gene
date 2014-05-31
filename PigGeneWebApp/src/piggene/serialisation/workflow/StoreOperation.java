package piggene.serialisation.workflow;

import java.util.Set;

public class StoreOperation extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_SINGLE_ELEM;

	private String relation;
	private String input;
	private String operation;
	private String comment;

	public StoreOperation() {
	}

	public StoreOperation(final String relation, final String input, final String operation, final String comment) {
		this.relation = relation;
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
		StoreOperation.workflowType = workflowType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
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

	// TODO extend to different storage possibilities...
	@Override
	public String getPigScriptRepresentation(final Set<Workflow> parentWfs) {
		StringBuilder sb = new StringBuilder();
		sb.append(parseInformation(getComment()));
		sb.append("STORE");
		sb.append(" ");
		sb.append(getInput());
		sb.append(" INTO '$");
		sb.append(getRelation());
		sb.append("'");
		sb.append(";");
		return sb.toString();
	}

}