package piggene.helper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.actions.WorkflowSerialisation;

public class CleanUpWfDefinitionHelper {
	private static Properties prop = new Properties();
	private static String wfAbbr;
	
	static {
		try {
			prop.load(WorkflowSerialisation.class.getClassLoader().getResourceAsStream("config.properties"));
			wfAbbr = prop.getProperty("wfAbbr");
		} catch (final IOException e) {
			// problem loading the properties file
			e.printStackTrace();
		}
	}

	public static void cleanUp(String componentName, String connectorName, String type) throws IOException {
		List<String> listOfWorkflowNames = WorkflowSerialisation.getListOfWorkflowNames(wfAbbr);
		for(String wfName : listOfWorkflowNames) {
			Workflow wf = WorkflowSerialisation.load(wfName, wfAbbr);
			modifyWorkflowsThatContainComponent(wf, wfName, componentName, connectorName, type);
		}
	}
	
	private static void modifyWorkflowsThatContainComponent(Workflow wf, String encodedWfName, String componentName, String connectorName, String type) throws IOException {
		List<Workflow> components = wf.getComponents();
		for(Workflow comp : components) {
			if(comp.getName().equals(componentName)) {
				String uid = comp.getUid();
				if(type.equals("input")) {
					modifyInputParameterMapping(wf, uid, connectorName);
				} else {
					modifyOutputParameterMapping(wf, uid, connectorName);
				}
				WorkflowSerialisation.store(wf, encodedWfName, wfAbbr);
				break;
			}
		}
	}
	
	private static void modifyInputParameterMapping(Workflow wf, String uid, String connectorName) {
		Map<String, Map<String, String>> inputParameterMapping = wf.getParameterMapping().getInputParameterMapping();
		Map<String, String> map = inputParameterMapping.get(uid);
		map.remove(connectorName);
	}

	private static void modifyOutputParameterMapping(Workflow wf, String uid, String connectorName) {
		Map<String, Map<String, String>> outputParameterMapping = wf.getParameterMapping().getOutputParameterMapping();
		Map<String, String> map = outputParameterMapping.get(uid);
		map.remove(connectorName);
	}
	
}