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
import piggene.serialisation.workflow.WorkflowConverter;
import piggene.serialisation.workflow.WorkflowSerialisation;

public class WorkflowStorageService extends ServerResource {

	@Override
	protected Representation post(final Representation entity) throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();
		Workflow workflow = null;

		try {
			JsonRepresentation representation = new JsonRepresentation(entity);
			org.json.JSONObject data = representation.getJsonObject();
			workflow = WorkflowConverter.processClientJSONData(data);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try {
			WorkflowSerialisation.store(workflow);
		} catch (IOException e) {
			
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