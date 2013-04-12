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

import piggene.exceptions.UnpossibleWorkflowFileOperation;
import piggene.response.ServerResponseObject;
import piggene.serialisation.JSONConverter;
import piggene.serialisation.UntouchableFiles;
import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowComponent;
import piggene.serialisation.WorkflowWriter;
import piggene.serialisation.scriptcreation.PigScript;

import com.google.gson.JsonSyntaxException;

/**
 * SerialisationProcessor class is used to serialize a workflow. The workflow
 * definition is saved in a yaml-file. Additionally a pig script is created.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class SerialisationProcessor extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		Workflow workflow;

		try { // parse the input
			final JSONArray array = getJsonArray(entity);
			workflow = processClientData(array);
			if (UntouchableFiles.list.contains(workflow.getName())) {
				throw new UnpossibleWorkflowFileOperation();
			}
		} catch (final UnpossibleWorkflowFileOperation e) {
			obj.setSuccess(false);
			obj.setMessage("It is impossible to override an example workflow!");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JsonSyntaxException e) {
			obj.setSuccess(false);
			obj.setMessage("The workflow could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while processing the workflow.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try { // write pig-script
			PigScript.generateAndWrite(workflow);
		} catch (final IOException e1) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while creating the pig-script.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try { // write yaml-file
			WorkflowWriter.write(workflow);
		} catch (final IOException e) {
			e.printStackTrace();
			obj.setSuccess(false);
			obj.setMessage("An error occured while saving the workflow.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private Workflow processClientData(final JSONArray array) throws JsonSyntaxException, JSONException {
		ArrayList<WorkflowComponent> workflow = null;
		final String description = array.getString(array.length() - 2);
		final String filename = array.getString(array.length() - 1);
		workflow = JSONConverter.convertJsonArrayIntoWorkflow(array);
		return new Workflow(filename, description, workflow);
	}

	private JSONArray getJsonArray(final Representation entity) throws JSONException, IOException {
		JsonRepresentation representant = null;
		JSONArray data = null;
		representant = new JsonRepresentation(entity);
		data = representant.getJsonArray();
		return data;
	}

}