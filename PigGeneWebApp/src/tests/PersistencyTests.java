package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import piggene.serialisation.WorkflowConverter;
import piggene.serialisation.WorkflowSerialisation;
import piggene.serialisation.workflow.FilterOperation;
import piggene.serialisation.workflow.JoinOperation;
import piggene.serialisation.workflow.LoadOperation;
import piggene.serialisation.workflow.OrderByOperation;
import piggene.serialisation.workflow.RegisterOperation;
import piggene.serialisation.workflow.StoreOperation;
import piggene.serialisation.workflow.Workflow;

public class PersistencyTests {
	private static JSONObject wfJSON;

	@BeforeClass
	public static void setUp() {
		try {
			wfJSON = new JSONObject();
			wfJSON.accumulate("name", "testWf");
			wfJSON.accumulate("description", "test description");

			JSONArray wfDef = new JSONArray();
			JSONObject wfLine;
			ArrayList<String> differentOperations = new ArrayList<String>() {
				{
					add("REGISTER");
					add("LOAD");
					add("FILTER");
					add("JOIN");
					add("STORE");
				}
			};
			for (int i = 0; i < 5; i++) {
				wfLine = new JSONObject();
				wfLine.accumulate("relation", "R".concat(String.valueOf(i)));
				wfLine.accumulate("input", "R".concat(String.valueOf(i + 2)));
				wfLine.accumulate("input2", "R".concat(String.valueOf(i + 3)));
				wfLine.accumulate("operation", differentOperations.get(i));
				wfLine.accumulate("options", "options part one");
				wfLine.accumulate("options2", "options part two");
				wfLine.accumulate("comment", "comment".concat(String.valueOf(i)));
				wfDef.put(wfLine);
			}

			// recursive workflow
			wfLine = new JSONObject();
			wfLine.accumulate("name", "testWf_rec");
			wfLine.accumulate("description", "test description rec");

			JSONObject wfLine_rec = new JSONObject();
			wfLine_rec.accumulate("relation", "R rec");
			wfLine_rec.accumulate("input", "R rec 1");
			wfLine_rec.accumulate("input2", "R rec 2");
			wfLine_rec.accumulate("operation", "ORDER");
			wfLine_rec.accumulate("options", "options rec one");
			wfLine_rec.accumulate("options2", "options rec");
			wfLine_rec.accumulate("comment", "comment rec");
			wfLine.put("steps", new JSONArray().put(wfLine_rec));

			JSONArray in_rec = new JSONArray();
			in_rec.put("input3");
			wfLine.put("inputParameters", in_rec);
			wfLine.put("outputParameters", new JSONArray());
			wfDef.put(wfLine);
			//

			wfJSON.put("steps", wfDef);

			JSONArray in = new JSONArray();
			in.put("input1");
			in.put("input2");
			JSONArray out = new JSONArray();
			out.put("output1");

			wfJSON.put("inputParameters", in);
			wfJSON.put("outputParameters", out);
		} catch (JSONException e) {
			fail("error: setup of JSON Objects failed");
		}
		assertNotNull(wfJSON);
	}

	@Test
	public void convertJSONIntoWorkflowObj() {
		Workflow wf = null;
		try {
			wf = WorkflowConverter.processClientJSONData(wfJSON);
		} catch (JSONException e) {
			fail("error: JSON conversion of workflow failed");
		}
		assertNotNull(wf);
		assertNotNull(wf.getName());
		assertNotNull(wf.getSteps());
		assertEquals("testWf", wf.getName());
		assertEquals(RegisterOperation.class, wf.getSteps().get(0).getClass());
		assertEquals(LoadOperation.class, wf.getSteps().get(1).getClass());
		assertEquals(FilterOperation.class, wf.getSteps().get(2).getClass());
		assertEquals(JoinOperation.class, wf.getSteps().get(3).getClass());
		assertEquals(StoreOperation.class, wf.getSteps().get(4).getClass());
		assertEquals(Workflow.class, wf.getSteps().get(5).getClass());
		assertNotNull(wf.getSteps().get(5).getSteps());
		assertEquals(OrderByOperation.class, wf.getSteps().get(5).getSteps().get(0).getClass());
	}

	@Test
	public void persistWfDefinition() {
		Workflow wf = null;
		try {
			wf = WorkflowConverter.processClientJSONData(wfJSON);
			assertNotNull(wf);
			WorkflowSerialisation.store(wf);
		} catch (JSONException e) {
			fail("error: JSON conversion of workflow failed");
		} catch (IOException e) {
			fail("error: workflow-definition could not be saved to disk");
		}

	}

	@Test
	public void loadAndCheckPersistedWfDefinition() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf");
			assertNotNull(wf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}