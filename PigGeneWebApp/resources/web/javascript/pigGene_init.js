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
	nameCounter = 1;
	resetWorkflowButtonsAndTableContent();
	setSaveStateSavedAndDisplayStatus();
	resetAllOperationDialogs();
	resetFormContainerOperation();
	resetFormContainerSize();
}


/**
 * Function is used to initialize a new workflow. Therefore the
 * workflow container, the form container and the operation
 * dialog get modified. Also the comment box gets cleared and
 * the line dialog gets hidden.
 */
function initializeNewWorkflow() {
	$('#workflowName').addClass('new');
	prepareContainers();
	resetFormContainerOperation();
	resetWorkflow();
	clearCommentTextbox();
	hideLineDetailDialog();
}


/**
 * Function is used to modify the size of the workflow container.
 * The form container gets displayed and the operations dialog
 * gets modified. The new save state gets set and displayed.
 */
function prepareContainers() {
	$('#workflowContainer').removeClass('span12').addClass('span10');
	$('#formContainer').removeClass('hide');
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
	resetAllOperationDialogs();
	setSaveStateSavedAndDisplayStatus();
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
 * Function is used to initialize the data loaded from the server and to
 * display the workflow in form of a table by calling a helper function.
 * @param data
 */
function initializeAndDisplayLoadedWorkflow(data) {
	description = data.description;
	workflow = data.workflow;
	nameCounter = workflow.length;
	displayTable();
}


/**
 * Function is used to display the save button, the download button
 * and the description button.
 */
function initializeButtons() {
	$('#downloadScript').removeClass('hide');
	$('#saveWfBtn').removeClass('hide');
	$('#descriptionBtn').removeClass('hide');
}
