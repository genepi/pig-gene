package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowReader;

public class WorkflowLoaderService extends ServerResource {

	@Override
	public Representation get() throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();

		String workflowName = getRequest().getAttributes().get("id").toString();
		try {
			Workflow workflow = WorkflowReader.read(workflowName);
			obj.setData(workflow);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while loading the workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}