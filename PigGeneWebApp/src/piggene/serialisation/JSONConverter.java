package piggene.serialisation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONConverter {

	/*
	 * if the columns array gets modified - the private "setWorkflowAttribute"
	 * method must be adapted too.
	 */
	private static final String[] columns = new String[] { "relation", "operation", "relation2", "options" };

	public static ArrayList<WorkflowComponent> convertJsonArrayIntoWorkflow(final JSONArray array) throws JSONException {
		ArrayList<WorkflowComponent> workflow = new ArrayList<WorkflowComponent>();
		int arrayLength = array.length();
		String attValue;

		for (int i = 0; i < arrayLength; i++) {
			WorkflowComponent workComponent = new WorkflowComponent();
			for (String colName : columns) {
				attValue = getColumnAttribute(array.getJSONObject(i), colName);
				setWorkflowAttribute(workComponent, colName, attValue);
			}
			workflow.add(workComponent);
		}
		return workflow;
	}

	private static String getColumnAttribute(final JSONObject obj, final String colName) throws JSONException {
		return (obj.get(colName)).toString();
	}

	private static void setWorkflowAttribute(final WorkflowComponent workComponent, final String colName, final String attValue) {
		if (colName.equals("relation")) {
			workComponent.setRelation(attValue);
		} else if (colName.equals("operation")) {
			workComponent.setOperation(attValue);
		} else if (colName.equals("relation2")) {
			workComponent.setRelation2(attValue);
		} else if (colName.equals("options")) {
			workComponent.setOptions(attValue);
		}
	}

}