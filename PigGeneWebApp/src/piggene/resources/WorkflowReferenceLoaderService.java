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
import piggene.serialisation.workflow.WorkflowType;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowReferenceLoaderService extends ServerResource {

	@Override
	public Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		final String workflowName = getRequest().getAttributes().get("id").toString();
		final String type = getRequest().getAttributes().get("type").toString();
		try {
			final Workflow workflow = WorkflowSerialisation.load(workflowName, type);
			workflow.setWorkflowType(WorkflowType.WORKFLOW_REFERENCE);
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