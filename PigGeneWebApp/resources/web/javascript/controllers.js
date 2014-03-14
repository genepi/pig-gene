pigGeneApp.controller("NavBarCtrl", function($scope) {
	$scope.buttons = buttons;
});

pigGeneApp.controller("WorkflowCtrl", ["$scope", "WfPersistency", function($scope, WfPersistency) {
		$scope.data = {};
		var res = WfPersistency.Load.get({"id":"rangeQuery"}).$promise.then(function(response) {
			if(!response.success) {
				//TODO fix error message
				alert("something baaaaaaaaaaaaaaaaaaad happend");
				console.log(response.message);
				return;
			}
			$scope.data.returnValue = response.data;
		});
}]);

pigGeneApp.controller("SendDataToServer", ["$scope", "WfPersistency", function($scope, WfPersistency) {
	$scope.performPostRequest = function() {
		var myWiw = new WfPersistency.Save(wiw);
		myWiw.$save();
	}
}]);