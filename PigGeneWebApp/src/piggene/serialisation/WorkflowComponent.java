package piggene.serialisation;

import java.io.Serializable;

public class WorkflowComponent implements Serializable {
	private static final long serialVersionUID = -7293718140809017884L;

	private String relName;
	private String relation;
	private String operation;
	private String relation2;
	private String options;
	private String options2;

	public String getRelName() {
		return relName;
	}

	public void setRelName(final String relName) {
		this.relName = relName;
	}

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

	public String getOptions2() {
		return options2;
	}

	public void setOptions2(final String options2) {
		this.options2 = options2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("relName:");
		sb.append(relName);
		sb.append(" ");
		sb.append("relation:");
		sb.append(relation);
		sb.append(" ");
		sb.append("operation:");
		sb.append(operation);
		sb.append(" ");
		sb.append("relation2:");
		sb.append(relation2);
		sb.append(" ");
		sb.append("options:");
		sb.append(options);
		sb.append(" ");
		sb.append("options2:");
		sb.append(options2);
		return sb.toString();
	}

}