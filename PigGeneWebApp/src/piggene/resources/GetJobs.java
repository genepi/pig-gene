package piggene.resources;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class GetJobs extends ServerResource {

	@Get
	public Representation get() {

		return new StringRepresentation("Test Res!");

	}

}