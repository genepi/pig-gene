pigGeneApp.controller("NavBarCtrl", function($scope) {
	$scope.buttons = buttons;
});

function WorkflowCtrl($scope, $routeParams, WfPersistency) {
	WfPersistency.Load.get({"id":$routeParams.id}).$promise.then(function(response) {
		if(!response.success) {
			//TODO fix error message
			alert("something baaaaaaaaaaaaaaaaaaad happend");
			console.log(response.message);
			return;
		}
		workflow = response.data;
		$scope.workflow = workflow;
	});
};

//pigGeneApp.controller("SendDataToServer", ["$scope", "WfPersistency", function($scope, WfPersistency) {
//	$scope.performPostRequest = function() {
//		var myWiw = new WfPersistency.Save(wiw);
//		myWiw.$save();
//	}
//}]);