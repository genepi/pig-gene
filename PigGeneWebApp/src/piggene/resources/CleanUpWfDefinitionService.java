package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.helper.CleanUpWfDefinitionHelper;

public class CleanUpWfDefinitionService extends ServerResource {
	@Override
	protected Representation post(Representation entity) throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();
		String componentName;
		String connectorName;
		String type;
		
		try {
			JsonRepresentation representation = new JsonRepresentation(entity);
			final org.json.JSONObject transferedData = representation.getJsonObject();
			componentName = transferedData.getString("componentName");
			 connectorName = transferedData.getString("connectorName");
			 type = transferedData.getString("type");
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error ocurred while parsing the input data");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}
		
		
		try {
			CleanUpWfDefinitionHelper.cleanUp(componentName, connectorName, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}