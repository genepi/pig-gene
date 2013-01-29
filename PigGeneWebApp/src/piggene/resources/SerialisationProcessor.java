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

import piggene.response.ServerResponseObject;
import piggene.serialisation.JSONConverter;
import piggene.serialisation.WorkflowComponent;
import piggene.serialisation.WorkflowWriter;
import piggene.serialisation.scriptcreation.PigScript;

import com.google.gson.JsonSyntaxException;

public class SerialisationProcessor extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		ArrayList<WorkflowComponent> workflow;
		String filename = "filename";

		try { // parse the input
			final JSONArray array = getJsonArray(entity);
			filename = array.getString(array.length() - 1);
			workflow = processClientData(array);
		} catch (final JsonSyntaxException e2) {
			obj.setSuccess(false);
			obj.setMessage("The workflow could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e2) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while processing the workflow.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try { // write pig-script
			PigScript.generateAndWrite(workflow, filename);
		} catch (final IOException e1) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while creating the pig-script.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try { // write yaml-file
			WorkflowWriter.write(workflow, filename);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while saving the workflow.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private ArrayList<WorkflowComponent> processClientData(final JSONArray array) throws JsonSyntaxException, JSONException {
		ArrayList<WorkflowComponent> workflow = null;
		workflow = JSONConverter.convertJsonArrayIntoWorkflow(array);
		return workflow;
	}

	// TODO: think about the exception handling of this method...
	private JSONArray getJsonArray(final Representation entity) throws JSONException {
		JsonRepresentation representant = null;
		JSONArray data = null;
		try {
			representant = new JsonRepresentation(entity);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = representant.getJsonArray();
		return data;
	}

}