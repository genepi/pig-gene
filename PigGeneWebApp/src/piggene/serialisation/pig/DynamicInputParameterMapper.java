package piggene.serialisation.pig;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DynamicInputParameterMapper {
	private static Map<String, Map<String, String>> paramMapping;

	public static void setParamMapping(final Map<String, Map<String, String>> paramMapping) {
		DynamicInputParameterMapper.paramMapping = paramMapping;
	}

	public static void setParamMapping(final Map<String, Map<String, String>> paramMapping, final String wfName) {
		if (paramMapping != null) {
			DynamicInputParameterMapper.paramMapping = buildNewMap(paramMapping, wfName);
		}
	}

	public static void addParamMapping(final Map<String, Map<String, String>> paramMapping) {
		if (paramMapping != null) {
			DynamicInputParameterMapper.paramMapping.putAll(paramMapping);
		}
	}

	public static void addParamMapping(final Map<String, Map<String, String>> paramMapping, final String wfName) {
		if (paramMapping != null) {
			DynamicInputParameterMapper.paramMapping.putAll(buildNewMap(paramMapping, wfName));
		}
	}

	public static String getMappedValue(final String wfName, final String paramName) {
		if (DynamicInputParameterMapper.paramMapping.containsKey(wfName)) {
			Map<String, String> map = DynamicInputParameterMapper.paramMapping.get(wfName);
			if (map != null && map.containsKey(paramName)) {
				return map.get(paramName);
			}
		}
		return null;
	}

	public static String getRepresentation() {
		return DynamicInputParameterMapper.paramMapping.toString();
	}

	private static Map<String, Map<String, String>> buildNewMap(final Map<String, Map<String, String>> paramMapping, final String wfName) {
		final Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		Map<String, String> map = null;

		for (Entry<String, Map<String, String>> entry : paramMapping.entrySet()) {
			map = entry.getValue();
			for (String key : map.keySet()) {
				map.put(key, map.get(key).concat("_").concat(wfName));
			}
			result.put(entry.getKey(), map);
		}
		return result;
	}
}