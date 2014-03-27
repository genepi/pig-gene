package piggene.serialisation.cloudgene;

/**
 * MapReduceConfig class is used to hold the yaml-file information.
 */
import java.util.List;
import java.util.Vector;

public class MapReduceConfig {

	private List<Step> steps = new Vector<Step>();

	private String params;

	private String pig;

	private List<Parameter> inputs = new Vector<Parameter>();

	private List<Parameter> outputs = new Vector<Parameter>();

	public String getParams() {
		return params;
	}

	public void setParams(final String params) {
		this.params = params;
	}

	public List<Parameter> getInputs() {
		return inputs;
	}

	public void setInputs(final List<Parameter> inputs) {
		this.inputs = inputs;
	}

	public List<Parameter> getOutputs() {
		return outputs;
	}

	public void setOutputs(final List<Parameter> outputs) {
		this.outputs = outputs;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(final List<Step> steps) {
		this.steps = steps;
	}

	public void setPig(final String pig) {
		this.pig = pig;
	}

	public String getPig() {
		return pig;
	}

}