pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = "bs3"; // bootstrap3 theme
});

pigGeneApp.factory("WfPersistency", function($resource) {
	return {
		Load: $resource("/wf/:id", {id: "@id"}),
		Save: $resource("/save/wf/"),
		Delete: $resource("/del/:id", {id: "@id"}),
		Download: $resource("/dwnld/:id", {id: "@id"})
	};
});

pigGeneApp.factory("SharedWfService", ["$rootScope", "$location", "WfPersistency", function($rootScope, $location, WfPersistency) {
	var sharedWorkflow = {};
	
	sharedWorkflow.workflow = {};
	
	sharedWorkflow.initializeNewWorkflow = function() {
		var emptyWorkflow = {
				name: "newWf",
				description: "description of the new workflow",
				workflowType: "WORKFLOW",
				components: []
		};
		sharedWorkflow.workflow = emptyWorkflow;
		sharedWorkflow.broadcastWfChange();
		$location.path('/wf/' + "newWf").replace();
	};
	
	sharedWorkflow.loadWfDefinition = function(id) {
	};
	
	sharedWorkflow.loadReferencedWfDefinition = function(id) {
	};
	
	sharedWorkflow.persistWfDefinition = function() {
		var myWf = new WfPersistency.Save(this.workflow);
		myWf.$save(function(u,putResponseHeaders) {
			$location.path('/wf/' + sharedWorkflow.workflow.name).replace();
		});
	};
	
	sharedWorkflow.updateInOutParams = function() {
	};
	
	sharedWorkflow.checkRegexAndPushData = function(opt, data, referencedInputParameters) {
	};
	
	sharedWorkflow.deleteWfDefinition = function() {
	}
	
	sharedWorkflow.loadExistingWorkflowNames = function() {
	};
	
	sharedWorkflow.downloadScript = function() {
	};
	
	sharedWorkflow.prepForBroadcast = function(modWf) {
		this.workflow = modWf;
		this.broadcastWfChange();
	};
	
	sharedWorkflow.broadcastWfChange = function() {
		$rootScope.$broadcast("handleWfChange");
	};
	
	sharedWorkflow.broadcastRefWfChange = function() {
	};
	
	sharedWorkflow.broadcastExWfNamesChange = function() {
	}
	
	return sharedWorkflow;
}]);

function workflowRouteConfig($routeProvider) {
	$routeProvider
		.when('/wf/:id', {
			controller: WorkflowCtrl,
			templateUrl: 'workflowRepresentation.html'
		});
}
	
pigGeneApp.config(workflowRouteConfig);