package piggene.serialisation.workflow.parameter;

import java.util.List;

public class WorkflowParameter {
	private List<LinkParameter> inputParameter;
	private List<LinkParameter> outputParameter;

	public WorkflowParameter() {
	}

	public WorkflowParameter(final List<LinkParameter> inputParameter, final List<LinkParameter> outputParameter) {
		this.inputParameter = inputParameter;
		this.outputParameter = outputParameter;
	}

	public List<LinkParameter> getInputParameter() {
		return inputParameter;
	}

	public void setInputParameter(final List<LinkParameter> inputParameter) {
		this.inputParameter = inputParameter;
	}

	public void addInputParameter(final LinkParameter inputParameter) {
		if (!inputConnectorAlreadyContained(inputParameter)) {
			this.inputParameter.add(inputParameter);
		}
	}

	public List<LinkParameter> getOutputParameter() {
		return outputParameter;
	}

	public void setOutputParameter(final List<LinkParameter> outputParameter) {
		this.outputParameter = outputParameter;
	}

	public void addOutputParameter(final LinkParameter outputParameter) {
		if (!outputConnectorAlreadyContained(outputParameter)) {
			this.outputParameter.add(outputParameter);
		}
	}

	private boolean outputConnectorAlreadyContained(final LinkParameter outputParameter) {
		boolean contained = false;
		for (final LinkParameter p : this.outputParameter) {
			if (p.getConnector().equals(outputParameter.getConnector())) {
				contained = true;
				break;
			}
		}
		return contained;
	}

	private boolean inputConnectorAlreadyContained(final LinkParameter inputParameter) {
		boolean contained = false;
		for (final LinkParameter p : this.inputParameter) {
			if (p.getConnector().equals(inputParameter.getConnector())) {
				contained = true;
				break;
			}
		}
		return contained;
	}

}