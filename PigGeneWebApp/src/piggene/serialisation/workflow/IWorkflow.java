package piggene.serialisation.workflow;

import java.io.IOException;

import piggene.serialisation.pig.MissingParameterException;

public interface IWorkflow {
	public static final String EQUAL_SYMBOL = " = ";

	public String getPigScriptRepresentation(final boolean renameParam, final String wfName) throws IOException, MissingParameterException;

}