package piggene.resources;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.WorkflowSerialisation;

public class WorkflowDeletionService extends ServerResource {

	@Override
	protected Representation delete() throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();

		String workflowName = getRequest().getAttributes().get("id").toString();
		if(WorkflowSerialisation.remove(workflowName)) {
			obj.setSuccess(true);
			obj.setMessage("success");
		} else {
			obj.setSuccess(false);
			obj.setMessage("An error occured while deleting the workflow data.");
		}

		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}
	
}