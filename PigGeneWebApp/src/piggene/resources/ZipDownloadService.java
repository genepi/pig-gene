package piggene.resources;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.zip.ZipFileLoader;

public class ZipDownloadService extends ServerResource {

	@Override
	protected Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		final String workflowName = getRequest().getAttributes().get("id").toString();
		final String zipFilePath = ZipFileLoader.returnFilePath(workflowName);

		obj.setSuccess(true);
		obj.setMessage("success");

		return new FileRepresentation(zipFilePath, MediaType.APPLICATION_ZIP, 0);
	}

}