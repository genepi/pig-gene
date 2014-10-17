package piggene.serialisation.workflow.parameter;

public class OutputLinkParameter extends LinkParameter {
	private boolean persistent;

	public OutputLinkParameter() {
	}

	public OutputLinkParameter(final String name) {
		super.setName(name);
	}

	public OutputLinkParameter(final String name, final String persistent) {
		super.setName(name);
		this.persistent = ((persistent.toLowerCase()).equals("true") ? true : false);
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(final boolean persistent) {
		this.persistent = persistent;
	}

}