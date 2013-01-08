package piggene.serialisation.scriptcreation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import piggene.serialisation.WorkflowComponent;

public class PigScript {
	private static final String JAR_FILENAME = "pigGene.jar";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final char SEMICOLON = ';';

	public static void generateAndWrite(final ArrayList<WorkflowComponent> workflow, final String name) {
		final StringBuilder sb = new StringBuilder();
		sb.append(insertHeader());

		for (final WorkflowComponent comp : workflow) {
			sb.append(PigSnippetFactory.getPigScriptSnippet(comp));
			sb.append(SEMICOLON);
			sb.append(LINE_SEPARATOR);
		}
		PigScript.write(sb.toString(), name);
	}

	private static String insertHeader() {
		final StringBuilder sb = new StringBuilder();
		sb.append("REGISTER ");
		sb.append(JAR_FILENAME);
		sb.append(SEMICOLON);
		sb.append(LINE_SEPARATOR);
		return sb.toString();
	}

	private static void write(final String pigScript, final String name) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(name.concat(".pig")));
			out.write(pigScript);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}