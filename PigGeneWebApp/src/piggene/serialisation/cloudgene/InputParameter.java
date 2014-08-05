package piggene.serialisation.cloudgene;

/**
 * InputParameter class is used to hold the yaml-file information.
 */
public class InputParameter extends Parameter {

	@Override
	public boolean isInput() {
		return true;
	}

}