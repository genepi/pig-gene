package piggene.resources;

import java.io.File;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class ScriptProvider extends ServerResource {
	private static final String DIRECTORY = "pigScripts/";
	private static final String EXTENSION = ".pig";

	@Override
	@Get
	public Representation get() throws ResourceException {
		final String name = getRequest().getAttributes().get("filename").toString();
		final String path = DIRECTORY.concat(name.concat(EXTENSION));

		if (new File(path).isFile()) {
			return new FileRepresentation(path, MediaType.TEXT_PLAIN);
		}

		// TODO: change response
		return new StringRepresentation("damn", MediaType.TEXT_PLAIN);
	}

}