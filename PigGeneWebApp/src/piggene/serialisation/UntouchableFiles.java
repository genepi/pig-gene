package piggene.serialisation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class holds a list of all the workflow file names that must not be overridden
 * or deleted because they represent the example workflows.
 * 
 * @author Clemens Banas
 * @date April 2013
 */
public class UntouchableFiles {
	public static final List<String> list = Arrays
			.asList("sample_countVariants", "sample_distance", "sample_filterGT", "sample_leftJoin", "sample_normalJoin", "sample_rangeQuery");

	public static final List<String> referencedWorkflows = new ArrayList<String>();
}