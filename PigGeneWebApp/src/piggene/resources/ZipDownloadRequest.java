package piggene.resources;

import java.io.IOException;

import net.lingala.zip4j.exception.ZipException;
import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.zip.ZipFileGenerator;

public class ZipDownloadRequest extends ServerResource {

	@Override
	protected Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		final String workflowName = getRequest().getAttributes().get("id").toString();
		try {
			ZipFileGenerator.generateAndStoreFile(workflowName);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while generating the script.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final ZipException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while generating the zip-file.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}