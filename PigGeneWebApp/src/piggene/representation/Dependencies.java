package piggene.representation;

import java.util.ArrayList;
import java.util.List;

public class Dependencies {
	private List<String> dependency;

	public Dependencies() {
		this.dependency = new ArrayList<String>();
	}

	public List<String> getDependency() {
		return dependency;
	}

	public void setDependency(final List<String> dependency) {
		this.dependency = dependency;
	}

	public void addDependency(final String dependency) {
		this.dependency.add(dependency);
	}

	public boolean hasNoDependency() {
		return dependency.isEmpty();
	}

}