package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import piggene.serialisation.cloudgene.CloudgeneYamlGenerator;
import piggene.serialisation.pig.MissingParameterException;
import piggene.serialisation.pig.PigScriptGenerator;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowSerialisation;

public class ScriptCreationTests {

	@Test
	public void generatePigScript_standard() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-std");
			PigScriptGenerator.generateAndStoreScript(wf);
		} catch (IOException e) {
			fail("error: serialized (standard) workflow could not be loaded - was it created??");
		} catch (MissingParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(wf);
		// TODO !manually! check generated PigScript!!
	}

	@Test
	public void generatePigScript_recursive() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-rec");
			PigScriptGenerator.generateAndStoreScript(wf);
		} catch (IOException e) {
			fail("error: serialized (standard) workflow could not be loaded - was it created??");
		} catch (MissingParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(wf);
		// TODO !manually! check generated PigScript!!
	}

	@Test
	public void generateCloudgeneYaml_standard() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-std");
			CloudgeneYamlGenerator.generateAndStoreFile(wf);
		} catch (IOException e) {
			fail("error: serialized (standard) workflow could not be loaded - was it created??");
		}
		assertNotNull(wf);
		// TODO !manually! check generated PigScript!!
	}

	@Test
	public void generateCloudgeneYaml_recursive() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-rec");
			CloudgeneYamlGenerator.generateAndStoreFile(wf);
		} catch (IOException e) {
			fail("error: serialized (recursive) workflow could not be loaded - was it created??");
		}
		assertNotNull(wf);
		// TODO !manually! check generated PigScript!!
	}

}