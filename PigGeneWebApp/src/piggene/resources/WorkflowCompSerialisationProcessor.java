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

import piggene.exceptions.UnpossibleWorkflowComponentFileOperation;
import piggene.response.ServerResponseObject;
import piggene.serialisation.JSONConverter;
import piggene.serialisation.SingleWorkflowElement;
import piggene.serialisation.UntouchableFiles;
import piggene.serialisation.WorkflowComponent;
import piggene.serialisation.WorkflowComponentWriter;

import com.google.gson.JsonSyntaxException;

public class WorkflowCompSerialisationProcessor extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		WorkflowComponent component;

		try { // parse the input
			final JSONArray array = getJsonArray(entity);
			component = processClientData(array);
			if (UntouchableFiles.list.contains(component.getName())) {
				throw new UnpossibleWorkflowComponentFileOperation();
			}
		} catch (final UnpossibleWorkflowComponentFileOperation e) {
			obj.setSuccess(false);
			obj.setMessage("It is impossible to override an example workflow component!");
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
			obj.setMessage("An error occured while processing the workflow component.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try { // write workflow component yaml-file
			WorkflowComponentWriter.write(component);
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

	private WorkflowComponent processClientData(final JSONArray array) throws JsonSyntaxException, JSONException {
		final String filename = array.getString(array.length() - 1);
		ArrayList<SingleWorkflowElement> wfComponent = JSONConverter.convertJsonArrayIntoWorkflow(array);
		return new WorkflowComponent(filename, wfComponent);
	}

	private JSONArray getJsonArray(final Representation entity) throws JSONException, IOException {
		JsonRepresentation representant = null;
		JSONArray data = null;
		representant = new JsonRepresentation(entity);
		data = representant.getJsonArray();
		return data;
	}

}