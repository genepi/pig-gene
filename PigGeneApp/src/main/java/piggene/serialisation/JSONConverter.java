package piggene.serialisation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * JSONConverter class is used to convert a JsonArray into a workflow.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class JSONConverter {

	public static ArrayList<SingleWorkflowElement> convertJsonArrayIntoWorkflow(final JSONArray array) throws JsonSyntaxException, JSONException {
		final ArrayList<SingleWorkflowElement> workflow = new ArrayList<SingleWorkflowElement>();
		final Gson gson = new Gson();
		SingleWorkflowElement comp;

		// length-2: last two element elements are description and file name
		for (int i = 0; i < array.length() - 2; i++) {
			JSONObject currentArrayElement = array.getJSONObject(i);

			if ((Boolean) currentArrayElement.get("ref")) {
				String referenceWfName = (String) currentArrayElement.get("name");
				comp = new SingleWorkflowElement();
				comp.setReferenceName(referenceWfName);
				workflow.add(comp);

				// TODO add info to original (referenced) file that it is beeing
				// referenced!!! (needed to prevent deletion)

			} else {
				JSONArray workflowComponent = array.getJSONObject(i).getJSONArray("data");
				for (int j = 0; j < workflowComponent.length(); j++) {
					comp = gson.fromJson(workflowComponent.getString(j), SingleWorkflowElement.class);
					workflow.add(comp);
				}
			}

		}
		return workflow;
	}
}