pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3'; // bootstrap3 theme
});

pigGeneApp.directive('workflow', function() {
	return {
		restrict: "E",
		replace: true,
		scope : {
			workflow: '='
		},
		template: "<div><member ng-repeat='member in workflow' member='member'></member></div>"
	}
});

pigGeneApp.directive('member', function($compile) {
	return {
		restrict: "E",
		replace: true,
		scope: {
			member: '='
		},
		template: "<div><b>{{member.name}}</b>{{member.relation}}</div>",
		link: function(scope, iElement, iAttrs) {
			if(angular.isArray(scope.member.data)) {
				$compile("<workflow workflow='member.data'></workflow")(scope, function(cloned, scope) {
					iElement.append(cloned);
				});
			}
		}
	}
});


/////////////////////////////////////////////////////
// test

pigGeneApp.factory("Workflow", function($resource) {
	return $resource("/workflow/:id", {id: '@id'});
});

pigGeneApp.factory("SaveWf", function($resource){
	return $resource("/save/wf/");
});

function emailRouteConfig($routeProvider) {
	$routeProvider
		.when('/', {
			controller: ListController,
			templateUrl: 'list.html'
		})
		.when('/view/:id', {
			controller: DetailController,
			templateUrl: 'detail.html'
		})
		
		//testing
		.when('/wfTable', {
			controller: WorkflowTabCtrl,
			templateUrl: 'workflow.html'
		})
		///
		
		.otherwise({
			redirectTo: '/'
		});
}

pigGeneApp.config(emailRouteConfig);