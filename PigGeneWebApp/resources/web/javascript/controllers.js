pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.buttons = buttons;
	$scope.scriptType = scriptType;
	
	$scope.performNavBarAction = function(index) {
		switch(buttons[index].name) {
			case "newCompBtn":
						SharedWfService.initializeNewComponent();
						break;
			case "newWfBtn":
						SharedWfService.initializeNewWorkflow();
						break;
			case "openWfBtn":
						SharedWfService.openDef = true;
						SharedWfService.loadExistingWorkflowNames(true);
						break;
			case "deleteWfBtn": 	
						SharedWfService.broadcastWfDeletionCheckNotification();
						break;
			case "downloadScriptBtn": 
						SharedWfService.downloadScript();
						break;
			default: break;	
		}
	};
	
	$scope.hideEverything = function() {
		SharedWfService.hideParameterElements();
	};
	
}]);

pigGeneApp.controller("WorkflowCtrl", ["$scope", "$routeParams", "$location", "$filter", "$compile", "$timeout", "SharedWfService", function($scope, $routeParams, $location, $filter, $compile, $timeout, SharedWfService) {
	$scope.visible = false;
	
	$scope.workflow = SharedWfService.workflow;
	if($routeParams.id != "newWf") {
		SharedWfService.loadWfDefinition($routeParams.id);
	};
	
	$scope.workflowName = "abc";
	$scope.workflowDescription = "";
	
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
		
		$scope.workflowName = SharedWfService.workflow.name;
		$('#workflowName').val($scope.workflowName);
		
		$scope.workflowDescription = SharedWfService.workflow.description;
		$('#workflowDescription').val($scope.workflowDescription);
	});
	
	$scope.addNewComponent = function() {
		if(SharedWfService.checkWorkflowNameDefinitionExists()) {
			if(!$scope.pigScriptOnly) {
				SharedWfService.broadcastIllegalScriptCombination();
			} else {
				var newComp = {
						workflowType: "WORKFLOW_COMPONENT",
						scriptType: scriptType[0],
						content: "",
				};
				var modWf = $scope.workflow;
				modWf.components.push(newComp);
				SharedWfService.prepForBroadcast(modWf);
				SharedWfService.showParameterElements();
			}
		}
	}
	
	$scope.addExistingComponent = function() {
		if(SharedWfService.checkWorkflowNameDefinitionExists()) {
			SharedWfService.loadExistingWorkflowNames(false);
		}
	};
	
	$scope.checkType = function(type, checkVal) {
		return (type == checkVal);
	};
	
	$scope.parametersExist = function(param) {
		if(param) {
			if(param.length > 0) {
				return true;
			}
		}
		return false;
	};
	
	$scope.updateWfDefinition = function() {
		$scope.timeout = 2000
		if ($scope.pendingPromise) { 
			$timeout.cancel($scope.pendingPromise); 
		}
		$scope.pendingPromise = $timeout(function () { 
			SharedWfService.changeWfMetaInfo($scope.workflowName, $scope.workflowDescription);
		}, $scope.timeout);
	};
	
	$scope.editReferencedWf = function(id) {
		SharedWfService.persistWfDefinitionAndRedirectToReferencedWf(id);
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
				
				if(mode === 'std') {
					var scriptSelector = $compile('<span class="dropdown"><button class="btn btn-default dropdown-toggle scriptMenu" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true"><span id="scriptType">'+ $scope.workflow.components[index].scriptType.name + ' ' + '</span><span class="caret"></span></button><ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"><li role="presentation"><a name='+index+' role="menuitem" ng-click="setScriptType($event,0)">'+scriptType[0].name+' </a></li><li role="presentation"><a name='+index+' role="menuitem" ng-click="setScriptType($event,1)">'+scriptType[1].name+' </a></li></ul></span>')($scope);
					$(targetElement.parentNode).append(scriptSelector);
				}
				
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
			$(btnGroupElement.nextSibling.nextSibling).remove();
			$(btnGroupElement.nextSibling).remove();
		} else if(operation === "save" || operation === "cancel" || operation === "delete") {
			$(btnGroupElement.previousSibling).remove();
			$(btnGroupElement.nextSibling).remove();
		} else if(operation == "changeScript") {
			$(btnGroupElement.previousSibling.previousSibling).remove();
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
		if(index < $scope.workflow.components.length-1) {
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
		modWf.components[index].content = $(event.currentTarget.parentElement.parentElement.children[0]).val();
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentNode, "save");
	};
	
	$scope.cancelComponentModification = function(event) {
		var index = parseInt(event.currentTarget.name);
		var modWf = $scope.workflow;
		$(event.currentTarget.parentElement.parentElement.children[0]).val(modWf.components[index].content);
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentNode, "cancel");
	};
	
	$scope.deleteComponent = function(event) {
		var index = parseInt(event.currentTarget.name);
		var modWf = $scope.workflow;
		
		if(!$scope.pigScriptOnly && modWf.components[index].scriptType.id != 0) {
			$scope.pigScriptOnly = true;
		}
		
		modWf.components.splice(index, 1);
		if(modWf.components.length === 0) {
			modWf.parameter.inputParameter = [];
			modWf.parameter.outputParameter = [];
			SharedWfService.hideParameterElements();
		}
		SharedWfService.prepForBroadcast(modWf);
		$scope.removeOptionBtns(event.currentTarget.parentNode, "delete");
	};
	
	$scope.pigScriptOnly = true;
	
	$scope.setScriptType = function(event, scriptTypeID) {
		if(scriptTypeID != 0 && $scope.workflow.components.length > 1) {
			SharedWfService.broadcastIllegalScriptCombination();
		} else {
			var index = parseInt(event.currentTarget.name);
			var modWf = $scope.workflow;
			modWf.components[index].scriptType = scriptType[scriptTypeID];
			
			if(scriptTypeID != 0) {
				$scope.pigScriptOnly = false;
			} else if (scriptTypeID == 0 && $scope.noRmdScriptExists()) {
				$scope.pigScriptOnly = true;
			}
			
			SharedWfService.prepForBroadcast(modWf);
			$('#scriptType').html(scriptType[scriptTypeID].name + " ");
			$scope.removeOptionBtns(event.currentTarget.parentElement.parentElement.parentElement, "changeScript");
		}
	};
	
	$scope.noRmdScriptExists = function() {
		var comps = $scope.workflow.components;
		for(var i=0; i<comps.length; i++) {
			if(comps[i].scriptType.id != 0) {
				return false;
			}
		}
		return true;
	}
	
	$scope.isVisible = function() {
		return $scope.visible;
	};
	
	$scope.addInput = function() {
		var modWf = SharedWfService.workflow;
		var inputObj = {
				uid: SharedWfService.getUID(),
				connector: "",
				description: "", 
				position: {
					top: 0,
					left: 0
				}
		};
		modWf.parameter.inputParameter.push(inputObj);
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.removeInput = function(event, index) {
		$scope.detachJSPlumbConnections($(event.currentTarget).parent().children()[1]);
		
		var modWf = SharedWfService.workflow;
		var deleted = $scope.deleteInputParameterMappingEntry(modWf, modWf.parameter.inputParameter[index].connector)
		if(deleted != null || deleted != undefined) {
			modWf = deleted;
		}
		modWf.parameter.inputParameter.splice(index,1);
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.deleteInputParameterMappingEntry = function(modWf, connectorName) {
		for (var key in modWf.parameterMapping.inputParameterMapping) {
			   var wfObj = modWf.parameterMapping.inputParameterMapping[key];
			   for (var paramObj in wfObj) {
				   if(wfObj[paramObj] === connectorName) {
					  delete modWf.parameterMapping.inputParameterMapping[key][paramObj];
				   }
			   }
		}
	};
	
	$scope.addOutput = function() {
		var modWf = SharedWfService.workflow;
		var outputObj = {
				uid: SharedWfService.getUID(),
				connector: "",
				description: "",
				position: {
					top: 0,
					left: 0
				}
		};
		modWf.parameter.outputParameter.push(outputObj);
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.removeOutput = function(event,index) {
		$scope.detachJSPlumbConnections($(event.currentTarget).parent().children()[1]);
		
		var modWf = SharedWfService.workflow;
		var deleted = $scope.deleteOutputParameterMappingEntry(modWf, modWf.parameter.outputParameter[index].connector)
		if(deleted != null || deleted != undefined) {
			modWf = deleted;
		}
		modWf.parameter.outputParameter.splice(index,1);
		SharedWfService.prepForBroadcast(modWf);
	};
	
	$scope.deleteOutputParameterMappingEntry = function(modWf, connectorName) {
		var numberOfMatchingOutputParameterConnectors = 0;
		for(var i=0; i<modWf.parameter.outputParameter.length; i++) {
			if(modWf.parameter.outputParameter[i].connector === connectorName) {
				numberOfMatchingOutputParameterConnectors = numberOfMatchingOutputParameterConnectors + 1;
			}
		}
		if(numberOfMatchingOutputParameterConnectors == 1) {
			for (var key in modWf.parameterMapping.outputParameterMapping) {
				var wfObj = modWf.parameterMapping.outputParameterMapping[key];
				for (var paramObj in wfObj) {
					if(wfObj[paramObj] === connectorName) {
						delete modWf.parameterMapping.outputParameterMapping[key][paramObj];
					}
				}
			}
		}
	};
	
	$scope.detachJSPlumbConnections = function (element) {
		jsPlumb.detachAllConnections($(element));
	};
	
	$scope.$on("showParameterElements", function() {
		$scope.visible = true;
	});
	
	$scope.$on("hideParameterElements", function() {
		$scope.visible = false;
	});

}]);
	
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
		refWf["uid"] = SharedWfService.getUID();
		if(modWf.components.length == 0) {
			modWf.components[0] = refWf;
		} else {
			modWf.components.push(refWf);
		}
		
		var inputParameterMappingObj = {}; //input param mapping
		for(var i = 0; i < refWf.parameter.inputParameter.length; i++) {
			inputParameterMappingObj[refWf.parameter.inputParameter[i].connector] = "";
		}
		modWf.parameterMapping.inputParameterMapping[refWf.name] = inputParameterMappingObj;
		
		var outputParameterMappingObj = {}; //output param mapping
		for(var i = 0; i < refWf.parameter.outputParameter.length; i++) {
			outputParameterMappingObj[refWf.parameter.outputParameter[i].connector] = "";
		}
		modWf.parameterMapping.outputParameterMapping[refWf.name] = outputParameterMappingObj;
		
		SharedWfService.prepForBroadcast(modWf);
		$('#myModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("MissingInputCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("missingWfNameDefinition", function() {
		$('#missingInputModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("DeletionCheckCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("deletionCheckNotification", function() {
		$('#deletionCheckModal').modal('toggle');
	});
	
	$scope.deleteWf = function() {
		SharedWfService.deleteCurrentWfDefinition();
	};
	
}]);

pigGeneApp.controller("ScriptCheckCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("illegalScriptCombination", function() {
		$('#scriptChangeCheckModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller('PlumbCtrl', ["$scope", "SharedWfService", function($scope, SharedWfService) {

	$scope.redraw = function() {
		jsPlumb.detachEveryConnection();
	};
	
	$scope.init = function() {
		jsPlumb.bind("ready", function() {
			jsPlumb.bind("connection", function (connection) {
				$scope.$apply(function () {
					$scope.saveConnection(connection);
				});
			});
		});
	};
	
	$scope.saveConnection = function(connection) {
		var srcConnectionPoint = $('#'+connection.sourceId);
		var targetConnectionPoint = $('#'+connection.targetId);
		
		var srcDataType = srcConnectionPoint.attr('data-type');
		var targetDataType = targetConnectionPoint.attr('data-type');
		
		if(srcDataType === 'input-param' && targetDataType === 'ref-param') {
			var srcInputElement = $(srcConnectionPoint).parent().children()[2];
			var targetInputElement = $(targetConnectionPoint).parent().children()[3];
			var srcFieldVal = $(srcInputElement).val();
			var connectionName;
			if(srcFieldVal === undefined || srcFieldVal === false || srcFieldVal === "") {
				connectionName = SharedWfService.generateUniqueID();
				$(srcInputElement).val(connectionName);
				$(srcInputElement).trigger('input');
			} else {
				connectionName = srcFieldVal;
			}
			$(targetInputElement).val(connectionName);
			$(targetInputElement).trigger('input');
		} else if(srcDataType === 'input-param' && targetDataType === 'output-param') {
			//inform user, that this connection doesnt make any sense...
			//TODO implement better solution than alert!
			alert("please rethink this connection...");
		} else if(srcDataType === 'ref-param' && targetDataType === 'ref-param') {
			var srcInputElement = $(srcConnectionPoint).parent().children()[3];
			var targetInputElement = $(targetConnectionPoint).parent().children()[3];
			var srcFieldVal = $(srcInputElement).val();
			var connectionName;
			if(srcFieldVal === undefined || srcFieldVal === false || srcFieldVal === "") {
				connectionName = SharedWfService.generateUniqueID();
				$(srcInputElement).val(connectionName);
				$(srcInputElement).trigger('input');
			} else {
				connectionName = srcFieldVal;
			}
			$(targetInputElement).val(connectionName);
			$(targetInputElement).trigger('input');
		} else if(srcDataType === 'ref-param' && targetDataType === 'output-param') {
			var srcInputElement = $(srcConnectionPoint).parent().children()[3];
			var targetInputElement = $(targetConnectionPoint).parent().children()[2];
			var srcFieldVal = $(srcInputElement).val();
			var connectionName;
			if(srcFieldVal === undefined || srcFieldVal === false || srcFieldVal === "") {
				connectionName = SharedWfService.generateUniqueID();
				$(srcInputElement).val(connectionName);
				$(srcInputElement).trigger('input');
			} else {
				connectionName = srcFieldVal;
			}
			$(targetInputElement).val(connectionName);
			$(targetInputElement).trigger('input');
		}
	};
	
	$scope.$on("$routeChangeSuccess", function($currentRoute, $previousRoute) {
		setTimeout( function() {
			$scope.loadElementPositions(SharedWfService.workflow.components);
			$scope.loadConnectorElementPositions(SharedWfService.workflow.parameter.inputParameter);
			$scope.loadConnectorElementPositions(SharedWfService.workflow.parameter.outputParameter);
			$scope.loadConnections();
		}, 
		100);
	});
	
	$scope.loadElementPositions = function(elements) {
		if(elements != null || elements != undefined) {
			for(var i=0; i<elements.length; i++) {
				var el = elements[i];
				var HTMLelement = $.find('*[data-id="' + el.uid + '"]');
				if(el.position != null && el.position.top != null && el.position.left != null) {
					$(HTMLelement).parent().css({top: el.position.top, left: el.position.left, position:'absolute'});
				}
			}
		}
	};
	
	$scope.loadConnectorElementPositions = function(elements) {
		if(elements != null || elements != undefined) {
			for(var i=0; i<elements.length; i++) {
				var el = elements[i];
				var HTMLelement = $.find('*[data-id="' + el.uid + '"]');
				$(HTMLelement).parent().parent().css({top: el.position.top, left: el.position.left, position:'absolute'});
			}
		}
	};
	
	$scope.loadConnections = function() {
		var connections = $scope.workflow.connections;
		if(connections != null || connections != undefined) {
			$.each(connections, function(idx, elem) {
				var sourceElement = $.find('*[data-id="' + elem.source + '"]');
				var targetElement = $.find('*[data-id="' + elem.target + '"]');
				var connection = jsPlumb.connect({
					source: $(sourceElement).attr('id'),
					target: $(targetElement).attr('id'),
					container: 'workflow-graph',
					anchors: anchors
				});
			});
		}
	};
	
	$scope.deleteExistingComponent = function(event, index) {
		var parentDiv = $(event.currentTarget).parent();
		var connections = $(parentDiv).find('.circle');
		for(var i=0; i<connections.length; i++) {
			$scope.detachJSPlumbConnections($(connections[i]));
		}
		var modWf = $scope.workflow;
		modWf.components.splice(index,1);
		var componentName = $($(parentDiv).children()[0]).children()[0].innerHTML;
		var mapping = modWf.parameterMapping.outputParameterMapping[componentName];
		var connectorList = [];
		for (var key in mapping) {
			   connectorList.push(mapping[key]);
		}
		for(var i=0; i<modWf.parameter.outputParameter.length; i++) {
			if($.inArray(modWf.parameter.outputParameter[i].connector, connectorList) != -1) {
				modWf.parameter.outputParameter[i].connector = "";
			}
		}
		delete modWf.parameterMapping.inputParameterMapping[componentName];
		delete modWf.parameterMapping.outputParameterMapping[componentName];
		SharedWfService.prepForBroadcast(modWf);
	};
	
}]);