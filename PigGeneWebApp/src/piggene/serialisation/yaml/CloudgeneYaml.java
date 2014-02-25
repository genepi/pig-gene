package piggene.serialisation.yaml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Properties;

import piggene.serialisation.SingleWorkflowElement;
import piggene.serialisation.Workflow;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class CloudgeneYaml {
	private static Properties prop = new Properties();
	private static String pigFiles = "apps/piggene/";

	static {
		try {
			prop.load(CloudgeneYaml.class.getClassLoader().getResourceAsStream("config.properties"));
			pigFiles = prop.getProperty("pigFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateCloudgeneYamlFile(final Workflow workflow) throws IOException {
		final App app = new App();
		StringBuilder parameters = new StringBuilder();
		final ArrayList<Parameter> inputs = new ArrayList<Parameter>();
		final ArrayList<Parameter> outputs = new ArrayList<Parameter>();

		final ArrayList<SingleWorkflowElement> workflowDefinition = workflow.getWorkflow();
		for (final SingleWorkflowElement wfc : workflowDefinition) {
			if ("LOAD".equals(wfc.getOperation())) {
				parameters = appendParameter(parameters, wfc.getInput());
				inputs.add(createInputParameter(wfc.getInput()));
			} else if ("STORE".equals(wfc.getOperation())) {
				parameters = appendParameter(parameters, wfc.getRelation());
				outputs.add(createOutputParameter(wfc.getRelation()));
			}
		}

		final Step step = new Step();
		step.setName("PigScript");
		step.setPig(workflow.getName().concat(".pig"));
		step.setParams(parameters.toString().trim());

		final ArrayList<Step> steps = new ArrayList<Step>();
		steps.add(step);

		final MapReduceConfig mapred = new MapReduceConfig();
		mapred.setSteps(steps);
		mapred.setInputs(inputs);
		mapred.setOutputs(outputs);

		app.setName(workflow.getName());
		app.setDescription(workflow.getDescription());
		app.setVersion("0.1.0");
		app.setCategory("Piggene");
		app.setMapred(mapred);

		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(pigFiles.concat(workflow.getName().concat(".yaml")))));
		writer.getConfig().setClassTag("cloudgene.mapred.apps.App", App.class);
		writer.getConfig().setPropertyElementType(MapReduceConfig.class, "steps", Step.class);
		writer.getConfig().setPropertyElementType(MapReduceConfig.class, "inputs", InputParameter.class);
		writer.getConfig().setPropertyElementType(MapReduceConfig.class, "outputs", OutputParameter.class);
		writer.write(app);
		writer.close();
	}

	private static StringBuilder appendParameter(final StringBuilder parameters, final String parameterName) {
		return parameters.append("-param ").append(parameterName).append("=$").append(parameterName).append(" ");
	}

	private static Parameter createInputParameter(final String parameterName) {
		final InputParameter param = new InputParameter();
		param.setId(parameterName);
		param.setDescription(parameterName);
		param.setType("hdfs-file");
		return param;
	}

	private static Parameter createOutputParameter(final String parameterName) {
		final OutputParameter param = new OutputParameter();
		param.setId(parameterName);
		param.setDescription(parameterName);
		param.setType("hdfs-folder");
		param.setDownload(true);
		param.setTemp(false);
		param.setZip(false);
		param.setMergeOutput(false);
		return param;
	}

}