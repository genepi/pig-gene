package piggene.serialisation.workflow;

import java.io.IOException;

public class WorkflowComponent extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_COMPONENT;
	private String content;

	public WorkflowComponent(final String content) {
		this.content = content;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		WorkflowComponent.workflowType = workflowType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public String getPigScriptRepresentation(final String wfName) throws IOException {
		// TODO Auto-generated method stub
		return "";
	}

}