package piggene.serialisation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowWriter {

	public static void write(final ArrayList<WorkflowComponent> workflow, final String name) throws IOException {
		YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(name.concat(".yaml"))));
		writer.write(workflow);
		writer.close();
	}

}