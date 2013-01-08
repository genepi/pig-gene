package piggene.resources;

import net.sf.json.JSONArray;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import piggene.serialisation.WorkflowComponent;

public class GetJobs extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		// testing normal string representation...
		// Form form = new Form(entity);
		// String name = (String) form.getFirstValue("name");
		// return new StringRepresentation("Hallo " + name);

		// testing JSON representation...
		final WorkflowComponent objectJAVA = new WorkflowComponent();
		objectJAVA.setRelation("a");
		objectJAVA.setOperation("FILTER");
		objectJAVA.setOptions("<5");
		objectJAVA.setRelation2("b");

		final JSONArray jArray = JSONArray.fromObject(objectJAVA);
		return new StringRepresentation(jArray.toString(), MediaType.APPLICATION_JSON);
	}

}