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
						SharedWfService.loadExistingWorkflowNames();
						break;
			case "saveWfBtn": 
						SharedWfService.persistWfDefinition();
						break;
			case "deleteWfBtn": 		
						break;
			case "downloadScriptBtn": 	
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

function WorkflowCtrl($scope, $routeParams, $filter, SharedWfService) {
	//TODO des muss i no umbauen...
	$scope.workflow = SharedWfService.workflow;
	if($routeParams.id != "newWf") {
		SharedWfService.loadWfDefinition($routeParams.id);
	}
	
	$scope.operations = operations;
	
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
	});
	
	$scope.changeInputParameter = function(newInputParameter, index) {
		var modWf = $scope.workflow;
		modWf.inputParameters[index] = newInputParameter;
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.changeOutputParameter = function(newOutputParameter, index) {
		var modWf = $scope.workflow;
		modWf.outputParameters[index] = newOutputParameter;
		SharedWfService.prepForBroadcast(modWf);
	};
	
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
		modWf.steps.splice(index, 1);
		SharedWfService.prepForBroadcast(modWf);
	}
	
	$scope.addInputParameter = function() {
		//TODO implement
		var modWf = $scope.workflow;
		modWf.inputParameters.push("");
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.addOutputParameter = function() {
		var modWf = $scope.workflow;
		modWf.outputParameters.push("");
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.addStep = function() {
		$scope.inserted = {
				relation: 'R' + ($scope.workflow.steps.length+1),
				input: "",
				operation: $scope.operations[2].name,
				options: "",
				options2: "",
				comment: "",
				active: false
		};
		var modWf = $scope.workflow;
		modWf.steps.push($scope.inserted);
		SharedWfService.prepForBroadcast(modWf);
	}
	
	$scope.addWorkflow = function() {
		//TODO implement this functionality
	};
	
	$scope.changeWfOperation = function(operation, index) {
		var modWf = $scope.workflow;
		modWf.steps[index].operation = operation;
		SharedWfService.prepForBroadcast(modWf);
	}
};

pigGeneApp.controller("ModalCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	$scope.radioSelection = "";
	
	$scope.$on("handleExWfNamesChange", function() {
		$scope.existingWorkflows = SharedWfService.existingWorkflows;
		$('#myModal').modal('toggle');
	});
	
	$scope.openSelectedWorkflow = function() {
		var selection = $scope.radioSelection;
		if(!(selection == null || selection == "")) {
			SharedWfService.loadWfDefinition(selection);
			$location.path("/wf/" + $scope.radioSelection);
			$('#myModal').modal('toggle');
		}
	}
}]);


