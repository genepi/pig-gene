package piggene.serialisation.workflow;

import java.io.IOException;

public class ReferencedWorkflow extends Workflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW_REFERENCE;

	private String name;
	private String description;

	public ReferencedWorkflow() {
	}

	public ReferencedWorkflow(final String name, final String description) {
		this.name = name;
		this.description = description;
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
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public String getPigScriptRepresentation() throws IOException {
		Workflow referencedWorkflow = WorkflowSerialisation.load(this.getName());
		return referencedWorkflow.getPigScriptRepresentation();
	}

}