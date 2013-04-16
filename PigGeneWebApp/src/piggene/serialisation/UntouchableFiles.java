package piggene.serialisation;

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
	public static final List<String> list = Arrays.asList("filterExample", "joinExample");
}