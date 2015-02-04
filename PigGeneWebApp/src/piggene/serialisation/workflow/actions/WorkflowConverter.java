package piggene.serialisation.workflow.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import piggene.serialisation.workflow.Position;
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
		final List<Workflow> components = convertJSONComponents(name, data.getJSONArray("components"));

		final List<LinkParameter> inputParameter = convertJSONParameters("input", data.getJSONObject("parameter").getJSONArray("inputParameter"));
		final List<LinkParameter> outputParameter = convertJSONParameters("output", data.getJSONObject("parameter").getJSONArray("outputParameter"));
		final Map<String, Map<String, String>> inputParameterMapping = convertJSONMapping(data.getJSONObject("parameterMapping").getJSONObject(
				"inputParameterMapping"));
		final Map<String, Map<String, String>> outputParameterMapping = convertJSONMapping(data.getJSONObject("parameterMapping").getJSONObject(
				"outputParameterMapping"));

		final WorkflowParameter parameter = new WorkflowParameter(inputParameter, outputParameter);
		final WorkflowParameterMapping parameterMapping = new WorkflowParameterMapping(inputParameterMapping, outputParameterMapping);
		return new Workflow(name, description, components, parameter, parameterMapping);
	}

	private static List<Workflow> convertJSONComponents(final String name, final JSONArray jsonArray) throws JSONException {
		final List<Workflow> components = new ArrayList<Workflow>();
		for (int i = 0; i < jsonArray.length(); i++) {
			final JSONObject component = jsonArray.getJSONObject(i);
			if (component.has("content")) { // simple text-content
				final JSONObject scriptType = component.getJSONObject("scriptType");
				components.add(new WorkflowComponent(name, component.getString("content"), new ScriptType(scriptType.getInt("id"), scriptType
						.getString("name"))));
			} else { // referenced workflow element
				final JSONObject position;
				if (component.has("position")) {
					position = component.getJSONObject("position");
					components.add(new WorkflowReference(component.getString("name"), new Position(position.getInt("top"), position.getInt("left"))));

				} else {
					components.add(new WorkflowReference(component.getString("name"), new Position(0, 0)));
				}
			}
		}
		return components;
	}

	private static List<LinkParameter> convertJSONParameters(final String type, final JSONArray jsonArray) throws JSONException {
		final List<LinkParameter> parameters = new ArrayList<LinkParameter>();
		String name;
		String description;
		for (int i = 0; i < jsonArray.length(); i++) {
			name = jsonArray.getJSONObject(i).getString("name");
			description = jsonArray.getJSONObject(i).getString("description");
			if (type.equals("input")) {
				InputLinkParameter inputLinkParam;
				if (description != null) {
					inputLinkParam = new InputLinkParameter(name, description);
				} else {
					inputLinkParam = new InputLinkParameter(name);
				}
				parameters.add(inputLinkParam);
			} else if (type.equals("output")) {
				OutputLinkParameter outputLinkParam;
				if (description != null) {
					outputLinkParam = new OutputLinkParameter(name, description);
				} else {
					outputLinkParam = new OutputLinkParameter(name);
				}
				parameters.add(outputLinkParam);
			}
		}
		return parameters;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, String>> convertJSONMapping(final JSONObject jsonObject) throws JSONException {
		final Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		final Iterator<String> keys = jsonObject.keys();
		Iterator<String> innerKeys;
		while (keys.hasNext()) {
			final String key = keys.next();
			final Map<String, String> inputMap = new HashMap<String, String>();
			final JSONObject object = jsonObject.getJSONObject(key);
			innerKeys = object.keys();
			while (innerKeys.hasNext()) {
				final String innerKey = innerKeys.next();
				inputMap.put(innerKey, object.getString(innerKey));
			}
			map.put(key, inputMap);
		}
		return map;
	}

}