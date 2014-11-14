pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

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
		var modWf = this.workflow;
		if(newWfName !== undefined) {
			modWf.name = newWfName;
		}
		if(newWfDescription !== undefined) {
			modWf.description = newWfDescription;
		}
		this.prepForBroadcast(modWf);
		
		var oldWfName = this.workflow.name;
		if(!oldWfName && oldWfName != newWfName) {
			//delete call for oldName WF
			this.deleteWfDefinition(oldName);
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
			sharedWorkflow.refWorkflow = response.data;
			sharedWorkflow.broadcastRefWfChange();
			sharedWorkflow.showParameterElements();
		});
	};
	
	sharedWorkflow.persistWfDefinition = function() {
		if(!$.isEmptyObject(this.workflow)) {
			var myWf = new WfPersistency.Save(this.workflow);
			myWf.$save(function(u,putResponseHeaders) {
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
	
	return sharedWorkflow;
}]);

function workflowRouteConfig($routeProvider) {
	$routeProvider
		.when('/wf/:id', {
			controller: WorkflowCtrl,
			templateUrl: 'workflowRepresentation.html'
		});
}
	
pigGeneApp.config(workflowRouteConfig);

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