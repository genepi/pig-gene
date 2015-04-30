package piggene.resources;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class DetermineElementType extends ServerResource {

	@Override
	protected Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();
		final String workflowName = getRequest().getAttributes().get("id").toString();
		final String type = WorkflowSerialisation.determineType(workflowName);

		obj.setData(type);
		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}