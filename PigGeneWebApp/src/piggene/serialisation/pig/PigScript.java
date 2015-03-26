package piggene.serialisation.pig;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PigScript {
	private static Properties prop = new Properties();
	private static String scriptFilesPath;
	private static String fileExtension = ".pig";

	static {
		try {
			prop.load(PigScriptGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			scriptFilesPath = prop.getProperty("scriptFiles");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static String load(final String scriptName) throws IOException {
		final Path path = FileSystems.getDefault().getPath(scriptFilesPath, "/", scriptName, scriptName.concat(fileExtension));
		String script = null;
		final byte[] bytes = Files.readAllBytes(path);
		script = new String(bytes, "UTF-8");
		return script;
	}

}