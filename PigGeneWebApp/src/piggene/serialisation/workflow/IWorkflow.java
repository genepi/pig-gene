package piggene.serialisation.workflow;

import java.io.IOException;
import java.util.Set;

public interface IWorkflow {
	public static final String EQUAL_SYMBOL = " = ";

	public String getPigScriptRepresentation(Set<Workflow> parentWfs) throws IOException;
}