package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.List;

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
		StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		sb.append(preparePigScriptCommand(name));
		sb.append(preparePigScriptCommand(description));

		for (Workflow wf : components) {
			sb.append(lineSeparator);
			sb.append(wf.getPigScriptRepresentation(wfName));
		}
		return sb.toString();
	}

}