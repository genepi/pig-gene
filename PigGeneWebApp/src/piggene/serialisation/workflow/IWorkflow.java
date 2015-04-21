package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.Map;

public interface IWorkflow {
	public static final String EQUAL_SYMBOL = " = ";

	public String getPigScriptRepresentation(Workflow surroundingWorkflow) throws IOException;

	public Map<String, String> getRMarkDownScriptRepresentations() throws IOException;

}