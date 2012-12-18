package piggene;

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