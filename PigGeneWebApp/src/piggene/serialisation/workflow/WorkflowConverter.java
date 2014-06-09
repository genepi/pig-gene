package piggene.serialisation.workflow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class WorkflowConverter {
	private static Gson gson = new Gson();

	public static Workflow processClientJSONData(final JSONObject data) throws JSONException {
		String name = data.getString("name");
		String description = data.getString("description");
		List<Workflow> steps = convertJSONSteps(data.getJSONArray("steps"));
		List<String> inputParameters = convertWfSpecificParameters(data.getString("inputParameters"));
		List<String> outputParameters = convertWfSpecificParameters(data.getString("outputParameters"));
		Map<String, Map<String, String>> inputParameterMapping = convertParameterMapping(data.getString("inputParameterMapping"));
		Map<String, Map<String, String>> outputParameterMapping = convertParameterMapping(data.getString("outputParameterMapping"));
		return new Workflow(name, description, steps, inputParameters, outputParameters, inputParameterMapping, outputParameterMapping);
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
	 * @return <Workflow>
	 * @throws JSONException
	 */
	private static List<Workflow> convertJSONSteps(final JSONArray jsonArray) throws JSONException {
		List<Workflow> steps = new ArrayList<Workflow>();

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
				case "GROUP BY":
					steps.add(gson.fromJson(step.toString(), GroupByOperation.class));
					break;
				case "ORDER BY":
					steps.add(gson.fromJson(step.toString(), OrderByOperation.class));
					break;
				case "STORE":
					steps.add(gson.fromJson(step.toString(), StoreOperation.class));
					break;
				default:
					break;
				}
			} else { // "recursive" workflow definition
				steps.add(new WorkflowReference(step.getString("name")));
			}
		}
		return steps;
	}

	private static List<String> convertWfSpecificParameters(final String json) {
		Type stringList = new TypeToken<List<String>>() {
		}.getType();
		List<String> list = gson.fromJson(json, stringList);
		return list;
	}

	private static Map<String, Map<String, String>> convertParameterMapping(final String json) throws JsonSyntaxException, JSONException {
		Type stringStringMap = new TypeToken<Map<String, Map<String, String>>>() {
		}.getType();
		Map<String, Map<String, String>> map = gson.fromJson(json, stringStringMap);
		return map;
	}

}