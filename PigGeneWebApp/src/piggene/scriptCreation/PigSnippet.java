package piggene.scriptCreation;

public abstract class PigSnippet implements PigScript {
	private static final String header = "REGISTER pigGene.jar;";

	public abstract String toPigScript();

	public static String getHeader() {
		return header;
	}

}