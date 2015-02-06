package piggene.serialisation.workflow.parameter;


public class InputLinkParameter extends LinkParameter {

	public InputLinkParameter() {
	}

	public InputLinkParameter(final String name) {
		super.setName(name);
	}

	public InputLinkParameter(final String name, final String description) {
		super.setName(name);
		super.setDescription(description);
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