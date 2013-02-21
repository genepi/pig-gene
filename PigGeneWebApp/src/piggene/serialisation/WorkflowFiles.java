package piggene.serialisation;

import java.io.File;
import java.util.ArrayList;

public class WorkflowFiles {
	private static final String YAML_PATH = "yamlFiles/";
	private static final String PIG_PATH = "pigScripts/";
	private static final String YAML_EXTENSION = ".yaml";
	private static final String PIG_EXTENSION = ".pig";

	public static boolean doesFileExist(final String filename) {
		final File file = new File(YAML_PATH.concat(filename.concat(YAML_EXTENSION)));
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static ArrayList<String> getAllFileNames() {
		final File file = new File(YAML_PATH);
		final File[] files = file.listFiles();
		if (files.length == 0) {
			return null;
		}
		final ArrayList<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			fileNames.add(f.getName());
		}
		return fileNames;
	}

	public static boolean deleteFile(final String filename) {
		final File pigFile = new File(PIG_PATH.concat(filename.concat(PIG_EXTENSION)));
		final File yamlFile = new File(YAML_PATH.concat(filename.concat(YAML_EXTENSION)));
		return pigFile.delete() && yamlFile.delete();
	}

}