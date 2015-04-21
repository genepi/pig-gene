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
			if (wf instanceof WorkflowReference || ((WorkflowComponent) wf).getScriptType().getId() == PIG_SCRIPT_TYPE_ID) {
				sb.append(lineSeparator);
				sb.append(insertIndentationTabs());
				final String pigScriptRepresentation = applyParameterMapping(wf.getPigScriptRepresentation(this),
						surroundingWorkflow.getParameterMapping(), surroundingWorkflow.getParameter(), super.getUid());
				sb.append(adjustIndentation(pigScriptRepresentation));
				sb.append(lineSeparator);
			}
		}
		sb.append(lineSeparator);
		WorkflowReference.indentation--;
		return sb.toString();
	}

	private void addVirtualInputParametersForRMarkDownScriptsTo(final Workflow surroundingWorkflow) throws IOException {
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
				// TODO add OR call to if-clause to catch rmd outputs...
				// no matching parameter connector because of plot-type
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