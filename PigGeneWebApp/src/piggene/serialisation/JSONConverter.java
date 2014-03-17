package piggene.serialisation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JSONConverter {

	public static ArrayList<SingleWorkflowElement> convertJSONWorkflow(final JSONArray jsonArray) throws JSONException {
		ArrayList<SingleWorkflowElement> workflowElements = new ArrayList<SingleWorkflowElement>();
		Gson gson = new Gson();

		for (int i = 0; i < jsonArray.length(); i++) {
			workflowElements.add(gson.fromJson(jsonArray.get(i).toString(), SingleWorkflowElement.class));
		}
		return workflowElements;
	}

	public static ArrayList<String> convertJSONWorkflowParams(final JSONArray jsonArray) throws JsonSyntaxException, JSONException {
		ArrayList<String> params = new ArrayList<String>();
		Gson gson = new Gson();

		for (int i = 0; i < jsonArray.length(); i++) {
			params.add(gson.fromJson(jsonArray.get(i).toString(), String.class));
		}
		return params;
	}

}