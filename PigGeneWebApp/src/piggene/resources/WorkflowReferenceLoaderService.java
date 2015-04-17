package piggene.resources;

import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowType;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class WorkflowReferenceLoaderService extends ServerResource {
	private static final int APACHE_PIG_SCRIPT = 0;

	@Override
	public Representation get() throws ResourceException {
		final ServerResponseObject obj = new ServerResponseObject();

		final String workflowName = getRequest().getAttributes().get("id").toString();
		final String type = getRequest().getAttributes().get("type").toString();
		try {
			final Workflow workflow = WorkflowSerialisation.load(workflowName, type);

			workflow.setWorkflowType(WorkflowType.WORKFLOW_REFERENCE);

			// TODO remove
			// final String mergedContent =
			// mergeComponents(workflow.getComponents());
			// final List<Workflow> components = new ArrayList<Workflow>();
			// components.add(new WorkflowComponent(workflow.getName(),
			// mergedContent, new ScriptType(0, "Apache Pig Script")));
			// workflow.setComponents(components);

			obj.setData(workflow);
		} catch (final IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while loading the workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");

		final JsonConfig config = new JsonConfig();
		config.setExcludes(new String[] { "RMarkDownScriptRepresentations", "lineSeparator" });
		return new StringRepresentation(JSONObject.fromObject(obj, config).toString(), MediaType.APPLICATION_JSON);
	}

	// TODO remove
	// private String mergeComponents(final List<Workflow> components) throws
	// IOException {
	// final StringBuilder sb = new StringBuilder();
	// Workflow wf;
	// boolean added = false;
	// for (int i = 0; i < components.size(); i++) {
	// wf = components.get(i);
	// if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
	// final String workflowName = wf.getName();
	// final Workflow referencedWf = WorkflowSerialisation.load(workflowName,
	// WorkflowSerialisation.determineType(workflowName));
	// sb.append(mergeComponents(referencedWf.getComponents()));
	// } else if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_COMPONENT))
	// {
	// final WorkflowComponent comp = (WorkflowComponent) wf;
	// if (comp.getScriptType().getId() == APACHE_PIG_SCRIPT) {
	// if (added) {
	// sb.append(System.getProperty("line.separator"));
	// }
	// sb.append(comp.getContent());
	// added = true;
	// }
	// }
	// }
	// return sb.toString();
	// }

}