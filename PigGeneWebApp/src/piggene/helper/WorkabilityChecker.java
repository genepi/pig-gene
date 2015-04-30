package piggene.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;
import piggene.serialisation.workflow.parameter.LinkParameter;

public class WorkabilityChecker {

	public static void checkConnectionIntegrity(final Workflow surroundingWorkflow) throws MissingConnectionException, IOException {
		final Map<String, Map<String, String>> inputParameterMapping = surroundingWorkflow.getParameterMapping().getInputParameterMapping();
		final Map<String, Map<String, String>> outputParameterMapping = surroundingWorkflow.getParameterMapping().getOutputParameterMapping();
		for (final Workflow comp : surroundingWorkflow.getComponents()) {
			final String name = comp.getName();
			final Map<String, String> inputParamMap = inputParameterMapping.get(comp.getUid());
			final Map<String, String> outputParamMap = outputParameterMapping.get(comp.getUid());

			WorkabilityChecker.checkInputConnectors(name, inputParamMap, surroundingWorkflow);
			WorkabilityChecker.checkOutputConnectors(name, outputParamMap);
			WorkabilityChecker.checkConnectedToItself(name, inputParamMap, outputParamMap);
		}
	}

	private static void checkInputConnectors(final String name, final Map<String, String> parameterMap, final Workflow surroundingWorkflow)
			throws IOException, MissingConnectionException {
		final Workflow component = WorkflowSerialisation.load(name, WorkflowSerialisation.determineType(name));
		final List<LinkParameter> inputParameters = component.getParameter().getInputParameter();
		if (parameterIsMissing(inputParameters, parameterMap)) {
			final List<String> unconnectedInputParams = getListOfEmptyConnectors(parameterMap, inputParameters);
			throw new MissingConnectionException(getErrorMsgString(name, "input", unconnectedInputParams));
		}
	}

	private static void checkOutputConnectors(final String name, final Map<String, String> parameterMap) throws IOException,
			MissingConnectionException {
		final Workflow component = WorkflowSerialisation.load(name, WorkflowSerialisation.determineType(name));
		final List<LinkParameter> outputParameters = component.getParameter().getOutputParameter();
		if (parameterIsMissing(outputParameters, parameterMap)) {
			final List<String> unconnectedOutputParams = getListOfEmptyConnectors(parameterMap, outputParameters);
			throw new MissingConnectionException(getErrorMsgString(name, "output", unconnectedOutputParams));
		}
	}

	private static boolean parameterIsMissing(final List<LinkParameter> parameterList, final Map<String, String> parameterMap) {
		for (final LinkParameter p : parameterList) {
			if (!parameterMap.containsKey(p.getConnector()) || parameterMap.get(p.getConnector()).equals("")) {
				return true;
			}
		}
		return false;
	}

	private static List<String> getListOfEmptyConnectors(final Map<String, String> parameterMap, final List<LinkParameter> parameterList) {
		final List<String> unconnectedInputParams = new ArrayList<String>();
		for (final LinkParameter param : parameterList) {
			if (!parameterMap.containsKey(param.getConnector())) {
				unconnectedInputParams.add(param.getDescription());
			}
		}
		return unconnectedInputParams;
	}

	private static void checkConnectedToItself(final String name, final Map<String, String> inputParamMap, final Map<String, String> outputParamMap)
			throws MissingConnectionException {
		for (final String inputKey : inputParamMap.keySet()) {
			final String inputValue = inputParamMap.get(inputKey);
			for (final String outputKey : outputParamMap.keySet()) {
				final String outputValue = outputParamMap.get(outputKey);
				if (inputValue.equals(outputValue)) {
					final StringBuilder sb = new StringBuilder();
					sb.append("There exists a problem in component ");
					sb.append("'" + name + "'. ");
					sb.append("It is not permitted that a components output is connected to an input of the same component.");
					throw new MissingConnectionException(sb.toString());
				}
			}
		}
	}

	private static String getErrorMsgString(final String name, final String type, final List<String> unconnectedParams) {
		final StringBuilder sb = new StringBuilder();
		sb.append("There exists a problem in component ");
		sb.append("'" + name + "'. ");

		if (unconnectedParams.size() > 1) {
			sb.append("The ");
			sb.append(type);
			sb.append(" parameters: ");

			boolean skip = true;
			for (final String s : unconnectedParams) {
				if (!skip) {
					sb.append(", ");
					skip = false;
				}
				sb.append(s);
			}
			sb.append("are not connected. ");
		} else if (unconnectedParams.size() == 0) {
			sb.append("Missing ");
			sb.append(type);
			sb.append(" parameter connection. ");
		} else {
			sb.append("The ");
			sb.append(type);
			sb.append(" parameter: '");
			sb.append(unconnectedParams.get(0));
			sb.append("' is not connected. ");
		}
		sb.append("Please fix this problem to be able to proceed.");
		return sb.toString();
	}

}