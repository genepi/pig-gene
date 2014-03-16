pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.buttons = buttons;
	
	$scope.performNavBarAction = function(index) {
		$scope.changeActiveNavBarIcon(index);
		
		//TODO button handling depending on current button...
		switch(buttons[index].name) {
			case "newWfBtn":
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

function WorkflowCtrl($scope, $routeParams, SharedWfService) {
	SharedWfService.loadWfDefinition($routeParams.id);
		
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
	});
	
	$scope.removeStep = function(index) {
		SharedWfService.removeStep(index);
	};
	
	$scope.addStep = function() {
		$scope.inserted = {
				relation: 'R' + ($scope.workflow.workflow.length+1),
				input: '-',
				operation: null,
				options: '-',
				options2: '-',
				comment: '-',
				active: false
		};
		SharedWfService.addStep($scope.inserted);
	}
};

pigGeneApp.controller("ModalCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	$scope.radioSelection = "";
	
	$scope.$on("handleExWfNamesChange", function() {
		$scope.existingWorkflows = SharedWfService.existingWorkflows;
		$('#myModal').modal('toggle');
	});
	
	$scope.openSelectedWorkflow = function() {
		$location.path("/wf/" + $scope.radioSelection);
		$('#myModal').modal('toggle');
	}
}]);


