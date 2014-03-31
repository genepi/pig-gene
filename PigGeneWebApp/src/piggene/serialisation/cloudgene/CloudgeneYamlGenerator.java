package piggene.serialisation.cloudgene;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Properties;

import piggene.serialisation.workflow.LoadOperation;
import piggene.serialisation.workflow.StoreOperation;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowType;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class CloudgeneYamlGenerator {
	private static Properties prop = new Properties();
	private static String pigFiles = "apps/piggene/";
	private static StringBuilder parameters;
	private static ArrayList<Parameter> inputs;
	private static ArrayList<Parameter> outputs;

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			pigFiles = prop.getProperty("pigFiles");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAndStoreFile(final Workflow workflow) throws IOException {
		parameters = new StringBuilder();
		inputs = new ArrayList<Parameter>();
		outputs = new ArrayList<Parameter>();
		App app = new App();

		retrieveInAndOutputParameters(workflow);

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

		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(pigFiles.concat(workflow
				.getName().concat(".yaml")))));
		writer.getConfig().setClassTag("cloudgene.mapred.apps.App", App.class);
		writer.getConfig().setPropertyElementType(MapReduceConfig.class, "steps", Step.class);
		writer.getConfig().setPropertyElementType(MapReduceConfig.class, "inputs", InputParameter.class);
		writer.getConfig().setPropertyElementType(MapReduceConfig.class, "outputs", OutputParameter.class);
		writer.write(app);
		writer.close();
	}

	private static void retrieveInAndOutputParameters(final Workflow workflow) {
		ArrayList<String> inputParams = workflow.getInputParameters();
		ArrayList<String> outputParams = workflow.getOutputParameters();

		int inIdx = 0;
		int outIdx = 0;
		for (Workflow step : workflow.getSteps()) {

			// TODO mapping: inputs fuer subworkflow

			if (step.getWorkflowType().equals(WorkflowType.WORKFLOW_SINGLE_ELEM)) {
				if (step.getClass().equals(LoadOperation.class)) {
					String inputParamName = ((LoadOperation) step).getInput();
					parameters = appendParameter(parameters, inputParamName, inputParams.get(inIdx++));
					inputs.add(createInputParameter(inputParamName));
				} else if (step.getClass().equals(StoreOperation.class)) {
					String outputParamName = ((StoreOperation) step).getRelation();
					parameters = appendParameter(parameters, outputParamName, outputParams.get(outIdx++));
					outputs.add(createOutputParameter(outputParamName));
				}
			}
		}
	}

	private static StringBuilder appendParameter(final StringBuilder parameters, final String parameterValue,
			final String parameterName) {
		return parameters.append("-param ").append(parameterName).append("=$").append(parameterValue).append(" ");
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