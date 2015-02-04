package piggene.serialisation.workflow.parameter;

public class OutputLinkParameter extends LinkParameter {
	private boolean persistent;
	private String description;

	public OutputLinkParameter() {
	}

	public OutputLinkParameter(final String name) {
		super.setName(name);
	}

	public OutputLinkParameter(final String name, final String description) {
		super.setName(name);
		this.description = description;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(final boolean persistent) {
		this.persistent = persistent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}