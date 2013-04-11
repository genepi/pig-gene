package piggene.response;

/**
 * ServerResponseObject class is used to hold the information transfered from
 * the server to the client.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class ServerResponseObject {
	private boolean success;
	private String message;
	private Object data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(final Object data) {
		this.data = data;
	}

}