package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.List;

public class Workflow implements IWorkflow {
	private WorkflowType workflowType = WorkflowType.WORKFLOW;

	private String name;
	private String description;
	private List<Workflow> components;

	public Workflow() {
		// TODO Auto-generated constructor stub
	}

	public Workflow(String name, String description, List<Workflow> components) {
		this.name = name;
		this.description = description;
		this.components = components;
	}

	public WorkflowType getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Workflow> getComponents() {
		return components;
	}

	public void setComponents(List<Workflow> components) {
		this.components = components;
	}

	@Override
	public String getPigScriptRepresentation(String wfName) throws IOException {
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