package piggene.serialisation.workflow.parameter;

import java.util.ArrayList;
import java.util.List;
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

	public List<String> getCorrespondingInputParameterValues(final String inputParam) {
		final List<String> correspondingInputParamVals = new ArrayList<String>();
		for (final String compParam : inputParameterMapping.keySet()) {
			final Map<String, String> map = inputParameterMapping.get(compParam);
			for (final String key : map.keySet()) {
				if (map.get(key).equals(inputParam)) {
					correspondingInputParamVals.add(compParam.concat(".").concat(key));
				}
			}
		}
		return correspondingInputParamVals;
	}

	public List<String> getCorrespondingOutputParameterValues(final String outputParam) {
		final List<String> correspondingOutputParamVals = new ArrayList<String>();
		for (final String compParam : outputParameterMapping.keySet()) {
			final Map<String, String> map = outputParameterMapping.get(compParam);
			for (final String key : map.keySet()) {
				if (map.get(key).equals(outputParam)) {
					correspondingOutputParamVals.add(compParam.concat(".").concat(key));
				}
			}
		}
		return correspondingOutputParamVals;
	}

	public List<String> getMatchingInAndOutputParameterValuesFromAllWorkflows() {
		final List<String> matchingParameterValues = new ArrayList<String>();
		final List<String> outputParameterValues = new ArrayList<String>();
		for (final String key : outputParameterMapping.keySet()) {
			for (final String innerKey : outputParameterMapping.get(key).keySet()) {
				outputParameterValues.add(outputParameterMapping.get(key).get(innerKey));
			}
		}

		final List<String> inputParameterValues = new ArrayList<String>();
		for (final String key : inputParameterMapping.keySet()) {
			for (final String innerKey : inputParameterMapping.get(key).keySet()) {
				inputParameterValues.add(inputParameterMapping.get(key).get(innerKey));
			}
		}

		for (final String s : outputParameterValues) {
			if (inputParameterValues.contains(s)) {
				matchingParameterValues.add(s);
			}
		}

		return matchingParameterValues;
	}

}