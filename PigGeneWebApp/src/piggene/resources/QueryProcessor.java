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
import piggene.serialisation.WorkflowReader;
import piggene.serialisation.WorkflowWriter;
import piggene.serialisation.scriptcreation.PigScript;

import com.google.gson.JsonSyntaxException;

public class QueryProcessor extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		final ArrayList<WorkflowComponent> workflow = processClientData(entity);

		// pig-script
		try {
			PigScript.generateAndWrite(workflow, "myScript");
		} catch (final IOException e1) {
			// TODO return pigscript-error info...
		}

		// yaml-file
		try {
			WorkflowWriter.write(workflow, "myWorkflow");
		} catch (final IOException e) {
			e.printStackTrace();
			// TODO return yaml-error info...
		}

		// /////////// just for testing purpose...
		try {
			System.out.println("output: " + WorkflowReader.read("myWorkflow"));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ///////////////////////////

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private ArrayList<WorkflowComponent> processClientData(final Representation entity) {
		final JSONArray array = getJsonArray(entity);
		ArrayList<WorkflowComponent> workflow = null;
		try {
			workflow = JSONConverter.convertJsonArrayIntoWorkflow(array);
		} catch (final JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workflow;
	}

	private JSONArray getJsonArray(final Representation entity) {
		JsonRepresentation representant;
		JSONArray data = null;
		try {
			representant = new JsonRepresentation(entity);
			data = representant.getJsonArray();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}