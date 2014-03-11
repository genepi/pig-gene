package piggene.resources;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class MyRestTest extends ServerResource {

	@Override
	public Representation get() throws ResourceException {
		int id = Integer.parseInt(getRequest().getAttributes().get("id").toString());
		System.out.println("the sent id was: " + id);
		return new StringRepresentation("GREAT");
	}

}
