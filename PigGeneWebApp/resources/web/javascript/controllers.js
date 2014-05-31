pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.buttons = buttons;
	
	$scope.performNavBarAction = function(index) {
		$scope.changeActiveNavBarIcon(index);
		
		//TODO button handling depending on current button...
		switch(buttons[index].name) {
			case "newWfBtn":
						SharedWfService.initializeNewWorkflow();
						break;
			case "openWfBtn":
						SharedWfService.openDef = true;
						SharedWfService.loadExistingWorkflowNames();
						break;
			case "saveWfBtn": 
						SharedWfService.persistWfDefinition();
						break;
			case "deleteWfBtn": 	
						SharedWfService.deleteWfDefinition();
						break;
			case "downloadScriptBtn": 
						SharedWfService.downloadScript();
						break;
			case "runJobBtn": 			
						break;
			default: break;
		}
	}
	
	$scope.changeActiveNavBarIcon = function(index) {
		for(var i=0; i<buttons.length; i++) {
			buttons[i].active = "";
		}
		buttons[index].active = "active";
	};
}]);

function WorkflowCtrl($scope, $routeParams, $location, $filter, SharedWfService) {
	//TODO des muss i no umbauen...
	$scope.workflow = SharedWfService.workflow;
	if($routeParams.id != "newWf") {
		SharedWfService.loadWfDefinition($routeParams.id);
	}
	
	$scope.operations = operations;
	
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
	});
	
//	$scope.changeInputParameter = function(wfName, paramName) {
//		var modWf = $scope.workflow;
//		modWf.inputParameterMapping[wfName] = {};
//		modWf.inputParameterMapping[wfName][paramName] = this.inParamVal;
//		SharedWfService.prepForBroadcast(modWf);
//	};
	
//	$scope.changeOutputParameter = function(newOutputParameter, index) {
//		var modWf = $scope.workflow;
//		modWf.outputParameters[index] = newOutputParameter;
//		SharedWfService.prepForBroadcast(modWf);
//	};
	
	//TODO change implementation of changeStatus!!!
	$scope.changeStatus = function(step, index) {
		var selected = [];
		if (step.operation.name) {
			selected = $filter('filter')($scope.operations, {name: step.operation.name});
			$scope.changeWfOperation(selected[0].name, index);
		} else if(step.operation) {
			selected = $filter('filter')($scope.operations, {name: step.operation});
		}
		return selected.length ? selected[0].name : "Not set";
	};
	
	$scope.removeStep = function(index) {
		var modWf = $scope.workflow;
		if(modWf.steps[index].workflowType === "WORKFLOW_REFERENCE") {
			var wfName = modWf.steps[index].name
			delete modWf.inputParameterMapping[wfName];
		}
		modWf.steps.splice(index, 1);
		SharedWfService.prepForBroadcast(modWf);
	}
	
	$scope.moveUp = function(index) {
		if(index != 0) {
			var modWf = $scope.workflow;
			var prevLine = modWf.steps[index-1];
			modWf.steps[index-1] = modWf.steps[index];
			modWf.steps[index] = prevLine;
			SharedWfService.prepForBroadcast(modWf);
		}
	};
	
	$scope.moveDown = function(index) {
		if(index != $scope.workflow.steps.length) {
			var modWf = $scope.workflow;
			var nextLine = modWf.steps[index+1];
			modWf.steps[index+1] = modWf.steps[index];
			modWf.steps[index] = nextLine;
			SharedWfService.prepForBroadcast(modWf);
		}
	};

//	$scope.addInputParameter = function() {
//		//TODO implement
//		var modWf = $scope.workflow;
//		modWf.inputParameters.push("");
//		SharedWfService.prepForBroadcast(modWf);
//	};
//	
//	$scope.addOutputParameter = function() {
//		var modWf = $scope.workflow;
//		modWf.outputParameters.push("");
//		SharedWfService.prepForBroadcast(modWf);
//	};
	
	$scope.addStep = function() {
		$scope.inserted = {
				relation: 'R' + ($scope.workflow.steps.length+1),
				input: "",
				operation: $scope.operations[2].name,
				options: "",
				options2: "",
				comment: "",
				workflowType: "WORKFLOW_SINGLE_ELEM",
				active: false
		};
		var modWf = $scope.workflow;
		modWf.steps.push($scope.inserted);
		SharedWfService.prepForBroadcast(modWf);
	}
	
	$scope.addWorkflow = function() {
		SharedWfService.openDef = false;
		SharedWfService.loadExistingWorkflowNames();
	};
	
	$scope.changeWfOperation = function(operation, index) {
		var modWf = $scope.workflow;
		modWf.steps[index].operation = operation;
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.checkType = function(type, checkVal) {
		return (type == checkVal);
	};

	$scope.editReferencedWf = function(id) {
		$location.path("/wf/" + id);
	}
};

pigGeneApp.controller("ModalCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	$scope.radioSelection = "";
	$scope.openDef = true;
	
	$scope.$on("handleExWfNamesChange", function() {
		$scope.existingWorkflows = SharedWfService.existingWorkflows;
		$scope.openDef = SharedWfService.openDef;
		$('#myModal').modal('toggle');
	});
	
	$scope.openSelectedWorkflow = function() {
		var selection = $scope.radioSelection;
		if(!(selection == null || selection == "")) {
			SharedWfService.loadWfDefinition(selection);
			$location.path("/wf/" + $scope.radioSelection);
			$('#myModal').modal('toggle');
		}
	};
	
	$scope.addSelectedWorkflow = function() {
		var selection = $scope.radioSelection;
		if(!(selection == null || selection == "")) {
			//AJAX call to get important wf data
			SharedWfService.loadReferencedWfDefinition(selection);
		}
	};
	
	$scope.$on("handleRefWfChange", function() {
		var modWf = SharedWfService.workflow;
		var refWf = SharedWfService.refWorkflow;
		
		var inserted = {
				name: refWf.name,
				description: refWf.description,
				inputParameters: refWf.inputParameters,
				outputParameters: refWf.outputParameters,
				workflowType: "WORKFLOW_REFERENCE",
				steps: refWf.steps
		}
		if(modWf.steps.length == 0) {
			modWf.steps[0] = inserted;
		} else {
			modWf.steps.push(inserted);
		}
		
		modWf.inputParameterMapping[refWf.name] = {};
		inserted.inputParameters.forEach(function(entry) {
			modWf.inputParameterMapping[refWf.name][entry] = "";
		});
		
		SharedWfService.prepForBroadcast(modWf);
		$('#myModal').modal('toggle');
	})
	
}]);