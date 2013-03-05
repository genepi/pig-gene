/**
 * 
 * @author Clemens Banas
 * @date April 2013
 */

function preSaveStdContent() {
	stdContent = $('#tableContainer').html();
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

function resetWorkflow() {
	workflow = [];
	$('#saveWfBtn').addClass('hide');
	$('#downloadScript').addClass('hide');
	$('#workflowDescription').addClass('hide');
	$('#descriptionBtn').addClass('hide');
	description = '';
	$('#description').val('');
	$('#workflowName').html('workflow');
	$('#tableContainer').html(stdContent);
	setSaveStateSavedAndDisplayStatus();
	resetStandardBehaviorForAll();
	resetFormContainerOperation();
	$('#workflowContainer.well').css('min-height','193px');
}