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

pigGeneApp.controller('EditableRowCtrl', function($scope) {
	$scope.removeStep = function(index) {
		$scope.workflow.workflow.splice(index, 1);
	};

	$scope.addStep = function() {
		$scope.inserted = {
			relation: "R" + ($scope.workflow.workflow.length+1),
			input: "-",
			operation: null,
			options: "-",
			options2: "-",
			comment: "-",
			active: false
		};
		$scope.workflow.workflow.push($scope.inserted);
	};	
});

//pigGeneApp.controller("SendDataToServer", ["$scope", "WfPersistency", function($scope, WfPersistency) {
//	$scope.performPostRequest = function() {
//		var myWiw = new WfPersistency.Save(wiw);
//		myWiw.$save();
//	}
//}]);