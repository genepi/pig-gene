package piggene.serialisation;

import java.util.ArrayList;

public class WorkflowComponent {
	private String name;
	private ArrayList<SingleWorkflowElement> wfComponent;

	public WorkflowComponent() {

	}

	public WorkflowComponent(final String name, final ArrayList<SingleWorkflowElement> wfComponent) {
		this.name = name;
		this.wfComponent = wfComponent;
	}

	public void setName(final String name) {
		this.name = name;
	}


	public void setWfComponent(final ArrayList<SingleWorkflowElement> wfComponent) {
		this.wfComponent = wfComponent;
	}

	public String getName() {
		return name;
	}

	public ArrayList<SingleWorkflowElement> getWfComponent() {
		return wfComponent;
	}
	
}