package piggene.exceptions;

/**
 * UnpossibleWorkflowFileOperation class is thrown if someone wants to override
 * or delete an examle workflow.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class UnpossibleWorkflowFileOperation extends Exception {
	private static final long serialVersionUID = -2058304590442296453L;

	public UnpossibleWorkflowFileOperation() {
		super();
	}

	public UnpossibleWorkflowFileOperation(final String message) {
		super(message);
	}

}