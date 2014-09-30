package piggene.serialisation.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkflowConverter {
	public static Workflow processClientJSONData(final JSONObject data) throws JSONException {
		String name = data.getString("name");
		String description = data.getString("description");
		List<Workflow> components = convertJSONComponents(data.getJSONArray("components"));
		List<String> inputParams = convertJSONParameters(data.getJSONArray("inputParams"));
		Map<String, Map<String, String>> inputParamMapping = convertJSONMapping(data.getJSONObject("inputParamMapping"));
		List<String> outputParams = convertJSONParameters(data.getJSONArray("outputParams"));
		Map<String, Map<String, String>> outputParamMapping = convertJSONMapping(data.getJSONObject("outputParamMapping"));
		return new Workflow(name, description, components, inputParams, outputParams, inputParamMapping, outputParamMapping);
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

	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, String>> convertJSONMapping(final JSONObject jsonObject) throws JSONException {
		Map<String, Map<String, String>> inputParamMapping = new HashMap<String, Map<String, String>>();
		Iterator<String> keys = jsonObject.keys();
		Iterator<String> innerKeys;
		while (keys.hasNext()) {
			String key = keys.next();
			Map<String, String> map = new HashMap<String, String>();
			JSONObject object = jsonObject.getJSONObject(key);
			innerKeys = object.keys();
			while (innerKeys.hasNext()) {
				String innerKey = innerKeys.next();
				map.put(innerKey, object.getString(innerKey));
			}
			inputParamMapping.put(key, map);
		}
		return inputParamMapping;
	}

}