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
	private ArrayList<String> inputParameters;
	private ArrayList<String> outputParameters;

	public Workflow() {

	}

	public Workflow(final String name, final String description, final ArrayList<SingleWorkflowElement> workflow, final ArrayList<String> inputParameters,
			final ArrayList<String> outputParameters) {
		this.name = name;
		this.description = description;
		this.workflow = workflow;
		this.inputParameters = inputParameters;
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

	public ArrayList<String> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(final ArrayList<String> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public ArrayList<String> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(final ArrayList<String> outputParameters) {
		this.outputParameters = outputParameters;
	}

}