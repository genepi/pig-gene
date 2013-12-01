package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowComponentWriter {
	private static Properties prop = new Properties();
	private static String workflowCompDefs;

	static {
		try {
			prop.load(WorkflowComponentWriter.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowCompDefs = prop.getProperty("workflowCompDefs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write(final WorkflowComponent wfComponent) throws IOException {
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(workflowCompDefs.concat(wfComponent.getName().concat(".yaml")))));
		writer.getConfig().setPropertyElementType(WorkflowComponent.class, "wfComponent", SingleWorkflowElement.class);
		writer.write(wfComponent);
		writer.close();
	}

}