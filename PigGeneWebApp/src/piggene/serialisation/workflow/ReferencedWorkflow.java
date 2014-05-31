package piggene.serialisation.workflow;

import java.io.IOException;

import piggene.serialisation.pig.DynamicParameterMapper;

public class ReferencedWorkflow extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_REFERENCE;

	private String name;

	public ReferencedWorkflow() {
	}

	public ReferencedWorkflow(final String name) {
		this.name = name;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		ReferencedWorkflow.workflowType = workflowType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) throws IOException {
		String workflowName = this.name;
		Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName);
		DynamicParameterMapper.addParamMapping(referencedWorkflow.getInputParameterMapping());

		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		sb.append(parseInfo(workflowName));
		sb.append(parseInfo(referencedWorkflow.getDescription()));

		for (Workflow wf : referencedWorkflow.getSteps()) {
			sb.append(System.getProperty("line.separator"));
			sb.append(wf.getPigScriptRepresentation(true, workflowName));
		}
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

}