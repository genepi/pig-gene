package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import com.esotericsoftware.yamlbeans.YamlWriter;

/**
 * WorkflowWriter class is used to write a workflow definition into a file on
 * the file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WorkflowWriter {
	private static Properties prop = new Properties();
	private static String workflowDefs;

	static {
		try {
			prop.load(WorkflowWriter.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefs = prop.getProperty("workflowDefs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write(final Workflow workflow) throws IOException {
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(workflowDefs.concat(workflow.getName().concat(".yaml")))));
		writer.getConfig().setPropertyElementType(Workflow.class, "workflow", SingleWorkflowElement.class);
		writer.write(workflow);
		writer.close();
	}

}