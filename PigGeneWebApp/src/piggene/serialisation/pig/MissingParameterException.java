package piggene.serialisation.pig;

public class MissingParameterException extends Exception {
	private static final long serialVersionUID = 8531482572454598873L;

	public MissingParameterException() {
		super();
	}

	public MissingParameterException(final String msg) {
		super(msg);
	}

}