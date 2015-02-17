package piggene.serialisation.workflow.parameter;

import piggene.serialisation.workflow.Position;

public class InputLinkParameter extends LinkParameter {

	public InputLinkParameter() {
	}

	public InputLinkParameter(final String uid, final String connector, final String description, final Position position) {
		super.setUid(uid);
		super.setConnector(connector);
		super.setDescription(description);
		super.setPosition(position);
	}

}