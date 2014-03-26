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

import piggene.serialisation.WorkflowConverter;
import piggene.serialisation.WorkflowSerialisation;
import piggene.serialisation.scriptcreation.PigScript;
import piggene.serialisation.workflow.Workflow;

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
			PigScript.generateAndWrite(workflow);
			// TODO
			// CloudgeneYaml.generateCloudgeneYamlFile(workflow);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while saving the submitted workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	// private Workflow processClientWfData(final org.json.JSONObject data)
	// throws JSONException {
	// String name = data.getString("name");
	// String description = data.getString("description");
	// ArrayList<SingleWorkflowElement> workflowElements =
	// WorkflowConverter.convertJSONWorkflow(data
	// .getJSONArray("workflow"));
	// ArrayList<String> inputParameters =
	// WorkflowConverter.convertJSONWorkflowParams(data
	// .getJSONArray("inputParameters"));
	// ArrayList<String> outputParameters =
	// WorkflowConverter.convertJSONWorkflowParams(data
	// .getJSONArray("outputParameters"));
	// return new Workflow(name, description, workflowElements, inputParameters,
	// outputParameters);
	// }

}