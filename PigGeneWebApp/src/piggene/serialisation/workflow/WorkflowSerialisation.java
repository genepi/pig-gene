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
				resolvedSteps.add(getAllDependingReferencedWorkflowSteps(wf.getName()));
			} else {
				resolvedSteps.add(wf);
			}
		}
		return new Workflow(workflow.getName(), workflow.getDescription(), resolvedSteps, workflow.getInputParams(), workflow.getInputParamMapping());
	}

	private static Workflow getAllDependingReferencedWorkflowSteps(final String workflowName) throws IOException {
		Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName);
		// change type because it is a RESOLVED REFERENCED wf
		referencedWorkflow.setWorkflowType(WorkflowType.WORKFLOW_REFERENCE);

		// combine content of all recursively used components
		// within one single WORKFLOW_COMPONENT
		WorkflowComponent resolvedWfComp = new WorkflowComponent();
		resolvedWfComp.setContent(mergeContentOfReferencedWorkflow(referencedWorkflow.getComponents()));

		List<Workflow> componentList = new ArrayList<Workflow>();
		componentList.add(resolvedWfComp);
		referencedWorkflow.setComponents(componentList);
		return referencedWorkflow;
	}

	private static String mergeContentOfReferencedWorkflow(final List<Workflow> components) throws IOException {
		StringBuilder sb = new StringBuilder();
		boolean appendLineBreak = false;
		if (!(components == null)) {
			for (Workflow wf : components) {
				if (appendLineBreak) {
					sb.append(System.getProperty("line.separator"));
				}
				appendLineBreak = true;
				if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
					sb.append(mergeContentOfReferencedWorkflow(WorkflowSerialisation.load(wf.getName()).getComponents()));
				} else if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_COMPONENT)) {
					sb.append(((WorkflowComponent) wf).getContent());
				}
			}
		}
		return sb.toString();
	}

	public static boolean remove(final String name) {
		File file = new File(workflowDefsPath.concat(name).concat(fileExtension));
		return file.delete();
	}

}