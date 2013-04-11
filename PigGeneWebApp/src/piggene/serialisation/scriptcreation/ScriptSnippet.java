package piggene.serialisation.scriptcreation;

import piggene.serialisation.WorkflowComponent;

/**
 * ScriptSnippet class is used to return a USER SCRIPT based on the specified
 * workflow component.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class ScriptSnippet extends PigSnippet {

	public ScriptSnippet(final WorkflowComponent comp) {
		super(comp);
	}

	@Override
	public String toPigScript() {
		final StringBuilder sb = new StringBuilder();
		final String comment = comp.getComment();
		if (!comment.equals("-")) {
			sb.append(System.getProperty("line.separator"));
			sb.append("//");
			sb.append(comment);
			sb.append(System.getProperty("line.separator"));
		}
		final String script = comp.getOptions().trim();
		if (script.endsWith(";")) {
			sb.append(script.substring(0, script.length() - 1));
		} else {
			sb.append(comp.getOptions());
		}
		return sb.toString();
	}

}