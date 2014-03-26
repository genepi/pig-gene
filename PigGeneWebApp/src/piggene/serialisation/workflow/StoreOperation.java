package piggene.serialisation.workflow;

public class StoreOperation extends Workflow {
	private String relation;
	private String input;
	private String comment;

	public StoreOperation() {
		this.workflowType = WorkflowType.SINGLE_ELEM;
	}

	public StoreOperation(final String relation, final String input, final String comment) {
		this();
		this.relation = relation;
		this.input = input;
		this.comment = comment;
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
	public String getPigScriptRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append(parseComment(getComment()));
		sb.append("STORE");
		sb.append(" ");
		sb.append(getInput());
		sb.append(" INTO '$");
		sb.append(getRelation());
		sb.append("'");
		return sb.toString();
	}

}