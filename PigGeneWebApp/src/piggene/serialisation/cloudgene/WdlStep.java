package piggene.serialisation.cloudgene;

import java.util.HashMap;
import java.util.Map;

public class WdlStep {

	private String jar;

	private String mapper;

	private String reducer;

	private String params;

	private String name;

	private String exec;

	private String pig;

	private String rmd;

	private String template;

	private String output;

	private String job;

	private String classname;

	private int id;

	private boolean cache = false;

	private String generates = "";

	private Map<String, String> mapping = new HashMap<String, String>();

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

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

	public String getRmd() {
		return rmd;
	}

	public void setRmd(final String rmd) {
		this.rmd = rmd;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(final String output) {
		this.output = output;
	}

	public void setJob(final String job) {
		this.job = job;
	}

	public String getJob() {
		return job;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(final String classname) {
		this.classname = classname;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(final boolean cache) {
		this.cache = cache;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setGenerates(final String generates) {
		this.generates = generates;
	}

	public String getGenerates() {
		return generates;
	}

	public void setTemplate(final String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void setMapping(final Map<String, String> mapping) {
		this.mapping = mapping;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}

}
