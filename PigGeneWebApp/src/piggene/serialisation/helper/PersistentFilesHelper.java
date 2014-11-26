package piggene.serialisation.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

	public static List<String> getAllWorkflowFileNames() {
		final File file = new File(workflowDefs);
		final File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		final List<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			fileNames.add(f.getName());
		}
		return fileNames;
	}

	public static List<String> getAllWorkflowFileNamesWithoutExtension() {
		final File file = new File(workflowDefs);
		final File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		final List<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			fileNames.add(f.getName().replaceAll(YAML_EXTENSION, ""));
		}
		Collections.sort(fileNames, new Comparator<String>() {
			@Override
			public int compare(String name1, String name2) {
				return name1.compareTo(name2);
			}
		});
		return fileNames;
	}

	public static boolean deleteFile(final String filename) {
		final File pigFile = new File(pigFiles.concat(filename.concat(PIG_EXTENSION)));
		final File yamlFileWorkflow = new File(workflowDefs.concat(filename.concat(YAML_EXTENSION)));
		final File yamlFileCloudgene = new File(cloudgeneYaml.concat(filename.concat(YAML_EXTENSION)));
		return pigFile.delete() && yamlFileWorkflow.delete() && yamlFileCloudgene.delete();
	}

}