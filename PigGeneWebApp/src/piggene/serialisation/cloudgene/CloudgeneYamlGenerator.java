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
import piggene.serialisation.workflow.WorkflowComponent;
import piggene.serialisation.workflow.WorkflowType;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

import com.esotericsoftware.yamlbeans.YamlWriter;

public class CloudgeneYamlGenerator {
	private static Properties prop = new Properties();
	private static String cloudgeneYamls = "";
	private static StringBuilder pigScriptParameters;
	private static final String RmdfileExtension = ".Rmd";

	static {
		try {
			prop.load(CloudgeneYamlGenerator.class.getClassLoader().getResourceAsStream("config.properties"));
			cloudgeneYamls = prop.getProperty("cloudgeneYamls");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static void generateAndStoreFile(final Workflow workflow) throws IOException {
		pigScriptParameters = new StringBuilder();

		// calls also set pigScriptParameters global variable - therefore have
		// to be made before step creation!!
		final List<WdlParameter> scriptInputs = retrieveWorkflowInputParameters(workflow.getParameter());
		final List<WdlParameter> scriptOutputs = retrieveWorkflowOutputParameters(workflow.getParameter());

		final List<WdlStep> steps = new ArrayList<WdlStep>();
		if (!(workflow.getComponents().size() == 1 && workflow.getComponents().get(0) instanceof WorkflowComponent && ((WorkflowComponent) workflow
				.getComponents().get(0)).getScriptType().getId() == 1)) {
			final WdlStep pigScriptStep = new WdlStep();
			pigScriptStep.setName("PigScript");
			pigScriptStep.setPig(workflow.getName().concat(".pig"));
			pigScriptStep.setParams(pigScriptParameters.toString().trim());
			steps.add(pigScriptStep);
		}

		final List<WdlStep> rmdSteps = retrieveRmdSteps(null, workflow);
		if (rmdSteps.size() > 0) {
			steps.addAll(rmdSteps);
			final WdlParameter reportParameter = new WdlParameterOutput();
			reportParameter.setId("report");
			reportParameter.setDescription("plot");
			reportParameter.setType("local-folder");
			reportParameter.setDownload(true);
			reportParameter.setTemp(false);
			scriptOutputs.add(reportParameter);
		}

		final WdlMapReduce mapred = new WdlMapReduce();
		mapred.setSteps(steps);
		mapred.setInputs(scriptInputs);
		mapred.setOutputs(scriptOutputs);

		final WdlApp app = new WdlApp();
		app.setName(workflow.getName());
		app.setDescription(workflow.getDescription());
		app.setVersion("0.3.0");
		app.setCategory("Piggene");
		app.setMapred(mapred);

		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(cloudgeneYamls.concat(workflow.getName()).concat("/")
				.concat("cloudgene.yaml"))));
		writer.getConfig().writeConfig.setWriteRootTags(false);
		writer.getConfig().setPropertyElementType(WdlMapReduce.class, "steps", WdlStep.class);
		writer.getConfig().setPropertyElementType(WdlMapReduce.class, "inputs", WdlParameterInput.class);
		writer.getConfig().setPropertyElementType(WdlMapReduce.class, "outputs", WdlParameterOutput.class);
		writer.getConfig().setPropertyElementType(WdlStep.class, "mapping", HashMap.class);
		writer.write(app);
		writer.close();
	}

	private static List<WdlParameter> retrieveWorkflowInputParameters(final WorkflowParameter workflowParameter) {
		final List<WdlParameter> pigScriptInputs = new ArrayList<WdlParameter>();
		final List<LinkParameter> inputParameters = workflowParameter.getInputParameter();
		for (final LinkParameter in : inputParameters) {
			appendParameter(pigScriptParameters, in.getConnector());
			pigScriptInputs.add(createInputParameter(in.getConnector(), in.getDescription()));
		}
		return pigScriptInputs;
	}

	private static List<WdlParameter> retrieveWorkflowOutputParameters(final WorkflowParameter workflowParameter) {
		final List<WdlParameter> pigScriptOutputs = new ArrayList<WdlParameter>();
		final List<LinkParameter> outputParameters = workflowParameter.getOutputParameter();
		for (final LinkParameter out : outputParameters) {
			appendParameter(pigScriptParameters, out.getConnector());
			pigScriptOutputs.add(createOutputParameter(out.getConnector(), out.getDescription()));
		}
		return pigScriptOutputs;
	}

	private static StringBuilder appendParameter(final StringBuilder parameters, final String parameterName) {
		return parameters.append("-param ").append(parameterName).append("=$").append(parameterName).append(" ");
	}

	private static WdlParameter createInputParameter(final String parameterName, final String description) {
		final WdlParameterInput param = new WdlParameterInput();
		param.setId(parameterName);
		param.setDescription(description);
		param.setType("hdfs-folder");
		return param;
	}

	private static WdlParameter createOutputParameter(final String parameterName, final String description) {
		final WdlParameterOutput param = new WdlParameterOutput();
		param.setId(parameterName);
		if (description.equals("")) {
			param.setDescription("plot output param");
		} else {
			param.setDescription(description);
		}
		param.setType("hdfs-folder");
		param.setDownload(true);
		param.setTemp(false);
		param.setZip(false);
		param.setRemoveHeader(false);
		param.setMergeOutput(true);
		return param;
	}

	private static List<WdlStep> retrieveRmdSteps(final Workflow surroundingWorkflow, Workflow workflow) throws IOException {
		final List<WdlStep> rmdSteps = new ArrayList<WdlStep>();
		String referencedWfUid = null;
		if (workflow.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
			final String workflowName = workflow.getName();
			referencedWfUid = workflow.getUid();
			workflow = WorkflowSerialisation.load(workflowName, WorkflowSerialisation.determineType(workflowName));
		}
		if (workflow.getComponents() == null || workflow.getComponents().size() == 0) {
			return rmdSteps;
		}
		if (workflow.getComponents().size() > 1 || !(workflow.getComponents().get(0) instanceof WorkflowComponent)) {
			for (final Workflow comp : workflow.getComponents()) {
				rmdSteps.addAll(retrieveRmdSteps(workflow, comp));
			}
		} else if (((WorkflowComponent) workflow.getComponents().get(0)).getScriptType().getId() == 1) {
			if (referencedWfUid == null) {
				throw new IOException("uid of referenced wf was not properly set");
			}
			final WdlStep rmdStep = new WdlStep();
			rmdStep.setName("RmdScript");
			rmdStep.setRmd(workflow.getName().concat(RmdfileExtension));
			rmdStep.setOutput("${report}/".concat(workflow.getName().concat(String.valueOf(System.currentTimeMillis()).concat(".html"))));

			final Map<String, String> mapping = new HashMap<String, String>();
			for (final LinkParameter in : workflow.getParameter().getInputParameter()) {
				String name = "";
				if (in.getConnector().startsWith("$")) {
					name = in.getConnector().substring(1);
				} else {
					name = in.getConnector();
				}
				if (surroundingWorkflow != null) {
					final WorkflowParameterMapping parameterMapping = surroundingWorkflow.getParameterMapping();
					final Map<String, Map<String, String>> inputParameterMapping = parameterMapping.getInputParameterMapping();

					String connectorVal = inputParameterMapping.get(referencedWfUid).get(in.getConnector());
					if (connectorVal != null) {
						connectorVal = "$".concat(connectorVal);
					}
					mapping.put(name, connectorVal);
				}
			}
			mapping.put("title", workflow.getName());
			mapping.put("xaxis", "x");
			mapping.put("yaxis", "y");
			rmdStep.setMapping(mapping);
			rmdSteps.add(rmdStep);
		} else {
			// skip workflow components with "PigScript Type" (=scripttype: 0)"
		}
		return rmdSteps;
	}
}