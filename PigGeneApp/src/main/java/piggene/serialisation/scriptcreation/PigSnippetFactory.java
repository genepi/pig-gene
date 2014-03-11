package piggene.serialisation.scriptcreation;

import piggene.serialisation.SingleWorkflowElement;

/**
 * PigSnippetFactory is used to return a snippet. Which snippet representation
 * that is depends on the operation contained in the specified workflow
 * component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class PigSnippetFactory {

	public static String getPigScriptSnippet(final SingleWorkflowElement comp) {
		if (comp.getOperation().equals("FILTER")) {
			return new FilterSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("JOIN")) {
			return new JoinSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("SELECT")) {
			return new SelectSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("LOAD")) {
			return new LoadSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("REGISTER")) {
			return new RegisterSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("GROUP")) {
			return new GroupSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("ORDER")) {
			return new OrderSnippet(comp).toPigScript();
		} else if (comp.getOperation().equals("STORE")) {
			return new StoreSnippet(comp).toPigScript();
		} else {
			return new ScriptSnippet(comp).toPigScript();
		}
	}

}