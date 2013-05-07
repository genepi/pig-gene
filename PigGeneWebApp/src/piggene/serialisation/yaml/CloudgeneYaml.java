package piggene.serialisation.yaml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import piggene.serialisation.Workflow;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class CloudgeneYaml {
	private final static String PATH = "apps/piggene/";

	public static void generateCloudgeneYamlFile(final Workflow workflow) throws IOException {
		final App app = new App();
		app.setName(workflow.getName());
		app.setDescription(workflow.getDescription());
		app.setVersion("1.0.0");
		app.setCategory("Piggene");

		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(PATH.concat(workflow.getName().concat(".yaml")))));
		writer.write(app);
		writer.close();
	}

}
