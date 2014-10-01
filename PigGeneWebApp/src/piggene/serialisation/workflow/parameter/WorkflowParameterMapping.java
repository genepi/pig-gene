package piggene.serialisation.workflow.parameter;

public class WorkflowParameterMapping {
	private LinkParameterMapping inputParameterMapping;
	private LinkParameterMapping outputParameterMapping;

	public WorkflowParameterMapping() {
	}

	public WorkflowParameterMapping(final LinkParameterMapping inputParameterMapping, final LinkParameterMapping outputParameterMapping) {
		this.inputParameterMapping = inputParameterMapping;
		this.outputParameterMapping = outputParameterMapping;
	}

	public LinkParameterMapping getInputParameterMapping() {
		return inputParameterMapping;
	}

	public void setInputParameterMapping(final LinkParameterMapping inputParameterMapping) {
		this.inputParameterMapping = inputParameterMapping;
	}

	public LinkParameterMapping getOutputParameterMapping() {
		return outputParameterMapping;
	}

	public void setOutputParameterMapping(final LinkParameterMapping outputParameterMapping) {
		this.outputParameterMapping = outputParameterMapping;
	}

}