package piggene.serialisation.pig;

import java.util.Map;

public class DynamicInputParameterMapper {
	private static Map<String, Map<String, String>> parmMapping;

	public static void setParamMapping(final Map<String, Map<String, String>> parmMapping) {
		DynamicInputParameterMapper.parmMapping = parmMapping;
	}

	public static void addParamMapping(final Map<String, Map<String, String>> parmMapping) {
		DynamicInputParameterMapper.parmMapping.putAll(parmMapping);
	}

	public static String getMappedValue(final String wfName, final String paramName) {
		if (DynamicInputParameterMapper.parmMapping.containsKey(wfName)) {
			Map<String, String> map = DynamicInputParameterMapper.parmMapping.get(wfName);
			if (map.containsKey(paramName)) {
				return map.get(paramName);
			}
		}
		return null;
	}

}