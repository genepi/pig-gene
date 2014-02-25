function NavBarController($scope) {
	$scope.buttons = [
	   {name:"newWfBtn", title:"create new workflow", showState:true, text:"new"},
	   {name:"showWfBtn", title:"open existing workflow", showState:true, text:"open"},
	   {name:"saveWfBtn", title:"save workflow", showState:true, text:"save"},
	   {name:"deleteWfBtn", title:"delete workflow", showState:true, text:"delete"},
	   {name:"downloadScriptBtn", title:"download pig script", showState:true, text:"download"},
	   {name:"runJobBtn", title:"run workflow on cluster", showState:true, text:"run"}
	];
}


var workflow = [
                {relation:"R1", input:"input1", input2:"-", operation:"LOAD", options:"vcf", options2:"-", comment:"Loads the input file."},
                {relation:"R2", input:"R1", input2:"-", operation:"FILTER", options:"pos==138004", options2:"-", comment:"Filters all lines that match position '138004'."},
                {relation:"R3", input:"R2", input2:"", operation:"STORE", options:"-", options2:"-", comment:"-"}          
             ];


function WorkflowController($scope) {
	$scope.workflow = workflow;
} 


function SettingsController($scope) {
	
}