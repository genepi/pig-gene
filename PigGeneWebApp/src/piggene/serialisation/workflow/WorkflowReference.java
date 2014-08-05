package piggene.serialisation.workflow;

import java.io.IOException;

public class WorkflowReference extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_REFERENCE;

	private String name;

	public WorkflowReference() {
	}

	public WorkflowReference(final String name) {
		this.name = name;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		WorkflowReference.workflowType = workflowType;
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
	public String getPigScriptRepresentation(final String wfName) throws IOException {
		// WorkflowReference.indentation++;
		// String workflowName = this.name;
		// Workflow referencedWorkflow =
		// WorkflowSerialisation.load(workflowName);
		// DynamicInputParameterMapper.addParamMapping(referencedWorkflow.getInputParameterMapping(),
		// workflowName);
		//
		// StringBuilder sb = new StringBuilder();
		// sb.append(System.getProperty("line.separator"));
		// sb.append(insertIndentationTabs());
		// sb.append(parseInfo(workflowName));
		// sb.append(insertIndentationTabs());
		// sb.append(parseInfo(referencedWorkflow.getDescription()));
		//
		// for (Workflow wf : referencedWorkflow.getSteps()) {
		// sb.append(System.getProperty("line.separator"));
		// sb.append(insertIndentationTabs());
		// sb.append(wf.getPigScriptRepresentation(true, workflowName));
		// }
		// sb.append(System.getProperty("line.separator"));
		// WorkflowReference.indentation = 0;
		// return sb.toString();
		return "";
	}

}