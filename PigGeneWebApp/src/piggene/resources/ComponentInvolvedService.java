package piggene.resources;

import java.io.IOException;
import java.util.Set;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.representation.ConnectionGraphException;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class ComponentInvolvedService extends ServerResource {

	@Override
	protected Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();
		final String componentName = getRequest().getAttributes().get("id").toString();

		Set<String> workflowsThatIncludeComponent;
		try {
			workflowsThatIncludeComponent = WorkflowSerialisation.getWorkflowsThatIncludeComponent(componentName);
			obj.setData(workflowsThatIncludeComponent);
		} catch (final IOException e) {
			e.printStackTrace();
			obj.setSuccess(false);
			obj.setMessage("An error occured while loading the workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("The data could not be parsed because of a syntax error.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final ConnectionGraphException e) {
			obj.setSuccess(false);
			obj.setMessage("Problem detecting the dependent workflows, that use this component.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");

		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}