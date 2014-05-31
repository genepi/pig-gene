package piggene.serialisation.workflow;

import java.io.IOException;

public interface IWorkflow {
	public static final String EQUAL_SYMBOL = " = ";

	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) throws IOException;

}