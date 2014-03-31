package piggene.serialisation.workflow;

public class FilterOperation extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_SINGLE_ELEM;

	private String relation;
	private String input;
	private String operation;
	private String options;
	private String comment;

	public FilterOperation() {
	}

	public FilterOperation(final String relation, final String input, final String operation, final String options,
			final String comment) {
		this.relation = relation;
		this.input = input;
		this.operation = operation;
		this.options = options;
		this.comment = comment;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		FilterOperation.workflowType = workflowType;
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

	public String getOptions() {
		return options;
	}

	public void setOptions(final String options) {
		this.options = options;
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
		sb.append(getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append("FILTER");
		sb.append(" ");
		sb.append(getInput());
		sb.append(" BY ");
		sb.append(getOptions());
		return sb.toString();
	}

}