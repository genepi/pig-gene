package piggene.serialisation.workflow;

public class FlowComponent {
	private String name;
	private Position position;

	public FlowComponent() {

	}

	public FlowComponent(final String name, final Position position) {
		this.name = name;
		if (position == null) {
			this.position = new Position(0, 0);
		}
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

}