package piggene.resources;

import java.io.File;

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
	private static final String PATH = "apps/piggene/";
	private static final String EXTENSION = ".pig";

	@Override
	@Get
	public Representation get() {
		final String name = getRequest().getAttributes().get("filename").toString();
		final String path = PATH.concat(name.concat(EXTENSION));

		if (path == null || !new File(path).isFile()) {
			return new StringRepresentation("Error: Server was not able to load the content of the script.", MediaType.TEXT_PLAIN);
		}
		return new FileRepresentation(path, MediaType.TEXT_PLAIN);
	}

}