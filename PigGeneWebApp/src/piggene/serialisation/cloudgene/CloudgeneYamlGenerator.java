package piggene.serialisation.cloudgene;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameter;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class CloudgeneYamlGenerator {
	private static Properties prop = new Properties();
	private static String cloudgeneYamls = "";
	private static StringBuilder parameters;
	private static List<WdlParameter> inputs;
	private static List<WdlParameter> outputs;

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			cloudgeneYamls = prop.getProperty("cloudgeneYamls");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAndStoreFile(final Workflow workflow) throws IOException {
		parameters = new StringBuilder();
		inputs = new ArrayList<WdlParameter>();
		outputs = new ArrayList<WdlParameter>();

		retrieveAndSetParameters(workflow.getParameter());

		final List<WdlStep> steps = new ArrayList<WdlStep>();
		final WdlStep pigScriptStep = new WdlStep();
		pigScriptStep.setName("PigScript");
		pigScriptStep.setPig(workflow.getName().concat(".pig"));
		pigScriptStep.setParams(parameters.toString().trim());
		steps.add(pigScriptStep);

		final int noOfRMDSteps = workflow.getRMarkDownScriptRepresentations().size();
		if (noOfRMDSteps > 0) {
			final String fileExtension = ".Rmd";
			for (int i = 0; i < noOfRMDSteps; i++) {
				final WdlStep rMdScriptStep = new WdlStep();
				rMdScriptStep.setName("RmdScript");
				if (noOfRMDSteps == 1) {
					rMdScriptStep.setRmd(workflow.getName().concat(fileExtension));
					rMdScriptStep.setOutput("${report}/".concat(workflow.getName()).concat(".html"));
				} else {
					rMdScriptStep.setRmd(workflow.getName().concat("_").concat(String.valueOf(i)).concat(fileExtension));
					rMdScriptStep.setOutput("${report}/".concat(workflow.getName().concat("_").concat(String.valueOf(i))).concat(".html"));
				}

				final Map<String, String> mapping = new HashMap<String, String>();
				// TODO add mapping
				mapping.put("input", "TODO");
				mapping.put("title", "TODO");
				mapping.put("xaxis", "TODO");
				mapping.put("yaxis", "TODO");
				rMdScriptStep.setMapping(mapping);

				steps.add(rMdScriptStep);
			}

			final WdlParameter reportParameter = new WdlParameterOutput();
			reportParameter.setId("report");
			reportParameter.setDescription("XY-Plot");
			reportParameter.setType("local-folder");
			reportParameter.setDownload(true);
			reportParameter.setTemp(false);
			outputs.add(reportParameter);
		}

		final WdlMapReduce mapred = new WdlMapReduce();
		mapred.setSteps(steps);
		mapred.setInputs(inputs);
		mapred.setOutputs(outputs);

		final WdlApp app = new WdlApp();
		app.setName(workflow.getName());
		app.setDescription(workflow.getDescription());
		app.setVersion("0.2.0");
		app.setCategory("Piggene");
		app.setMapred(mapred);

		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(cloudgeneYamls.concat(workflow.getName()).concat("/")
				.concat("cloudgene.yaml"))));
		writer.getConfig().setClassTag("cloudgene.mapred.apps.WdlApp", WdlApp.class);
		writer.getConfig().setPropertyElementType(WdlMapReduce.class, "steps", WdlStep.class);
		writer.getConfig().setPropertyElementType(WdlMapReduce.class, "inputs", WdlParameterInput.class);
		writer.getConfig().setPropertyElementType(WdlMapReduce.class, "outputs", WdlParameterOutput.class);
		writer.getConfig().setPropertyElementType(WdlStep.class, "mapping", HashMap.class);
		writer.write(app);
		writer.close();
	}

	private static void retrieveAndSetParameters(final WorkflowParameter workflowParameter) {
		final List<LinkParameter> inputParameters = workflowParameter.getInputParameter();
		final List<LinkParameter> outputParameters = workflowParameter.getOutputParameter();
		for (final LinkParameter in : inputParameters) {
			appendParameter(parameters, in.getName());
			inputs.add(createInputParameter(in.getName()));
		}
		for (final LinkParameter out : outputParameters) {
			appendParameter(parameters, out.getName());
			outputs.add(createOutputParameter(out.getName()));
		}
	}

	private static StringBuilder appendParameter(final StringBuilder parameters, final String parameterName) {
		return parameters.append("-param ").append(parameterName).append("=$").append(parameterName).append(" ");
	}

	private static WdlParameter createInputParameter(final String parameterName) {
		final WdlParameterInput param = new WdlParameterInput();
		param.setId(parameterName);
		param.setDescription("input paramter");
		param.setType("hdfs-file");
		return param;
	}

	private static WdlParameter createOutputParameter(final String parameterName) {
		final WdlParameterOutput param = new WdlParameterOutput();
		param.setId(parameterName);
		param.setDescription("output parameter");
		param.setType("hdfs-folder");
		param.setDownload(true);
		param.setTemp(false);
		param.setZip(false);
		param.setRemoveHeader(false);
		param.setMergeOutput(true);
		return param;
	}

}