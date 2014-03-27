package piggene.serialisation.workflow;

import java.util.ArrayList;

public class Workflow implements WorkflowInterface {
	protected WorkflowType workflowType;

	private String name;
	private String description;
	private ArrayList<Workflow> steps;

	private ArrayList<String> inputParameters;
	private ArrayList<String> outputParameters;

	public Workflow() {

	}

	public Workflow(final String name, final String description, final ArrayList<Workflow> steps,
			final ArrayList<String> inputParameters, final ArrayList<String> outputParameters) {
		this.workflowType = WorkflowType.WORKFLOW;
		this.name = name;
		this.description = description;
		this.steps = steps;
		this.inputParameters = inputParameters;
		this.outputParameters = outputParameters;
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

	public ArrayList<Workflow> getSteps() {
		return steps;
	}

	public void setSteps(final ArrayList<Workflow> steps) {
		this.steps = steps;
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

	protected String parseComment(final String comment) {
		final StringBuilder sb = new StringBuilder();
		if (!comment.equals("-")) {
			sb.append("--");
			sb.append(comment);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	@Override
	public String getPigScriptRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append(parseComment(getName()));
		sb.append(parseComment(getDescription()));

		for (Workflow wf : steps) {
			sb.append(System.getProperty("line.separator"));
			sb.append(wf.getPigScriptRepresentation());
			sb.append(";");
		}
		return sb.toString();
	}

}