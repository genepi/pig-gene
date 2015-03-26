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
import piggene.helper.PigGenePackageException;

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

		try {
			LibraryAddOn.downloadLibraryFile(libraryLink);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("The given URL is not accessible.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final ZipException e) {
			obj.setSuccess(false);
			obj.setMessage("Server is unable to unzip the downloaded zip-file.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final PigGenePackageException e) {
			obj.setSuccess(false);
			obj.setMessage(e.getMessage());
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}