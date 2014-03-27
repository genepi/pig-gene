package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import piggene.serialisation.WorkflowSerialisation;
import piggene.serialisation.scriptcreation.PigScript;
import piggene.serialisation.workflow.Workflow;

public class ScriptCreationTests {

	@Test
	public void generatePigScript_standard() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-std");
			PigScript.generateAndWrite(wf);
		} catch (IOException e) {
			fail("error: serialized (standard) workflow could not be loaded - was it created??");
		}
		assertNotNull(wf);
		// TODO !manually! check generated PigScript!!
	}

	@Test
	public void generatePigScript_recursive() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-rec");
			PigScript.generateAndWrite(wf);
		} catch (IOException e) {
			fail("error: serialized (standard) workflow could not be loaded - was it created??");
		}
		assertNotNull(wf);
		// TODO !manually! check generated PigScript!!
	}

}