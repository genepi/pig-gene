package piggene.serialisation.workflow;

public class LoadOperation extends Workflow {
	private String relation;
	private String input;
	private String options;
	private String options2;
	private String comment;

	public LoadOperation() {
		this.workflowType = WorkflowType.SINGLE_ELEM;
	}

	public LoadOperation(final String relation, final String input, final String options, final String options2,
			final String comment) {
		super();
		this.relation = relation;
		this.input = input;
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

	// TODO extend to enable different txt-options...
	@Override
	public String getPigScriptRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append(parseComment(getComment()));
		sb.append(getRelation());
		sb.append(EQUAL_SYMBOL);
		sb.append("LOAD");
		sb.append(" '$");
		sb.append(getInput());
		sb.append("' ");

		// TODO
		sb.append("USING");
		sb.append(" ");
		sb.append("pigGene.storage.merged.PigGeneStorage()");
		return sb.toString();
	}

}