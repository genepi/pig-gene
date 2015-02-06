var pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = "bs3"; // bootstrap3 theme
});

pigGeneApp.factory("WfPersistency", function($resource) {
	return {
		Load: $resource("/wf/:id", {id: "@id"}),
		Ref: $resource("/ref/:id", {id: "@id"}),
		Save: $resource("/save/wf/"),
		Delete: $resource("/del/:id", {id: "@id"}),
		Download: $resource("/dwnld/:id", {id: "@id"})
	};
});

pigGeneApp.factory("SharedWfService", ["$rootScope", "$location", "WfPersistency", function($rootScope, $location, WfPersistency) {
	var uniqueConnectorIDCounter = 1;
	var uniqueInputIDCounter = 1;
	var uniqueOutputIDCounter = 1;
	var sharedWorkflow = {};
	
	sharedWorkflow.workflow = {};
	sharedWorkflow.existingWorkflows = {};
	sharedWorkflow.openDef = true;
	
	sharedWorkflow.initializeNewComponent = function() {
		var emptyWorkflow = {
				name: "newWf",
				description: "workflow description",
				workflowType: "WORKFLOW",
				components: [{
					workflowType: "WORKFLOW_COMPONENT",
					scriptType: scriptType[0],
					content: "",
				}],
				parameter: {
					inputParameter: [{name:"",description:""}],
					outputParameter: [{name:"",description:""}]
				},
				parameterMapping: {
					inputParameterMapping: {},
					outputParameterMapping: {}
				}
		};
		sharedWorkflow.workflow = emptyWorkflow;
		sharedWorkflow.broadcastWfChange();
		sharedWorkflow.redirectLocation("/wf/admin/", "newWf");
	};
	
	sharedWorkflow.initializeNewWorkflow = function() {
		var emptyWorkflow = {
				name: "newWf",
				description: "workflow description",
				workflowType: "WORKFLOW",
				components: [],
				flowComponents: [],
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
		sharedWorkflow.broadcastWfChange();
		sharedWorkflow.redirectLocation("/wf/", "newWf");
	};
	
	sharedWorkflow.changeWfMetaInfo = function(newWfName, newWfDescription) {
		var oldWfName = this.workflow.name;
		var modWf = this.workflow;
		if(newWfName !== undefined) {
			modWf.name = newWfName;
		}
		if(newWfDescription !== undefined) {
			modWf.description = newWfDescription;
		}
		this.prepForBroadcast(modWf);
		
		if(oldWfName != undefined && newWfName != undefined && oldWfName != newWfName) {
			//delete call for oldName WF
			this.deleteWfDefinition(oldWfName);
		}
	};
	
	sharedWorkflow.loadWfDefinition = function(id) {
//		var oldPath = $location.$$path;
		WfPersistency.Load.get({"id":id}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.workflow = response.data;
			sharedWorkflow.showParameterElements();
			sharedWorkflow.broadcastWfChange();
//			sharedWorkflow.redirectLocation(oldPath, id);
		});
	};
	
	sharedWorkflow.deleteWfDefinition = function(id) {
		WfPersistency.Delete.remove({"id":id}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
		});
	};
	
	sharedWorkflow.deleteCurrentWfDefinition = function() {
		WfPersistency.Delete.remove({"id":this.workflow.name}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			$location.path("").replace();
		});
	};
	
	sharedWorkflow.loadReferencedWfDefinition = function(id) {
		WfPersistency.Ref.get({"id":id}).$promise.then(function(response) {
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
	
	sharedWorkflow.persistWfDefinition = function() {
		if(!$.isEmptyObject(this.workflow)) {
			var wfToStore = {
					encodedName: encodeURI(this.workflow.name),
					workflow: this.workflow
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
	
	sharedWorkflow.persistWfDefinitionAndRedirectToReferencedWf = function(refWfName) {
		var myWf = new WfPersistency.Save(this.workflow);
		myWf.$save(function(u,putResponseHeaders) {
			sharedWorkflow.redirectLocation($location.$$path, sharedWorkflow.workflow.name);
			setTimeout(function() {
				sharedWorkflow.openDef = true;
				sharedWorkflow.loadWfDefinition(refWfName);
				sharedWorkflow.redirectLocation($location.$$path, refWfName);
			},1);
		});
	};
	
	sharedWorkflow.redirectLocation = function(oldPath, wfName) {
		var patternAdmin = /.*\/wf\/admin\/.*/;
		if(patternAdmin.test(oldPath)) {
			$location.path("/wf/admin/" + wfName).replace();
		} else {
			$location.path("/wf/" + wfName).replace();
		}
	};
	
	sharedWorkflow.loadExistingWorkflowNames = function(openWfDefinition) {
		WfPersistency.Load.get({}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert(response.message);
				console.log(response.message);
				return;
			}
			sharedWorkflow.openDef = openWfDefinition;
			sharedWorkflow.existingWorkflows = response.data;
			sharedWorkflow.broadcastExWfNamesChange();
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
	
	sharedWorkflow.saveWorkflowComponentPosition = function(elementPosition) {
		var elementName = elementPosition.name;
		var modWf = this.workflow;
		for(var i=0; i<modWf.components.length; i++) {
			if(modWf.components[i].name === elementName) {
				modWf.components[i].position = {
						top: elementPosition.top,
						left: elementPosition.left
				};
				break;
			}
		}
		this.prepForBroadcast(modWf);
	};
	
	sharedWorkflow.saveFlowComponentPosition = function(flowComponent) {
		var name = flowComponent.name;
		var modWf = this.workflow;
		for(var i=0; i<modWf.flowComponents.length; i++) {
			if(modWf.flowComponents[i].name === name) {
				modWf.flowComponents[i].position = {
						top: flowComponent.top,
						left: flowComponent.left
				};
				break;
			}
		}
		this.prepForBroadcast(modWf);
	};
	
	sharedWorkflow.prepForBroadcast = function(modWf) {
		this.workflow = modWf;
		this.broadcastWfChange();
	};
	
	sharedWorkflow.broadcastWfChange = function() {
		$rootScope.$broadcast("handleWfChange");
		this.persistWfDefinition(); //autosave
	};
	
	sharedWorkflow.broadcastRefWfChange = function() {
		$rootScope.$broadcast("handleRefWfChange");
	};
	
	sharedWorkflow.broadcastExWfNamesChange = function() {
		$rootScope.$broadcast("handleExWfNamesChange");
	};
	
	sharedWorkflow.broadcastWfDeletionCheckNotification = function() {
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
	
	sharedWorkflow.checkWorkflowNameDefinitionExists = function() {
		if(this.workflow.name === "newWf") {
			$rootScope.$broadcast("missingWfNameDefinition");
			return false;
		}
		return true;
	};
	
	sharedWorkflow.generateUniqueID = function() {
		return ("$connector_" + uniqueConnectorIDCounter++);
	};
	
	sharedWorkflow.generateUniqueInputID = function() {
		return ("input-element_" + uniqueInputIDCounter++);
	};
	
	return sharedWorkflow;
}]);

pigGeneApp.config(function($routeProvider, $locationProvider) {
	$routeProvider
	.when('/wf/admin/:id', {
		templateUrl: 'adminView.html'
	})
	.when('/wf/:id', {
		templateUrl: 'standardView.html'
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
            		SharedWfService.changeWfMetaInfo($scope.workflowName, $scope.workflowDescription);
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
			console.log("jsPlumb added an element...");
			
			jsPlumb.draggable(element, {
				stop: function() {
					var positionInformation = {
							name: $(element).attr('data-name'),
							top: $(element).position().top,
							left: $(element).position().left
					};
					if(($(element).attr('data-type') === 'ref-element')) {
						SharedWfService.saveWorkflowComponentPosition(positionInformation);
					} else if(($(element).attr('data-type') === 'input-element')) {
						SharedWfService.saveFlowComponentPosition(positionInformation);
					}
				}
			});
			
			//TODO push if not yet in array!!!
//			if(($(element).attr('data-type') === 'input-element')) {
//				var modWf = SharedWfService.workflow;
//				var comp = {
//						name: $(element).attr('data-name'), 
//						position: {
//							top: 0,
//							left: 0
//						} 
//				};
//				modWf.flowComponents.push(comp);
//				SharedWfService.prepForBroadcast(modWf);
//			}
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
				$(element).parent().parent().attr('data-name', SharedWfService.generateUniqueInputID());
			} else if($(element).attr('data-type') === 'ref-param') {
				var attr = $(element).parent().parent().parent().parent().attr('data-type');
				if(typeof attr === typeof undefined || attr === false) {
					$(element).parent().parent().parent().parent().attr('data-type', 'ref-element');
					var name = $(element).parent().parent().parent().children()[0].children[0].innerHTML;
					$(element).parent().parent().parent().parent().attr('data-name', name);
				}
			}
		}
	};
});

pigGeneApp.directive('plumbTarget', function() {
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
				$(element).parent().parent().attr('data-name', 'TODO_OUTPUTNAME');
			} else if($(element).attr('data-type') === 'ref-param') {
				var attr = $(element).parent().parent().parent().parent().attr('data-type');
				if(typeof attr === typeof undefined || attr === false) {
					$(element).parent().parent().parent().parent().attr('data-type', 'ref-element');
					var name = $(element).parent().parent().parent().children()[0].children[0].innerHTML;
					$(element).parent().parent().parent().parent().attr('data-name', name);
				}
			}
		}
	};
});