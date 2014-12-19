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
	var uniqueIDCounter = 0;
	var sharedWorkflow = {};
	
	sharedWorkflow.workflow = {};
	sharedWorkflow.existingWorkflows = {};
	sharedWorkflow.openDef = true;
	
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
		sharedWorkflow.broadcastWfChange();
		$location.path('/wf/' + "newWf").replace();
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
			$location.path("").replace();
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
				$location.path('/wf/' + sharedWorkflow.workflow.name).replace();
			});
		}
	};
	
	sharedWorkflow.persistWfDefinitionAndRedirectToReferencedWf = function(refWfName) {
		var myWf = new WfPersistency.Save(this.workflow);
		myWf.$save(function(u,putResponseHeaders) {
			$location.path('/wf/' + sharedWorkflow.workflow.name).replace();
			setTimeout(function() {
				sharedWorkflow.openDef = true;
				sharedWorkflow.loadWfDefinition(refWfName);
				$location.path("/wf/" + refWfName);
			},1);
		});
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
	
	sharedWorkflow.setElementPosition = function(elementPosition) {
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
	
	sharedWorkflow.broadcastAdminModeToggle = function() {
		$rootScope.$broadcast("toggleAdminMode");
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
		//TODO change implementation
		return ("$uniqueParameterConnection" + uniqueIDCounter++);
	};
	
	return sharedWorkflow;
}]);

pigGeneApp.config(function($routeProvider, $locationProvider) {
	$routeProvider
	.when('/wf/:id', {
		controller: 'RouteCtrl',
		templateUrl: 'uirouter.html'
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
					if(($(element).attr('data-type') === 'ref-element')) {
						SharedWfService.setElementPosition(
								{
									type: $(element).attr('data-type'),
									name: $(element).attr('data-name'),
									top: $(element).position().top,
									left: $(element).position().left
								}
						);
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
				$(element).parent().parent().attr('data-name', 'TODO_INPUTNAME');
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