package piggene.serialisation.cloudgene;

/**
 * Step class is used to hold the yaml-file information.
 */
public class Step {

	private String params;

	private String name;

	private String pig;

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

	public void setPig(final String pig) {
		this.pig = pig;
	}

	public String getPig() {
		return pig;
	}

}
