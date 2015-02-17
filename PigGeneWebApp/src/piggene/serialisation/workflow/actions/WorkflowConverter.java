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
				final Position position = getPosition(component);
				components.add(new WorkflowReference(component.getString("name"), position));
			}
		}
		return components;
	}

	private static List<LinkParameter> convertJSONParameters(final String type, final JSONArray jsonArray) throws JSONException {
		final List<LinkParameter> parameters = new ArrayList<LinkParameter>();
		String uid;
		String connector;
		String description;
		Position position = null;

		for (int i = 0; i < jsonArray.length(); i++) {
			final JSONObject comp = jsonArray.getJSONObject(i);
			uid = comp.getString("uid");
			connector = comp.getString("connector");
			description = comp.getString("description");
			position = getPosition(comp);
			if (type.equals("input")) {
				parameters.add(new InputLinkParameter(uid, connector, description, position));
			} else if (type.equals("output")) {
				parameters.add(new OutputLinkParameter(uid, connector, description, position));
			}
		}
		return parameters;
	}

	private static Position getPosition(final JSONObject comp) throws JSONException {
		Position position = null;
		if (comp.has("position") && !comp.isNull("position")) {
			final JSONObject posObj = comp.getJSONObject("position");
			position = new Position(posObj.getInt("top"), posObj.getInt("left"));
		}
		return position;
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