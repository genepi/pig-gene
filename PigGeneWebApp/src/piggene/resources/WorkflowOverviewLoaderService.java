package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowHelper;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowOverviewLoaderService extends ServerResource {

	@Override
	public Representation get() {
		final ServerResponseObject obj = new ServerResponseObject();
		final List<String> typeMatchingNames = new ArrayList<String>();
		try {

			final String type = getRequest().getAttributes().get("type").toString();
			final List<String> names = WorkflowSerialisation.getListOfWorkflowNames();
			if (names == null) {
				throw new IOException();
			}

			// TODO
			// alle vorhandenen Workflows laden
			// in Abhängigkeit des typs nur die "passenden" (comp od wf) filtern
			// deren Dateinamen zurückgeben

			// wenn wf keine components aber input/outputs hat: wf
			// sonst: component

			for (final String workflowName : names) {
				final Workflow workflow = WorkflowSerialisation.resolveWorkflowReferences(WorkflowSerialisation.load(workflowName));

				if (type.equals("comp") && !WorkflowHelper.containesReferencedWorkflow(workflow)) {
					typeMatchingNames.add(workflowName);
				} else if (type.equals("wf") && WorkflowHelper.containesReferencedWorkflow(workflow)) {
					typeMatchingNames.add(workflowName);
				}
			}

			if (typeMatchingNames.isEmpty()) {
				throw new IOException();
			}

		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("Currently there are no workflow definitions saved on the server.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		obj.setData(new JSONObject().accumulate("title", "open existing workflow").accumulate("names", JSONArray.fromObject(typeMatchingNames)));
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

}