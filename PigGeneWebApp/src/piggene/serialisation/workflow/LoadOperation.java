package piggene.serialisation.workflow;

import piggene.serialisation.pig.DynamicInputParameterMapper;
import piggene.serialisation.pig.MissingParameterException;

public class LoadOperation extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_SINGLE_ELEM;

	private String relation;
	private String input;
	private String operation;
	private String options;
	private String options2;
	private String comment;

	public LoadOperation() {
	}

	public LoadOperation(final String relation, final String input, final String operation, final String options, final String options2,
			final String comment) {
		this.relation = relation;
		this.input = input;
		this.operation = operation;
		this.options = options;
		this.options2 = options2;
		this.comment = comment;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		LoadOperation.workflowType = workflowType;
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
	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) throws MissingParameterException {
		String mappedInputValue = DynamicInputParameterMapper.getMappedValue(wfName, input);

		StringBuilder sb = new StringBuilder();
		sb.append(parseInfo(getComment()));
		sb.append(removeLeadingDollarSign(getRelation()));
		addOutputParamToIndex(getRelation(), wfName);
		sb.append(renameParameters(renameParam, wfName));
		sb.append(EQUAL_SYMBOL);
		if (mappedInputValue != null) {
			sb.append(mappedInputValue);
		} else if (!getInput().startsWith("$")) {
			sb.append("LOAD");
			sb.append(" '$");
			sb.append(removeLeadingDollarSign(getInput()));
			sb.append("' ");
			sb.append("USING");
			sb.append(" ");
			sb.append("pigGene.storage.merged.PigGeneStorage()");
		} else {
			throw new MissingParameterException("ERROR: some input parameter values are not defined correctly");
		}
		sb.append(";");
		return sb.toString();
	}

}