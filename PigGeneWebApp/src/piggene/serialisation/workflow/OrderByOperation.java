package piggene.serialisation.workflow;

import piggene.serialisation.pig.DynamicInputParameterMapper;
import piggene.serialisation.pig.DynamicOutputParameterMapper;

public class OrderByOperation extends Workflow implements IWorkflowOperation {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_SINGLE_ELEM;

	private String relation;
	private String input;
	private String operation;
	private String options;
	private String comment;

	public OrderByOperation() {
	}

	public OrderByOperation(final String relation, final String input, final String operation, final String options, final String comment) {
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
		OrderByOperation.workflowType = workflowType;
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
	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) {
		String mappedInputValue = DynamicInputParameterMapper.getMappedValue(wfName, input);
		if (mappedInputValue == null) {
			mappedInputValue = DynamicOutputParameterMapper.getMappedValue(wfName, input);
		}

		StringBuilder sb = new StringBuilder();
		sb.append(parseInfo(getComment()));
		sb.append(removeLeadingDollarSign(getRelation()));
		sb.append(renameParameters(renameParam, wfName));
		sb.append(EQUAL_SYMBOL);
		sb.append("ORDER");
		sb.append(" ");
		if (mappedInputValue != null) {
			sb.append(mappedInputValue);
		} else {
			sb.append(removeLeadingDollarSign(getInput()));
			if (!getInput().startsWith("$")) { // relation from same wf
				sb.append(renameParameters(renameParam, wfName));
			}
		}
		sb.append(" BY ");
		sb.append(getOptions());
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