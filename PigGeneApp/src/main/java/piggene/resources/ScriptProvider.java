package piggene.resources;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * ScriptProvider class is used to provide the created pig script for
 * downloading.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class ScriptProvider extends ServerResource {
	private static final String EXTENSION = ".pig";
	private static Properties prop = new Properties();
	private static String pigFiles;

	static {
		try {
			prop.load(ScriptProvider.class.getClassLoader().getResourceAsStream("config.properties"));
			pigFiles = prop.getProperty("pigFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	@Get
	public Representation get() {
		final String name = getRequest().getAttributes().get("filename").toString();
		final String file = pigFiles.concat(name.concat(EXTENSION));

		if (file == null || !new File(file).isFile()) {
			return new StringRepresentation("Error: Server was not able to load the content of the script.", MediaType.TEXT_PLAIN);
		}
		return new FileRepresentation(file, MediaType.TEXT_PLAIN);
	}

}