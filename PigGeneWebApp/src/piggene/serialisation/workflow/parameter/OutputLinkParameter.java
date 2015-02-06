package piggene.serialisation.workflow.parameter;


public class OutputLinkParameter extends LinkParameter {
	private boolean persistent;

	public OutputLinkParameter() {
	}

	public OutputLinkParameter(final String name) {
		super.setName(name);
	}

	public OutputLinkParameter(final String name, final String description) {
		super.setName(name);
		super.setDescription(description);
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(final boolean persistent) {
		this.persistent = persistent;
	}

	@Override
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	public void setDescription(final String description) {
		super.setDescription(description);
	}

}