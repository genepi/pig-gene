package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorkflowComponent extends Workflow {
	private ScriptType scriptType;
	private String name;
	private WorkflowType workflowType = WorkflowType.WORKFLOW_COMPONENT;
	private String content;

	public WorkflowComponent() {
	}

	public WorkflowComponent(final String name, final String content, final ScriptType scriptType) {
		this.name = name;
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
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Map<String, String> getRMarkDownScriptRepresentations() throws IOException {
		final Map<String, String> rMdContent = new HashMap<String, String>();
		if (scriptType.getName().equals("R Markdown Script")) {
			rMdContent.put(this.getName(), content);
			return rMdContent;
		}
		return null;
	}

}