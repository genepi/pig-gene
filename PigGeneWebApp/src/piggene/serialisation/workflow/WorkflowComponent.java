package piggene.serialisation.workflow;

import java.io.IOException;

public class WorkflowComponent extends Workflow {
	private WorkflowType workflowType = WorkflowType.WORKFLOW_COMPONENT;
	private String content;

	public WorkflowComponent() {
	}

	public WorkflowComponent(final String content) {
		this.content = content;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return this.workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public String getPigScriptRepresentation(final String wfName) throws IOException {
		return content + lineSeparator;
	}

}