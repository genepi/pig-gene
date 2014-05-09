package piggene.serialisation.pig;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Properties;
import java.nio.file.Path;


public class PigScript {
	private static Properties prop = new Properties();
	private static String pigFilesPath;
	private static String fileExtension = ".pig";

	static {
		try {
			prop.load(PigScriptGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			pigFilesPath = prop.getProperty("pigFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String load(String scriptName) {
		Path path = FileSystems.getDefault().getPath(pigFilesPath, scriptName.concat(fileExtension));
		String script = null;
		try {
			byte[] bytes = Files.readAllBytes(path);
			script = new String(bytes, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return script;
	}
	
}