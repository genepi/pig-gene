pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = "bs3"; // bootstrap3 theme
});

pigGeneApp.factory("WfPersistency", function($resource) {
	return {
		Load: $resource("/wf/:id", {id: "@id"}),
		Save: $resource("/save/wf/")
	};
});

pigGeneApp.factory("SharedWfService", ["$rootScope", "WfPersistency", function($rootScope, WfPersistency) {
	var sharedWorkflow = {};
	
	sharedWorkflow.workflow = {};
	sharedWorkflow.existingWorkflows = {};
	
	sharedWorkflow.loadWfDefinition = function(id) {
		WfPersistency.Load.get({"id":id}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert("something baaaaaaaaaaaaaaaaaaad happend");
				console.log(response.message);
				return;
			}
			sharedWorkflow.workflow = response.data;
			sharedWorkflow.broadcastWfChange();
		});
	};
	
	sharedWorkflow.persistWfDefinition = function() {
		var myWf = new WfPersistency.Save(this.workflow);
		myWf.$save();
	};
	
	sharedWorkflow.removeStep = function(index) {
		this.workflow.workflow.splice(index, 1);
		this.broadcastWfChange();
	}
	
	sharedWorkflow.addStep = function(insertedStep) {
		this.workflow.workflow.push(insertedStep);
		this.broadcastWfChange();
	}
	
	sharedWorkflow.loadExistingWorkflowNames = function() {
		WfPersistency.Load.get({}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert("something baaaaaaaaaaaaaaaaaaad happend");
				console.log(response.message);
				return;
			}
			sharedWorkflow.existingWorkflows = response.data;
			sharedWorkflow.broadcastExWfNamesChange();
		});
	};
	
	sharedWorkflow.broadcastWfChange = function() {
		$rootScope.$broadcast("handleWfChange");
	};
	
	sharedWorkflow.broadcastExWfNamesChange = function() {
		$rootScope.$broadcast("handleExWfNamesChange");
	}
	
//	sharedWorkflow.broadcastExistingWfChange = function() {
//		$rootScope.$broadcast("handleExistingWfChange");
//	};
	
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