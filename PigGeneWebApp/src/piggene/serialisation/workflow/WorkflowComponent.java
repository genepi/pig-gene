package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkflowComponent extends Workflow {
	private WorkflowType workflowType = WorkflowType.WORKFLOW_COMPONENT;
	private ScriptType scriptType;
	private String content;

	public WorkflowComponent() {
	}

	public WorkflowComponent(final String content, final ScriptType scriptType) {
		this.content = content;
		this.scriptType = scriptType;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return this.workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(final ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public String getPigScriptRepresentation(final String wfName) throws IOException {
		if (scriptType.getName().equals("Apache Pig Script")) {
			return content;
		}
		return null;
	}

	@Override
	public List<String> getRMarkDownScriptRepresentations() throws IOException {
		final List<String> rMdContent = new ArrayList<String>();
		if (scriptType.getName().equals("R Markdown Script")) {
			rMdContent.add(content);
			return rMdContent;
		}
		return null;
	}

}