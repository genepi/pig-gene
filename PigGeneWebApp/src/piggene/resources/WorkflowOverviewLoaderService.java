package piggene.resources;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

import piggene.response.ServerResponseObject;
import piggene.serialisation.PersistentFiles;

public class WorkflowOverviewLoaderService extends ServerResource {

	@Override
	public Representation get() {
		ServerResponseObject obj = new ServerResponseObject();
		ArrayList<String> filenames = PersistentFiles.getAllWorkflowFileNames();

		if (filenames == null) {
			obj.setSuccess(false);
			obj.setMessage("Currently there are no workflow definitions saved on the server.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		obj.setData(filenames);
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}