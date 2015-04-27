pigGeneApp.controller("NavBarCtrl", ["$scope", "SharedWfService", "$location", function($scope, SharedWfService, $location) {
	$scope.buttons = buttons;
	$scope.storeIndicator =  {
			showState: false,
			logo: ""
	};
	
	$scope.performNavBarAction = function(index) {
		switch(buttons[index].name) {
			case "components":
						$scope.redirectToHome();
						$scope.modifyActiveState(index);
						SharedWfService.hideAdditionalButtons();
						SharedWfService.resetWorkflow();
						SharedWfService.showComponentNavBar();
						break;
			case "workflows":
						$scope.redirectToHome();
						$scope.modifyActiveState(index);
						SharedWfService.hideAdditionalButtons();
						SharedWfService.resetWorkflow();
						SharedWfService.showWfNavBar();
						break;
			case "library file":
						SharedWfService.broadcastLibraryFileLinkInput();
						break;
			default: break;	
		}
	};
	
	$scope.redirectToHome = function() {
		$location.path("/home");
		$scope.storeIndicator.showState = false;
	};
	
	$scope.modifyActiveState = function(activeIndex) {
		for(var i=0; i<buttons.length; i++) {
			buttons[i].active = "";
		}
		buttons[activeIndex].active = "active";
	};
	
	$scope.hideEverything = function() {
		SharedWfService.hideParameterElements();
		SharedWfService.hideAdditionalButtons();
	};
	
	$scope.$on("showSpinningIndicator", function() {
		$scope.storeIndicator.logo = "fa fa-refresh fa-spin"
		$scope.storeIndicator.showState = true;
	});
	
	$scope.$on("showTickIndicator", function() {
		$scope.storeIndicator.logo = "fa fa-check";
		$scope.storeIndicator.showState = true;
	});
	
	$scope.$on("showProblemIndicator", function() {
		$scope.storeIndicator.logo = "fa fa-exclamation";
		$scope.storeIndicator.showState = true;
	});
	
}]);

pigGeneApp.controller("ComponentNavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.componentButtons = componentButtons;
	$scope.showCompNavBar = false;
	
	$scope.performComponentNavBarAction = function(index) {
		switch(componentButtons[index].name) {
			case "createComponentBtn":
						SharedWfService.initializeNewComponent();	
						break;
			case "openComponentBtn":
						SharedWfService.openDef = true;
						SharedWfService.loadExistingWorkflowNames(true, compAbbr, false);
						break;
			case "deleteComponentBtn": 	
						SharedWfService.broadcastDeletionCheckNotification();
						break;
			default: break;	
		}
	};
	
	$scope.$on("showCompNavBar", function() {
		$scope.showCompNavBar = true;
	});
	
	$scope.$on("hideCompNavBar", function() {
		$scope.showCompNavBar = false;
	});
	
}]);

pigGeneApp.controller("WorkflowNavBarCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.workflowButtons = workflowButtons;
	$scope.showWfNavBar = false;
	
	$scope.performComponentNavBarAction = function(index) {
		switch(workflowButtons[index].name) {
			case "createWfBtn":
						SharedWfService.initializeNewWorkflow();
						break;
			case "openWfBtn":
						SharedWfService.openDef = true;
						SharedWfService.loadExistingWorkflowNames(true, wfAbbr, false);
						break;
			case "deleteWfBtn": 	
						SharedWfService.broadcastDeletionCheckNotification();
						break;
			case "downloadScriptBtn": 	
						SharedWfService.downloadScript();
						break;
			case "downloadZipBtn": 	
						workflowButtons[index].logo = "fa fa-refresh fa-spin";
						SharedWfService.downloadZip();
						break;
			default: break;	
		}
	};
	
	$scope.$on("showWfNavBar", function() {
		$scope.showWfNavBar = true;
	});
	
	$scope.$on("hideWfNavBar", function() {
		$scope.showWfNavBar = false;
	});
	
}]);

pigGeneApp.controller("WorkflowCtrl", ["$scope", "$routeParams", "$location", "$filter", "$compile", "$timeout", "SharedWfService", function($scope, $routeParams, $location, $filter, $compile, $timeout, SharedWfService) {
	$scope.visible = false;
	$scope.scriptType = scriptType;
	
	$scope.workflow = SharedWfService.workflow;
	if($routeParams.id != "newWf") {
		var type = wfAbbr;
		var patternComp = /.*\/wf\/comp\/.*/;
		if(patternComp.test($location.$$url)) {
			type = compAbbr;
		}
		SharedWfService.loadWfDefinition($routeParams.id, type);
	};
	
	$scope.workflowName = "abc";
	$scope.workflowDescription = "";
	$scope.scriptContent = "";
	
	$scope.$on("handleWfChange", function() {
		$scope.workflow = SharedWfService.workflow;
		
		$scope.workflowName = SharedWfService.workflow.name;
		$('#workflowName').val($scope.workflowName);
		$('#componentName').val($scope.workflowName);
		
		$scope.workflowDescription = SharedWfService.workflow.description;
		$('#workflowDescription').val($scope.workflowDescription);
		$('#componentDescription').val($scope.workflowDescription);
		
		var comp = SharedWfService.workflow.components[0];
		if(comp != undefined) {
			$scope.scriptContent = comp.content;
			$('#scriptContent').val($scope.scriptContent);
		}
	});
	
	$scope.addExistingComponent = function() {
		if(SharedWfService.checkWorkflowNameDefinitionExists()) {
			SharedWfService.loadExistingWorkflowNames(false, compAbbr, true);
		}
	};
	
	$scope.addExistingWorkflow = function() {
		if(SharedWfService.checkWorkflowNameDefinitionExists()) {
			SharedWfService.loadExistingWorkflowNames(false, wfAbbr, true);
		}
	};
	
	$scope.parametersExist = function(param) {
		if(param) {
			if(param.length > 0) {
				return true;
			}
		}
		return false;
	};
	
	$scope.updateWfDefinition = function(type) {
		$scope.timeout = 2000
		if ($scope.pendingPromise) { 
			$timeout.cancel($scope.pendingPromise); 
		}
		$scope.pendingPromise = $timeout(function () { 
			SharedWfService.changeMetaInfo($scope.workflowName, $scope.workflowDescription, type);
		}, $scope.timeout);
	};
	
	$scope.setScriptType = function(scriptTypeID) {
		var modWf = $scope.workflow;
		modWf.components[0].scriptType = scriptType[scriptTypeID];
		
		SharedWfService.prepForBroadcast(modWf, compAbbr);
		$('#scriptType').html(scriptType[scriptTypeID].name + " ");
	};
	
	$scope.isVisible = function() {
		return $scope.visible;
	};
	
	$scope.addInput = function(type) {
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
		SharedWfService.prepForBroadcast(modWf, type);
	};
	
	$scope.removeInput = function(event, index, type) {
		var modWf = SharedWfService.workflow;
		if(event != null) {
			$scope.detachJSPlumbConnections($(event.currentTarget).parent().children()[1]);
			var deleted = $scope.deleteInputParameterMappingEntry(modWf, modWf.parameter.inputParameter[index].connector)
			if(deleted != null || deleted != undefined) {
				modWf = deleted;
			}
		}
		modWf.parameter.inputParameter.splice(index,1);
		SharedWfService.prepForBroadcast(modWf, type);
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
	
	$scope.addOutput = function(type) {
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
		SharedWfService.prepForBroadcast(modWf, type);
	};
	
	$scope.removeOutput = function(event, index, type) {
		var modWf = SharedWfService.workflow;
		if(event != null) {
			$scope.detachJSPlumbConnections($(event.currentTarget).parent().children()[1]);
			var deleted = $scope.deleteOutputParameterMappingEntry(modWf, modWf.parameter.outputParameter[index].connector)
			if(deleted != null || deleted != undefined) {
				modWf = deleted;
			}
		}
		modWf.parameter.outputParameter.splice(index,1);
		SharedWfService.prepForBroadcast(modWf, type);
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
	
	$scope.openComponentDefinition = function(workflowName) {
		SharedWfService.loadWfDefinition(workflowName, compAbbr);
		$location.path("/wf/comp/" + workflowName);
		setTimeout(function() {
			$('#scriptContent').val(SharedWfService.workflow.components[0].content);
		}, 200);
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
	
pigGeneApp.controller("WfLoadingCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	$scope.radioSelection = "";
	$scope.openDef = true;
	
	$scope.$on("handleExWfNamesChange", function() {
		$scope.existingWorkflows = SharedWfService.existingWorkflows;
		$scope.openDef = SharedWfService.openDef;
		$('#WfLoadingModal').modal('toggle');
	});
	
	$scope.openSelectedWorkflow = function() {
		var selection = $scope.radioSelection;
		if(!(selection == null || selection == "")) {
			SharedWfService.loadWfDefinition(selection, wfAbbr);
			$location.path("/wf/" + $scope.radioSelection);
			$('#WfLoadingModal').modal('toggle');
		}
	};
	
	$scope.addSelectedWorkflow = function() {
		var selection = $scope.radioSelection;
		if(!(selection == null || selection == "")) {
			//AJAX call to get important wf data
			var type = wfAbbr;
			if(SharedWfService.wfComposing) {
				type = compAbbr;
			}
			SharedWfService.loadReferencedWfDefinition(selection, type);
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
		modWf.parameterMapping.inputParameterMapping[refWf.uid] = inputParameterMappingObj;
		
		var outputParameterMappingObj = {}; //output param mapping
		for(var i = 0; i < refWf.parameter.outputParameter.length; i++) {
			outputParameterMappingObj[refWf.parameter.outputParameter[i].connector] = "";
		}
		modWf.parameterMapping.outputParameterMapping[refWf.uid] = outputParameterMappingObj;
		
		SharedWfService.prepForBroadcast(modWf, wfAbbr);
		$('#WfLoadingModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("CompLoadingCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	$scope.radioSelection = "";
	$scope.openDef = true;
	
	$scope.$on("handleExCompNamesChange", function() {
		$scope.existingWorkflows = SharedWfService.existingWorkflows;
		$scope.openDef = SharedWfService.openDef;
		$('#CompLoadingModal').modal('toggle');
	});
	
	$scope.openSelectedComponent = function() {
		var selection = $scope.radioSelection;
		if(!(selection == null || selection == "")) {
			SharedWfService.loadWfDefinition(selection, compAbbr);
			$location.path("/wf/comp/" + $scope.radioSelection);
			$('#CompLoadingModal').modal('toggle');
		}
	};
	
}]);

pigGeneApp.controller("MissingInputCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("missingWfNameDefinition", function() {
		$('#missingInputModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("DeletionCheckCtrl", ["$scope", "$location", "SharedWfService", function($scope, $location, SharedWfService) {
	
	$scope.$on("deletionCheckNotification", function() {
		$('#deletionCheckModal').modal('toggle');
	});
	
	$scope.delete = function() {
		var type = wfAbbr;
		var patternComp = /.*\/wf\/comp\/.*/;
		if(patternComp.test($location.$$url)) {
			type = compAbbr;
		}
		SharedWfService.deleteCurrentDefinition(type);
	};
	
}]);

pigGeneApp.controller("ScriptCheckCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("illegalScriptCombination", function() {
		$('#scriptChangeCheckModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("ZipCreationCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("zipCreationMessage", function() {
		workflowButtons[4].logo = "fa fa-file-archive-o";
		$('#successfulZipCreationModal').modal('toggle');
	});
	
	$scope.$on("resetZipIcon", function() {
		workflowButtons[4].logo = "fa fa-file-archive-o";
	});
	
}]);

pigGeneApp.controller("LibraryLinkInputCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	$scope.link = "";
	
	$scope.$on("libraryFileLinkInput", function() {
		$('#libraryLinkInputModal').modal('toggle');
	});
	
	$scope.downloadLibFile = function() {
		buttons[2].logo = "fa fa-refresh fa-spin";
		for(var i=0; i<buttons.length; i++) {
			buttons[i].active = "";
		}
		buttons[2].active = "active";
		SharedWfService.downloadLibFile($scope.link);
		$scope.link = "";
	};
	
}]);

pigGeneApp.controller("LibraryLinkNotificationCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("libDownloadSuccessMsg", function() {
		buttons[2].logo = "fa fa-puzzle-piece";
		buttons[2].active = "";
		$('#libraryLinkDownloadStatusMsg').html('successfully finished');
		$('#libraryLinkDownloadStatusBtn').removeClass('btn-danger');
		$('#libraryLinkDownloadStatusBtn').addClass('btn-success');
		$('#libraryLinkDownloadNotificationModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("pointlessConnectionCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("pointlessConnectionMsg", function() {
		$('#pointlessConnectionModal').modal('toggle');
	});
	
}]);

pigGeneApp.controller("serverExceptionInfoCtrl", ["$scope", "SharedWfService", function($scope, SharedWfService) {
	
	$scope.$on("serverExceptionMsg", function() {
		$('#serverExceptionInfoModal').modal('toggle');
		$('#serverExceptionMsgContainer').html(SharedWfService.serverExceptionMsg);
	});
	
}]);


pigGeneApp.controller('PlumbCtrl', ["$scope", "SharedWfService", function($scope, SharedWfService) {

	//TODO remove
//	$scope.redraw = function() {
//		jsPlumb.detachEveryConnection();
//	};
	
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
			SharedWfService.broadcastPointlessConnectionOperation();
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
		250);
	});
	
	$scope.loadElementPositions = function(elements) {
		if(elements != null || elements != undefined) {
			for(var i=0; i<elements.length; i++) {
				var el = elements[i];
				var HTMLelement = $.find('*[data-id="' + el.uid + '"]');
				if(el.position != null && el.position.top != null && el.position.left != null) {
					$(HTMLelement).css({top: el.position.top, left: el.position.left, position:'absolute'});
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
		var componentUID = modWf.components[index].uid;
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
		delete modWf.parameterMapping.inputParameterMapping[componentUID];
		delete modWf.parameterMapping.outputParameterMapping[componentUID];
		SharedWfService.prepForBroadcast(modWf, wfAbbr);
	};
	
}]);