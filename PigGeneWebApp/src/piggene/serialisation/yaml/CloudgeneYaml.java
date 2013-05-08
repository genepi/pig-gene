package piggene.serialisation.yaml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import piggene.serialisation.Workflow;
import piggene.serialisation.WorkflowComponent;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class CloudgeneYaml {
	private final static String PATH = "apps/piggene/";

	public static void generateCloudgeneYamlFile(final Workflow workflow) throws IOException {
		final App app = new App();
		StringBuilder parameters = new StringBuilder();
		final ArrayList<Parameter> inputs = new ArrayList<Parameter>();
		final ArrayList<Parameter> outputs = new ArrayList<Parameter>();

		final ArrayList<WorkflowComponent> workflowDefinition = workflow.getWorkflow();
		for (final WorkflowComponent wfc : workflowDefinition) {
			if (wfc.getOperation().equals("LOAD")) {
				parameters = appendParameter(parameters, wfc.getInput());
				inputs.add(createInputParameter(wfc.getInput()));
			} else if (wfc.getOperation().equals("STORE")) {
				parameters = appendParameter(parameters, wfc.getRelation());
				outputs.add(createOutputParameter(wfc.getRelation()));
			}
		}

		final Step step = new Step();
		step.setName("PigScript");
		step.setPig(workflow.getName());
		step.setParams(parameters.toString().trim());

		final ArrayList<Step> steps = new ArrayList<Step>();
		steps.add(step);

		final MapReduceConfig mapred = new MapReduceConfig();
		mapred.setSteps(steps);
		mapred.setInputs(inputs);
		mapred.setOutputs(outputs);

		app.setName(workflow.getName());
		app.setDescription(workflow.getDescription());
		app.setVersion("1.0.0");
		app.setCategory("Piggene");
		app.setMapred(mapred);

		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(PATH.concat(workflow.getName().concat(".yaml")))));
		writer.getConfig().setClassTag("App.class", App.class);
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
		param.setType("hdfs-folder");
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