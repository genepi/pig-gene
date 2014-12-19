package piggene.serialisation.workflow;

public class Position {
	private int top;
	private int left;

	public Position() {

	}

	public Position(final int top, final int left) {
		this.top = top;
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(final int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(final int left) {
		this.left = left;
	}

}