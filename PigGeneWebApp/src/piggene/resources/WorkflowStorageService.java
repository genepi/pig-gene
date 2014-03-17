package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.JSONConverter;
import piggene.serialisation.SingleWorkflowElement;
import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowWriter;
import piggene.serialisation.scriptcreation.PigScript;
import piggene.serialisation.yaml.CloudgeneYaml;

public class WorkflowStorageService extends ServerResource {

	@Override
	protected Representation post(final Representation entity) throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();
		Workflow workflow = null;

		try {
			JsonRepresentation representation = new JsonRepresentation(entity);
			org.json.JSONObject data = representation.getJsonObject();
			workflow = processClientWfData(data);
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
			WorkflowWriter.write(workflow);
			PigScript.generateAndWrite(workflow);
			CloudgeneYaml.generateCloudgeneYamlFile(workflow);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while saving the submitted workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private Workflow processClientWfData(final org.json.JSONObject data) throws JSONException {
		String name = data.getString("name");
		String description = data.getString("description");
		ArrayList<SingleWorkflowElement> workflowElements = JSONConverter.convertJSONWorkflow(data.getJSONArray("workflow"));
		ArrayList<String> inputParameters = JSONConverter.convertJSONWorkflowParams(data.getJSONArray("inputParameters"));
		ArrayList<String> outputParameters = JSONConverter.convertJSONWorkflowParams(data.getJSONArray("outputParameters"));
		return new Workflow(name, description, workflowElements, inputParameters, outputParameters);
	}

}