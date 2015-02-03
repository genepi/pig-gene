package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import piggene.serialisation.workflow.actions.WorkflowSerialisation;
import piggene.serialisation.workflow.parameter.WorkflowParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

public class WorkflowReference extends Workflow {
	private static int indentation = 0;

	private WorkflowType workflowType = WorkflowType.WORKFLOW_REFERENCE;

	private String name;
	private Position position;

	public WorkflowReference() {
	}

	public WorkflowReference(final String name, final Position position) {
		this.name = name;
		this.position = position;
	}

	@Override
	public WorkflowType getWorkflowType() {
		return this.workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	@Override
	public String getPigScriptRepresentation(final String surroundingWorkflowName) throws IOException {
		WorkflowReference.indentation++;
		final String workflowName = this.name;
		final Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName);

		final StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		sb.append(insertIndentationTabs());
		sb.append(preparePigScriptCommand(workflowName));
		sb.append(insertIndentationTabs());
		sb.append(preparePigScriptCommand(referencedWorkflow.getDescription()));

		for (final Workflow wf : referencedWorkflow.getComponents()) {
			if (wf instanceof WorkflowReference || ((WorkflowComponent) wf).getScriptType().getName().equals("Apache Pig Script")) {
				sb.append(lineSeparator);
				sb.append(insertIndentationTabs());
				final Workflow surroundingWorkflow = WorkflowSerialisation.load(surroundingWorkflowName);
				final String pigScriptRepresentation = applyParameterMapping(wf.getPigScriptRepresentation(workflowName),
						surroundingWorkflow.getParameterMapping(), surroundingWorkflow.getParameter(), workflowName);
				sb.append(adjustIndentation(pigScriptRepresentation));
				sb.append(lineSeparator);
			}
		}
		sb.append(lineSeparator);
		WorkflowReference.indentation--;
		return sb.toString();
	}

	@Override
	public Map<String, String> getRMarkDownScriptRepresentations() throws IOException {
		final String workflowName = this.name;
		final Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName);
		final Map<String, String> rmdScripts = new HashMap<String, String>();

		Map<String, String> content;
		for (final Workflow wf : referencedWorkflow.getComponents()) {
			content = wf.getRMarkDownScriptRepresentations();
			if (content != null) {
				rmdScripts.putAll(content);
			}
		}
		return rmdScripts;
	}

	private String insertIndentationTabs() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < WorkflowReference.indentation; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	private String adjustIndentation(final String pigScriptRepresentation) {
		return pigScriptRepresentation.replaceAll("[\\r\\n]+", lineSeparator.concat(insertIndentationTabs()));
	}

	private String applyParameterMapping(final String pigScriptRepresentation, final WorkflowParameterMapping parameterMapping,
			final WorkflowParameter wfParameter, final String workflowName) {
		final Map<String, String> inputParameterMap = parameterMapping.retrieveInputMapByKey(workflowName);
		final Map<String, String> outputParameterMap = parameterMapping.retrieveOutputMapByKey(workflowName);

		final String regex = "(\\$\\w+)(\\b)";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(pigScriptRepresentation);
		final StringBuffer sb = new StringBuffer();

		while (m.find()) {
			final String key = m.group(1);
			String replacementName = null;
			if (inputParameterMap.containsKey(key)) { // inputParam
				replacementName = inputParameterMap.get(key);
			} else if (outputParameterMap.containsKey(key)) { // outputParam
				replacementName = outputParameterMap.get(key);
			}
			if (replacementName != null && replacementName.startsWith("$")) {
				replacementName = "\\".concat(replacementName);
			} else if (replacementName == null) {
				replacementName = "\\" + key;
			}
			m.appendReplacement(sb, (replacementName != null) ? replacementName : key);
		}

		m.appendTail(sb);
		return sb.toString();
	}

}