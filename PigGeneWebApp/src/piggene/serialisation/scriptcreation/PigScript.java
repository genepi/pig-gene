package piggene.serialisation.scriptcreation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowComponent;

/**
 * PigScript class is responsible for creating and writing the pig script to the
 * file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class PigScript {
	private static final String JAR_FILENAME = "pigGene.jar";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final char SEMICOLON = ';';
	private static final String PATH = "pigScripts/";

	public static void generateAndWrite(final Workflow workflow) throws IOException {
		final StringBuilder sb = new StringBuilder();
		if (!workflow.getDescription().equals("")) {
			sb.append("//");
			sb.append(workflow.getDescription());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(insertHeader());
		for (final WorkflowComponent comp : workflow.getWorkflow()) {
			sb.append(PigSnippetFactory.getPigScriptSnippet(comp));
			sb.append(SEMICOLON);
			sb.append(LINE_SEPARATOR);
		}
		PigScript.write(sb.toString(), workflow.getName());
	}

	private static String insertHeader() {
		final StringBuilder sb = new StringBuilder();
		sb.append("REGISTER ");
		sb.append(JAR_FILENAME);
		sb.append(SEMICOLON);
		sb.append(LINE_SEPARATOR);
		return sb.toString();
	}

	private static void write(final String pigScript, final String name) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(PATH.concat(name.concat(".pig"))));
			out.write(pigScript);
		} finally {
			try {
				out.close();
			} catch (final IOException e) {
				// ignore
			}
		}
	}

}