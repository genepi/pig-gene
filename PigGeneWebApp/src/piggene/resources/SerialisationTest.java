package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import piggene.serialisation.JSONConverter;
import piggene.serialisation.WorkflowComponent;
import piggene.serialisation.WorkflowWriter;

public class SerialisationTest extends ServerResource {

	@Override
	@Post
	// TODO: sinnvolles Exception-Handling einbauen...
	public Representation post(final Representation entity) {
		String message = null;

		JSONArray array = null;
		try {
			array = getJsonArray(entity);
			ArrayList<WorkflowComponent> workflow = JSONConverter.convertJsonArrayIntoWorkflow(array);
			WorkflowWriter.write(workflow, "myWorkflow");
			message = "output written...";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			message = "io -> exception!!!";
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			message = "json -> exception!!!";
			e.printStackTrace();
		}
		return new StringRepresentation(message); // message just for testing...
	}

	private JSONArray getJsonArray(final Representation entity) throws IOException, JSONException {
		JsonRepresentation representant = new JsonRepresentation(entity);
		return representant.getJsonArray();
	}

}