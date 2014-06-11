package piggene.serialisation.pig;

import java.util.ArrayList;

public class ExistingOutputParameters {
	private static ArrayList<String> outputParameter;

	public static void initializeArrayList() {
		outputParameter = new ArrayList<String>();
	}

	public static void addOutputParameter(final String param) {
		if (!ExistingOutputParameters.outputParameter.contains(param)) {
			ExistingOutputParameters.outputParameter.add(param);
		}
	}

	public static boolean containsOutputParameter(final String param) {
		return ExistingOutputParameters.outputParameter.contains(param);
	}

	public static String getRepresentation() {
		return ExistingOutputParameters.outputParameter.toString();
	}

}