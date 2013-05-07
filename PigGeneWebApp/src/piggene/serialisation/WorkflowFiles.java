package piggene.serialisation;

import java.io.File;
import java.util.ArrayList;

/**
 * WorkflowFiles class is used for operations on the workflow definition files
 * saved on the file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WorkflowFiles {
	private static final String PATH = "apps/piggene/";
	private static final String YAML_EXTENSION = ".yaml";
	private static final String PIG_EXTENSION = ".pig";

	public static boolean doesFileExist(final String filename) {
		final File file = new File(PATH.concat(filename.concat(YAML_EXTENSION)));
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static ArrayList<String> getAllFileNames() {
		final File file = new File(PATH);
		final File[] files = file.listFiles();
		if (files.length == 0) {
			return null;
		}
		final ArrayList<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			if (!f.getName().endsWith(".pig")) {
				fileNames.add(f.getName());
			}
		}
		return fileNames;
	}

	public static boolean deleteFile(final String filename) {
		final File pigFile = new File(PATH.concat(filename.concat(PIG_EXTENSION)));
		final File yamlFile = new File(PATH.concat(filename.concat(YAML_EXTENSION)));
		return pigFile.delete() && yamlFile.delete();
	}

}