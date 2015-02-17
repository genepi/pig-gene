package piggene.serialisation.workflow.parameter;

import piggene.serialisation.workflow.Position;

public class OutputLinkParameter extends LinkParameter {

	public OutputLinkParameter() {
	}

	public OutputLinkParameter(final String uid, final String connector, final String description, final Position position) {
		super.setUid(uid);
		super.setConnector(connector);
		super.setDescription(description);
		super.setPosition(position);
	}

}