package piggene.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowComponent;
import piggene.serialisation.workflow.WorkflowSerialisation;
import piggene.serialisation.workflow.WorkflowType;

public class WorkflowReferenceLoaderService extends ServerResource {

	@Override
	public Representation get() throws ResourceException {
		ServerResponseObject obj = new ServerResponseObject();

		String workflowName = getRequest().getAttributes().get("id").toString();
		try {
			Workflow workflow = WorkflowSerialisation.load(workflowName);

			workflow.setWorkflowType(WorkflowType.WORKFLOW_REFERENCE);
			String mergedContent = mergeComponents(workflow.getComponents());
			List<Workflow> components = new ArrayList<Workflow>();
			components.add(new WorkflowComponent(mergedContent));
			workflow.setComponents(components);

			obj.setData(workflow);
		} catch (IOException e) {
			obj.setSuccess(false);
			obj.setMessage("An error occured while loading the workflow data.");
			return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
		}

		obj.setSuccess(true);
		obj.setMessage("success");
		return new StringRepresentation(JSONObject.fromObject(obj).toString(), MediaType.APPLICATION_JSON);
	}

	private String mergeComponents(final List<Workflow> components) {
		StringBuilder sb = new StringBuilder();
		Workflow wf;
		for (int i = 0; i < components.size(); i++) {
			wf = components.get(i);
			if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW)) {
				sb.append(mergeComponents(wf.getComponents()));
			} else if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_COMPONENT)) {
				if (i > 0) {
					sb.append(System.getProperty("line.separator"));
				}
				sb.append(((WorkflowComponent) wf).getContent());
			}
		}
		return sb.toString();
	}

}