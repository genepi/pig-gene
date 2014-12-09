package piggene.serialisation.cloudgene;

import java.util.List;
import java.util.Vector;

public class WdlMapReduce {

	private String jar;

	private String mapper;

	private String reducer;

	private String exec;

	private String pig;

	private String params;

	private List<WdlStep> steps = new Vector<WdlStep>();

	private List<WdlParameter> inputs = new Vector<WdlParameter>();

	private List<WdlParameter> outputs = new Vector<WdlParameter>();

	private String path;

	private String manifestFile;

	private String type = "sequence";

	private WdlStep setup = null;

	public String getJar() {
		return jar;
	}

	public void setJar(final String jar) {
		this.jar = jar;
	}

	public String getMapper() {
		return mapper;
	}

	public void setMapper(final String mapper) {
		this.mapper = mapper;
	}

	public String getReducer() {
		return reducer;
	}

	public void setReducer(final String reducer) {
		this.reducer = reducer;
	}

	public String getParams() {
		return params;
	}

	public void setParams(final String params) {
		this.params = params;
	}

	public List<WdlParameter> getInputs() {
		return inputs;
	}

	public void setInputs(final List<WdlParameter> inputs) {
		this.inputs = inputs;
	}

	public List<WdlParameter> getOutputs() {
		return outputs;
	}

	public void setOutputs(final List<WdlParameter> outputs) {
		this.outputs = outputs;
	}

	public List<WdlStep> getSteps() {
		return steps;
	}

	public void setSteps(final List<WdlStep> steps) {
		this.steps = steps;
	}

	public String getPath() {
		return path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public void setExec(final String exec) {
		this.exec = exec;
	}

	public String getExec() {
		return exec;
	}

	public void setPig(final String pig) {
		this.pig = pig;
	}

	public String getPig() {
		return pig;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setManifestFile(final String manifestFile) {
		this.manifestFile = manifestFile;
	}

	public String getManifestFile() {
		return manifestFile;
	}

	public void setSetup(final WdlStep setup) {
		this.setup = setup;
	}

	public WdlStep getSetup() {
		return setup;
	}

}
