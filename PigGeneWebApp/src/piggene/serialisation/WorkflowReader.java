package piggene.serialisation;

import java.io.FileReader;
import java.io.IOException;

import com.esotericsoftware.yamlbeans.YamlReader;

public class WorkflowReader {
	private final static String PATH = "yamlFiles/";

	public static Workflow read(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(PATH.concat(name.concat(".yaml"))));
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		return workflow;
	}

}