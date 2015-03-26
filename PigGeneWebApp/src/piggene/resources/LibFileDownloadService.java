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

public class LibFileDownloadService extends ServerResource {

	@Override
	protected Representation post(final Representation entity) throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();
		String librayLink = null;

		try {
			final JsonRepresentation representation = new JsonRepresentation(entity);
			final org.json.JSONObject transferedData = representation.getJsonObject();
			librayLink = transferedData.getString("link");
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		try {
			LibraryAddOn.downloadLibraryFile(librayLink);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("A problem ocurred while trying to download the specified library input.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final ZipException e) {
			obj.setSuccess(false);
			obj.setMessage("The content of the specified library zip-file could not be extracted.");
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