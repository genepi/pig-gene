package piggene.resources;

import net.sf.json.JSONArray;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class GetJobs extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		// testing normal string representation...
		// Form form = new Form(entity);
		// String name = (String) form.getFirstValue("name");
		// return new StringRepresentation("Hallo " + name);

		// testing JSON representation...
		final MyTestObject objectJAVA = new MyTestObject();
		objectJAVA.setOper("FILTER");
		objectJAVA.setOpt("<5");
		objectJAVA.setRel("a");
		objectJAVA.setRel2("b");
		
		final JSONArray jArray = JSONArray.fromObject(objectJAVA);
		return new StringRepresentation(jArray.toString(), MediaType.APPLICATION_JSON);
	}

}