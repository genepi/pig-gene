package piggene.serialisation;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.yamlbeans.YamlReader;

public class WorkflowReader {

	@SuppressWarnings("unchecked")
	public static ArrayList<WorkflowComponent> read(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(name.concat(".yaml")));
		final ArrayList<WorkflowComponent> workflow = (ArrayList<WorkflowComponent>) reader.read();
		reader.close();
		return workflow;
	}

}