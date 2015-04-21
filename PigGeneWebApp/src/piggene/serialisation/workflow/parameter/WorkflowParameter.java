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

	public List<LinkParameter> getOutputParameter() {
		return outputParameter;
	}

	public void setOutputParameter(final List<LinkParameter> outputParameter) {
		this.outputParameter = outputParameter;
	}

	public void addOutputParameter(final LinkParameter outputParameter) {
		if (!this.outputParameter.contains(outputParameter)) {
			this.outputParameter.add(outputParameter);
		}
	}

}