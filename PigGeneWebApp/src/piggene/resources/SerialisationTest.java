package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import piggene.ServerResponseObject;
import piggene.serialisation.JSONConverter;
import piggene.serialisation.WorkflowComponent;
import piggene.serialisation.WorkflowWriter;

public class SerialisationTest extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		try {
			final JSONArray array = getJsonArray(entity);
			final ArrayList<WorkflowComponent> workflow = JSONConverter.convertJsonArrayIntoWorkflow(array);
			WorkflowWriter.write(workflow, "myWorkflow");
			obj.setSuccess(true);
			obj.setMessage("success");

			// Test-object will be returned...
			final MyTestObject objectJava = new MyTestObject();
			objectJava.setOper("FILTER");
			objectJava.setOpt("<5");
			objectJava.setRel("a");
			objectJava.setRel2("b");
			final MyTestObject[] objectJavaArr = new MyTestObject[] { objectJava };
			obj.setData(objectJavaArr);

		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage(e.getMessage());
			e.printStackTrace();
		}
		final JSONObject responseObj = JSONObject.fromObject(obj);
		return new StringRepresentation(responseObj.toString(), MediaType.APPLICATION_JSON);
	}

	private JSONArray getJsonArray(final Representation entity) throws IOException, JSONException {
		final JsonRepresentation representant = new JsonRepresentation(entity);
		return representant.getJsonArray();
	}

}