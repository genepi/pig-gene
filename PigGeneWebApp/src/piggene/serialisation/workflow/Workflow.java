package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.List;

public class Workflow implements IWorkflow {
	private WorkflowType workflowType = WorkflowType.WORKFLOW;

	private String name;
	private String description;
	private List<Workflow> components;

	protected String lineSeparator = System.getProperty("line.separator");

	public Workflow() {
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
		this.workflowType = workflowType;
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
		sb.append(lineSeparator);
		sb.append(preparePigScriptCommand(name));
		sb.append(preparePigScriptCommand(description));

		for (Workflow wf : components) {
			sb.append(lineSeparator);
			sb.append(wf.getPigScriptRepresentation(wfName));
		}
		return sb.toString();
	}

	protected String preparePigScriptCommand(final String info) {
		final StringBuilder sb = new StringBuilder();
		if (!(info.equals("-") || info.equals(""))) {
			sb.append("--");
			sb.append(info);
			sb.append(lineSeparator);
		}
		return sb.toString();
	}

}