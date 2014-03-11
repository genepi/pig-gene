package piggene.serialisation.scriptcreation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import piggene.serialisation.SingleWorkflowElement;
import piggene.serialisation.Workflow;

/**
 * PigScript class is responsible for creating and writing the pig script to the
 * file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class PigScript {
	private static final String[] jarFilenames = new String[] { "pigGene.jar", "SeqPig.jar", "hadoop-bam-6.0.jar", "sam-1.93.jar", "picard-1.93.jar", "variant-1.93.jar",
			"tribble-1.93.jar", "commons-jexl-2.1.1.jar" };
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final char SEMICOLON = ';';
	private static final String PATH = "apps/piggene/";

	public static void generateAndWrite(final Workflow workflow) throws IOException {
		final StringBuilder sb = new StringBuilder();
		// TODO relationen umbenennen!!!

		sb.append(insertDescription(workflow.getDescription()));
		sb.append(insertHeader());
		for (final SingleWorkflowElement comp : workflow.getWorkflow()) {
			sb.append(parseSnippet(comp));
		}
		PigScript.write(sb.toString(), workflow.getName());
	}

	private static String insertDescription(String description) {
		final StringBuilder sb = new StringBuilder();
		if (!description.equals("")) {
			sb.append("--");
			sb.append(description);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	private static String insertHeader() {
		final StringBuilder sb = new StringBuilder();
		for (final String name : jarFilenames) {
			sb.append("REGISTER ");
			sb.append(name);
			sb.append(SEMICOLON);
			sb.append(LINE_SEPARATOR);
		}
		return sb.toString();
	}

	private static String parseSnippet(SingleWorkflowElement comp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(PigSnippetFactory.getPigScriptSnippet(comp));
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
			} catch (final IOException ignore) {
				// ignore
			}
		}
	}

}