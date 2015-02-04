package piggene.serialisation.workflow.parameter;

public class InputLinkParameter extends LinkParameter {

	private String description;

	public InputLinkParameter() {
	}

	public InputLinkParameter(final String name) {
		super.setName(name);
	}

	public InputLinkParameter(final String name, final String description) {
		super.setName(name);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}