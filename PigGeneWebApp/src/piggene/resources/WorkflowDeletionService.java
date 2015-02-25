package piggene.resources;

import java.io.FileNotFoundException;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowDeletionService extends ServerResource {

	@Override
	protected Representation delete() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		try {
			final String workflowName = getRequest().getAttributes().get("id").toString();
			final String type = getRequest().getAttributes().get("type").toString();
			if (WorkflowSerialisation.remove(workflowName, type)) {
				obj.setSuccess(true);
				obj.setMessage("success");
			} else {
				obj.setSuccess(false);
				obj.setMessage("An error occured while deleting the workflow data.");
			}
		} catch (final FileNotFoundException e) {
			obj.setSuccess(true);
			obj.setMessage(e.getMessage());
		}

		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}