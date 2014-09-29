package piggene.serialisation.workflow;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkflowConverter {
	public static Workflow processClientJSONData(final JSONObject data) throws JSONException {
		String name = data.getString("name");
		String description = data.getString("description");
		List<String> inputParameters = convertJSONParameters(data.getJSONArray("inputParams"));
		List<Workflow> components = convertJSONComponents(data.getJSONArray("components"));
		return new Workflow(name, description, components, inputParameters);
	}

	private static List<Workflow> convertJSONComponents(final JSONArray jsonArray) throws JSONException {
		List<Workflow> components = new ArrayList<Workflow>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject component = jsonArray.getJSONObject(i);
			if (component.has("content")) { // simple text-content
				components.add(new WorkflowComponent(component.getString("content")));
			} else { // referenced workflow element
				components.add(new WorkflowReference(component.getString("name")));
			}
		}
		return components;
	}

	private static List<String> convertJSONParameters(final JSONArray jsonArray) throws JSONException {
		List<String> parameters = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String param = (String) jsonArray.get(i);
			parameters.add(param);
		}
		return parameters;
	}

}