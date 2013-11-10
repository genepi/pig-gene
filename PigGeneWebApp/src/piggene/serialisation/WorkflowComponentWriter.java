package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowComponentWriter {
	private final static String PATH = "workflowComp/";

	public static void write(final WorkflowComponent wfComponent) throws IOException {
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(PATH.concat(wfComponent.getName().concat(".yaml")))));
		writer.getConfig().setPropertyElementType(WorkflowComponent.class, "wfComponent", SingleWorkflowElement.class);
		writer.write(wfComponent);
		writer.close();
	}

}