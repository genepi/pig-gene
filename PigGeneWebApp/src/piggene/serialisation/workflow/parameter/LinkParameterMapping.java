package piggene.serialisation.workflow.parameter;

import java.util.Map;

import com.google.gson.JsonObject;

public class LinkParameterMapping {
	private Map<String, Map<LinkParameter, LinkParameter>> map;

	public LinkParameterMapping() {
	}

	public LinkParameterMapping(final Map<String, Map<LinkParameter, LinkParameter>> map) {
		this.map = map;
	}

	public Map<LinkParameter, LinkParameter> getMapByKey(final String key) {
		return this.map.get(key);
	}

	public void setMapByKey(final String key, final Map<LinkParameter, LinkParameter> value) {
		this.map.put(key, value);
	}

	public void setMapByKey(final String key, final JsonObject value) {
		// TODO implement
	}

	public Map<String, Map<LinkParameter, LinkParameter>> getMap() {
		return map;
	}

	public void setMap(final Map<String, Map<LinkParameter, LinkParameter>> map) {
		this.map = map;
	}

}