package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		final String regex = "(\\$\\w+)(\\b)";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(content);
		final StringBuffer sb = new StringBuffer();
		while (m.find()) {
			final String param = m.group(1);
			m.appendReplacement(sb, "'\\".concat(param).concat("'"));
		}
		m.appendTail(sb);
		return sb.toString();
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