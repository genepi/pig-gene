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
	
	$scope.removeStep = function(index) {
	}
	
	$scope.moveUp = function(index) {
	};
	
	$scope.moveDown = function(index) {
	};

	$scope.addNewComponent = function() {
		alert("new comp clicked");
	};
	
	$scope.addExistingComponent = function() {
		alert("existing comp clicked - TODO!");
	};
	
	$scope.changeWfOperation = function(operation, index) {
	};
	
	$scope.checkType = function(type, checkVal) {
	};

	$scope.editReferencedWf = function(id) {
	}
};

pigGeneApp.controller("ModalCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	$scope.radioSelection = "";
	$scope.openDef = true;
	
	$scope.$on("handleExWfNamesChange", function() {
	});
	
	$scope.openSelectedWorkflow = function() {
	};
	
	$scope.addSelectedWorkflow = function() {
	};
	
	$scope.$on("handleRefWfChange", function() {
	})
	
}]);