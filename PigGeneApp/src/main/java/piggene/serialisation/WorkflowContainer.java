package piggene.serialisation;

import java.util.ArrayList;

public class WorkflowContainer {
	private String name;
	private String description;
	private ArrayList<Workflow> workflows;

	public WorkflowContainer() {

	}

	public WorkflowContainer(final String name, final String description, final ArrayList<Workflow> workflows) {
		this.name = name;
		this.description = description;
		this.workflows = workflows;
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

	public ArrayList<Workflow> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(final ArrayList<Workflow> workflows) {
		this.workflows = workflows;
	}

}