package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Workflow implements IWorkflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW;

	private String name;
	private String description;
	private ArrayList<Workflow> steps;

	private Map<String, Map<String, String>> inputParameterMapping;
	private Map<String, Map<String, String>> outputParameterMapping;

	public Workflow() {

	}

	public Workflow(final String name, final String description, final ArrayList<Workflow> steps,
			final Map<String, Map<String, String>> inputParameterMapping, final Map<String, Map<String, String>> outputParameterMapping) {
		this.name = name;
		this.description = description;
		this.steps = steps;
		this.inputParameterMapping = inputParameterMapping;
		this.outputParameterMapping = outputParameterMapping;
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

	public ArrayList<Workflow> getSteps() {
		return steps;
	}

	public void setSteps(final ArrayList<Workflow> steps) {
		this.steps = steps;
	}
	
	public Map<String, Map<String, String>> getInputParameterMapping() {
		return inputParameterMapping;
	}

	public void setInputParameterMapping(
			Map<String, Map<String, String>> inputParameterMapping) {
		this.inputParameterMapping = inputParameterMapping;
	}

	public Map<String, Map<String, String>> getOutputParameterMapping() {
		return outputParameterMapping;
	}

	public void setOutputParameterMapping(
			Map<String, Map<String, String>> outputParameterMapping) {
		this.outputParameterMapping = outputParameterMapping;
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
	public String getPigScriptRepresentation() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
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