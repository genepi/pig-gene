package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowConverter;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowStorageService extends ServerResource {

	@Override
	protected Representation post(final Representation entity) throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();
		Workflow workflow = null;
		String encodedWfName = null;
		String type = null;

		try {
			final JsonRepresentation representation = new JsonRepresentation(entity);
			final org.json.JSONObject transferedData = representation.getJsonObject();
			workflow = WorkflowConverter.processClientJSONData(transferedData.getJSONObject("workflow"));
			type = transferedData.getString("type");
			encodedWfName = transferedData.getString("encodedName");
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			e.printStackTrace();
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try {
			WorkflowSerialisation.store(workflow, encodedWfName, type);
		} catch (final IOException e) {
			e.printStackTrace();
			obj.setSuccess(false);
			obj.setMessage("An error occured while saving the submitted workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}
}