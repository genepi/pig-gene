package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowLoaderService extends ServerResource {

	@Override
	public Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		try {
			final String workflowName = getRequest().getAttributes().get("id").toString();
			final Workflow workflow = WorkflowSerialisation.resolveWorkflowReferences(WorkflowSerialisation.load(workflowName));
			obj.setData(workflow);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while loading the workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");

		final JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "RMarkDownScriptRepresentations", "lineSeparator" });
		return new StringRepresentation(JSONObject.fromObject(obj, config).toString(), MediaType.APPLICATION_JSON);
	}

}