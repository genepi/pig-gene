package piggene.resources;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

import piggene.serialisation.helper.PersistentFilesHelper;

public class WorkflowOverviewLoaderService extends ServerResource {

	@Override
	public Representation get() {
		ServerResponseObject obj = new ServerResponseObject();
		List<String> filenames = PersistentFilesHelper.getAllWorkflowFileNamesWithoutExtension();

		if (filenames == null) {
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