pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.buttons = buttons;
	
	$scope.performNavBarAction = function(index) {
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
						//TODO: implement functionality
						break;
			default: break;
		}
	}
}]);

function WorkflowCtrl($scope, $routeParams, $location, $filter, SharedWfService) {
	$scope.workflow = SharedWfService.workflow;
	if($routeParams.id != "newWf") {
		SharedWfService.loadWfDefinition($routeParams.id);
	}
	
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
	});

	$scope.addNewComponent = function() {
		var newComp = {
			workflowType: "WORKFLOW_COMPONENT",
			content: "please type your desired pig-operations"
		};
		var modWf = $scope.workflow;
		modWf.components.push(newComp);
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.addExistingComponent = function() {
		SharedWfService.loadExistingWorkflowNames();
	};
	
	$scope.checkType = function(type, checkVal) {
		return (type == checkVal);
	};
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
		if(modWf.components.length == 0) {
			modWf.components[0] = refWf;
		} else {
			modWf.components.push(refWf);
		}
		SharedWfService.prepForBroadcast(modWf);
		$('#myModal').modal('toggle');
	});
}]);