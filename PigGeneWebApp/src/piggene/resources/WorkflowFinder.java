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

import piggene.exceptions.UnpossibleWorkflowFileOperation;
import piggene.response.ServerResponseObject;
import piggene.serialisation.UntouchableFiles;
import piggene.serialisation.WorkflowFiles;

/**
 * WorkflowFinder class is used to check if a specified workflow-file exists.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WorkflowFinder extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();
		String filename = "";

		try {
			final JsonRepresentation representant = new JsonRepresentation(entity);
			filename = representant.getJsonObject().getString("filename");
			if (UntouchableFiles.list.contains(filename)) {
				throw new UnpossibleWorkflowFileOperation();
			}
		} catch (final UnpossibleWorkflowFileOperation e) {
			obj.setSuccess(false);
			obj.setMessage("It is impossible to override an example workflow!");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while checking if the filename is already used.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while processing your request.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		if (WorkflowFiles.doesFileExist(filename)) {
			obj.setData(Boolean.TRUE);
		} else {
			obj.setData(Boolean.FALSE);
		}
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}
}