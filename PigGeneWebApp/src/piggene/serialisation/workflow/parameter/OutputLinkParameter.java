package piggene.serialisation.workflow.parameter;

public class OutputLinkParameter extends LinkParameter {
	private boolean persistent;

	public OutputLinkParameter() {
	}

	public OutputLinkParameter(final String name) {
		super.setName(name);
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(final boolean persistent) {
		this.persistent = persistent;
	}

}