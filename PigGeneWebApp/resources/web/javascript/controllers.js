pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.buttons = buttons;
	
	$scope.performNavBarAction = function(index) {
		for(var i=0; i<buttons.length; i++) {
			buttons[i].active = "";
		}
		buttons[index].active = "active";
		
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

pigGeneApp.controller("ModalCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.$on("handleExWfNamesChange", function() {
		$scope.existingWorkflows = SharedWfService.existingWorkflows;
		$('#myModal').modal('toggle'); //laden der wfNamen darf nur dann durchgefuehrt werden wenn open gecklickt wurde...
	});
}]);


