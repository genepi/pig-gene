package piggene.serialisation;

import java.io.Serializable;

public class WorkflowComponent implements Serializable {
	private static final long serialVersionUID = -7293718140809017884L;

	private String relation;
	private String operation;
	private String relation2;
	private String options;

	public String getRelation() {
		return relation;
	}

	public void setRelation(final String relation) {
		this.relation = relation;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
	}

	public String getRelation2() {
		return relation2;
	}

	public void setRelation2(final String relation2) {
		this.relation2 = relation2;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(final String options) {
		this.options = options;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("relation: ");
		sb.append(relation);
		sb.append(" ");
		sb.append("operation: ");
		sb.append(operation);
		sb.append(" ");
		sb.append("relation2: ");
		sb.append(relation2);
		sb.append(" ");
		sb.append("options: ");
		sb.append(options);
		sb.append(" ");
		return sb.toString();
	}

}