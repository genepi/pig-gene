package piggene.serialisation.workflow;

public class WorkflowHelper {

	public static boolean containesReferencedWorkflow(final Workflow wf) {
		if (wf.getComponents().isEmpty()) {
			return false;
		}
		for (final Workflow wfComp : wf.getComponents()) {
			if (!(wfComp instanceof WorkflowComponent)) {
				return true;
			}
		}
		return false;
	}

}