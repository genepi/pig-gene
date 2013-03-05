/**
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
 * dialog and to display the standard text "operations".
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
		hideScriptDialog();
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
function showSecurityAlertRemove() {
	$('#removeCheckModal').modal('show');
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
	$('#removeCheckModal').modal('hide');
	closePopovers();
	hideInputErrors();
	workflow.splice(highlightedRowIndex,1);
	if(workflow.length == 0) {
		resetDialogsAndHighlightings();
		resetWorkflow();
	} else {
		resetDialogsAndHighlightings();
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



function removeTableRowHighlighting(index) {
	$('#operationTable tbody tr:nth-child('+ index +')').removeClass('warning');
}

function addTableRowHighlighting(index) {
	$('#operationTable tbody tr:nth-child('+ index +')').addClass('warning');
}





function hideScriptDialog() {
	$('#scriptDialog').hide('slow');
	$('#scriptDialog').addClass('hide');
}



function hideTxtSeparatorOptions() {
	$('#loadFiletypeSeparator.btn-group').css('display','none');
}

function showTxtSeparatorOptions() {
	$('#loadFiletypeSeparator.btn-group').css('display','inline-block');
}

function highlightUpperRow() {
	if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
		removeTableRowLabeling('warning');
		$('#operationTable tbody tr:nth-child('+(highlightedRowIndex)+')').addClass('warning');
		highlightedRowIndex--;
		displayCorrespondingContainerInfo();
	}
}

function highlightLowerRow() {
	var rowCount = $('#operationTable tr').length;
	if (~highlightedRowIndex && highlightedRowIndex!=rowCount-2 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')')) {
		removeTableRowLabeling('warning');
		$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+2)+')').addClass('warning');
		highlightedRowIndex++;
		displayCorrespondingContainerInfo();
	}
}

///**
// * Hides all input-dialog-forms except the form given to the function.
// */
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

function hideInputErrors() {
	$('#inputError').hide();
}

function closePopovers() {
	$('#showWfBtn').popover('hide').removeClass('pop');
	$('#deleteWfBtn').popover('hide').removeClass('pop');
}









	function removeTableRowLabeling(label) {
		$('#tableContainer tr').each(function(){
			$(this).removeClass(label);
		});
	}


	



	
	
	function setStandardBehaviorSuccessModal() {
		$('#closeSuccModal').removeAttr('download').removeAttr('href');
		$('#successModal').modal('show');
	}
	
	function setModificationBehaviorSuccessModal(filename) {
		$('#closeSuccModal').attr('download', filename + '.pig');
		$('#closeSuccModal').attr('href', 'http://localhost:8080/dwld/' + filename);
		$('#successModal').modal('show');
	}
	


	function setModificationBehavior(operation) {
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
		$('#lineDetails').removeClass('hide');
		modifyContainerHeight();
	}
	
	function setCommentField(comment) {
		if(comment == '' || comment == '-') {
			$('#comments').val('');
		} else {
			$('#comments').val(comment);
		}
	}

	
	function displayCorrespondingContainerInfo() {
		var data = workflow[highlightedRowIndex];
		setCommentField(data.comment);

		if(data.operation=='REGISTER') {
			$('#regFileName').val(data.relation);
			hideScriptDialog();
			setModificationBehavior('register');
		} else if(data.operation=='LOAD') {
			$('#loadName').val(data.name);
			$('#fileName').val(data.relation);
			hideScriptDialog();
			setModificationBehavior('load');
		} else if(data.operation=='STORE'){
			$('#storeName').val(data.name);
			$('#relToStore').val(data.relation);
			hideScriptDialog();
			setModificationBehavior('store');
		} else if(data.operation=='FILTER') {
			$('#filtName').val(data.name);
			$('#filtRel').val(data.relation);
			$('#filtOpt').val(data.options);
			hideScriptDialog();
			setModificationBehavior('filter');
		} else if(data.operation=='JOIN') {
			$('#joinName').val(data.name);
			$('#joinRel').val(data.relation);
			$('#joinOpt').val(data.options);
			$('#joinRel2').val(data.relation2);
			$('#joinOpt2').val(data.options2);
			hideScriptDialog();
			setModificationBehavior('join');
		} else if(data.operation=='SCRIPT') {
			$('#scriptTextarea').val(data.options);
			setModificationBehavior('script');
			setFormContainerOperation('user defined script');
		}
	}

	
	
	

	
	function showInputErrorMsg(errText) {
		$('#inputErrMsg').html(errText);
		$('#inputError').show('slow');
	}

	function showSecurityAlert(filename) {
		$('#saveCheckModal').modal('show');
		$('#overrideBtn').on('click', function() {
			$('#saveCheckModal').modal('hide');
			saveWorkflow(filename);
		});
	}
	
	

	
	function showDiscardChangesAlert() {
		$('#discardChangesCheckModal').modal('show');
	}
	
	

	function hoverOverArrowAction(element) {
		$(element).toggleClass('icon-white');
	}
	
	function hideLineDetailDialog() {
		$('#lineDetails').addClass('hide');
	}
	
	function hideShowWfBtnPopover() {
		$('#showWfBtn').popover('hide').removeClass('pop').removeClass('showWfBtnPopover');
	}

	function hideDeleteWfBtnPopover() {
		$('#deleteWfBtn').popover('hide').removeClass('pop').removeClass('deleteWfBtnPopover');
	}
	
	function showExpandedDescriptionIcon() {
		$('#descriptionIcon').removeClass('icon-plus-sign').addClass('icon-minus-sign');
	}
	

	

	
	/**
	 * Calls a helper function to convert the workflow-object into a html table and displays the result.
	 */
	function displayTable() {
		var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
		$('#tableContainer').html(tab);
	}
	
	function resetStandardBehaviorForAll() {
		resetOperationDialog('register');
		resetOperationDialog('load');
		resetOperationDialog('store');
		resetOperationDialog('filter');
		resetOperationDialog('join');
		resetOperationDialog('script');
	}
	
	function showSaveNameModal() {
		$('#saveNameModal').modal('show');
	}
