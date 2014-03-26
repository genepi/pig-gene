package piggene.serialisation.workflow;

public class OrderByOperation extends Workflow {
	private String relation;
	private String input;
	private String options;
	private String comment;

	public OrderByOperation() {
		this.workflowType = WorkflowType.SINGLE_ELEM;
	}

	public OrderByOperation(final String relation, final String input, final String options, final String comment) {
		this();
		this.relation = relation;
		this.input = input;
		this.options = options;
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
		sb.append("ORDER");
		sb.append(" ");
		sb.append(getInput());
		sb.append(" BY ");
		sb.append(getOptions());
		return sb.toString();
	}

}