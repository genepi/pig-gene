package piggene.resources;

import java.io.IOException;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.helper.MissingConnectionException;
import piggene.serialisation.RMarkDown.RMarkDownGenerator;
import piggene.serialisation.cloudgene.CloudgeneYamlGenerator;
import piggene.serialisation.pig.PigScriptGenerator;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class ScriptDownloadService extends ServerResource {
	private static Properties prop = new Properties();
	private static String wfAbbr;

	static {
		try {
			prop.load(WorkflowSerialisation.class.getClassLoader().getResourceAsStream("config.properties"));
			wfAbbr = prop.getProperty("wfAbbr");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	@Override
	protected Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		final String workflowName = getRequest().getAttributes().get("id").toString();
		try {
			final Workflow workflow = WorkflowSerialisation.load(workflowName, wfAbbr);
			PigScriptGenerator.generateAndStoreScript(workflow);
			RMarkDownGenerator.generateAndStoreScripts(workflow);
			CloudgeneYamlGenerator.generateAndStoreFile(workflow);
			final String script = PigScriptGenerator.load(workflowName);
			if (script == null) {
				throw new IOException("could not load generated pig script.");
			}
			obj.setData(script);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while generating the script.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final MissingConnectionException e) {
			obj.setSuccess(false);
			obj.setMessage(e.getMessage());
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}