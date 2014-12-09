package piggene.serialisation.RMarkDown;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import piggene.serialisation.workflow.Workflow;

public class RMarkDownGenerator {
	private static Properties prop = new Properties();
	private static String scriptFilesPath;

	static {
		try {
			prop.load(RMarkDownGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			scriptFilesPath = prop.getProperty("scriptFiles");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAndStoreScripts(final Workflow workflow) throws IOException {
		createNeededFolders(workflow.getName());
		final List<String> rmdScripts = workflow.getRMarkDownScriptRepresentations();
		if (rmdScripts.size() == 1) {
			RMarkDownGenerator.write(rmdScripts.get(0), workflow.getName(), workflow.getName());
		} else if (rmdScripts.size() > 1) {
			for (int i = 0; i < rmdScripts.size(); i++) {
				RMarkDownGenerator.write(rmdScripts.get(i), workflow.getName(), workflow.getName().concat("_").concat(String.valueOf(i)));
			}
		}
	}

	private static void createNeededFolders(final String folderName) throws IOException {
		final File destinationFolder = new File(scriptFilesPath + folderName + "/");
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
	}

	private static void write(final String script, final String folder, final String name) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(scriptFilesPath.concat(folder).concat("/").concat(name.concat(".Rmd"))));
			out.write(script);
		} catch (final Exception e) {
			// TODO change catch clause
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (final IOException ignore) {
				// ignore
			}
		}
	}

}