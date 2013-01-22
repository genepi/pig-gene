package piggene.serialisation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JSONConverter {

	public static ArrayList<WorkflowComponent> convertJsonArrayIntoWorkflow(final JSONArray array) throws JsonSyntaxException, JSONException {
		final ArrayList<WorkflowComponent> workflow = new ArrayList<WorkflowComponent>();
		final Gson gson = new Gson();
		WorkflowComponent comp;

		for (int i = 0; i < array.length(); i++) {
			comp = gson.fromJson(array.getString(i), WorkflowComponent.class);
			workflow.add(comp);
		}
		return workflow;
	}

}