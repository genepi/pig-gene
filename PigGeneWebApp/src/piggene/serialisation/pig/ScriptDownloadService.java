package piggene.serialisation.pig;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.resources.ServerResponseObject;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowSerialisation;

public class ScriptDownloadService extends ServerResource {

	@Override
	protected Representation get() throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();

		String workflowName = getRequest().getAttributes().get("id").toString();
		try {
			Workflow workflow = WorkflowSerialisation.load(workflowName);
			PigScriptGenerator.generateAndStoreScript(workflow);
			String script = PigScript.load(workflowName);
			if (script == null) {
				throw new IOException("could not load generated pig script.");
			}
			obj.setData(script);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while generating the script.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (MissingParameterException e) {
			obj.setSuccess(false);
			obj.setMessage(e.getMessage());
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}