package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import piggene.response.ServerResponseObject;
import piggene.serialisation.SingleWorkflowElement;
import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowContainer;
import piggene.serialisation.WorkflowReader;

/**
 * DeserialisationService is used to deserialize a saved workflow definition.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class DeserialisationService extends ServerResource {

	@Override
	@Post
	public Representation post(final Representation entity) {
		final ServerResponseObject obj = new ServerResponseObject();

		try {
			final JsonRepresentation representant = new JsonRepresentation(entity);
			final String filename = representant.getJsonObject().getString("filename");
			final Workflow workflow = WorkflowReader.read(filename);
			final WorkflowContainer resolvedWorkflow = resolveWfReferences(workflow);
			obj.setData(resolvedWorkflow);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while loading the data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while processing your request.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private WorkflowContainer resolveWfReferences(final Workflow workflow) throws IOException {
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();

		// TODO einzelfall überlegen, wenn keine referenzierten Workflows
		// enthalten sind...
		ArrayList<SingleWorkflowElement> allWfElements = new ArrayList<SingleWorkflowElement>();
		boolean added = false;
		for (final SingleWorkflowElement comp : workflow.getWorkflow()) {
			String referenceName = comp.getReferenceName();
			if (referenceName != null) {
				workflows.add(WorkflowReader.read(referenceName));
				added = true;
			}
		}
		// TODO
		if (!added) {
			workflows.add(workflow);
		}

		return new WorkflowContainer(workflow.getName(), workflow.getDescription(), workflows);
	}
}