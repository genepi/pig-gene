package piggene.serialisation.pig;

import java.util.Map;

public class DynamicOutputParameterMapper {
	private static Map<String, Map<String, String>> parmMapping;

	public static void setParamMapping(final Map<String, Map<String, String>> parmMapping) {
		DynamicOutputParameterMapper.parmMapping = parmMapping;
	}

	public static void addParamMapping(final Map<String, Map<String, String>> parmMapping) {
		DynamicOutputParameterMapper.parmMapping.putAll(parmMapping);
	}

	public static String getMappedValue(final String wfName, final String paramName) {
		if (DynamicOutputParameterMapper.parmMapping.containsKey(wfName)) {
			Map<String, String> map = DynamicOutputParameterMapper.parmMapping.get(wfName);
			if (map.containsKey(paramName)) {
				return map.get(paramName);
			}
		}
		return null;
	}

}