package piggene.representation;

public class Connection {
	private String source;
	private String target;

	public Connection() {

	}

	public Connection(final String source, final String target) {
		this.source = source;
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

}
