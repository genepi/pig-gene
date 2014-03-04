pigGeneApp.controller("NavBarCtrl", function($scope) {
	$scope.buttons = buttons;
});

pigGeneApp.controller('EditableRowCtrl', function($scope) {
	$scope.workflow = workflow;
	$scope.operations = operations;
	
	$scope.showStatus = function(step,index) {
		if(step.operation && step.operation.name) {
			workflow[index].operation = step.operation.name;
		}
		return workflow[index].operation;
	};
	
	$scope.removeStep = function(index) {
		$scope.workflow.splice(index, 1);
	};

	$scope.addStep = function() {
		$scope.inserted = {
			relation: "R" + ($scope.workflow.length+1),
			input: "-",
			operation: null,
			options: "-",
			options2: "-",
			comment: "-",
			active: false
		};
		$scope.workflow.push($scope.inserted);
	};	
});

pigGeneApp.controller('WorkflowCtrl', function($scope) {
	$scope.steps = wiw;
});


/////////////////////////////////////////////////////////////////////////////////////
// test

pigGeneApp.controller('MyCtrl1', ['$scope', 'Workflow', function($scope, Workflow) {
		var res = Workflow.get({}, {'id':1});
		$scope.returnValue = res;
}]);

function ListController($scope) {
	$scope.messages = messages;
}

function DetailController($scope, $routeParams) {
	$scope.message = messages[$routeParams.id];
}