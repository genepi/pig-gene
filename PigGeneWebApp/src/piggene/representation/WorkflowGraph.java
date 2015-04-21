package piggene.representation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import piggene.serialisation.workflow.Workflow;
import piggene.serialisation.workflow.parameter.LinkParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameter;
import piggene.serialisation.workflow.parameter.WorkflowParameterMapping;

public class WorkflowGraph {

	public static List<Workflow> constructWorkflowGraph(final Workflow workflow) throws IOException {
		final Map<Workflow, Dependencies> unorderedDependencyMapping = new HashMap<Workflow, Dependencies>();
		for (final Entry<String, Map<String, String>> e : workflow.getParameterMapping().getInputParameterMapping().entrySet()) {
			final Workflow wf = matchAndSetWorkflow(e.getKey(), workflow.getComponents());
			final Dependencies dependencies = assignDependencies(e.getValue().values(), workflow.getParameter(), workflow.getParameterMapping(),
					workflow.getComponents());
			unorderedDependencyMapping.put(wf, dependencies);
		}
		final List<Workflow> dependencyBasedOrdering = orderDependencyMapping(unorderedDependencyMapping);
		return dependencyBasedOrdering;
	}

	private static Workflow matchAndSetWorkflow(final String uid, final List<Workflow> components) throws IOException {
		return matchWorkflowUID(uid, components);
	}

	private static Workflow matchWorkflowUID(final String uid, final List<Workflow> components) {
		Workflow matchingWf = null;
		for (final Workflow wf : components) {
			if (wf.getUid().equals(uid)) {
				matchingWf = wf;
				break;
			}
		}
		return matchingWf;
	}

	private static Dependencies assignDependencies(final Collection<String> connectorValues, final WorkflowParameter parameters,
			final WorkflowParameterMapping parameterMapping, final List<Workflow> components) {
		final Dependencies dependencies = new Dependencies();
		for (final String connector : connectorValues) {
			if (!connectorMatchesInputParameter(connector, parameters.getInputParameter())) {
				final String dependendWfUID = getCorrespondingWfUID(connector, parameterMapping.getOutputParameterMapping());
				dependencies.addDependency(matchWorkflowUID(dependendWfUID, components).getName());
			}
		}
		return dependencies;
	}

	private static boolean connectorMatchesInputParameter(final String connector, final List<LinkParameter> inputParameters) {
		for (final LinkParameter p : inputParameters) {
			if (p.getConnector().equals(connector)) {
				return true;
			}
		}
		return false;
	}

	private static String getCorrespondingWfUID(final String connector, final Map<String, Map<String, String>> outputParameterMapping) {
		for (final Entry<String, Map<String, String>> e : outputParameterMapping.entrySet()) {
			final String uid = e.getKey();
			for (final String value : e.getValue().values()) {
				if (value.equals(connector)) {
					return uid;
				}
			}
		}
		return null;
	}

	private static List<Workflow> orderDependencyMapping(final Map<Workflow, Dependencies> unorderedDependencyMapping) {
		final List<Workflow> dependencyBasedOrdering = new ArrayList<Workflow>();
		final Set<Workflow> temporaryKeySet = unorderedDependencyMapping.keySet();

		// first step: insert all workflow without dependencies
		// to other workflows or components
		for (final Workflow wf : temporaryKeySet) {
			if (unorderedDependencyMapping.get(wf).hasNoDependency()) {
				dependencyBasedOrdering.add(wf);
			}
		}
		for (final Workflow wf : dependencyBasedOrdering) {
			temporaryKeySet.remove(wf);
		}

		// second step: iteratively add workflows whose dependencies
		// are existing in the list
		while (!temporaryKeySet.isEmpty()) {
			final List<Workflow> temporaryAddedWfList = new ArrayList<Workflow>();
			for (final Workflow wf : temporaryKeySet) {
				if (allDependendWfExist(dependencyBasedOrdering, unorderedDependencyMapping.get(wf))) {
					dependencyBasedOrdering.add(wf);
					temporaryAddedWfList.add(wf);
				}
			}
			for (final Workflow wf : temporaryAddedWfList) {
				temporaryKeySet.remove(wf);
			}
		}
		return dependencyBasedOrdering;
	}

	private static boolean allDependendWfExist(final List<Workflow> existingDependencies, final Dependencies dependencies) {
		for (final String dependency : dependencies.getDependency()) {
			if (!dependencyMatchesWfName(existingDependencies, dependency)) {
				return false;
			}
		}
		return true;
	}

	private static boolean dependencyMatchesWfName(final List<Workflow> existingDependencies, final String dependency) {
		for (final Workflow wf : existingDependencies) {
			if (wf.getName().equals(dependency)) {
				return true;
			}
		}
		return false;
	}

}