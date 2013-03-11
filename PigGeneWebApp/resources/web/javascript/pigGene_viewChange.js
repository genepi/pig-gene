/**
 * View changes of the dialogs, buttons and workflow representation.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

/**
 * Function is responsible to hide the operations selection 
 * dialog and to display the currently operation name.
 */
function setFormContainerOperation(operation) {
	$('#stepAction').addClass('hide');
	$('#workflowOps').html(operation.toUpperCase());
}


/**
 * Function is responsible to show the operations selection 
 * dialog and to display the standard text 'operations'.
 */
function resetFormContainerOperation() {
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
}


/**
 * Function resets all the operation-dialogs to their standard 
 * behavior and removes the yellow highlighting of the selected 
 * table row.
 */
function resetDialogsAndHighlightings() {
	resetAllOperationDialogs();
	resetFormContainerOperation();
	removeTableRowHighlighting(highlightedRowIndex+1);
	hideLineDetailDialog();
	modifyContainerHeight();
}


/**
 * Function calls helper function to reset all the different 
 * operation dialogs to their standard behavior.
 */
function resetAllOperationDialogs() {
	resetOperationDialog('register');
	resetOperationDialog('load');
	resetOperationDialog('store');
	resetOperationDialog('filter');
	resetOperationDialog('join');
	resetOperationDialog('script');
}


/**
 * Function is used to set the modification behavior of the operation
 * dialog and all its containing elements; like buttons. All other
 * operation dialogs except the needed one get hidden. The operation
 * name gets set as headline and the line details dialog gets displayed.
 * At the end the container height gets modified.
 * @param operation
 */
function setOperationDialog(operation) {
	var submitBtn = '#' + operation + 'Submit';
	var submitChangeBtn = '#' + operation + 'SubmitChange';
	var deleteBtn = '#' + operation + 'Delete';
	var dialog = '#' + operation + 'Dialog';
	
	$(submitBtn).addClass('hide');
	$(submitChangeBtn).addClass('modification');
	$(submitChangeBtn).removeClass('hide');
	$(deleteBtn).removeClass('hide');
	$(dialog).show('slow');
	$(dialog).removeClass('hide');
	hideNotSpecifiedInputDialogs(operation);
	setFormContainerOperation(operation);
	showLineDetailDialog();
	modifyContainerHeight();
}


/**
 * Function is used to reset a operation dialog and all its containing 
 * elements; like buttons and input fields. 
 * Special handling of the load and the user script operation dialog.
 * @param operation
 */
function resetOperationDialog(operation) {
	var submitBtn = '#' + operation + 'Submit';
	var submitChangeBtn = '#' + operation + 'SubmitChange';
	var deleteBtn = '#' + operation + 'Delete';
	var dialog = '#' + operation + 'Dialog';
	
	$(submitChangeBtn).addClass('hide');
	$(submitBtn).removeClass('hide');
	$(submitChangeBtn).removeClass('modification');
	$(deleteBtn).addClass('hide');
	$(dialog).children('input[type=text]').val('');
	$(dialog).hide('slow');
	$(dialog).addClass('hide');
	
	if(operation == 'load') {
		$('#loadFiletypeSeparator.btn-group').css('display','none');
		$('#loadVcf').addClass('active');
		$('#loadTxt').removeClass('active');
	}
	if(operation == 'script') {
		hideScriptDialogSlow();
	}
}


/**
 * Function displays the given input dialog.
 * @param dialog
 */
function showInputDialogSlow(dialog) {
	$(dialog).show('slow');
}


/**
 * Function displays the script dialog.
 */
function showScriptDialogSlow() {
	$('#scriptDialog').show('slow');
	$('#scriptDialog').removeClass('hide');
}


/**
 * Function hides the script dialog.
 */
function hideScriptDialogSlow() {
	$('#scriptDialog').hide('slow');
	$('#scriptDialog').addClass('hide');
}


/**
 * Function displays a security alert to warn the user 
 * that he is going to delete a line of the workflow.
 */
function showSecurityAlertRemoveLine() {
	$('#removeLineCheckModal').modal('show');
}


/**
 * Function checks which button was clicked and resets the corresponding 
 * operation dialog depending on that information. It hides all errors  
 * and additional information dialogs and basically resets the operation 
 * dialog to its default behavior.
 * @param clicked button
 */
function finishReset(button) {
	button.removeClass('modification');
	var buttonName = $(button).attr('id');
	
	if(buttonName.indexOf('register') == 0) {
		resetOperationDialog('register');
	} else if(buttonName.indexOf('load') == 0) {
		resetOperationDialog('load');
	} else if (buttonName.indexOf('store') == 0) {
		resetOperationDialog('store');
	} else if (buttonName.indexOf('filter') == 0) {
		resetOperationDialog('filter');
	} else if (buttonName.indexOf('join') == 0) {
		resetOperationDialog('join');
	} else if (buttonName.indexOf('script') == 0) {
		resetOperationDialog('script');
	}
	clearCommentTextbox();
	resetFormContainerOperation();
	removeTableRowLabeling('warning');
	hideLineDetailDialog();
	hideInputErrors();
	modifyContainerHeight();
}


/**
 * Function is used to clear the comment textarea.
 */
function clearCommentTextbox() {
	$('#comments').val('');
}


/**
 * Function is used to delete a table row, reset the dialogs and 
 * removes the highlighting of that particular table row.
 */
function deleteRowAndDisplayTable() {
	$('#removeLineCheckModal').modal('hide');
	closePopovers();
	hideInputErrors();
	workflow.splice(highlightedRowIndex,1);
	if(workflow.length == 0) {
		resetDialogsAndHighlightings();
		resetWorkflow();
	} else {
		resetDialogsAndHighlightings();
		setSaveStateUnsavedAndDisplayStatus();
		displayTable();
	}
}


/**
 * Function is used to reset the workflow buttons to their initial state.
 * The description of the workflow gets cleared and the table container gets
 * filled with the initial content.
 */
function resetWorkflowButtonsAndTableContent() {
	$('#saveWfBtn').addClass('hide');
	$('#downloadScript').addClass('hide');
	$('#workflowDescription').addClass('hide');
	$('#descriptionBtn').addClass('hide');
	description = '';
	$('#description').val('');
	setWorkflowName('workflow');
	$('#tableContainer').html(stdContent);
}


/**
 * Function is used to set the given name as the new workflow name, 
 * displayed in the container above the workflow table.
 * @param workflow name to set
 */
function setWorkflowName(name) {
	$('#workflowName').html(name);
}


/**
 * Function resets the heigth of the form container to the initial value.
 */
function resetFormContainerSize() {
	$('#workflowContainer.well').css('min-height','193px');
}


/**
 * Function is used to show/hide the *-symbol after the workflow name. 
 * The symbol is an indication of the state of the workflow - i.e.: saved or not.
 */
function toggleSaveStateVisualisation() {
	if($('#saveState').hasClass('saved')) {
		$('#saveState').html('');
	} else {
		$('#saveState').html(' *');
	}
}


/**
 * Function is used to hide the description of the workflow and to display the plus
 * icon instead of the minus icon to indicate that the description is collapsed.
 */
function descriptionButtonHandling() {
	$('#workflowDescription').addClass('hide');
	$('#descriptionIcon').removeClass('icon-minus-sign').addClass('icon-plus-sign');
}


/**
 * Function is used to give a visual hint that the comment was saved.
 */
function blinkEffectComments() {
	setTimeout(function() {
		$('#comments').focus();
	}, 150);
	setTimeout(function() {
		$('#comments').blur();
	}, 500);
}


/**
 * Function is used to remove the table row highlighting of the current line and to add highlighting 
 * to the upper table row. The operation container on the right side gets updated too.
 */
function highlightUpperRow() {
	if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
		removeTableRowLabeling('warning');
		addTableRowHighlighting(highlightedRowIndex);
		highlightedRowIndex--;
		displayCorrespondingContainerInfo();
	}
}


/**
 * Function is used to remove the table row highlighting of the current line and to add highlighting
 * to the lower table row. The operation container on the right side gets updated too.
 */
function highlightLowerRow() {
	var rowCount = $('#operationTable tr').length;
	if (~highlightedRowIndex && highlightedRowIndex!=rowCount-2 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')')) {
		removeTableRowLabeling('warning');
		addTableRowHighlighting(highlightedRowIndex+2);
		highlightedRowIndex++;
		displayCorrespondingContainerInfo();
	}
}


/**
 * Function is used to remove the given label from all table rows in the tableContainer div.
 * @param label to remove
 */
function removeTableRowLabeling(label) {
	$('#tableContainer tr').each(function(){
		$(this).removeClass(label);
	});
}


/**
 * Function is used to retrieve the data saved in the global workflow-array. Depending on the operation
 * of the currently highlighted row the data is set into the corresponding fields of the operation dialog
 * on the right side of the webpage. 
 */
function displayCorrespondingContainerInfo() {
	var data = workflow[highlightedRowIndex];
	setCommentField(data.comment);

	if(data.operation=='REGISTER') {
		$('#regFileName').val(data.relation);
		hideScriptDialogSlow();
		setOperationDialog('register');
	} else if(data.operation=='LOAD') {
		$('#loadName').val(data.name);
		$('#fileName').val(data.relation);
		hideScriptDialogSlow();
		setOperationDialog('load');
	} else if(data.operation=='STORE'){
		$('#storeName').val(data.name);
		$('#relToStore').val(data.relation);
		hideScriptDialogSlow();
		setOperationDialog('store');
	} else if(data.operation=='FILTER') {
		$('#filtName').val(data.name);
		$('#filtRel').val(data.relation);
		$('#filtOpt').val(data.options);
		hideScriptDialogSlow();
		setOperationDialog('filter');
	} else if(data.operation=='JOIN') {
		$('#joinName').val(data.name);
		$('#joinRel').val(data.relation);
		$('#joinOpt').val(data.options);
		$('#joinRel2').val(data.relation2);
		$('#joinOpt2').val(data.options2);
		hideScriptDialogSlow();
		setOperationDialog('join');
	} else if(data.operation=='SCRIPT') {
		$('#scriptTextarea').val(data.options);
		setOperationDialog('script');
		setFormContainerOperation('user defined script');
	}
}


/**
 * Function is used to set the given value in the comment field.
 * @param comment
 */
function setCommentField(comment) {
	if(comment == '' || comment == '-') {
		$('#comments').val('');
	} else {
		$('#comments').val(comment);
	}
}


/**
 * Hides all input-dialog-forms except the dialog given to the function.
 * @param operation dialog
 */
function hideNotSpecifiedInputDialogs(dialog) {
	if(dialog != 'register') {
		$('#registerDialog').hide('slow');
	}
	if(dialog != 'load') {
		$('#loadDialog').hide('slow');
	}
	if(dialog != 'store') {
		$('#storeDialog').hide('slow');
	}
	if(dialog != 'filter') {
		$('#filterDialog').hide('slow');
	}
	if(dialog != 'join') {
		$('#joinDialog').hide('slow');
	}
	if(dialog != 'script') {
		$('#scriptDialog').hide('slow');
	}
}


/**
 * Function is used to display the discard changes check modal.
 */
function showDiscardChangesAlert() {
	$('#discardChangesCheckModal').modal('show');
}


/**
 * Function is used to show a security alert to check if the user
 * really wants to override a workflow with the given name.
 * @param fileName
 */
function showSecurityAlert(fileName) {
	$('#overrideFilename').html(fileName);
	$('#saveCheckModal').modal('show');
	
}


/**
 * Function displays a security alert to avoid unwanted data loss.
 */
function showSecurityAlertRemoveWorkflow(fileName) {
	$('#deleteWfName').html(fileName);
	$('#removeWorkflowCheckModal').modal('show');
}


/**
 * Function is used to convert the okay-button in the success modal into a link
 * that directly downloads the server created pigscript when clicked by the user.
 * @param filename
 */
function setModificationBehaviorSuccessModal(filename) {
	$('#closeSuccModal').attr('download', filename + '.pig');
	$('#closeSuccModal').attr('href', 'http://localhost:8080/dwld/' + filename);
	$('#successModal').modal('show');
}


/**
 * Function is used to convert the okay-button into
 * a 'normal' link without download functionality.
 */
function setStandardBehaviorSuccessModal() {
	$('#closeSuccModal').removeAttr('download').removeAttr('href');
	$('#successModal').modal('show');
}


/**
 * Function is used to change the description icon 
 * from plus to minus to indicate the expanded view.
 */
function showExpandedDescriptionIcon() {
	$('#descriptionIcon').removeClass('icon-plus-sign').addClass('icon-minus-sign');
}


/**
 * Function is used to change the color of the given element
 * from black to white to simulate a hover effect.
 * @param element
 */
function hoverOverArrowAction(element) {
	$(element).toggleClass('icon-grey');
}


/**
 * Function is used to hide the additional text separator options
 * in the operations dialog when a load operation is selected.
 */
function hideTxtSeparatorOptions() {
	$('#loadFiletypeSeparator.btn-group').css('display','none');
}


/**
 * Function is used to show the additional text separator options
 * in the operations dialog when a load operation is selected.
 */
function showTxtSeparatorOptions() {
	$('#loadFiletypeSeparator.btn-group').css('display','inline-block');
}


/**
 * Function is used to show the line details dialog.
 */
function showLineDetailDialog() {
	$('#lineDetails').removeClass('hide');
}


/**
 * Function is used to hide the line details dialog
 */
function hideLineDetailDialog() {
	$('#lineDetails').addClass('hide');
}


/**
 * Function is used to hide the popover that displays the existing workflows (for loading).
 */
function hideShowWfBtnPopover() {
	$('#showWfBtn').popover('hide').removeClass('pop').removeClass('showWfBtnPopover');
}


/**
 * Function is used to hide the popover that displays the existing workflows (for deletion).
 */
function hideDeleteWfBtnPopover() {
	$('#deleteWfBtn').popover('hide').removeClass('pop').removeClass('deleteWfBtnPopover');
}


/**
 * Function is used to remove the table row highlighting in the workflow table
 * at the line of the given index.
 * @param index
 */
function removeTableRowHighlighting(index) {
	$('#operationTable tbody tr:nth-child('+ index +')').removeClass('warning');
}

/**
 * Function is used to add a table row highlighting in the workflow table 
 * at the line of the given index.
 * @param index
 */
function addTableRowHighlighting(index) {
	$('#operationTable tbody tr:nth-child('+ index +')').addClass('warning');
}


/**
 * Function is used to hide the input error dialog.
 */
function hideInputErrors() {
	$('#inputError').hide();
}


/**
 * Function is used to close all popovers.
 */
function closePopovers() {
	$('#showWfBtn').popover('hide').removeClass('pop');
	$('#deleteWfBtn').popover('hide').removeClass('pop');
}


/**
 * Function is used to show the input error dialog.
 * @param errText
 */
function showInputErrorMsg(errText) {
	$('#inputErrMsg').html(errText);
	$('#inputError').show('slow');
}


/**
 * Calls a helper function to convert the workflow-object into a html table and displays the result.
 */
function displayTable() {
	var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
	$('#tableContainer').html(tab);
}


/**
 * Function is used to show the save name modal dialog.
 */
function showSaveNameModal() {
	$('#saveNameModal').modal('show');
}
