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
	
	public boolean isContainedWithinOuterWfParams(String paramName) {
		for(LinkParameter p : inputParameter) {
			if(p.getName().equals(paramName)) {
				return true;
			}
		}
		for(LinkParameter p : outputParameter) {
			if(p.getName().equals(paramName)) {
				return true;
			}
		}
		return false;
	}
	
}