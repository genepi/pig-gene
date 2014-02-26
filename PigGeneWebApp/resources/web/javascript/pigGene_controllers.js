var pigGeneApp = angular.module("pigGene",["xeditable"]);
pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3'; // bootstrap3 theme
});

pigGeneApp.controller("NavBarCtrl", function($scope) {
	$scope.buttons = buttons;
});

pigGeneApp.controller('EditableRowCtrl', function($scope, $filter, $http) {
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