package piggene.representation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

public class WorkflowGraph {

	public static List<Connection> createConnectionList(final Workflow wf) throws JSONException, WorkflowGraphException {
		final List<Connection> connections = new ArrayList<Connection>();
		final List<LinkParameter> inputParameters = wf.getParameter().getInputParameter();
		final List<LinkParameter> outputParameters = wf.getParameter().getOutputParameter();
		final WorkflowParameterMapping parameterMapping = wf.getParameterMapping();

		// component connected with input element
		for (final LinkParameter p : inputParameters) {
			final String connector = p.getConnector();
			if (contentCheck(connector)) {
				for (final String s : parameterMapping.getCorrespondingInputParameterValues(connector)) {
					connections.add(new Connection(p.getUid(), s));
				}
			}
		}

		// component connected with output element
		for (final LinkParameter p : outputParameters) {
			final String connector = p.getConnector();
			if (contentCheck(connector)) {
				for (final String s : parameterMapping.getCorrespondingOutputParameterValues(connector)) {
					connections.add(new Connection(s, p.getUid()));
				}
			}
		}

		for (final String parameterName : parameterMapping.getMatchingInAndOutputParameterValuesFromAllWorkflows()) {
			if (contentCheck(parameterName)) {
				final List<String> correspondingOutputParameterValues = parameterMapping.getCorrespondingOutputParameterValues(parameterName);
				if (correspondingOutputParameterValues.size() > 1) {
					throw new WorkflowGraphException("Only one corresponding output parameter value allowed!");
				}
				for (final String target : parameterMapping.getCorrespondingInputParameterValues(parameterName)) {
					connections.add(new Connection(correspondingOutputParameterValues.get(0), target));
				}
			}
		}
		return connections;
	}

	private static boolean contentCheck(final String connector) {
		if (connector == null || connector == "") {
			return false;
		}
		return true;
	}

}