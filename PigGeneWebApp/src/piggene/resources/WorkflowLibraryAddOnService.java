package piggene.resources;

import java.io.IOException;

import net.lingala.zip4j.exception.ZipException;
import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.helper.LibraryAddOn;

public class WorkflowLibraryAddOnService extends ServerResource {

	@Override
	protected Representation post(final Representation entity) throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();
		String libraryLink = null;

		try {
			final JsonRepresentation representation = new JsonRepresentation(entity);
			final org.json.JSONObject transferedData = representation.getJsonObject();
			libraryLink = transferedData.getString("libraryLink");
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			e.printStackTrace();
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		// TODO modify exception handling!

		try {
			LibraryAddOn.downloadLibraryFile(libraryLink);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}