package piggene.serialisation;

import java.io.File;
import java.util.ArrayList;

/**
 * PersistentFiles class is used for operations on the workflow and workflow
 * component definition files saved on the file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class PersistentFiles {
	private static final String YAML_PATH_WORKFLOW = "workflowDefs/";
	private static final String YAML_PATH_WORKFLOWCOMPONENT = "workflowComp/";
	private static final String YAML_PATH_CLOUDGENE = "apps/piggene/";
	private static final String PIG_PATH = "apps/piggene/";
	private static final String YAML_EXTENSION = ".yaml";
	private static final String PIG_EXTENSION = ".pig";

	public static boolean doesWorkflowFileExist(final String filename) {
		final File file = new File(YAML_PATH_WORKFLOW.concat(filename.concat(YAML_EXTENSION)));
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static boolean doesWorkflowComponentFileExist(final String filename) {
		final File file = new File(YAML_PATH_WORKFLOWCOMPONENT.concat(filename.concat(YAML_EXTENSION)));
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static ArrayList<String> getAllWorkflowFileNames() {
		final File file = new File(YAML_PATH_WORKFLOW);
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

	public static ArrayList<String> getAllWorkflowComponentFileNames() {
		final File file = new File(YAML_PATH_WORKFLOWCOMPONENT);
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

	public static boolean deleteFile(final String filename) {
		final File pigFile = new File(PIG_PATH.concat(filename.concat(PIG_EXTENSION)));
		final File yamlFileWorkflow = new File(YAML_PATH_WORKFLOW.concat(filename.concat(YAML_EXTENSION)));
		final File yamlFileCloudgene = new File(YAML_PATH_CLOUDGENE.concat(filename.concat(YAML_EXTENSION)));
		return pigFile.delete() && yamlFileWorkflow.delete() && yamlFileCloudgene.delete();
	}

}