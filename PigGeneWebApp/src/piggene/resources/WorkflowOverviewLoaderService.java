package piggene.resources;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowOverviewLoaderService extends ServerResource {

	@Override
	public Representation get() {
		ServerResponseObject obj = new ServerResponseObject();
		List<String> filenames;
		try {
			filenames = WorkflowSerialisation.getListOfWorkflowNames();
			if(filenames == null) {
				throw new IOException();
			}
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("Currently there are no workflow definitions saved on the server.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}
		
		obj.setSuccess(true);
		obj.setMessage("success");
		obj.setData(new JSONObject().accumulate("title", "open existing workflow").accumulate("names", JSONArray.fromObject(filenames)));
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}