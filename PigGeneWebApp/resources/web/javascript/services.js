pigGeneApp = angular.module("pigGene",["ngResource", "ngRoute", "xeditable"]);

pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3'; // bootstrap3 theme
});


/////////////////////////////////////////////////////
// test

pigGeneApp.factory("Workflow", function($resource) {
	return $resource("/workflow/:id", {id: '@id'})
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
		.otherwise({
			redirectTo: '/'
		});
}

pigGeneApp.config(emailRouteConfig);