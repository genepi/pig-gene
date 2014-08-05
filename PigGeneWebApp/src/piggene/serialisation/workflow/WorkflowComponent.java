package piggene.serialisation.workflow;

import java.io.IOException;

public class WorkflowComponent extends Workflow {
	private  WorkflowType workflowType = WorkflowType.WORKFLOW_COMPONENT;
	private String content;

	public WorkflowComponent(){
		
	}
	
	public WorkflowComponent(String content) {
		this.content = content;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	@Override
	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getPigScriptRepresentation(String wfName) throws IOException {
		// TODO Auto-generated method stub
		return "";
	}

}