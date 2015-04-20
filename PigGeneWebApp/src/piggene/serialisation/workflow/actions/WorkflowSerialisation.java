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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import piggene.representation.ConnectionGraph;
import piggene.representation.ConnectionGraphException;
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
	private static String componentDefsPath;
	private static String fileExtension = ".yaml";
	private static String wfAbbr;
	private static String compAbbr;

	static {
		try {
			prop.load(WorkflowSerialisation.class.getClassLoader().getResourceAsStream("config.properties"));
			workflowDefsPath = prop.getProperty("workflowDefs");
			componentDefsPath = prop.getProperty("componentDefs");
			wfAbbr = prop.getProperty("wfAbbr");
			compAbbr = prop.getProperty("compAbbr");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static void store(final Workflow workflow, final String encodedWfName, final String type) throws IOException {
		try {
			FileOutputStream fos = null;
			if (type.equals(wfAbbr)) {
				fos = new FileOutputStream(workflowDefsPath.concat(encodedWfName.concat(fileExtension)));
			} else if (type.equals(compAbbr)) {
				fos = new FileOutputStream(componentDefsPath.concat(encodedWfName.concat(fileExtension)));
			}
			final YamlWriter writer = new YamlWriter(new OutputStreamWriter(fos));
			writer.getConfig().setPropertyElementType(Workflow.class, "components", Workflow.class);
			writer.write(workflow);
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static Workflow load(final String name, final String type) throws IOException {
		if (type == null) {
			throw new IOException();
		}
		FileReader fr = null;
		if (type.equals(wfAbbr)) {
			fr = new FileReader(workflowDefsPath.concat(name.concat(fileExtension)));
		} else if (type.equals(compAbbr)) {
			fr = new FileReader(componentDefsPath.concat(name.concat(fileExtension)));
		}
		final YamlReader reader = new YamlReader(fr);
		reader.getConfig().setPropertyElementType(Workflow.class, "components", Workflow.class);
		final Workflow workflow = (Workflow) reader.read();
		reader.close();
		return workflow;
	}

	public static Workflow resolveWorkflowReferences(final Workflow workflow) throws IOException, JSONException, ConnectionGraphException {
		final List<Workflow> resolvedSteps = new ArrayList<Workflow>();
		for (final Workflow wf : workflow.getComponents()) {
			if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_REFERENCE)) {
				resolvedSteps.add(getAllDependingReferencedWorkflowSteps(((WorkflowReference) wf).getUid(), wf.getName(),
						((WorkflowReference) wf).getPosition()));
			} else {
				resolvedSteps.add(wf);
			}
		}

		final Workflow wf = new Workflow(workflow.getName(), workflow.getDescription(), resolvedSteps, workflow.getParameter(),
				workflow.getParameterMapping());
		wf.setConnections(ConnectionGraph.createConnectionList(wf));
		return wf;
	}

	private static Workflow getAllDependingReferencedWorkflowSteps(final String uid, final String workflowName, final Position positionInfo)
			throws IOException {
		final Workflow referencedWorkflow = WorkflowSerialisation.load(workflowName, determineType(workflowName));
		// change type because it is a RESOLVED REFERENCED wf
		referencedWorkflow.setWorkflowType(WorkflowType.WORKFLOW_REFERENCE);
		referencedWorkflow.setUid(uid);
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
					final String workflowName = wf.getName();
					sb.append(mergeContentOfReferencedWorkflow(WorkflowSerialisation.load(workflowName, determineType(workflowName)).getComponents()));
				} else if (wf.getWorkflowType().equals(WorkflowType.WORKFLOW_COMPONENT)) {
					sb.append(((WorkflowComponent) wf).getContent());
				}
			}
		}
		return sb.toString();
	}

	public static boolean remove(final String name, final String type) throws FileNotFoundException {
		File file = null;
		if (type.equals(wfAbbr)) {
			file = new File(workflowDefsPath.concat(name).concat(fileExtension));
		} else if (type.equals(compAbbr)) {
			file = new File(componentDefsPath.concat(name).concat(fileExtension));
		}
		if (file == null || !file.exists()) {
			throw new FileNotFoundException("file does not exist");
		}
		return file.delete();
	}

	public static List<String> getListOfWorkflowNames(final String type) throws IOException {
		File directory = null;
		if (type.equals(wfAbbr)) {
			directory = new File(workflowDefsPath);
		} else if (type.equals(compAbbr)) {
			directory = new File(componentDefsPath);
		}
		if (directory == null) {
			return null;
		}
		final File[] files = directory.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		final List<String> fileNames = new ArrayList<String>();
		for (final File f : files) {
			final String fileName = java.net.URLDecoder.decode(f.getName(), "UTF-8");
			final String regex = "([\\w\\d -]+).(\\w+\\b)";
			final Pattern p = Pattern.compile(regex);
			final Matcher m = p.matcher(fileName);
			if (m.find()) {
				fileNames.add(m.group(1));
			}

		}
		Collections.sort(fileNames, new Comparator<String>() {
			@Override
			public int compare(final String name1, final String name2) {
				return name1.compareTo(name2);
			}
		});
		return fileNames;
	}

	public static String determineType(final String workflowName) {
		File f = new File(workflowDefsPath.concat(workflowName).concat(fileExtension));
		if (f.exists()) {
			return wfAbbr;
		}
		f = new File(componentDefsPath.concat(workflowName).concat(fileExtension));
		if (f.exists()) {
			return compAbbr;
		}
		return null;
	}

}