package piggene.serialisation.workflow;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class WorkflowConverter {
	private static Gson gson = new Gson();

	public static Workflow processClientJSONData(final JSONObject data) throws JSONException {
		String name = data.getString("name");
		String description = data.getString("description");
		ArrayList<Workflow> steps = convertJSONSteps(data.getJSONArray("steps"));
		ArrayList<String> inputParameters = convertJSONParams(data.getJSONArray("inputParameters"));
		ArrayList<String> outputParameters = convertJSONParams(data.getJSONArray("outputParameters"));
		return new Workflow(name, description, steps, inputParameters, outputParameters);
	}

	private static ArrayList<Workflow> convertJSONSteps(final JSONArray jsonArray) throws JSONException {
		ArrayList<Workflow> steps = new ArrayList<Workflow>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject step = jsonArray.getJSONObject(i);

			if (step.has("operation")) { //
				switch (step.getString("operation")) {
				case "REGISTER":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), RegisterOperation.class));
					break;
				case "LOAD":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), LoadOperation.class));
					break;
				case "FILTER":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), FilterOperation.class));
					break;
				case "JOIN":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), JoinOperation.class));
					break;
				case "SELECT":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), SelectOperation.class));
					break;
				case "GROUP":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), GroupByOperation.class));
					break;
				case "ORDER":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), OrderByOperation.class));
					break;
				case "STORE":
					steps.add(gson.fromJson(jsonArray.get(i).toString(), StoreOperation.class));
					break;
				default:
					break;
				}
			} else { // recursive workflow definition
				steps.add(processClientJSONData((JSONObject) jsonArray.get(i)));
			}
		}
		return steps;
	}

	private static ArrayList<String> convertJSONParams(final JSONArray jsonArray) throws JsonSyntaxException,
			JSONException {
		ArrayList<String> params = new ArrayList<String>();

		for (int i = 0; i < jsonArray.length(); i++) {
			params.add(gson.fromJson(jsonArray.get(i).toString(), String.class));
		}
		return params;
	}

}