package piggene.serialisation.workflow.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.WorkflowComponent;
import piggene.serialisation.workflow.WorkflowReference;
import piggene.serialisation.workflow.parameter.InputLinkParameter;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.LinkParameterMapping;
import piggene.serialisation.workflow.parameter.OutputLinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameter;

public class WorkflowConverter {
	public static Workflow processClientJSONData(final JSONObject data) throws JSONException {
		String name = data.getString("name");
		String description = data.getString("description");
		List<Workflow> components = convertJSONComponents(data.getJSONArray("components"));
		List<LinkParameter> inputParameter = convertJSONParameters("input", data.getJSONObject("parameter").getJSONArray("inputParameter"));
		List<LinkParameter> outputParameter = convertJSONParameters("output", data.getJSONObject("parameter").getJSONArray("outputParameter"));
		WorkflowParameter parameter = new WorkflowParameter(inputParameter, outputParameter);

		LinkParameterMapping inputParameterMapping = convertJSONMapping("input",
				data.getJSONObject("parameterMapping").getJSONObject("inputParameterMapping"));
		LinkParameterMapping outputParameterMapping = convertJSONMapping("output",
				data.getJSONObject("parameterMapping").getJSONObject("outputParameterMapping"));
		return new Workflow(name, description, components, parameter, inputParameterMapping, outputParameterMapping);
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

	private static List<LinkParameter> convertJSONParameters(final String type, final JSONArray jsonArray) throws JSONException {
		List<LinkParameter> parameters = new ArrayList<LinkParameter>();
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
	private static LinkParameterMapping convertJSONMapping(final String type, final JSONObject jsonObject) throws JSONException {
		Map<String, Map<LinkParameter, LinkParameter>> map = new HashMap<String, Map<LinkParameter, LinkParameter>>();

		Iterator<String> keys = jsonObject.keys();
		Iterator<String> innerKeys;
		while (keys.hasNext()) {
			String key = keys.next();
			Map<LinkParameter, LinkParameter> innerMap = new HashMap<LinkParameter, LinkParameter>();
			JSONObject object = jsonObject.getJSONObject(key);
			innerKeys = object.keys();
			while (innerKeys.hasNext()) {
				String innerKey = innerKeys.next();
				if (type.equals("input")) {
					innerMap.put(new InputLinkParameter(innerKey), new InputLinkParameter(object.getString(innerKey)));
				} else if (type.equals("output")) {
					System.out.println(object);
					System.out.println(object.getString(innerKey));

					// TODO type...
					innerMap.put(new OutputLinkParameter(innerKey), new OutputLinkParameter(object.getString(innerKey)));
				}
			}
			map.put(key, innerMap);
		}
		return new LinkParameterMapping(map);
	}

}