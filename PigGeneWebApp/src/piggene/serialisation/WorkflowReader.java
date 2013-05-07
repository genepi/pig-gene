package piggene.serialisation;

import java.io.FileReader;
import java.io.IOException;

import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * WorkflowReader class is used to read the content of a workflow definition
 * file saved on the file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WorkflowReader {
	private final static String PATH = "workflowDefs/";

	public static Workflow read(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(PATH.concat(name.concat(".yaml"))));
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		return workflow;
	}

}