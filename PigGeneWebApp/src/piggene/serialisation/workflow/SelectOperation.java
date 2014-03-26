package piggene.serialisation.workflow;

public class SelectOperation extends Workflow {
	private String relation;
	private String input;
	private String options;
	private String comment;

	public SelectOperation() {
		this.workflowType = WorkflowType.SINGLE_ELEM;
	}

	public SelectOperation(final String relation, final String input, final String options, final String comment) {
		super();
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
		sb.append("FOREACH ");
		sb.append(getInput());
		sb.append(" GENERATE ");
		sb.append(getOptions());
		return sb.toString();
	}

}