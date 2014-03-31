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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import piggene.serialisation.workflow.FilterOperation;
import piggene.serialisation.workflow.GroupByOperation;
import piggene.serialisation.workflow.JoinOperation;
import piggene.serialisation.workflow.LoadOperation;
import piggene.serialisation.workflow.OrderByOperation;
import piggene.serialisation.workflow.ReferencedWorkflow;
import piggene.serialisation.workflow.RegisterOperation;
import piggene.serialisation.workflow.SelectOperation;
import piggene.serialisation.workflow.StoreOperation;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowConverter;
import piggene.serialisation.workflow.WorkflowSerialisation;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("serial")
public class PersistencyTests {
	private static JSONObject wfJSON;
	private static JSONObject wfJSON_level1;
	private static JSONObject wfJSON_level2;
	private static JSONObject wfJSON_level3;
	private static JSONObject wfJSON_rec;

	@BeforeClass
	public static void setUp() {
		createStandardWf();
		assertNotNull(wfJSON);
		createDependingRecursiveWorkflowDefinition1();
		assertNotNull(wfJSON_level1);
		assertNotNull(wfJSON_level2);
		assertNotNull(wfJSON_level3);
		createRecursiveWf();
		assertNotNull(wfJSON_rec);
	}

	@Test
	public void convertJSONIntoWorkflowObj_standard() {
		Workflow wf = null;
		try {
			wf = WorkflowConverter.processClientJSONData(wfJSON);
		} catch (JSONException e) {
			fail("error: JSON conversion of standard workflow failed");
		}
		assertNotNull(wf);
		assertNotNull(wf.getName());
		assertEquals("testWf_PersistencyTests-std", wf.getName());
		assertNotNull(wf.getDescription());
		assertEquals("test description", wf.getDescription());
		assertNotNull(wf.getSteps());
		assertEquals(6, wf.getSteps().size());
		assertEquals(RegisterOperation.class, wf.getSteps().get(0).getClass());
		assertEquals(LoadOperation.class, wf.getSteps().get(1).getClass());
		assertEquals(LoadOperation.class, wf.getSteps().get(2).getClass());
		assertEquals(FilterOperation.class, wf.getSteps().get(3).getClass());
		assertEquals(JoinOperation.class, wf.getSteps().get(4).getClass());
		assertEquals(StoreOperation.class, wf.getSteps().get(5).getClass());
	}

	@Test
	public void convertJSONIntoWorkflowObj_recDependantWf1() {
		Workflow wf_level1 = null;
		try {
			wf_level1 = WorkflowConverter.processClientJSONData(wfJSON_level1);
		} catch (JSONException e) {
			fail("error: JSON conversion of recursive dependant workflow 1 failed");
		}
		assertNotNull(wf_level1);
		assertNotNull(wf_level1.getName());
		assertEquals("testWf_PersistencyTests-rec-level1", wf_level1.getName());
		assertNotNull(wf_level1.getDescription());
		assertEquals("description of rec-level1 workflow", wf_level1.getDescription());
		assertNotNull(wf_level1.getSteps());
		assertEquals(8, wf_level1.getSteps().size());
		assertEquals(ReferencedWorkflow.class, wf_level1.getSteps().get(0).getClass());
		assertEquals(RegisterOperation.class, wf_level1.getSteps().get(1).getClass());
		assertEquals(LoadOperation.class, wf_level1.getSteps().get(2).getClass());
		assertEquals(FilterOperation.class, wf_level1.getSteps().get(3).getClass());
		assertEquals(JoinOperation.class, wf_level1.getSteps().get(4).getClass());
		assertEquals(StoreOperation.class, wf_level1.getSteps().get(5).getClass());
		assertEquals(StoreOperation.class, wf_level1.getSteps().get(6).getClass());
		assertEquals(StoreOperation.class, wf_level1.getSteps().get(7).getClass());
	}

	@Test
	public void convertJSONIntoWorkflowObj_recDependantWf2() {
		Workflow wf_level2 = null;
		try {
			wf_level2 = WorkflowConverter.processClientJSONData(wfJSON_level2);
		} catch (JSONException e) {
			fail("error: JSON conversion of recursive dependant workflow 2 failed");
		}
		assertNotNull(wf_level2);
		assertNotNull(wf_level2.getName());
		assertEquals("testWf_PersistencyTests-rec-level2", wf_level2.getName());
		assertNotNull(wf_level2.getDescription());
		assertEquals("description of rec-level2 workflow", wf_level2.getDescription());
		assertNotNull(wf_level2.getSteps());
		assertEquals(4, wf_level2.getSteps().size());
		assertEquals(LoadOperation.class, wf_level2.getSteps().get(0).getClass());
		assertEquals(GroupByOperation.class, wf_level2.getSteps().get(1).getClass());
		assertEquals(StoreOperation.class, wf_level2.getSteps().get(2).getClass());
		assertEquals(ReferencedWorkflow.class, wf_level2.getSteps().get(3).getClass());
	}

	@Test
	public void convertJSONIntoWorkflowObj_recDependantWf3() {
		Workflow wf_level3 = null;
		try {
			wf_level3 = WorkflowConverter.processClientJSONData(wfJSON_level3);
		} catch (JSONException e) {
			fail("error: JSON conversion of recursive dependant workflow 3 failed");
		}
		assertNotNull(wf_level3);
		assertNotNull(wf_level3.getName());
		assertEquals("testWf_PersistencyTests-rec-level3", wf_level3.getName());
		assertNotNull(wf_level3.getDescription());
		assertEquals("description of rec-level3 workflow", wf_level3.getDescription());
		assertNotNull(wf_level3.getSteps());
		assertEquals(9, wf_level3.getSteps().size());
		assertEquals(OrderByOperation.class, wf_level3.getSteps().get(0).getClass());
		assertEquals(SelectOperation.class, wf_level3.getSteps().get(1).getClass());
		assertEquals(LoadOperation.class, wf_level3.getSteps().get(2).getClass());
		assertEquals(LoadOperation.class, wf_level3.getSteps().get(3).getClass());
		assertEquals(LoadOperation.class, wf_level3.getSteps().get(4).getClass());
		assertEquals(LoadOperation.class, wf_level3.getSteps().get(5).getClass());
		assertEquals(JoinOperation.class, wf_level3.getSteps().get(6).getClass());
		assertEquals(StoreOperation.class, wf_level3.getSteps().get(7).getClass());
		assertEquals(StoreOperation.class, wf_level3.getSteps().get(8).getClass());
	}

	@Test
	public void convertJSONIntoWorkflowObj_recursive() {
		Workflow wf = null;
		try {
			wf = WorkflowConverter.processClientJSONData(wfJSON_rec);
		} catch (JSONException e) {
			fail("error: JSON conversion of recursive workflow failed");
		}
		assertNotNull(wf);
		assertNotNull(wf.getName());
		assertEquals("testWf_PersistencyTests-rec", wf.getName());
		assertNotNull(wf.getDescription());
		assertEquals("description of outermost workflow", wf.getDescription());
		assertNotNull(wf.getSteps());
		assertEquals(5, wf.getSteps().size());
		assertEquals(LoadOperation.class, wf.getSteps().get(0).getClass());
		assertEquals(LoadOperation.class, wf.getSteps().get(1).getClass());
		assertEquals(GroupByOperation.class, wf.getSteps().get(2).getClass());
		assertEquals(StoreOperation.class, wf.getSteps().get(3).getClass());
		assertEquals(ReferencedWorkflow.class, wf.getSteps().get(4).getClass());
	}

	@Test
	public void persistWfDefinition_standard() {
		Workflow wf = null;
		try {
			wf = WorkflowConverter.processClientJSONData(wfJSON);
			assertNotNull(wf);
			assertEquals(2, wf.getInputParameters().size());
			WorkflowSerialisation.store(wf);
		} catch (JSONException e) {
			fail("error: JSON conversion of workflow failed");
		} catch (IOException e) {
			fail("error: workflow-definition could not be saved to disk");
		}
	}

	@Test
	public void persistWfDefinition_recursiveDependencies() {
		Workflow wf1 = null;
		Workflow wf2 = null;
		Workflow wf3 = null;
		try {
			wf1 = WorkflowConverter.processClientJSONData(wfJSON_level1);
			wf2 = WorkflowConverter.processClientJSONData(wfJSON_level2);
			wf3 = WorkflowConverter.processClientJSONData(wfJSON_level3);
			assertNotNull(wf1);
			assertNotNull(wf2);
			assertNotNull(wf3);
			WorkflowSerialisation.store(wf1);
			WorkflowSerialisation.store(wf2);
			WorkflowSerialisation.store(wf3);
		} catch (JSONException e) {
			fail("error: JSON conversion of workflow failed");
		} catch (IOException e) {
			fail("error: workflow-definition could not be saved to disk");
		}
	}

	@Test
	public void persistWfDefinition_recursive() {
		Workflow wf = null;
		try {
			wf = WorkflowConverter.processClientJSONData(wfJSON_rec);
			assertNotNull(wf);
			WorkflowSerialisation.store(wf);
		} catch (JSONException e) {
			fail("error: JSON conversion of workflow failed");
		} catch (IOException e) {
			fail("error: workflow-definition could not be saved to disk");
		}
	}

	@Test
	public void xLoadAndCheckPersistedWfDefinition_standard() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-std");
		} catch (IOException e) {
			fail("error: serialized (standard) workflow could not be loaded");
		}
		assertNotNull(wf);
	}

	@Test
	public void xLoadAndCheckPersistedWfDefinition_recursive() {
		Workflow wf = null;
		try {
			wf = WorkflowSerialisation.load("testWf_PersistencyTests-rec");
		} catch (IOException e) {
			fail("error: serialized (recursive) workflow could not be loaded");
		}
		assertNotNull(wf);
	}

	private static void createStandardWf() {
		try {
			wfJSON = new JSONObject();
			wfJSON.accumulate("name", "testWf_PersistencyTests-std");
			wfJSON.accumulate("description", "test description");

			JSONArray wfDef = new JSONArray();
			JSONObject wfLine;
			ArrayList<String> differentOperations = new ArrayList<String>() {
				{
					add("REGISTER");
					add("LOAD");
					add("LOAD");
					add("FILTER");
					add("JOIN");
					add("STORE");
				}
			};
			for (int i = 0; i < differentOperations.size(); i++) {
				wfLine = new JSONObject();
				if (differentOperations.get(i).equals("STORE")) {
					wfLine.accumulate("relation", "output_" + i);
				} else {
					wfLine.accumulate("relation", "R".concat(String.valueOf(i)));
				}

				if (differentOperations.get(i).equals("LOAD")) {
					wfLine.accumulate("input", "input_" + i);
				} else {
					wfLine.accumulate("input", "R".concat(String.valueOf(i + 2)));
				}
				wfLine.accumulate("input2", "R".concat(String.valueOf(i + 3)));
				wfLine.accumulate("operation", differentOperations.get(i));
				wfLine.accumulate("options", "options part one");
				wfLine.accumulate("options2", "options part two");
				wfLine.accumulate("comment", "comment".concat(String.valueOf(i)));
				wfDef.put(wfLine);
			}

			wfJSON.put("steps", wfDef);

			JSONArray in = new JSONArray();
			in.put("testinput1");
			in.put("testinput2");
			JSONArray out = new JSONArray();
			out.put("testoutput");

			wfJSON.put("inputParameters", in);
			wfJSON.put("outputParameters", out);
		} catch (JSONException e) {
			fail("error: setup of JSON Objects failed");
		}
	}

	private static void createRecursiveWf() {
		try {
			wfJSON_rec = new JSONObject();
			wfJSON_rec.accumulate("name", "testWf_PersistencyTests-rec");
			wfJSON_rec.accumulate("description", "description of outermost workflow");

			JSONArray wfDef = new JSONArray();
			JSONObject wfLine = null;
			ArrayList<String> differentOperations = new ArrayList<String>() {
				{
					add("LOAD");
					add("LOAD");
					add("GROUP");
					add("STORE");
				}
			};
			for (int i = 0; i < differentOperations.size(); i++) {
				wfLine = new JSONObject();
				wfLine.accumulate("relation", "R_outermost".concat(String.valueOf(i)));
				if (differentOperations.get(i).equals("LOAD")) {
					wfLine.accumulate("input", "input_" + i);
				} else if (differentOperations.get(i).equals("STORE")) {
					wfLine.accumulate("input", "output_" + i);
				} else {
					wfLine.accumulate("input", "R".concat(String.valueOf(i + 2)));
				}
				wfLine.accumulate("input2", "R_outermost".concat(String.valueOf(i + 3)));
				wfLine.accumulate("operation", differentOperations.get(i));
				wfLine.accumulate("options", "options_outermost part one");
				wfLine.accumulate("options2", "options_outermost part two");
				wfLine.accumulate("comment", "comment_outermost".concat(String.valueOf(i)));
				wfDef.put(wfLine);
			}

			wfDef.put(wfJSON_level1);
			wfJSON_rec.put("steps", wfDef);

			JSONArray in = new JSONArray();
			in.put("input1_outermost");
			in.put("input2_outermost");
			JSONArray out = new JSONArray();
			out.put("output1_outermost");
			wfJSON_rec.put("inputParameters", in);
			wfJSON_rec.put("outputParameters", out);

		} catch (JSONException e) {
			fail("error: setup of JSON Objects failed");
		}
		assertNotNull(wfJSON_rec);
	}

	private static void createDependingRecursiveWorkflowDefinition1() {
		try {
			wfJSON_level1 = new JSONObject();
			wfJSON_level1.accumulate("name", "testWf_PersistencyTests-rec-level1");
			wfJSON_level1.accumulate("description", "description of rec-level1 workflow");

			JSONArray wfDef_level1 = new JSONArray();
			JSONObject wfLine_level1 = null;
			ArrayList<String> differentOperations_level1 = new ArrayList<String>() {
				{
					add("REGISTER");
					add("LOAD");
					add("FILTER");
					add("JOIN");
					add("STORE");
					add("STORE");
					add("STORE");
				}
			};
			createDependingRecursiveWorkflowDefinition2();
			wfDef_level1.put(wfJSON_level2);
			for (int i = 0; i < differentOperations_level1.size(); i++) {
				wfLine_level1 = new JSONObject();
				wfLine_level1.accumulate("relation", "R_level1_".concat(String.valueOf(i)));
				if (differentOperations_level1.get(i).equals("LOAD")) {
					wfLine_level1.accumulate("input", "input_level1_" + i);
				} else if (differentOperations_level1.get(i).equals("STORE")) {
					wfLine_level1.accumulate("input", "output_level1_" + i);
				} else {
					wfLine_level1.accumulate("input", "R".concat(String.valueOf(i + 2)));
				}
				wfLine_level1.accumulate("input2", "R_level1_".concat(String.valueOf(i + 3)));
				wfLine_level1.accumulate("operation", differentOperations_level1.get(i));
				wfLine_level1.accumulate("options", "options_level1 part one");
				wfLine_level1.accumulate("options2", "options_level1 part two");
				wfLine_level1.accumulate("comment", "comment_level1".concat(String.valueOf(i)));
				wfDef_level1.put(wfLine_level1);
			}
			wfJSON_level1.put("steps", wfDef_level1);

			JSONArray in_level1 = new JSONArray();
			in_level1.put("input1_level1");
			JSONArray out_level1 = new JSONArray();
			out_level1.put("output1_level1");
			out_level1.put("output2_level1");
			out_level1.put("output3_level1");
			wfJSON_level1.put("inputParameters", in_level1);
			wfJSON_level1.put("outputParameters", out_level1);
		} catch (JSONException e) {
			fail("error: setup of JSON Objects failed");
		}
	}

	private static void createDependingRecursiveWorkflowDefinition2() {
		try {
			wfJSON_level2 = new JSONObject();
			wfJSON_level2.accumulate("name", "testWf_PersistencyTests-rec-level2");
			wfJSON_level2.accumulate("description", "description of rec-level2 workflow");

			JSONArray wfDef_level2 = new JSONArray();
			JSONObject wfLine_level2 = null;
			ArrayList<String> differentOperations_level2 = new ArrayList<String>() {
				{
					add("LOAD");
					add("GROUP");
					add("STORE");
				}
			};
			for (int i = 0; i < differentOperations_level2.size(); i++) {
				wfLine_level2 = new JSONObject();
				wfLine_level2.accumulate("relation", "R_level2_".concat(String.valueOf(i)));
				if (differentOperations_level2.get(i).equals("LOAD")) {
					wfLine_level2.accumulate("input", "input_level2_" + i);
				} else if (differentOperations_level2.get(i).equals("STORE")) {
					wfLine_level2.accumulate("input", "output_level2_" + i);
				} else {
					wfLine_level2.accumulate("input", "R".concat(String.valueOf(i + 2)));
				}
				wfLine_level2.accumulate("input2", "R_level2_".concat(String.valueOf(i + 3)));
				wfLine_level2.accumulate("operation", differentOperations_level2.get(i));
				wfLine_level2.accumulate("options", "options_level2 part one");
				wfLine_level2.accumulate("options2", "options_level2 part two");
				wfLine_level2.accumulate("comment", "comment_level2".concat(String.valueOf(i)));
				wfDef_level2.put(wfLine_level2);
			}

			createDependingRecursiveWorkflowDefinition3();
			wfDef_level2.put(wfJSON_level3);
			wfJSON_level2.put("steps", wfDef_level2);

			JSONArray in_level2 = new JSONArray();
			in_level2.put("input1_level2");
			JSONArray out_level2 = new JSONArray();
			out_level2.put("output1_level2");
			wfJSON_level2.put("inputParameters", in_level2);
			wfJSON_level2.put("outputParameters", out_level2);
		} catch (JSONException e) {
			fail("error: setup of JSON Objects failed");
		}
	}

	private static void createDependingRecursiveWorkflowDefinition3() {
		try {
			wfJSON_level3 = new JSONObject();
			wfJSON_level3.accumulate("name", "testWf_PersistencyTests-rec-level3");
			wfJSON_level3.accumulate("description", "description of rec-level3 workflow");

			JSONArray wfDef_level3 = new JSONArray();
			JSONObject wfLine_level3 = null;
			ArrayList<String> differentOperations_level3 = new ArrayList<String>() {
				{
					add("ORDER");
					add("SELECT");
					add("LOAD");
					add("LOAD");
					add("LOAD");
					add("LOAD");
					add("JOIN");
					add("STORE");
					add("STORE");
				}
			};
			for (int i = 0; i < differentOperations_level3.size(); i++) {
				wfLine_level3 = new JSONObject();
				wfLine_level3.accumulate("relation", "R_level3_".concat(String.valueOf(i)));
				if (differentOperations_level3.get(i).equals("LOAD")) {
					wfLine_level3.accumulate("input", "input_level3_" + i);
				} else if (differentOperations_level3.get(i).equals("STORE")) {
					wfLine_level3.accumulate("input", "output_level3_" + i);
				} else {
					wfLine_level3.accumulate("input", "R".concat(String.valueOf(i + 2)));
				}
				wfLine_level3.accumulate("input2", "R_level3_".concat(String.valueOf(i + 3)));
				wfLine_level3.accumulate("operation", differentOperations_level3.get(i));
				wfLine_level3.accumulate("options", "options_level3 part one");
				wfLine_level3.accumulate("options2", "options_level3 part two");
				wfLine_level3.accumulate("comment", "comment_level3".concat(String.valueOf(i)));
				wfDef_level3.put(wfLine_level3);
			}
			wfJSON_level3.put("steps", wfDef_level3);

			JSONArray in_level3 = new JSONArray();
			in_level3.put("input1_level3");
			in_level3.put("input2_level3");
			in_level3.put("input3_level3");
			in_level3.put("input4_level3");
			JSONArray out_level3 = new JSONArray();
			out_level3.put("output1_level3");
			out_level3.put("output2_level3");
			wfJSON_level3.put("inputParameters", in_level3);
			wfJSON_level3.put("outputParameters", out_level3);
		} catch (JSONException e) {
			fail("error: setup of JSON Objects failed");
		}
	}

}