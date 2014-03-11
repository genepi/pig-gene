package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;

import piggene.exceptions.UnpossibleWorkflowFileOperation;
import piggene.response.ServerResponseObject;
import piggene.serialisation.JSONConverter;
import piggene.serialisation.SingleWorkflowElement;
import piggene.serialisation.UntouchableFiles;
import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowReader;
import piggene.serialisation.WorkflowWriter;
import piggene.serialisation.scriptcreation.PigScript;
import piggene.serialisation.yaml.CloudgeneYaml;

import com.google.gson.JsonSyntaxException;

/**
 * SerialisationService class is used to serialize workflows and workflow
 * components. The definition is saved in a yaml-file. For a Workflow,
 * additionally a pig script is created.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class SerialisationService extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		Workflow workflow = null;

		try { // parse the input
			final JSONArray array = getJsonArray(entity);
			workflow = processClientWfData(array);

			// TODO ��berlegen was in diesem Fall zu tun ist - unten bereits neue
			// Eintragung gemacht...
			if (UntouchableFiles.list.contains(workflow.getName())) {
				throw new UnpossibleWorkflowFileOperation();
			}
		} catch (final UnpossibleWorkflowFileOperation e) {
			obj.setSuccess(false);
			obj.setMessage("It is impossible to override an example!");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JsonSyntaxException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while processing the data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try {
			WorkflowWriter.write(workflow);

			workflow = resolveWfReferences(workflow);
			PigScript.generateAndWrite(workflow);
			CloudgeneYaml.generateCloudgeneYamlFile(workflow);
		} catch (final IOException e) {
			e.printStackTrace();
			obj.setSuccess(false);
			obj.setMessage("An error occured while saving the submitted data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private JSONArray getJsonArray(final Representation entity) throws JSONException, IOException {
		JsonRepresentation representant = null;
		JSONArray data = null;
		representant = new JsonRepresentation(entity);
		data = representant.getJsonArray();
		return data;
	}

	private Workflow processClientWfData(final JSONArray array) throws JsonSyntaxException, JSONException {
		final String description = array.getString(array.length() - 2);
		final String filename = array.getString(array.length() - 1);
		ArrayList<SingleWorkflowElement> workflow = JSONConverter.convertJsonArrayIntoWorkflow(array);
		return new Workflow(filename, description, workflow);
	}

	private Workflow resolveWfReferences(Workflow workflow) throws IOException {
		ArrayList<SingleWorkflowElement> allWfElements = new ArrayList<SingleWorkflowElement>();
		for (final SingleWorkflowElement comp : workflow.getWorkflow()) {
			String referenceName = comp.getReferenceName();
			if (referenceName != null) {
				Workflow referencedWf = WorkflowReader.read(referenceName);
				// TODO
				// UntouchableFiles.referencedWorkflows.add(referencedWf.getName());
				// recursion einbauen
				for (SingleWorkflowElement refComp : referencedWf.getWorkflow()) {
					allWfElements.add(refComp);
				}
			} else {
				allWfElements.add(comp);
			}
		}
		return new Workflow(workflow.getName(), workflow.getDescription(), allWfElements);
	}

}