/**
 * 
 * @author Clemens Banas
 * @date April 2013
 */


/**
 * Function is used to "save" the explanation, 
 * that is stored in the table container div initially.
 */
function preSaveStdContent() {
	stdContent = $('#tableContainer').html();
}


/**
 * Function is used to reset the workflow to the behavior it had in the beginning. 
 * The workflow variable is cleared and all the operation dialogs get set to their
 * initial behavior.
 */
function resetWorkflow() {
	workflow = [];
	resetWorkflowButtonsAndTableContent();
	setSaveStateSavedAndDisplayStatus();
	resetStandardBehaviorForAll();
	resetFormContainerOperation();
	resetFormContainerSize();
}




function setMissingFormValueText(textfield) {
    textfield.setCustomValidity('');
    if (!textfield.validity.valid) {
      textfield.setCustomValidity('this field cannot be left blank');  
    }
}

function initializeLoadedWorkflow(data) {
	description = data.description;
	workflow = data.workflow;
	nameCounter = workflow.length;
	displayTable();
}


function initializeButtons() {
	$('#downloadScript').removeClass('hide');
	$('#saveWfBtn').removeClass('hide');
	$('#descriptionBtn').removeClass('hide');
}

function prepareContainers() {
	$('#workflowContainer').removeClass('span12').addClass('span10');
	$('#formContainer').removeClass('hide');
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
	resetStandardBehaviorForAll();
	setSaveStateSavedAndDisplayStatus();
}

function initializeNewWorkflow() {
	$('#workflowName').addClass('new');
	prepareContainers();
	resetFormContainerOperation();
	resetWorkflow();
	clearCommentTextbox();
	hideLineDetailDialog();
}

function processSaveWfRequest() {
	var workflowName = $('#workflowName').html().trim();
	$('#saveDialogInput').val('');
	if('unnamed' != workflowName) {
		$('#saveDialogInput').val(workflowName);
	}
	showSaveNameModal();
}

