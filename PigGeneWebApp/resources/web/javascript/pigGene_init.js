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
 * The workflow variable is cleared and all the operation dialogs get set to their
 * initial behavior.
 */
function resetWorkflow() {
	workflow = [];
	inputCounter = 0;
	outputCounter = 0;
	resetTypeaheadRelations();
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
	$('#workflowName').addClass('new');
	clearCommentTextbox();
	hideLineDetailDialog();
	prepareContainers();
	resetWorkflow();
	initializeUsedRelations();
	showHelp();
	forceDownload = false;
	forceRun = false;
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


/**
 * Function is used to initialize the data loaded from the server.
 * @param data
 */
function initializeLoadedWorkflow(data) {
	description = data.description;
	workflow = data.workflow;
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