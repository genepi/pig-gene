package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowWriter {
	private final static String PATH = "yamlFiles/";

	public static void write(final ArrayList<WorkflowComponent> workflow, final String name) throws IOException {
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(PATH.concat(name.concat(".yaml")))));
		writer.write(workflow);
		writer.close();
	}

}