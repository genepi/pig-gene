var pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = "bs3"; // bootstrap3 theme
});

pigGeneApp.factory("WfPersistency", function($resource) {
	return {
		Existing: $resource("/ex/:type", {type: "@type"}),
		Load: $resource("/wf/:id/:type", {id: "@id", type: "@type"}),
		Ref: $resource("/ref/:id/:type", {id: "@id", type: "@type"}),
		Save: $resource("/save/wf"),
		Delete: $resource("/del/:id/:type", {id: "@id", type: "@type"}),
		Download: $resource("/dwnld/:id", {id: "@id"}),
		DownloadZip: $resource("/dwnldzip/:id", {id: "@id"}), 
		DownloadLibFile: $resource("/dwnldlib")
	};
	
});

pigGeneApp.factory("SharedWfService", ["$rootScope", "$location", "WfPersistency", function($rootScope, $location, WfPersistency) {
	var sharedWorkflow = {};
	
	sharedWorkflow.workflow = {};
	sharedWorkflow.existingWorkflows = {};
	sharedWorkflow.openDef = true;
	sharedWorkflow.wfComposing = false;
	
	sharedWorkflow.initializeNewComponent = function() {
		var emptyWorkflow = {
				name: "newWf",
				description: "component description",
				workflowType: "WORKFLOW",
				components: [{
					workflowType: "WORKFLOW_COMPONENT",
					scriptType: scriptType[0],
					content: "",
				}],
				parameter: {
					inputParameter: [],
					outputParameter: []
				},
				parameterMapping: {
					inputParameterMapping: {},
					outputParameterMapping: {}
				}
		};
		sharedWorkflow.workflow = emptyWorkflow;
		sharedWorkflow.broadcastWfChange(compAbbr);
		sharedWorkflow.redirectLocation("/wf/comp/", "newWf");
	};
	
	sharedWorkflow.initializeNewWorkflow = function() {
		var emptyWorkflow = {
				name: "newWf",
				description: "workflow description",
				workflowType: "WORKFLOW",
				components: [],
				parameter: {
					inputParameter: [],
					outputParameter: []
				},
				parameterMapping: {
					inputParameterMapping: {},
					outputParameterMapping: {}
				}
		};
		sharedWorkflow.workflow = emptyWorkflow;
		sharedWorkflow.broadcastWfChange(wfAbbr);
		sharedWorkflow.redirectLocation("/wf/", "newWf");
	};
	
	sharedWorkflow.resetWorkflow = function() {
		this.workflow = [];
	};
	
	sharedWorkflow.showAdditionalComponentButtons = function() {
		componentButtons[2].showState = true;
	};
	
	sharedWorkflow.showAdditionalWorkflowButtons = function() {
		for(var i=2; i<workflowButtons.length; i++) {
			workflowButtons[i].showState = true;
		}
	};
	
	sharedWorkflow.hideAdditionalComponentButtons = function() {
		componentButtons[2].showState = false;
	};
	
	sharedWorkflow.hideAdditionalWorkflowButtons = function() {
		for(var i=2; i<workflowButtons.length; i++) {
			workflowButtons[i].showState = false;
		}
	};
	
	sharedWorkflow.hideAdditionalButtons = function() {
		this.hideAdditionalComponentButtons();
		this.hideAdditionalWorkflowButtons();
	};
	
	sharedWorkflow.onlyShowAdditionalComponentButtons = function() {
		this.showAdditionalComponentButtons();
		this.hideAdditionalWorkflowButtons();
	};
	
	sharedWorkflow.onlyShowAdditionalWorkflowButtons = function() {
		this.hideAdditionalComponentButtons();
		this.showAdditionalWorkflowButtons();
	};
	
	sharedWorkflow.changeMetaInfo = function(newWfName, newWfDescription, type) {
		var oldWfName = this.workflow.name;
		var modWf = this.workflow;
		if(newWfName !== undefined) {
			modWf.name = newWfName;
		}
		if(newWfDescription !== undefined) {
			modWf.description = newWfDescription;
		}
		this.prepForBroadcast(modWf, type);
		
		if(oldWfName != undefined && newWfName != undefined && oldWfName != newWfName) {
			//delete call for oldName WF
			this.deleteWfDefinition(oldWfName, type);
		}
	};
	
	sharedWorkflow.loadWfDefinition = function(id, type) {
		WfPersistency.Load.get({"id":id, "type":type}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.workflow = response.data;
			sharedWorkflow.showParameterElements();
			if(type === compAbbr) {
				sharedWorkflow.onlyShowAdditionalComponentButtons();
			} else {
				sharedWorkflow.onlyShowAdditionalWorkflowButtons();
			}
			sharedWorkflow.broadcastWfChange(type);
		});
	};
	
	sharedWorkflow.deleteWfDefinition = function(id, type) {
		WfPersistency.Delete.remove({"id":id, "type":type}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
		});
	};
	
	sharedWorkflow.deleteCurrentDefinition = function(type) {
		WfPersistency.Delete.remove({"id":this.workflow.name, "type":type}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			$location.path("").replace();
		});
	};
	
	sharedWorkflow.loadReferencedWfDefinition = function(id, type) {
		WfPersistency.Ref.get({"id":id, "type":type}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			if(response.data.position === undefined) {
				response.data.position = {
						top: 0,
						left: 0
				};
			}
			sharedWorkflow.refWorkflow = response.data;
			sharedWorkflow.broadcastRefWfChange();
			sharedWorkflow.showParameterElements();
		});
	};
	
	sharedWorkflow.persistWfDefinition = function(type) {
		if(!$.isEmptyObject(this.workflow)) {
			var wfToStore = {
					encodedName: encodeURI(this.workflow.name),
					workflow: this.workflow,
					type: type
			}
			var myWf = new WfPersistency.Save.save(wfToStore).$promise.then(function(response) {
				if(!response.success) {
					//TODO fix error message
					alert(response.message);
					console.log(response.message);
					return;
				}
				sharedWorkflow.redirectLocation($location.$$path, sharedWorkflow.workflow.name);
			});
		}
	};
	
	sharedWorkflow.downloadLibFile = function(link) {
		var jsonLink = '{"link": "' + link + '"}'
		var libLink = new WfPersistency.DownloadLibFile.save(jQuery.parseJSON(jsonLink)).$promise.then(function(response) {
			//TODO
			//give user some information that the download worked/failed...
			
			if(!response.success) {
//				alert(response.message);
				console.log(response.message);
				sharedWorkflow.broadcastlibDownloadFailure();
				return;
			}
			sharedWorkflow.broadcastlibDownloadSuccess();
			
		});
	};
	
	sharedWorkflow.redirectLocation = function(oldPath, wfName) {
		var patternComp = /.*\/wf\/comp\/.*/;
		if(patternComp.test(oldPath)) {
			$location.path("/wf/comp/" + wfName).replace();
		} else {
			$location.path("/wf/" + wfName).replace();
		}
	};
	
	sharedWorkflow.loadExistingWorkflowNames = function(openWfDefinition, type, wfComposing) {
		WfPersistency.Existing.get({"type":type}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			if(type === compAbbr && wfComposing) {
				sharedWorkflow.wfComposing = true;
			} else {
				sharedWorkflow.wfComposing = false;
				var index = response.data.names.indexOf(sharedWorkflow.workflow.name);
				if(index != -1) {
					response.data.names.splice(index,1);
				}
			}
			if(response.data.names.length == 0) {
				alert("there exists no other workflow definition except the already opened one");
			} else {
				sharedWorkflow.existingWorkflows = response.data;
				if(type === wfAbbr || wfComposing) {
					sharedWorkflow.openDef = openWfDefinition;
					sharedWorkflow.broadcastExWfNamesChange();
				} else if (type === compAbbr) {
					sharedWorkflow.broadcastExCompNamesChange();
				}
			}
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
	
	sharedWorkflow.downloadZip = function() {
		WfPersistency.DownloadZip.get({"id":sharedWorkflow.workflow.name}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.broadcastZipCreation();
		});
	};
	
	sharedWorkflow.saveWorkflowComponentPosition = function(newPosInfo) {
		var modWf = this.workflow;
		for (var i=0; i<modWf.components.length; i++) {
			if(modWf.components[i].uid === newPosInfo.uid) {
				modWf.components[i].position = newPosInfo.position;
				break;
			}
		}
		this.prepForBroadcast(modWf, wfAbbr);
	};
	
	sharedWorkflow.saveInputParameterPosition = function(newPosInfo) {
		var modWf = this.workflow;
		for (var i=0; i<modWf.parameter.inputParameter.length; i++) {
			if(modWf.parameter.inputParameter[i].uid === newPosInfo.uid) {
				modWf.parameter.inputParameter[i].position = newPosInfo.position;
				break;
			}
		}
		this.prepForBroadcast(modWf, wfAbbr);
	};
	
	sharedWorkflow.saveOutputParameterPosition = function(newPosInfo) {
		var modWf = this.workflow;
		for (var i=0; i<modWf.parameter.outputParameter.length; i++) {
			if(modWf.parameter.outputParameter[i].uid === newPosInfo.uid) {
				modWf.parameter.outputParameter[i].position = newPosInfo.position;
				break;
			}
		}
		this.prepForBroadcast(modWf, wfAbbr);
	};
	
	sharedWorkflow.prepForBroadcast = function(modWf, type) {
		this.workflow = modWf;
		this.broadcastWfChange(type);
	};
	
	sharedWorkflow.broadcastWfChange = function(type) {
		$rootScope.$broadcast("handleWfChange");
		this.persistWfDefinition(type); //autosave
	};
	
	sharedWorkflow.broadcastRefWfChange = function() {
		$rootScope.$broadcast("handleRefWfChange");
	};
	
	sharedWorkflow.broadcastExWfNamesChange = function() {
		$rootScope.$broadcast("handleExWfNamesChange");
	};
	
	sharedWorkflow.broadcastExCompNamesChange = function() {
		$rootScope.$broadcast("handleExCompNamesChange");
	};
	
	sharedWorkflow.broadcastDeletionCheckNotification = function() {
		if(this.workflow != undefined && !jQuery.isEmptyObject(this.workflow)) {
			$rootScope.$broadcast("deletionCheckNotification");
		}
	};
	
	sharedWorkflow.broadcastIllegalScriptCombination = function() {
		$rootScope.$broadcast("illegalScriptCombination");
	};
	
	sharedWorkflow.showParameterElements = function() {
		$rootScope.$broadcast("showParameterElements");
	};
	
	sharedWorkflow.hideParameterElements = function() {
		$rootScope.$broadcast("hideParameterElements");
	};
	
	sharedWorkflow.showComponentNavBar = function() {
		$rootScope.$broadcast("showCompNavBar");
		$rootScope.$broadcast("hideWfNavBar");
	};
	
	sharedWorkflow.showWfNavBar = function() {
		$rootScope.$broadcast("showWfNavBar");
		$rootScope.$broadcast("hideCompNavBar");
	};
	
	sharedWorkflow.broadcastLibraryFileLinkInput = function() {
		$rootScope.$broadcast("libraryFileLinkInput");
	};
	
	sharedWorkflow.broadcastZipCreation = function() {
		$rootScope.$broadcast("zipCreationMessage");
	};
	
	sharedWorkflow.broadcastlibDownloadSuccess = function() {
		$rootScope.$broadcast("libDownloadSuccessMsg");
	};
	
	sharedWorkflow.broadcastlibDownloadFailure = function() {
		$rootScope.$broadcast("libDownloadFailureMsg");
	};
	
	sharedWorkflow.checkWorkflowNameDefinitionExists = function() {
		if(this.workflow.name === "newWf") {
			$rootScope.$broadcast("missingWfNameDefinition");
			return false;
		}
		return true;
	};
	
	sharedWorkflow.generateUniqueID = function() {
		return 'connector_' + this.getUID();
	};
	
	/**
	 * Generates a GUID string.
	 * @returns {String} The generated GUID.
	 * @example af8a8416-6e18-a307-bd9c-f2c947bbb3aa
	 * @author Slavik Meltser (slavik@meltser.info).
	 * @link http://slavik.meltser.info/?p=142
	 */
	sharedWorkflow.getUID = function guid() {
	    function _p8(s) {
	        var p = (Math.random().toString(16)+"000000000").substr(2,8);
	        return s ? "_" + p.substr(0,4) + "_" + p.substr(4,4) : p ;
	    }
	    return _p8() + _p8(true) + _p8(true) + _p8();
	};
	
	return sharedWorkflow;
}]);

pigGeneApp.config(function($routeProvider, $locationProvider) {
	$routeProvider
	.when('/wf/comp/:id', {
		templateUrl: 'componentView.html'
	})
	.when('/wf/:id', {
		templateUrl: 'workflowView.html'
	})
	.otherwise({
		redirectTo: '/home'
	});
});

pigGeneApp.directive('elastic', ['$timeout',
	  function($timeout) {
	    return {
	      restrict: 'A',
	      link: function($scope, element) {
	    	  		var resize = function() {
	    	  			element[0].style.height = "1px";
	    	  			return element[0].style.height = "" + element[0].scrollHeight + "px";
	    	  		};
	    	  		element.on("blur keyup change", resize);
	    	  		$timeout(resize, 0);
      			}
	    };
	  }
]);

pigGeneApp.directive('wfmetaInput', function($timeout, SharedWfService) {
    return {
        restrict: 'E',
        template: '<div><input id="workflowName" class="wfmetaInput" type="text" ng-model="workflowName" ng-change="update()" placeholder="{{placeholder}}"/><br><input id="workflowDescription" class="wfmetaInput" type="text" ng-model="workflowDescription" ng-change="update()" placeholder="workflow description"/></div>',
        scope: {
            value: '=',
            timeout: '@',
            placeholder: '@'
        },
        transclude: true,
        link: function ($scope) {
            $scope.timeout = parseInt($scope.timeout);
            $scope.update = function() {
                if ($scope.pendingPromise) { 
                	$timeout.cancel($scope.pendingPromise); 
                }
            	$scope.pendingPromise = $timeout(function () { 
            		SharedWfService.changeMetaInfo($scope.workflowName, $scope.workflowDescription, wfAbbr);
                }, $scope.timeout);
            };
        }
    }
});

pigGeneApp.directive('compmetaInput', function($timeout, SharedWfService) {
    return {
        restrict: 'E',
        template: '<div><input id="componentName" class="compmetaInput" type="text" ng-model="workflowName" ng-change="update()" placeholder="{{placeholder}}"/><br><input id="componentDescription" class="wfmetaInput" type="text" ng-model="workflowDescription" ng-change="update()" placeholder="component description"/></div>',
        scope: {
            value: '=',
            timeout: '@',
            placeholder: '@'
        },
        transclude: true,
        link: function ($scope) {
            $scope.timeout = parseInt($scope.timeout);
            $scope.update = function() {
                if ($scope.pendingPromise) { 
                	$timeout.cancel($scope.pendingPromise); 
                }
            	$scope.pendingPromise = $timeout(function () { 
            		SharedWfService.changeMetaInfo($scope.workflowName, $scope.workflowDescription, compAbbr);
                }, $scope.timeout);
            };
        }
    }
});

pigGeneApp.directive('postRender', [ '$timeout', function($timeout) {
	var def = {
		restrict : 'A', 
		terminal : true,
		transclude : true,
		link : function(scope, element, attrs) {
			$timeout(scope.redraw, 0);  //Calling a scoped method
		}
	};
	return def;
}]);



pigGeneApp.directive('plumbItem', function(SharedWfService) {
	return {
		replace: true,
		controller: 'PlumbCtrl',
		link: function (scope, element, attrs) {
			jsPlumb.draggable(element, {
				stop: function() {
					var position = {
								top: $(element).position().top,
								left: $(element).position().left
					}
					if(($(element).attr('data-type') === 'ref-element')) {
						var positionInformation = {
								uid: $(element).attr("data-id"),
								position: position
						};
						SharedWfService.saveWorkflowComponentPosition(positionInformation);
					} else {
						var uid = $($((element).children()[2]).children()[1]).attr("data-id");
						var positionInformation = {
								uid: uid,
								position: position
						};
						if(($(element).attr('data-type') === 'input-element')) {
							SharedWfService.saveInputParameterPosition(positionInformation);
						} else if(($(element).attr('data-type') === 'output-element')) {
							SharedWfService.saveOutputParameterPosition(positionInformation);
						}
					}
				}
			});
		}
	};
});

pigGeneApp.directive('plumbSource', function(SharedWfService) {
	return {
		replace: true,
		link: function (scope, element, attrs) {
			jsPlumb.makeSource(element, {
				anchor: 'RightMiddle',
				container: 'workflow-graph',
			});
			if($(element).attr('data-type') === 'input-param') {
				$(element).parent().parent().attr('data-type', 'input-element');
			} else if($(element).attr('data-type') === 'ref-param') {
				$(element).parent().parent().parent().parent().attr('data-type', 'ref-element');
			}
		}
	};
});

pigGeneApp.directive('plumbTarget', function(SharedWfService) {
	return {
		replace: true,
		link: function (scope, element, attrs) {
			jsPlumb.makeTarget(element, {
				anchor: 'LeftMiddle',
				container: 'workflow-graph',
				maxConnections:1
			});
			if($(element).attr('data-type') === 'output-param') {
				$(element).parent().parent().attr('data-type', 'output-element');
			} else if($(element).attr('data-type') === 'ref-param') {
				$(element).parent().parent().parent().parent().attr('data-type', 'ref-element');
			}
		}
	};
});