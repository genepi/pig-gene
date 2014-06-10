package piggene.serialisation.pig;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DynamicOutputParameterMapper {
	private static Map<String, Map<String, String>> paramMapping;

	public static void setParamMapping(final Map<String, Map<String, String>> paramMapping) {
		DynamicOutputParameterMapper.paramMapping = paramMapping;
	}

	public static void setParamMapping(final Map<String, Map<String, String>> paramMapping, final String wfName) {
		if (paramMapping != null) {
			DynamicOutputParameterMapper.paramMapping = buildNewMap(paramMapping, wfName);
		}
	}

	public static void addParamMapping(final Map<String, Map<String, String>> paramMapping) {
		if (paramMapping != null) {
			DynamicOutputParameterMapper.paramMapping.putAll(paramMapping);
		}
	}

	public static void addParamMapping(final Map<String, Map<String, String>> paramMapping, final String wfName) {
		if (paramMapping != null) {
			DynamicOutputParameterMapper.paramMapping.putAll(buildNewMap(paramMapping, wfName));
		}
	}

	public static String getMappedValue(final String wfName, final String paramName) {
		if (DynamicOutputParameterMapper.paramMapping.containsKey(wfName)) {
			Map<String, String> map = DynamicOutputParameterMapper.paramMapping.get(wfName);
			String parameterName = paramName;
			if (parameterName.startsWith("$")) {
				parameterName = parameterName.substring(1);
			}
			if (map != null && map.containsKey(parameterName)) {
				return map.get(parameterName);
			}
		}
		return null;
	}

	public static String getRepresentation() {
		return DynamicOutputParameterMapper.paramMapping.toString();
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