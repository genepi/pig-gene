package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

public class PigSnippetFactory {

	// TODO add additional operations (LOAD, ...)
	public static String getPigScriptSnippet(final WorkflowComponent comp) {
		if (comp.getOperation().equals("FILTER")) {
			return new FilterSnippet(comp).toPigScript();
		} else {
			return new JoinSnippet(comp).toPigScript();
		}
	}

}