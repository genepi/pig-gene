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

	/**
	 * The if-branch handles standard operations and adds the corresponding
	 * implementing classes to the steps collection. The else-branch handles the
	 * case that a referenced workflow is contained within an other workflow. To
	 * ensure up-to-date information in case the referenced workflow is
	 * modified, only the reference to that workflow is stored within the
	 * surrounding workflow. The "resolving" of that referenced information is
	 * done not before the script and the yaml-files are persisted.
	 * 
	 * @param jsonArray
	 * @return ArrayList<Workflow>
	 * @throws JSONException
	 */
	private static ArrayList<Workflow> convertJSONSteps(final JSONArray jsonArray) throws JSONException {
		ArrayList<Workflow> steps = new ArrayList<Workflow>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject step = jsonArray.getJSONObject(i);

			if (step.has("operation")) { //
				switch (step.getString("operation")) {
				case "REGISTER":
					steps.add(gson.fromJson(step.toString(), RegisterOperation.class));
					break;
				case "LOAD":
					steps.add(gson.fromJson(step.toString(), LoadOperation.class));
					break;
				case "FILTER":
					steps.add(gson.fromJson(step.toString(), FilterOperation.class));
					break;
				case "JOIN":
					steps.add(gson.fromJson(step.toString(), JoinOperation.class));
					break;
				case "SELECT":
					steps.add(gson.fromJson(step.toString(), SelectOperation.class));
					break;
				case "GROUP":
					steps.add(gson.fromJson(step.toString(), GroupByOperation.class));
					break;
				case "ORDER":
					steps.add(gson.fromJson(step.toString(), OrderByOperation.class));
					break;
				case "STORE":
					steps.add(gson.fromJson(step.toString(), StoreOperation.class));
					break;
				default:
					break;
				}
			} else { // "recursive" workflow definition
				steps.add(new ReferencedWorkflow(step.getString("name")));
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