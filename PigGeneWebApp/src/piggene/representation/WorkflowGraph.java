package piggene.representation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

public class WorkflowGraph {

	public static List<Connection> createConnectionList(final Workflow wf) throws JSONException {
		final List<Connection> connections = new ArrayList<Connection>();
		final List<LinkParameter> inputParameters = wf.getParameter().getInputParameter();
		final List<LinkParameter> outputParameters = wf.getParameter().getOutputParameter();
		final WorkflowParameterMapping parameterMapping = wf.getParameterMapping();

		// component connected with input element
		for (final LinkParameter p : inputParameters) {
			final String name = p.getName();
			for (final String s : parameterMapping.getCorrespondingInputParameterValues(name)) {
				connections.add(new Connection(name, s));
			}
		}

		// component connected with output element
		for (final LinkParameter p : outputParameters) {
			final String name = p.getName();
			for (final String s : parameterMapping.getCorrespondingOutputParameterValues(name)) {
				connections.add(new Connection(s, name));
			}
		}

		for (final String parameterName : parameterMapping.getMatchingInAndOutputParameterValuesFromAllWorkflows()) {

			// just one allowed
			final List<String> correspondingOutputParameterValues = parameterMapping.getCorrespondingOutputParameterValues(parameterName);
			if (correspondingOutputParameterValues.size() > 1) {
				// TODO
				// throw exception
			}

			for (final String target : parameterMapping.getCorrespondingInputParameterValues(parameterName)) {
				connections.add(new Connection(correspondingOutputParameterValues.get(0), target));
			}
		}

		return connections;
	}
}