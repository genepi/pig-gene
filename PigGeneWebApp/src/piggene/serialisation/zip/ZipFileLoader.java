package piggene.serialisation.zip;

import java.io.IOException;
import java.util.Properties;

import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class ZipFileLoader {
	private static Properties prop = new Properties();
	private static String zipFilePath;
	private static String zipFileExtension = ".zip";

	static {
		try {
			prop.load(WorkflowSerialisation.class.getClassLoader().getResourceAsStream("config.properties"));
			zipFilePath = prop.getProperty("zipFiles");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static String returnFilePath(final String name) {
		return zipFilePath.concat(name).concat(zipFileExtension);
	}

}