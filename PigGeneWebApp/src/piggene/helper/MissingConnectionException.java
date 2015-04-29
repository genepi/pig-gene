package piggene.helper;

public class MissingConnectionException extends Exception {
	private static final long serialVersionUID = -5850769166470385139L;

	public MissingConnectionException(final String msg) {
		super(msg);
	}

}