package piggene.serialisation.pig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import piggene.serialisation.workflow.Workflow;

public class PigScriptGenerator {
	private static Properties prop = new Properties();
	private static String pigFilesPath;
	private static String libFilesPath;
	private static String jarPath = "libs/";
	private static String[] jarLibNames = new String[] { "pigGene.jar", "SeqPig.jar", "hadoop-bam-6.2.jar", "samtools-1.107.jar", "picard-1.107.jar", "commons-jexl-2.1.1.jar"};

	static {
		try {
			prop.load(PigScriptGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			pigFilesPath = prop.getProperty("pigFiles");
			libFilesPath = prop.getProperty("libFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAndStoreScript(final Workflow workflow) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append(insertHeader());
		sb.append(workflow.getPigScriptRepresentation(workflow.getName()));
		createNeededFolders(workflow.getName());
		PigScriptGenerator.write(sb.toString(), workflow.getName());
	}
	
	private static void createNeededFolders(String folderName) throws IOException {
		File destinationFolder = new File(pigFilesPath + folderName + "/");
		File libs = null;
		if(!destinationFolder.exists()) {
			destinationFolder.mkdir();
			libs = new File(destinationFolder.getPath() + "/libs");
			libs.mkdir();
			copyLibFiles(libFilesPath.concat("/"), libs.toString().concat("/"));
		}
	}
	
	private static void copyLibFiles(String sourceFolderPath, String destinationFolderPath) throws IOException {
		File source = null;
		File destination = null;
		for(String fileName : jarLibNames) {
			source = new File(sourceFolderPath.concat(fileName));
			destination = new File(destinationFolderPath.concat(fileName));
			Files.copy(source.toPath(), destination.toPath());
		}
	}

	private static String insertHeader() {
		final StringBuilder sb = new StringBuilder();
		for (final String lib : jarLibNames) {
			sb.append("REGISTER ");
			sb.append(jarPath);
			sb.append(lib);
			sb.append(";");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	private static void write(final String pigScript, final String name) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(pigFilesPath.concat(name).concat("/").concat(name.concat(".pig"))));
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