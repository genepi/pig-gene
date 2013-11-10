package piggene.serialisation;

import java.io.FileReader;
import java.io.IOException;

import com.esotericsoftware.yamlbeans.YamlReader;

public class WorkflowComponentReader {
	private final static String PATH = "workflowComp/";

	public static WorkflowComponent read(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(PATH.concat(name.concat(".yaml"))));
		reader.getConfig().setPropertyElementType(WorkflowComponent.class, "wfComponent", SingleWorkflowElement.class);
		final WorkflowComponent component = (WorkflowComponent) reader.read();
		reader.close();
		return component;
	}

}
