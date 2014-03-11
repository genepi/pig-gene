package piggene.serialisation;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * WorkflowReader class is used to read the content of a workflow definition
 * file saved on the file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WorkflowReader {
	private static Properties prop = new Properties();
	private static String workflowDefs;

	static {
		try {
			prop.load(WorkflowReader.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefs = prop.getProperty("workflowDefs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Workflow read(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(workflowDefs.concat(name.concat(".yaml"))));
		reader.getConfig().setPropertyElementType(Workflow.class, "workflow", SingleWorkflowElement.class);
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		return workflow;
	}

}