package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.esotericsoftware.yamlbeans.YamlWriter;

/**
 * WorkflowWriter class is used to write a workflow definition into a file on
 * the file system.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class WorkflowWriter {
	private final static String PATH = "apps/piggene/";

	public static void write(final Workflow workflow) throws IOException {
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(PATH.concat(workflow.getName().concat(".yaml")))));
		writer.write(workflow);
		writer.close();
	}

}