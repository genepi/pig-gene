package piggene.serialisation.workflow.parameter;

import java.util.Map;

public class WorkflowParameterMapping {
	private Map<String, Map<String, String>> inputParameterMapping;
	private Map<String, Map<String, String>> outputParameterMapping;

	public WorkflowParameterMapping() {
	}

	public WorkflowParameterMapping(final Map<String, Map<String, String>> inputParameterMapping,
			final Map<String, Map<String, String>> outputParameterMapping) {
		this.inputParameterMapping = inputParameterMapping;
		this.outputParameterMapping = outputParameterMapping;
	}

	public Map<String, Map<String, String>> getInputParameterMapping() {
		return inputParameterMapping;
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

	public Map<String, String> retrieveInputMapByKey(final String key) {
		return this.inputParameterMapping.get(key);
	}

	public Map<String, String> retrieveOutputMapByKey(final String key) {
		return this.outputParameterMapping.get(key);
	}

}