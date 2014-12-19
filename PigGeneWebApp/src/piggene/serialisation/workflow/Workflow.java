package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import piggene.representation.Connection;
import piggene.serialisation.workflow.parameter.WorkflowParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

/**
 * @author clemens
 *
 */
public class Workflow implements IWorkflow {
	private WorkflowType workflowType = WorkflowType.WORKFLOW;

	private String name;
	private String description;
	private List<Workflow> components;

	private WorkflowParameter parameter;
	private WorkflowParameterMapping parameterMapping;
	private List<Connection> connections;

	protected String lineSeparator = System.getProperty("line.separator");

	public Workflow() {
	}

	public Workflow(final String name, final String description, final List<Workflow> components, final WorkflowParameter parameter,
			final WorkflowParameterMapping parameterMapping) {
		this.name = name;
		this.description = description;
		this.components = components;
		this.parameter = parameter;
		this.parameterMapping = parameterMapping;
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

	public List<Workflow> getComponents() {
		return components;
	}

	public void setComponents(final List<Workflow> components) {
		this.components = components;
	}

	public WorkflowParameter getParameter() {
		return parameter;
	}

	public void setParameter(final WorkflowParameter parameter) {
		this.parameter = parameter;
	}

	public WorkflowParameterMapping getParameterMapping() {
		return parameterMapping;
	}

	public void setParameterMapping(final WorkflowParameterMapping parameterMapping) {
		this.parameterMapping = parameterMapping;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	public void setLineSeparator(final String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(final List<Connection> connections) {
		this.connections = connections;
	}

	protected String preparePigScriptCommand(final String info) {
		final StringBuilder sb = new StringBuilder();
		if (!(info.equals("-") || info.equals(""))) {
			sb.append("--");
			sb.append(info);
			sb.append(lineSeparator);
		}
		return sb.toString();
	}

	@Override
	public String getPigScriptRepresentation(final String wfName) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		sb.append(preparePigScriptCommand(name));
		sb.append(preparePigScriptCommand(description));

		String content;
		for (final Workflow wf : components) {
			content = wf.getPigScriptRepresentation(wfName);
			if (content != null) {
				sb.append(content);
				sb.append(lineSeparator);
			}
		}
		return sb.toString();
	}

	@Override
	public Map<String, String> getRMarkDownScriptRepresentations() throws IOException {
		final Map<String, String> rmdScripts = new HashMap<String, String>();
		Map<String, String> content;
		for (final Workflow wf : components) {
			content = wf.getRMarkDownScriptRepresentations();
			if (content != null) {
				rmdScripts.putAll(content);
			}
		}
		return rmdScripts;
	}

}