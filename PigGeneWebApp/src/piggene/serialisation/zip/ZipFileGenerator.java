package piggene.serialisation.zip;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import piggene.serialisation.RMarkDown.RMarkDownGenerator;
import piggene.serialisation.cloudgene.CloudgeneYamlGenerator;
import piggene.serialisation.pig.PigScriptGenerator;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class ZipFileGenerator {
	private static Properties prop = new Properties();
	private static String configurationFiles = "";
	private static String zipFilesPath = "";
	private static String zipExtension = ".zip";
	private static String wfAbbr;

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			configurationFiles = prop.getProperty("scriptFiles");
			zipFilesPath = prop.getProperty("zipFiles");
			wfAbbr = prop.getProperty("wfAbbr");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAndStoreFile(final String workflowName) throws IOException, ZipException {
		final Workflow workflow = WorkflowSerialisation.load(workflowName, wfAbbr);
		PigScriptGenerator.generateAndStoreScript(workflow);
		RMarkDownGenerator.generateAndStoreScripts(workflow);
		CloudgeneYamlGenerator.generateAndStoreFile(workflow);

		createNeededFolder(workflowName);
		final ZipFile zipFile = new ZipFile(zipFilesPath + workflowName + zipExtension);
		final ZipParameters zipParams = new ZipParameters();
		zipParams.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		zipParams.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		zipFile.addFolder(configurationFiles + workflowName, zipParams);
	}

	private static void createNeededFolder(final String workflowName) throws IOException {
		final String path = zipFilesPath + "/";
		final File destinationFolder = new File(path);
		if (destinationFolder.exists()) {
			final File file = new File(path + workflowName + zipExtension);
			if (file.exists()) {
				file.delete();
			}
		} else {
			destinationFolder.mkdir();
		}
	}

	public static void extractZipFile(final String source, final String destination) throws ZipException {
		final ZipFile zipFile = new ZipFile(source);
		zipFile.extractAll(destination);
	}

}