package piggene.serialisation.workflow;

import piggene.serialisation.pig.DynamicInputParameterMapper;
import piggene.serialisation.pig.MissingParameterException;

public class JoinOperation extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_SINGLE_ELEM;

	private String relation;
	private String input;
	private String operation;
	private String input2;
	private String options;
	private String options2;
	private String comment;

	public JoinOperation() {
	}

	public JoinOperation(final String relation, final String input, final String operation, final String input2, final String options,
			final String options2, final String comment) {
		this.relation = relation;
		this.input = input;
		this.operation = operation;
		this.input2 = input2;
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
		JoinOperation.workflowType = workflowType;
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
	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) throws MissingParameterException {
		String mappedInputValue1 = DynamicInputParameterMapper.getMappedValue(wfName, input);
		String mappedInputValue2 = DynamicInputParameterMapper.getMappedValue(wfName, input2);

		StringBuilder sb = new StringBuilder();
		sb.append(parseInfo(getComment()));
		sb.append(removeLeadingDollarSign(getRelation()));
		addOutputParamToIndex(getRelation(), wfName);
		sb.append(renameParameters(renameParam, wfName));
		sb.append(EQUAL_SYMBOL);
		sb.append("JOIN");
		sb.append(" ");
		sb.append(resolveInputParameterValue(mappedInputValue1, getInput(), wfName, renameParam));
		sb.append(" BY (");
		sb.append(getOptions());
		sb.append("), ");
		sb.append(resolveInputParameterValue(mappedInputValue2, getInput2(), wfName, renameParam));
		sb.append(" BY (");
		sb.append(getOptions2());
		sb.append(')');
		sb.append(";");
		return sb.toString();
	}

}