package piggene.serialisation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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
			comp = gson.fromJson(array.getString(i), SingleWorkflowElement.class);
			workflow.add(comp);
		}
		return workflow;
	}

}