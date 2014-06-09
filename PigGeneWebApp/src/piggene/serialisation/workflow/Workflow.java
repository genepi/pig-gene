package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import piggene.serialisation.pig.DynamicInputParameterMapper;
import piggene.serialisation.pig.DynamicOutputParameterMapper;

public class Workflow implements IWorkflow {
	private static WorkflowType workflowType = WorkflowType.WORKFLOW;

	private String name;
	private String description;
	private List<Workflow> steps;

	private List<String> inputParameters;
	private List<String> outputParameters;

	private Map<String, Map<String, String>> inputParameterMapping;
	private Map<String, Map<String, String>> outputParameterMapping;

	public Workflow() {

	}

	public Workflow(final String name, final String description, final List<Workflow> steps, final List<String> inputParameters,
			final List<String> outputParameters, final Map<String, Map<String, String>> inputParameterMapping,
			final Map<String, Map<String, String>> outputParameterMapping) {
		this.name = name;
		this.description = description;
		this.steps = steps;
		this.inputParameters = inputParameters;
		this.outputParameters = outputParameters;
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

	public List<Workflow> getSteps() {
		return steps;
	}

	public void setSteps(final List<Workflow> steps) {
		this.steps = steps;
	}

	public Map<String, Map<String, String>> getInputParameterMapping() {
		return inputParameterMapping;
	}

	public List<String> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(final List<String> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public List<String> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(final List<String> outputParameters) {
		this.outputParameters = outputParameters;
	}

	public void setInputParameterMapping(final Map<String, Map<String, String>> inputParameterMapping) {
		this.inputParameterMapping = inputParameterMapping;
	}

	public Map<String, Map<String, String>> getOutputParameterMapping() {
		return outputParameterMapping;
	}

	public void setOutputParameterMapping(final Map<String, Map<String, String>> outputParameterMapping) {
		this.outputParameterMapping = outputParameterMapping;
	}

	protected String parseInfo(final String info) {
		final StringBuilder sb = new StringBuilder();
		if (!(info.equals("-") || info.equals(""))) {
			sb.append("--");
			sb.append(info);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	@Override
	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) throws IOException {
		DynamicInputParameterMapper.setParamMapping(inputParameterMapping, wfName);
		DynamicOutputParameterMapper.setParamMapping(outputParameterMapping);
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		sb.append(parseInfo(getName()));
		sb.append(parseInfo(getDescription()));

		for (Workflow wf : steps) {
			sb.append(System.getProperty("line.separator"));
			sb.append(wf.getPigScriptRepresentation(true, wfName));
		}
		return sb.toString();
	}

}