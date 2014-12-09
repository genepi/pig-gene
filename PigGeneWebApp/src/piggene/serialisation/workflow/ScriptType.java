package piggene.serialisation.workflow;

public class ScriptType {
	private String name;
	private int id;

	public ScriptType() {
		super();
	}

	public ScriptType(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}