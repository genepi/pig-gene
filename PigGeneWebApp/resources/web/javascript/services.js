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
			var loadedWf = sharedWorkflow.checkAndModifyInputParameters(response.data);
			loadedWf = sharedWorkflow.checkAndModifyOutputParameters(loadedWf);
			sharedWorkflow.workflow = loadedWf;
			sharedWorkflow.broadcastWfChange();
		});
	};
	
	//TODO remove: just temporarily needed
	sharedWorkflow.checkAndModifyInputParameters = function(wf) {
		var noInputFields = 0;
		var noInputParameters = wf.inputParameters.length;
		for(var i=0; i<wf.workflow.length; i++) {
			if(wf.workflow[i].operation == "LOAD") {
				noInputFields++;
			}
		}
		if(noInputParameters <= noInputFields) {
			for(var i=noInputParameters; i<noInputFields; i++) {
				wf.inputParameters.push("");
			}
		} 
		return wf;
	}
	
	//TODO remove: just temporarily needed
	sharedWorkflow.checkAndModifyOutputParameters = function(wf) {
		var noOutputFields = 0;
		var noOutputParameters = wf.outputParameters.length;
		for(var i=0; i<wf.workflow.length; i++) {
			if(wf.workflow[i].operation == "STORE") {
				noOutputFields++;
			}
		}
		if(noOutputParameters <= noOutputFields) {
			for(var i=noOutputParameters; i<noOutputFields; i++) {
				wf.outputParameters.push("");
			}
		} 
		return wf;
	}
	
	sharedWorkflow.persistWfDefinition = function() {
		var myWf = new WfPersistency.Save(this.workflow);
		myWf.$save();
	};
	
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
	
	sharedWorkflow.prepForBroadcast = function(modWf) {
		this.workflow = modWf;
		this.broadcastWfChange();
	}
	
	sharedWorkflow.broadcastWfChange = function() {
		$rootScope.$broadcast("handleWfChange");
	};
	
	sharedWorkflow.broadcastExWfNamesChange = function() {
		$rootScope.$broadcast("handleExWfNamesChange");
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