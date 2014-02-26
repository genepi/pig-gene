var pigGeneApp = angular.module("pigGene",["xeditable"]);
pigGeneApp.run(function(editableOptions) {
	  editableOptions.theme = 'bs3'; // bootstrap3 theme
});


pigGeneApp.controller("NavBarCtrl", function($scope) {
	$scope.buttons = [
	   {name:"newWfBtn", title:"create new workflow", showState:true, text:"new"},
	   {name:"showWfBtn", title:"open existing workflow", showState:true, text:"open"},
	   {name:"saveWfBtn", title:"save workflow", showState:true, text:"save"},
	   {name:"deleteWfBtn", title:"delete workflow", showState:true, text:"delete"},
	   {name:"downloadScriptBtn", title:"download pig script", showState:true, text:"download"},
	   {name:"runJobBtn", title:"run workflow on cluster", showState:true, text:"run"}
	];
});



pigGeneApp.controller('EditableRowCtrl', function($scope, $filter, $http) {
	$scope.workflow = workflow;

	$scope.operations = [
		{name: 'REGISTER'},
		{name: 'LOAD'},
		{name: 'FILTER'},
		{name: 'JOIN'},
		{name: 'SELECT'},
		{name: 'GROUP BY'},
		{name: 'ORDER BY'},
		{name: 'USER SCRIPT'},
		{name: 'STORE'}
	]; 

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