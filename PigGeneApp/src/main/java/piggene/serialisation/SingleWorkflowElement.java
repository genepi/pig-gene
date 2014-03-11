package piggene.serialisation;

/**
 * WorkflowComponent class is used to store the information contained in a
 * single workflow line (representing an operation).
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class SingleWorkflowElement {
	private String referenceName;
	private String relation;
	private String input;
	private String operation;
	private String input2;
	private String options;
	private String options2;
	private String comment;

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
	}

	public String getInput() {
		return input;
	}

	public void setInput(final String input) {
		this.input = input;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
	}

	public String getInput2() {
		return input2;
	}

	public void setInput2(final String input2) {
		this.input2 = input2;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(final String options) {
		this.options = options;
	}

	public String getOptions2() {
		return options2;
	}

	public void setOptions2(final String options2) {
		this.options2 = options2;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("referenceName:");
		sb.append(referenceName);
		sb.append(" ");
		sb.append("relation:");
		sb.append(relation);
		sb.append(" ");
		sb.append("input:");
		sb.append(input);
		sb.append(" ");
		sb.append("operation:");
		sb.append(operation);
		sb.append(" ");
		sb.append("input2:");
		sb.append(input2);
		sb.append(" ");
		sb.append("options:");
		sb.append(options);
		sb.append(" ");
		sb.append("options2:");
		sb.append(options2);
		return sb.toString();
	}

}