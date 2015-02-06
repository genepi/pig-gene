package piggene.serialisation.workflow.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.json.JSONException;

import piggene.representation.WorkflowGraph;
import piggene.serialisation.workflow.FlowComponent;
import piggene.serialisation.workflow.Position;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowComponent;
import piggene.serialisation.workflow.WorkflowReference;
import piggene.serialisation.workflow.WorkflowType;

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
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void store(final Workflow workflow, final String encodedWfName) throws IOException {
		try {
			final YamlWriter writer = new YamlWriter(new OutputStreamWriter(new FileOutputStream(workflowDefsPath.concat(encodedWfName
					.concat(fileExtension)))));
			writer.getConfig().setPropertyElementType(Workflow.class, "components", Workflow.class);
			writer.getConfig().setPropertyElementType(Workflow.class, "flowComponents", FlowComponent.class);
			writer.write(workflow);
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static Workflow load(final String name) throws IOException {
		final YamlReader reader = new YamlReader(new FileReader(workflowDefsPath.concat(name.concat(fileExtension))));
		reader.getConfig().setPropertyElementType(Workflow.class, "components", Workflow.class);
		reader.getConfig().setPropertyElementType(Workflow.class, "flowComponents", FlowComponent.class);
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		return workflow;
	}

	public static Workflow resolveWorkflowReferences(final Workflow workflow) throws IOException, JSONException {
		final List<Workflow> resolvedSteps = new ArrayList<Workflow>();
		for (final Workflow wf : workflow.getComponents()) {
			if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
				resolvedSteps.add(getAllDependingReferencedWorkflowSteps(wf.getName(), ((WorkflowReference) wf).getPosition()));
			} else {
				resolvedSteps.add(wf);
			}
		}

		final Workflow wf = new Workflow(workflow.getName(), workflow.getDescription(), resolvedSteps, workflow.getFlowComponents(),
				workflow.getParameter(), workflow.getParameterMapping());
		wf.setConnections(WorkflowGraph.createConnectionList(wf));
		return wf;
	}

	private static Workflow getAllDependingReferencedWorkflowSteps(final String workflowName, final Position positionInfo) throws IOException {
		final Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName);
		// change type because it is a RESOLVED REFERENCED wf
		referencedWorkflow.setWorkflowType(WorkflowType.WORKFLOW_REFERENCE);
		referencedWorkflow.setPosition(positionInfo);

		// combine content of all recursively used components
		// within one single WORKFLOW_COMPONENT
		final WorkflowComponent resolvedWfComp = new WorkflowComponent();
		resolvedWfComp.setContent(mergeContentOfReferencedWorkflow(referencedWorkflow.getComponents()));

		final List<Workflow> componentList = new ArrayList<Workflow>();
		componentList.add(resolvedWfComp);
		referencedWorkflow.setComponents(componentList);
		return referencedWorkflow;
	}

	private static String mergeContentOfReferencedWorkflow(final List<Workflow> components) throws IOException {
		final StringBuilder sb = new StringBuilder();
		boolean appendLineBreak = false;
		if (!(components == null)) {
			for (final Workflow wf : components) {
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

	public static boolean remove(final String name) throws FileNotFoundException {
		final File file = new File(workflowDefsPath.concat(name).concat(fileExtension));
		if (!file.exists()) {
			throw new FileNotFoundException("file does not exist");
		}
		return file.delete();
	}

	public static List<String> getListOfWorkflowNames() throws IOException {
		final File file = new File(workflowDefsPath);
		final File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		final List<String> fileNames = new ArrayList<String>();
		Workflow workflow = null;
		for (final File f : files) {
			workflow = WorkflowSerialisation.load(f.getName().replaceAll(fileExtension, ""));
			fileNames.add(workflow.getName());
		}
		Collections.sort(fileNames, new Comparator<String>() {
			@Override
			public int compare(final String name1, final String name2) {
				return name1.compareTo(name2);
			}
		});
		return fileNames;
	}

}