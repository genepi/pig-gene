package piggene.serialisation.workflow.parameter;

public class OutputLinkParameter extends LinkParameter {
	private LinkType type;

	public OutputLinkParameter() {
	}

	public OutputLinkParameter(final String name) {
		super.setName(name);
	}

	public OutputLinkParameter(final String name, final LinkType type) {
		super.setName(name);
		this.type = type;
	}

	public LinkType getType() {
		return type;
	}

	public void setType(final LinkType type) {
		this.type = type;
	}

}