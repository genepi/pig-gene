package piggene.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import piggene.response.ServerResponseObject;
import piggene.serialisation.Workflow;

public class WorkflowSaverService extends ServerResource {

	@Override
	public Representation post(final Representation entity) {
		ServerResponseObject obj = new ServerResponseObject();

		System.out.println("starting to access sent data...");
		JsonRepresentation representant = null;
		JSONObject wfDefinition = null;
		try {
			representant = new JsonRepresentation(entity);
			wfDefinition = representant.getJsonObject();

			// Workflow workflow = processClientWfData(wfDefinition);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return null;
	}

	private Workflow processClientWfData(final JSONObject wfDefinition) {
		System.out.println("i was hereeeeeeee2");
		return null;
		// final String description = wfDefinition.getString(array.length() -
		// 2);
		// final String filename = array.getString(array.length() - 1);
		// ArrayList<SingleWorkflowElement> workflow =
		// JSONConverter.convertJsonArrayIntoWorkflow(array);
		// return new Workflow(filename, description, workflow);
	}
}
