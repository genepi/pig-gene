package piggene.serialisation.workflow;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowSerialisation {
	private static Properties prop = new Properties();
	private static String workflowDefsPath;

	static {
		try {
			prop.load(WorkflowSerialisation.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefsPath = prop.getProperty("workflowDefs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void store(final Workflow workflow) throws IOException {
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(
				workflowDefsPath.concat(workflow.getName().concat(".yaml")))));
		writer.getConfig().setPropertyElementType(Workflow.class, "steps", Workflow.class);
		writer.write(workflow);
		writer.close();
	}

	public static Workflow load(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(workflowDefsPath.concat(name.concat(".yaml"))));
		reader.getConfig().setPropertyElementType(Workflow.class, "steps", Workflow.class);
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		Workflow resolveWorkflow = resolveWorkflowReferences(workflow);
		return resolveWorkflow;
	}

	private static Workflow resolveWorkflowReferences(final Workflow workflow) throws IOException {
		ArrayList<Workflow> resolvedSteps = new ArrayList<Workflow>();
		for (Workflow wf : workflow.getSteps()) {
			if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
				Workflow referencedWorkflow = WorkflowSerialisation.load(wf.getName());
				resolvedSteps.add(referencedWorkflow);
				resolveWorkflowReferences(referencedWorkflow);
			} else {
				resolvedSteps.add(wf);
			}
		}
		return new Workflow(workflow.getName(), workflow.getDescription(), resolvedSteps,
				workflow.getInputParameters(), workflow.getOutputParameters());
	}

}