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
	sharedWorkflow.refWorkflow = {};
	sharedWorkflow.existingWorkflows = {};
	sharedWorkflow.openDef = true;
	
	sharedWorkflow.initializeNewWorkflow = function() {
		var emptyWorkflow = {
				name: "newWf",
				description: "description of the new workflow",
				workflowType: "WORKFLOW",
				steps: [],
				inputParameters: [],
				outputParameters: [],
				inputParameterMapping: {},
				outputParameterMapping: {}
		};
		sharedWorkflow.workflow = emptyWorkflow;
		sharedWorkflow.broadcastWfChange();
		$location.path('/wf/' + "newWf").replace();
	};
	
	sharedWorkflow.loadWfDefinition = function(id) {
		WfPersistency.Load.get({"id":id}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.workflow = response.data;
			sharedWorkflow.broadcastWfChange();
		});
	};
	
	sharedWorkflow.loadReferencedWfDefinition = function(id) {
		WfPersistency.Load.get({"id":id}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.refWorkflow = response.data;
			sharedWorkflow.broadcastRefWfChange();
		});
	};
	
	sharedWorkflow.persistWfDefinition = function() {
		this.updateInOutParams();
		var myWf = new WfPersistency.Save(this.workflow);
		myWf.$save(function(u,putResponseHeaders) {
			$location.path('/wf/' + sharedWorkflow.workflow.name).replace();
		});
	};
	
	sharedWorkflow.updateInOutParams = function() {
		this.workflow.inputParameters = [];
		this.workflow.outputParameters = [];
		var referencedInputParameters = [];
		this.workflow.steps.forEach(function(entry) {
			if(entry.workflowType === "WORKFLOW_SINGLE_ELEM") {
				if(entry.operation === "LOAD") {
					sharedWorkflow.checkRegexAndPushData("in",entry.input,referencedInputParameters);
					sharedWorkflow.checkRegexAndPushData("out",entry.relation);
				} else if(entry.operation === "FILTER") {
					sharedWorkflow.checkRegexAndPushData("in",entry.input,referencedInputParameters);
					sharedWorkflow.checkRegexAndPushData("out",entry.relation);
				} else if(entry.operation === "JOIN") {
					sharedWorkflow.checkRegexAndPushData("in",entry.input,referencedInputParameters);
					sharedWorkflow.checkRegexAndPushData("in",entry.input2,referencedInputParameters);
					sharedWorkflow.checkRegexAndPushData("out",entry.relation);
				} else if(entry.operation === "SELECT") {
					sharedWorkflow.checkRegexAndPushData("in",entry.input,referencedInputParameters);
					sharedWorkflow.checkRegexAndPushData("out",entry.relation);
				} else if(entry.operation === "STORE") {
					sharedWorkflow.checkRegexAndPushData("in",entry.input,referencedInputParameters);
					sharedWorkflow.checkRegexAndPushData("out",entry.relation);
				}
			} else {
				entry.outputParameters.forEach(function(param) {
					referencedInputParameters.push(param + "_" + entry.name);
				});
			}
		});
	};
	
	sharedWorkflow.checkRegexAndPushData = function(opt, data, referencedInputParameters) {
		var regex = /(.*)([$])([A-Za-z0-9_]*)(.*)/;
		var arr;
		if(regex.test(data)) {
			arr = regex.exec(data);
			if(opt === "in" && ($.inArray(arr[3],referencedInputParameters) === -1)) {
				sharedWorkflow.workflow.inputParameters.push(arr[3]);
			} else if (opt === "out") {
				sharedWorkflow.workflow.outputParameters.push(arr[3]);
			}
		}
	};
	
	sharedWorkflow.deleteWfDefinition = function() {
		WfPersistency.Delete.remove({"id":this.workflow.name}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			$location.path("").replace();
		});
	}
	
	sharedWorkflow.loadExistingWorkflowNames = function() {
		WfPersistency.Load.get({}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.existingWorkflows = response.data;
			sharedWorkflow.broadcastExWfNamesChange();
		});
	};
	
	sharedWorkflow.downloadScript = function() {
		WfPersistency.Download.get({"id":sharedWorkflow.workflow.name}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			
		    var element = angular.element("<a/>");
		    element.attr({
		    	href: "data:attachment/plain;charset=utf-8," + encodeURI(response.data),
		    	target: "_blank",
		        download: sharedWorkflow.workflow.name + ".pig"
		     });
		    
		    var elem = element[0];
		    if(document.dispatchEvent) {
		    	var oEvent = document.createEvent( "MouseEvents" );
		    	oEvent.initMouseEvent("click", true, true, window, 1, 1, 1, 1, 1, false, false, false, false, 0, elem);
		    	elem.dispatchEvent(oEvent);
		    } else if (elem.click) {
		    	return elem.click();
		    } else if(elem.onclick) { 
		    	var result = elem.onclick(); 
		    	if (!result) {
		    		return result; 
	    		}
		    }
		});
	};
	
	sharedWorkflow.prepForBroadcast = function(modWf) {
		this.workflow = modWf;
		this.broadcastWfChange();
	};
	
	sharedWorkflow.broadcastWfChange = function() {
		$rootScope.$broadcast("handleWfChange");
	};
	
	sharedWorkflow.broadcastRefWfChange = function() {
		$rootScope.$broadcast("handleRefWfChange");
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