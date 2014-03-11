package piggene.serialisation;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.esotericsoftware.yamlbeans.YamlReader;

public class WorkflowComponentReader {
	private static Properties prop = new Properties();
	private static String workflowCompDefs;

	static {
		try {
			prop.load(WorkflowComponentReader.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowCompDefs = prop.getProperty("workflowCompDefs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static WorkflowComponent read(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(workflowCompDefs.concat(name.concat(".yaml"))));
		reader.getConfig().setPropertyElementType(WorkflowComponent.class, "wfComponent", SingleWorkflowElement.class);
		final WorkflowComponent component = (WorkflowComponent) reader.read();
		reader.close();
		return component;
	}

}