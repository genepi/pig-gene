package piggene.serialisation.workflow.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import piggene.serialisation.workflow.ScriptType;
import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowComponent;
import piggene.serialisation.workflow.WorkflowReference;
import piggene.serialisation.workflow.parameter.InputLinkParameter;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.OutputLinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

public class WorkflowConverter {

	public static Workflow processClientJSONData(final JSONObject data) throws JSONException {
		final String name = data.getString("name");
		final String description = data.getString("description");
		final List<Workflow> components = convertJSONComponents(data.getJSONArray("components"));

		final List<LinkParameter> inputParameter = convertJSONParameters("input", data.getJSONObject("parameter").getJSONArray("inputParameter"));
		final List<LinkParameter> outputParameter = convertJSONParameters("output", data.getJSONObject("parameter").getJSONArray("outputParameter"));
		final Map<String, Map<String, String>> inputParameterMapping = convertJSONMapping("input", data.getJSONObject("parameterMapping")
				.getJSONObject("inputParameterMapping"));
		final Map<String, Map<String, String>> outputParameterMapping = convertJSONMapping("output", data.getJSONObject("parameterMapping")
				.getJSONObject("outputParameterMapping"));

		final WorkflowParameter parameter = new WorkflowParameter(inputParameter, outputParameter);
		final WorkflowParameterMapping parameterMapping = new WorkflowParameterMapping(inputParameterMapping, outputParameterMapping);
		return new Workflow(name, description, components, parameter, parameterMapping);
	}

	private static List<Workflow> convertJSONComponents(final JSONArray jsonArray) throws JSONException {
		final List<Workflow> components = new ArrayList<Workflow>();
		for (int i = 0; i < jsonArray.length(); i++) {
			final JSONObject component = jsonArray.getJSONObject(i);
			if (component.has("content")) { // simple text-content
				final JSONObject scriptType = component.getJSONObject("scriptType");
				components.add(new WorkflowComponent(component.getString("content"), new ScriptType(scriptType.getInt("id"), scriptType
						.getString("name"))));
			} else { // referenced workflow element
				components.add(new WorkflowReference(component.getString("name")));
			}
		}
		return components;
	}

	private static List<LinkParameter> convertJSONParameters(final String type, final JSONArray jsonArray) throws JSONException {
		final List<LinkParameter> parameters = new ArrayList<LinkParameter>();
		String name;
		for (int i = 0; i < jsonArray.length(); i++) {
			name = jsonArray.getJSONObject(i).getString("name");
			if (type.equals("input")) {
				parameters.add(new InputLinkParameter(name));
			} else if (type.equals("output")) {
				parameters.add(new OutputLinkParameter(name));
			}
		}
		return parameters;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, String>> convertJSONMapping(final String type, final JSONObject jsonObject) throws JSONException {
		final Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		final Iterator<String> keys = jsonObject.keys();
		Iterator<String> innerKeys;
		while (keys.hasNext()) {
			final String key = keys.next();
			final Map<String, String> innerMap = new HashMap<String, String>();
			final JSONObject object = jsonObject.getJSONObject(key);
			innerKeys = object.keys();
			while (innerKeys.hasNext()) {
				final String innerKey = innerKeys.next();
				if (type.equals("input")) {
					innerMap.put(innerKey, object.getString(innerKey));
				} else if (type.equals("output")) {
					System.out.println(object);
					System.out.println(object.getString(innerKey));

					// TODO type...
					innerMap.put(innerKey, object.getString(innerKey));
				}
			}
			map.put(key, innerMap);
		}
		return map;
	}

}