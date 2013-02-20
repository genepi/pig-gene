package piggene.serialisation;

import java.io.File;
import java.util.ArrayList;

public class WorkflowFiles {
	private static final String PATH = "yamlFiles/";
	private static final String EXTENSION = ".yaml";

	public static boolean doesFileExist(final String filename) {
		final File file = new File(PATH.concat(filename.concat(EXTENSION)));
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
			fileNames.add(f.getName());
		}
		return fileNames;
	}

}