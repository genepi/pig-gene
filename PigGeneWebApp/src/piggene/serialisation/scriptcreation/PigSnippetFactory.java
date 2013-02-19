package piggene.serialisation.scriptcreation;

import piggene.serialisation.StoreSnippet;
import piggene.serialisation.WorkflowComponent;

public class PigSnippetFactory {

	public static String getPigScriptSnippet(final WorkflowComponent comp) {
		if (comp.getOperation().equals("FILTER")) {
			return new FilterSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("JOIN")) {
			return new JoinSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("LOAD")) {
			return new LoadSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("REGISTER")) {
			return new RegisterSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("STORE")) {
			return new StoreSnippet(comp).toPigScript();
		} else {
			return new ScriptSnippet(comp).toPigScript();
		}
	}
}