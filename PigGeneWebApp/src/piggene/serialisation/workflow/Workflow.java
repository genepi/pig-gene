package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.List;

public class Workflow implements IWorkflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW;

	private String name;
	private String description;
	private List<Workflow> components;

	public Workflow() {
		// TODO Auto-generated constructor stub
	}

	public Workflow(final String name, final String description, final List<Workflow> components) {
		this.name = name;
		this.description = description;
		this.components = components;
	}

	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(final WorkflowType workflowType) {
		Workflow.workflowType = workflowType;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<Workflow> getComponents() {
		return components;
	}

	public void setComponents(final List<Workflow> components) {
		this.components = components;
	}

	@Override
	public String getPigScriptRepresentation(final String wfName) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		sb.append(name);
		sb.append(description);

		for (Workflow wf : components) {
			sb.append(System.getProperty("line.separator"));
			sb.append(wf.getPigScriptRepresentation(wfName));
		}
		return sb.toString();
	}

}