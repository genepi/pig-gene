package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import piggene.serialisation.workflow.actions.WorkflowSerialisation;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.OutputLinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

public class WorkflowReference extends Workflow {
	private static int indentation = -1;
	private static final int PIG_SCRIPT_TYPE_ID = 0;
	private static final int RMD_SCRIPT_TYPE_ID = 1;

	private WorkflowType workflowType = WorkflowType.WORKFLOW_REFERENCE;
	private String name;

	public WorkflowReference() {
	}

	public WorkflowReference(final String uid, final String name, final Position position) {
		super.setUid(uid);
		this.name = name;
		super.setPosition(position);
	}

	@Override
	public WorkflowType getWorkflowType() {
		return this.workflowType;
	}

	@Override
	public void setWorkflowType(final WorkflowType workflowType) {
		this.workflowType = workflowType;
	}

	public static int getIndentation() {
		return indentation;
	}

	public static void setIndentation(final int indentation) {
		WorkflowReference.indentation = indentation;
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
	public Position getPosition() {
		return super.getPosition();
	}

	@Override
	public void setPosition(final Position position) {
		super.setPosition(position);
	}

	@Override
	public String getPigScriptRepresentation(final Workflow surroundingWorkflow) throws IOException {
		WorkflowReference.indentation++;
		final String workflowName = this.name;
		final Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName, WorkflowSerialisation.determineType(workflowName));

		final StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		sb.append(insertIndentationTabs());
		sb.append(preparePigScriptCommand(workflowName));
		sb.append(insertIndentationTabs());
		sb.append(preparePigScriptCommand(referencedWorkflow.getDescription()));

		addVirtualInputParametersForRMarkDownScriptsTo(surroundingWorkflow);
		for (final Workflow wf : referencedWorkflow.getComponents()) {
			if (wf instanceof WorkflowComponent && ((WorkflowComponent) wf).getScriptType().getId() == PIG_SCRIPT_TYPE_ID) {
				sb.append(lineSeparator);
				sb.append(insertIndentationTabs());
				final String pigScriptRepresentation = applyParameterMapping(wf.getPigScriptRepresentation(WorkflowSerialisation.load(this.getName(),
						WorkflowSerialisation.determineType(this.getName()))), surroundingWorkflow.getParameterMapping(),
						surroundingWorkflow.getParameter(), super.getUid());
				sb.append(adjustIndentation(pigScriptRepresentation));
				sb.append(lineSeparator);
			} else if (wf instanceof WorkflowReference) {
				final String uid = getComponentUID(surroundingWorkflow, referencedWorkflow);
				adaptSurroundingWorkflowParameterMapping(surroundingWorkflow, referencedWorkflow, uid);

				sb.append(lineSeparator);
				sb.append(insertIndentationTabs());
				final String pigScriptRepresentation = applyParameterMapping(wf.getPigScriptRepresentation(WorkflowSerialisation.load(this.getName(),
						WorkflowSerialisation.determineType(this.getName()))), surroundingWorkflow.getParameterMapping(),
						surroundingWorkflow.getParameter(), super.getUid());
				sb.append(adjustIndentation(pigScriptRepresentation));
				sb.append(lineSeparator);

			}
		}
		sb.append(lineSeparator);
		WorkflowReference.indentation--;
		return sb.toString();
	}

	private String getComponentUID(final Workflow surroundingWorkflow, final Workflow referencedWorkflow) {
		String uid = "";
		for (final Workflow zw : surroundingWorkflow.getComponents()) {
			if (zw.getName().equals(referencedWorkflow.getName())) {
				uid = zw.getUid();
				break;
			}
		}
		return uid;
	}

	private void adaptSurroundingWorkflowParameterMapping(final Workflow surroundingWorkflow, final Workflow referencedWorkflow, final String uid) {
		adaptSurroundingWorkflowInputParameterMapping(surroundingWorkflow, referencedWorkflow, uid);
		adaptSurroundingWorkflowOutputParameterMapping(surroundingWorkflow, referencedWorkflow, uid);
	}

	private void adaptSurroundingWorkflowInputParameterMapping(final Workflow surroundingWorkflow, final Workflow referencedWorkflow, final String uid) {
		final Map<String, String> map = surroundingWorkflow.getParameterMapping().getInputParameterMapping().get(uid);
		for (final String outerKey : referencedWorkflow.getParameterMapping().getInputParameterMapping().keySet()) {
			for (final String innerKey : referencedWorkflow.getParameterMapping().getInputParameterMapping().get(outerKey).keySet()) {
				final String value = referencedWorkflow.getParameterMapping().getInputParameterMapping().get(outerKey).get(innerKey);
				if (map.containsKey(value)) {
					final Map<String, Map<String, String>> modifiedInputParamMap = surroundingWorkflow.getParameterMapping()
							.getInputParameterMapping();
					modifiedInputParamMap.get(uid).put("$".concat(value), modifiedInputParamMap.get(uid).get(value));
					modifiedInputParamMap.get(uid).remove(value);
					surroundingWorkflow.getParameterMapping().setInputParameterMapping(modifiedInputParamMap);
				}
			}
		}
	}

	private void adaptSurroundingWorkflowOutputParameterMapping(final Workflow surroundingWorkflow, final Workflow referencedWorkflow,
			final String uid) {
		final Map<String, String> map = surroundingWorkflow.getParameterMapping().getOutputParameterMapping().get(uid);
		for (final String outerKey : referencedWorkflow.getParameterMapping().getOutputParameterMapping().keySet()) {
			for (final String innerKey : referencedWorkflow.getParameterMapping().getOutputParameterMapping().get(outerKey).keySet()) {
				final String value = referencedWorkflow.getParameterMapping().getOutputParameterMapping().get(outerKey).get(innerKey);
				if (map.containsKey(value)) {
					final Map<String, Map<String, String>> modifiedOutputParamMap = surroundingWorkflow.getParameterMapping()
							.getOutputParameterMapping();
					modifiedOutputParamMap.get(uid).put("$".concat(value), modifiedOutputParamMap.get(uid).get(value));
					modifiedOutputParamMap.get(uid).remove(value);
					surroundingWorkflow.getParameterMapping().setOutputParameterMapping(modifiedOutputParamMap);
				}
			}
		}
	}

	private void addVirtualInputParametersForRMarkDownScriptsTo(final Workflow surroundingWorkflow) throws IOException {
		if (surroundingWorkflow.getComponents() != null) {
			for (final Workflow wf : surroundingWorkflow.getComponents()) {
				final String workflowName = wf.getName();
				final Workflow plotWf = WorkflowSerialisation.load(workflowName, WorkflowSerialisation.determineType(workflowName));
				final Workflow plotComponent = plotWf.getComponents().get(0);
				if (plotComponent != null && plotComponent instanceof WorkflowComponent
						&& ((WorkflowComponent) plotComponent).getScriptType().getId() == RMD_SCRIPT_TYPE_ID) {
					final String uid = wf.getUid();
					for (final String s : surroundingWorkflow.getParameterMapping().retrieveInputMapByKey(uid).values()) {
						surroundingWorkflow.getParameter().addOutputParameter(new OutputLinkParameter("", s, "", new Position(0, 0)));
					}
				}
			}
		}
	}

	@Override
	public Map<String, String> getRMarkDownScriptRepresentations() throws IOException {
		final String workflowName = this.name;
		final Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName, WorkflowSerialisation.determineType(workflowName));
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
			final WorkflowParameter wfParameter, final String workflowUID) {
		final Map<String, String> inputParameterMap = parameterMapping.retrieveInputMapByKey(workflowUID);
		final Map<String, String> outputParameterMap = parameterMapping.retrieveOutputMapByKey(workflowUID);

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

			if (replacementName != null) {
				if (nameMatchesParameterConnector(replacementName, wfParameter)) {
					replacementName = "'\\$".concat(replacementName).concat("'");
				} else if (replacementName.startsWith("$")) {
					replacementName = "\\".concat(replacementName);
				}
			} else if (replacementName == null) {
				replacementName = "\\" + key;
			}
			m.appendReplacement(sb, (replacementName != null) ? replacementName : key);
		}
		m.appendTail(sb);
		return replaceQuotesResultingFromNestedWorkflowDefinitions(sb.toString());
	}

	private String replaceQuotesResultingFromNestedWorkflowDefinitions(final String script) {
		final String regex = "(\\')([a-zA-Z_0-9']+)(\\'[' '])";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(script);
		final StringBuffer sb = new StringBuffer();

		while (m.find()) {
			m.appendReplacement(sb, m.group(2).replace("'", "").concat(" "));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private boolean nameMatchesParameterConnector(final String name, final WorkflowParameter parameter) {
		for (final LinkParameter p : parameter.getInputParameter()) {
			if (p.getConnector().equals(name)) {
				return true;
			}
		}
		for (final LinkParameter p : parameter.getOutputParameter()) {
			if (p.getConnector().equals(name)) {
				return true;
			}
		}
		return false;
	}
}