package piggene.serialisation;

import java.util.ArrayList;

/**
 * Workflow class is used to store a workflow definition, its name and an
 * (optional) description.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class Workflow {
	private String name;
	private String description;
	private ArrayList<SingleWorkflowElement> workflow;

	public Workflow() {

	}

	public Workflow(final String name, final String description, final ArrayList<SingleWorkflowElement> workflow) {
		this.name = name;
		this.description = description;
		this.workflow = workflow;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setWorkflow(final ArrayList<SingleWorkflowElement> workflow) {
		this.workflow = workflow;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<SingleWorkflowElement> getWorkflow() {
		return workflow;
	}

}