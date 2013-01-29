package piggene.serialisation;

import java.io.File;
import java.util.ArrayList;

public class WorkflowFiles {

	public static ArrayList<String> getAllFileNames() {
		final File file = new File("yamlFiles/");
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