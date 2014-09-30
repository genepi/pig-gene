pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.buttons = buttons;
	
	$scope.performNavBarAction = function(index) {
		switch(buttons[index].name) {
			case "newWfBtn":
						SharedWfService.initializeNewWorkflow();
						break;
			case "openWfBtn":
						SharedWfService.openDef = true;
						SharedWfService.loadExistingWorkflowNames(true);
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
	};
	
	$scope.hideEverything = function() {
		SharedWfService.hideParameterElements();
	};
	
}]);

function WorkflowCtrl($scope, $routeParams, $location, $filter, $compile, SharedWfService) {
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
		SharedWfService.loadExistingWorkflowNames(false);
	};
	
	$scope.checkType = function(type, checkVal) {
		return (type == checkVal);
	};
	
	$scope.editReferencedWf = function(id) {
		SharedWfService.persistWfDefinitionAndRedirectToReferencedWf(id);
	};
	
	$scope.renderChangeHeadlineBtns = function(event) {
		var targetElement = event.currentTarget.parentElement;
		var options = "options"
			
		if(!$(targetElement).hasClass(options)) {
			var buttonGroup = $("<div class='btn-group textOptionBtns'></div>");
			var acceptBtn = $compile("<button type='button' class='btn btn-sm btn-success' ng-click='saveHeadlineModification($event)'><span class='glyphicon glyphicon-ok'></span></button>")($scope);
			var cancelBtn = $compile("<button type='button' class='btn btn-sm btn-warning' ng-click='cancelHeadlineModification($event)'><span class='glyphicon glyphicon-remove'></span></button>")($scope);
			$(buttonGroup).append(acceptBtn);
			$(buttonGroup).append(cancelBtn);
		}
		$(targetElement).append(buttonGroup);
		$(targetElement).addClass(options);
	};
	
	$scope.saveHeadlineModification = function(event) {
		var modWf = $scope.workflow;
		modWf.name = $(event.currentTarget.parentElement.parentElement.children[0]).text();
		modWf.description = $(event.currentTarget.parentElement.parentElement.children[1]).text();
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentElement, "textSave");
	};
	
	$scope.cancelHeadlineModification = function(event) {
		var modWf = $scope.workflow;
		$(event.currentTarget.parentElement.parentElement.children[0]).text(modWf.name);
		$(event.currentTarget.parentElement.parentElement.children[1]).text(modWf.description);
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentElement, "textCancel");
	};
	
	$scope.renderOptionBtns = function(event, index, mode) {
		var targetElement = event.currentTarget;
		var options = "options"
		
		if(!$(targetElement).hasClass(options)) {
			var buttonGroup1 = $("<div class='btn-group optionBtns1'></div>");
			var buttonGroup2 = $("<div class='btn-group optionBtns2'></div>");

			var upBtn = $compile("<button name="+index+" type='button' class='btn btn-sm btn-default' ng-click='moveComponentUp($event)'><span class='glyphicon glyphicon-arrow-up'></span></button>")($scope);
			var downBtn = $compile("<button name="+index+" type='button' class='btn btn-sm btn-default' ng-click='moveComponentDown($event)'><span class='glyphicon glyphicon-arrow-down'></span></button>")($scope);
			$(buttonGroup1).append(upBtn);
			$(buttonGroup1).append(downBtn);
			
			if(mode === 'std') {
				var acceptBtn = $compile("<button name="+index+" type='button' class='btn btn-sm btn-success' ng-click='saveComponentModification($event)'><span class='glyphicon glyphicon-ok'></span></button>")($scope);
				$(buttonGroup2).append(acceptBtn);
			}
			
			var cancelBtn = $compile("<button name="+index+" type='button' class='btn btn-sm btn-warning' ng-click='cancelComponentModification($event)'><span class='glyphicon glyphicon-remove'></span></button>")($scope);
			$(buttonGroup2).append(cancelBtn);
			var deleteBtn = $compile("<button name="+index+" type='button' class='btn btn-sm btn-danger' ng-click='deleteComponent($event)'><span class='glyphicon glyphicon-trash'></span></button>")($scope);
			$(buttonGroup2).append(deleteBtn);
			
			$(targetElement.parentNode).append(buttonGroup1);
			$(targetElement.parentNode).append(buttonGroup2);
			$(targetElement).addClass(options);
		}
	};
	
	$scope.removeOptionBtns = function(btnGroupElement, operation) {
		if(operation === 'textSave' || operation === 'textCancel') {
			$(btnGroupElement.parentNode).removeClass("options");
		} else {
			$(btnGroupElement.parentNode.children[0]).removeClass("options");
		}
		if(operation === "up" || operation === "down") {
			$(btnGroupElement.nextSibling).remove();
		} else if(operation === "save" || operation === "cancel" || operation === "delete") {
			$(btnGroupElement.previousSibling).remove();
		}
		$(btnGroupElement).remove();
	};
	
	$scope.moveComponentUp = function(event) {
		var index = parseInt(event.currentTarget.name);
		if(index >= 1) {
			var modWf = $scope.workflow;
			var prevElement = modWf.components[index-1];
			modWf.components[index-1] = modWf.components[index];
			modWf.components[index] = prevElement;
			SharedWfService.prepForBroadcast(modWf);
			$scope.removeOptionBtns(event.currentTarget.parentNode, "up");
		}
	};
	
	$scope.moveComponentDown = function(event) {
		var index = parseInt(event.currentTarget.name);
		if(index < $scope.workflow.components.length-2) {
			var modWf = $scope.workflow;
			var followingElement = modWf.components[index+1];
			modWf.components[index+1] = modWf.components[index];
			modWf.components[index] = followingElement;
			SharedWfService.prepForBroadcast(modWf);
			$scope.removeOptionBtns(event.currentTarget.parentNode, "down");
		}
	};
	
	$scope.saveComponentModification = function(event) {
		var index = parseInt(event.currentTarget.name);
		var modWf = $scope.workflow;
		modWf.components[index].content = $(event.currentTarget.parentElement.parentElement.children[0]).text();
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentNode, "save");
	};
	
	$scope.cancelComponentModification = function(event) {
		var index = parseInt(event.currentTarget.name);
		var modWf = $scope.workflow;
		$(event.currentTarget.parentElement.parentElement.children[0]).text(modWf.components[index].content);
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentNode, "cancel");
	};
	
	$scope.deleteComponent = function(event) {
		var index = parseInt(event.currentTarget.name);
		var modWf = $scope.workflow;
		modWf.components.splice(index, 1);
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentNode, "delete");
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
		var mappingObj = {};
		for(var i = 0; i<refWf.inputParams.length; i++) {
			mappingObj[refWf.inputParams[i]] = "";
		}
		modWf.inputParamMapping[refWf.name] = mappingObj;
		SharedWfService.prepForBroadcast(modWf);
		$('#myModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("InOutputParamCtrl", ["$scope", "$routeParams", "SharedWfService", function($scope, $routeParams, SharedWfService) {
	$scope.workflow = SharedWfService.workflow;
	$scope.visible = false;
	
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
	});
	
	$scope.addInput = function() {
		var modWf = SharedWfService.workflow;
		modWf.inputParams.push("");
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.isVisible = function() {
		return $scope.visible;
	};
	
	$scope.$on("showParameterElements", function() {
		$scope.visible = true;
	});
	
	$scope.$on("hideParameterElements", function() {
		$scope.visible = false;
	});
	
}]);