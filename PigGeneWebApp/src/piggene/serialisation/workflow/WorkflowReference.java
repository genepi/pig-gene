package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkflowReference extends Workflow {
	private static int indentation = 0;

	private WorkflowType workflowType = WorkflowType.WORKFLOW_REFERENCE;

	private String name;

	public WorkflowReference() {
	}

	public WorkflowReference(final String name) {
		this.name = name;
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

	@Override
	public String getPigScriptRepresentation(final String surroundingWorkflowName) throws IOException {
		WorkflowReference.indentation++;
		String workflowName = this.name;
		Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName);

		StringBuilder sb = new StringBuilder();
		sb.append(lineSeparator);
		sb.append(insertIndentationTabs());
		sb.append(preparePigScriptCommand(workflowName));
		sb.append(insertIndentationTabs());
		sb.append(preparePigScriptCommand(referencedWorkflow.getDescription()));

		for (Workflow wf : referencedWorkflow.getComponents()) {
			sb.append(lineSeparator);
			sb.append(insertIndentationTabs());
			String pigScriptRepresentation = applyInputParameterMapping(wf.getPigScriptRepresentation(workflowName),
					WorkflowSerialisation.load(surroundingWorkflowName).getInputParamMapping().get(workflowName));
			sb.append(adjustIndentation(pigScriptRepresentation));
			sb.append(lineSeparator);
		}
		sb.append(lineSeparator);
		WorkflowReference.indentation--;
		return sb.toString();
	}

	private String insertIndentationTabs() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < WorkflowReference.indentation; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	private String adjustIndentation(final String pigScriptRepresentation) {
		return pigScriptRepresentation.replaceAll("[\\r\\n]+", lineSeparator.concat(insertIndentationTabs()));
	}

	private String applyInputParameterMapping(final String pigScriptRepresentation, final Map<String, String> parameterMapping) {
		String regex = "(\\$)(\\w+)(\\b)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(pigScriptRepresentation);

		if (m.find()) {
			String replacementName = parameterMapping.get(m.group(2));
			return m.replaceAll("$1" + replacementName);
		}
		return pigScriptRepresentation;
	}

}