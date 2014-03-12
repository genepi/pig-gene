/**
 * Initialization functions.
 * 
 * @author Clemens Banas
 * @date April 2013
 */


/**
 * Function is used to 'save' the explanation, 
 * that is stored in the table container div initially.
 */
function preSaveStdContent() {
	stdContent = $('#tableContainer').html();
}


/**
 * Function to set an English error message if the user does 
 * not specify a value in a form field that must not be blank.
 */
function setMissingFormValueText(textfield) {
    textfield.setCustomValidity('');
    if (!textfield.validity.valid) {
      textfield.setCustomValidity('this field cannot be left blank');  
    }
}


/**
 * Function is used to reset the workflow to the behavior it had in the beginning. 
 * The workflow variable is cleared, the componentLineCounter variable is resetted, 
 * and all the operation dialogs get set to their initial behavior.
 */
function resetWorkflow() {
	workflow = [];
	componentLineCounter = 0;
//	resetTypeaheadRelations();
	resetWorkflowButtonsAndTableContent();
	setSaveStateSavedAndDisplayStatus();
	resetAllOperationDialogs();
	resetFormContainerOperation();
	resetFormContainerSize();
	modifyContainerHeight();
	forceDownload = false;
	forceRun = false;
}


/**
 * Function is used to initialize a new workflow. Therefore the
 * workflow container and the operation dialog get modified. Also 
 * the comment box gets cleared and the line dialog gets hidden. 
 * Also the used relations get reseted.
 */
function initializeNewWorkflow() {
	//TODO
//	$('#workflowName').addClass('new');
	clearCommentTextbox();
	hideLineDetailDialog();
	prepareContainers();
	resetWorkflow();
	showHelp();
	forceDownload = false;
	forceRun = false;
	//TODO
	$('#workflowContainer').html('');
}


/**
 * Function is used to modify the size of the workflow container
 * and the form container gets displayed.
 */
function prepareContainers() {
	$('#workflowContainer').removeClass('span12').addClass('span10');
	$('#formContainer').removeClass('hide');
}


/**
 * Function is used to retrieve the current workflow name and to
 * display the save modal dialog. If workflow has been saved before
 * then the name is set into the save dialog.
 */
function processSaveWfRequest() {
	var workflowName = $('#workflowName').html().trim();
	$('#saveDialogInput').val('');
	if('unnamed' != workflowName) {
		$('#saveDialogInput').val(workflowName);
	}
	showSaveNameModal();
}

//TODO
//function processSaveWfCompRequest() {
//	hideSaveOptionModal();
//}


//TODO ... description
/**
 * Function is used to initialize the data loaded from the server and to
 * reset the componentLineCounter variable to it's initial value.
 * @param data
 */
function initializeLoadedWorkflow(data) {
	//TODO aufrauemen
	//componentLineCounter = 1; nur wenn auf new gecklickt wird
	//	resetDescription();
	if(data.workflows.length > 1) {
		workflowName = data.name;
		description = data.description;
	}
	var workflowInformation = data.workflows;
	
	for(var i=0; i<workflowInformation.length; i++) {
		workflow[componentLineCounter] = {name:workflowInformation[i].name, description:workflowInformation[i].description, ref:true, data:workflowInformation[i].workflow};
		workflowDetailDisplay[componentLineCounter] = false;
		workflowDescDisplay[componentLineCounter] = false;
		componentLineCounter++;	
	}
}

//TODO
function addWorkflowComponent(component) {
	if(workflow.length == 0) {
		$('#workflowName').html('unnamed');
	}
	workflow = workflow.concat(renameComponentRelationNames(component));
}

//TODO
function renameComponentRelationNames(component) {
	if(component != null || component != undefined || component.lenght != 0) {
		//changed used relation names within operations
		for(var i=component.length-1; i>=0; i--) {
			for(var j=i; j>=0; j--) {
				var in1changed = false;
				var in2changed = false;
				if(!in1changed && component[i].input == component[j].relation) { 
					component[i].input = calculatedNewComponentRelationName(j);
					in1changed = true;
				} else if(!in2changed && component[i].input2 == component[j].relation) { //join
					component[i].input2 = calculatedNewComponentRelationName(j);
					in2changed = true;
				}
			}
		}
		
		//change operation result name
		for(var k=0; k<component.length; k++) {
			if(component[k].operation == 'STORE') {
				component[k].relation = generateOutputName('-',countNumberOfComponentStoreOperations(component, k));
				componentLineCounter++;
			} else {
				component[k].relation = 'C' + componentLineCounter++;
			} 
		}
	}
	return component;
}

//TODO
function calculatedNewComponentRelationName(lineNo) {
	return 'C' + (componentLineCounter+lineNo);
}

//TODO
function countNumberOfComponentStoreOperations(component, endIndex) {
	var number = 0;
	for(var i=0; i<endIndex; i++) {
		if(component[i].operation == 'STORE') {
			number++;
		}
	}
	return number;
}

/**
 * Function is used to display the delete button, the save button, the download button
 * and the description button.
 */
function initializeButtons() {
	$('#deleteWfBtn').removeClass('hide');
	$('#downloadScript').removeClass('hide');
	$('#runJob').removeClass('hide');
	$('#saveWfBtn').removeClass('hide');
	$('#descriptionBtn').removeClass('hide');
}