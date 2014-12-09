package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.RMarkDown.RMarkDownGenerator;
import piggene.serialisation.cloudgene.CloudgeneYamlGenerator;
import piggene.serialisation.pig.PigScript;
import piggene.serialisation.pig.PigScriptGenerator;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class ScriptDownloadService extends ServerResource {

	@Override
	protected Representation get() throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();

		String workflowName = getRequest().getAttributes().get("id").toString();
		try {
			Workflow workflow = WorkflowSerialisation.load(workflowName);
			PigScriptGenerator.generateAndStoreScript(workflow);
			RMarkDownGenerator.generateAndStoreScripts(workflow);
			CloudgeneYamlGenerator.generateAndStoreFile(workflow);
			String script = PigScript.load(workflowName);
			if (script == null) {
				throw new IOException("could not load generated pig script.");
			}
			obj.setData(script);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while generating the script.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}