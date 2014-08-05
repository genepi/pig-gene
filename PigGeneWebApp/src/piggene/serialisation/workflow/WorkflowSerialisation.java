package piggene.serialisation.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

public class WorkflowSerialisation {
	private static Properties prop = new Properties();
	private static String workflowDefsPath;
	private static String fileExtension = ".yaml";

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
		final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(workflowDefsPath.concat(workflow.getName().concat(
				fileExtension)))));
		writer.getConfig().setPropertyElementType(Workflow.class, "components", Workflow.class);
		writer.write(workflow);
		writer.close();
	}

	public static Workflow load(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(workflowDefsPath.concat(name.concat(fileExtension))));
		reader.getConfig().setPropertyElementType(Workflow.class, "components", Workflow.class);
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		return workflow;
	}

	public static Workflow resolveWorkflowReferences(final Workflow workflow) throws IOException {
		List<Workflow> resolvedSteps = new ArrayList<Workflow>();
		for (Workflow wf : workflow.getComponents()) {
			if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
				Workflow referencedWorkflow = WorkflowSerialisation.load(wf.getName());
				resolvedSteps.add(referencedWorkflow);
				resolveWorkflowReferences(referencedWorkflow);
			} else {
				resolvedSteps.add(wf);
			}
		}
		return new Workflow(workflow.getName(), workflow.getDescription(), resolvedSteps);
	}

	public static boolean remove(final String name) {
		File file = new File(workflowDefsPath.concat(name).concat(fileExtension));
		return file.delete();
	}

}