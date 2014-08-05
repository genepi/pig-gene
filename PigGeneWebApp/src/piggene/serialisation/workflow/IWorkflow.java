package piggene.serialisation.workflow;

import java.io.IOException;

public interface IWorkflow {
	public static final String EQUAL_SYMBOL = " = ";

	public String getPigScriptRepresentation(final String wfName) throws IOException;

}