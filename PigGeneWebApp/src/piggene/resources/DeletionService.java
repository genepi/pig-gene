package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import piggene.response.ServerResponseObject;
import piggene.serialisation.WorkflowFiles;

public class DeletionService extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		String filename = "";

		JsonRepresentation representant;
		try {
			representant = new JsonRepresentation(entity);
			filename = representant.getJsonObject().getString("filename");
			final boolean deleted = WorkflowFiles.deleteFile(filename);
			if (deleted) {
				obj.setSuccess(true);
				obj.setMessage("success");
			} else {
				obj.setSuccess(false);
				obj.setMessage("Server was not able to delete the selected file.");
			}
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while checking if the filename exists.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while processing your request.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}