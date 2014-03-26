package piggene.serialisation.workflow;

public class JoinOperation extends Workflow {
	private String relation;
	private String input;
	private String input2;
	private String options;
	private String options2;
	private String comment;

	public JoinOperation() {
		this.workflowType = WorkflowType.SINGLE_ELEM;
	}

	public JoinOperation(final String relation, final String input, final String input2, final String options,
			final String options2, final String comment) {
		this();
		this.relation = relation;
		this.input = input;
		this.input2 = input2;
		this.options = options;
		this.options2 = options2;
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

	public String getInput2() {
		return input2;
	}

	public void setInput2(final String input2) {
		this.input2 = input2;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(final String options) {
		this.options = options;
	}

	public String getOptions2() {
		return options2;
	}

	public void setOptions2(final String options2) {
		this.options2 = options2;
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
		sb.append("JOIN");
		sb.append(" ");
		sb.append(getInput());
		sb.append(" BY (");
		sb.append(getOptions());
		sb.append("), ");
		sb.append(getInput2());
		sb.append(" BY (");
		sb.append(getOptions2());
		sb.append(')');
		return sb.toString();
	}

}