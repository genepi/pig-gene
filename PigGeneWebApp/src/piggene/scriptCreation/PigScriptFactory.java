package piggene.scriptCreation;

import java.util.ArrayList;

import piggene.serialisation.WorkflowComponent;

public class PigScriptFactory {
	private final ArrayList<WorkflowComponent> components;
	private final ArrayList<PigSnippet> snippets;

	public PigScriptFactory(final ArrayList<WorkflowComponent> components) {
		this.components = components;
		snippets = new ArrayList<PigSnippet>();

	}

	private void generatePigSnippets() {

	}

	public String getPigScript() {
		final StringBuilder sb = new StringBuilder();
		sb.append(PigSnippet.getHeader());
		for (final PigSnippet snippet : snippets) {
			sb.append(snippet.toPigScript());
		}
		return sb.toString();
	}

}