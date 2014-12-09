package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.List;

public interface IWorkflow {
	public static final String EQUAL_SYMBOL = " = ";

	public String getPigScriptRepresentation(String wfName) throws IOException;

	public List<String> getRMarkDownScriptRepresentations() throws IOException;

}