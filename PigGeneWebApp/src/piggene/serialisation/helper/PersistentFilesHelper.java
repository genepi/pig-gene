package piggene.serialisation.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class PersistentFilesHelper {
	private static Properties prop = new Properties();
	private static String workflowDefs;
	private static String cloudgeneYaml;
	private static String pigFiles;

	private static final String YAML_EXTENSION = ".yaml";
	private static final String PIG_EXTENSION = ".pig";

	static {
		try {
			prop.load(PersistentFilesHelper.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefs = prop.getProperty("workflowDefs");
			cloudgeneYaml = prop.getProperty("cloudgeneYaml");
			pigFiles = prop.getProperty("pigFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean doesWorkflowFileExist(final String filename) {
		final File file = new File(workflowDefs.concat(filename.concat(YAML_EXTENSION)));
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static ArrayList<String> getAllWorkflowFileNames() {
		final File file = new File(workflowDefs);
		final File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		final ArrayList<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			fileNames.add(f.getName());
		}
		return fileNames;
	}

	public static ArrayList<String> getAllWorkflowFileNamesWithoutExtension() {
		final File file = new File(workflowDefs);
		final File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		final ArrayList<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			fileNames.add(f.getName().replaceAll(YAML_EXTENSION, ""));
		}
		return fileNames;
	}

	public static boolean deleteFile(final String filename) {
		final File pigFile = new File(pigFiles.concat(filename.concat(PIG_EXTENSION)));
		final File yamlFileWorkflow = new File(workflowDefs.concat(filename.concat(YAML_EXTENSION)));
		final File yamlFileCloudgene = new File(cloudgeneYaml.concat(filename.concat(YAML_EXTENSION)));
		return pigFile.delete() && yamlFileWorkflow.delete() && yamlFileCloudgene.delete();
	}

}