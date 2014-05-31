package piggene.serialisation.pig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import piggene.serialisation.workflow.Workflow;

public class PigScriptGenerator {
	private static Properties prop = new Properties();
	private static String pigFilesPath;

	static {
		try {
			prop.load(PigScriptGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			pigFilesPath = prop.getProperty("pigFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAndStoreScript(final Workflow workflow) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append(insertHeader());
		sb.append(workflow.getPigScriptRepresentation(false, workflow.getName()));
		PigScriptGenerator.write(sb.toString(), workflow.getName());
	}

	private static String insertHeader() {
		String[] jarFilenames = new String[] { "pigGene.jar", "SeqPig.jar", "hadoop-bam-6.0.jar", "sam-1.93.jar", "picard-1.93.jar",
				"variant-1.93.jar", "tribble-1.93.jar", "commons-jexl-2.1.1.jar" };
		final StringBuilder sb = new StringBuilder();
		for (final String name : jarFilenames) {
			sb.append("REGISTER ");
			sb.append(name);
			sb.append(";");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	private static void write(final String pigScript, final String name) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(pigFilesPath.concat(name.concat(".pig"))));
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