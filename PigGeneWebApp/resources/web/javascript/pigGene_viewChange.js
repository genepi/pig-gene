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
	resetOperationDialog('projection');
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
		resetLoadSpecifier();
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
	if(buttonName != undefined) {
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
		} else if (buttonName.indexOf('projection') == 0) {
			resetOperationDialog('projection');
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
	var operation = workflow[highlightedRowIndex].operation;
	deleteTypeaheadRelationByOperation(operation);
	if(operation == 'LOAD') {
		inputCounter--;
	} else if(operation == 'STORE') {
		outputCounter--;
	}
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
	$('#deleteWfBtn').addClass('hide');
	$('#downloadScript').addClass('hide');
	$('#runJob').addClass('hide');
	$('#workflowDescription').addClass('hide');
	$('#descriptionBtn').addClass('hide');
	description = '';
	$('#description').val('');
	setWorkflowName('PigGene - graphical pig script generator');
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
		$('#regFileName').val(data.input);
		hideScriptDialogSlow();
		setOperationDialog('register');
	} else if(data.operation=='LOAD') {
		$('#loadName').val(data.relation);
		if(data.options == 'vcf') {
			$('#loadVcf').addClass('active');
			$('#loadTxt').removeClass('active');
			if(data.options2 == 'ref') {
				$('#refFileBtn').addClass('active');
				$('#stdFileBtn').removeClass('active');
			} else {
				$('#refFileBtn').removeClass('active');
				$('#stdFileBtn').addClass('active');
			}
			hideTxtSeparatorOptions();
			$('#tabSeparator').addClass('active');
			$('#spaceSeparator').removeClass('active');
			$('#commaSeparator').removeClass('active');
		} else {
			$('#loadVcf').removeClass('active');
			$('#refFileBtn').removeClass('active');
			$('#stdFileBtn').addClass('active');
			$('#loadTxt').addClass('active');
			if(data.options2 == 'tab') {
				$('#tabSeparator').addClass('active');
				$('#spaceSeparator').removeClass('active');
				$('#commaSeparator').removeClass('active');
			} else if(data.options2 == 'space') {
				$('#spaceSeparator').addClass('active');
				$('#tabSeparator').removeClass('active');
				$('#commaSeparator').removeClass('active');
			} else {
				$('#commaSeparator').addClass('active');
				$('#tabSeparator').removeClass('active');
				$('#spaceSeparator').removeClass('active');
			}
			showTxtSeparatorOptions();
		}
		hideScriptDialogSlow();
		setOperationDialog('load');
	} else if(data.operation=='STORE'){
		$('#relToStore').val(data.input);
		hideScriptDialogSlow();
		setOperationDialog('store');
	} else if(data.operation=='FILTER') {
		$('#filtName').val(data.relation);
		$('#filtRel').val(data.input);
		$('#filtOpt').val(data.options);
		hideScriptDialogSlow();
		setOperationDialog('filter');
	} else if(data.operation=='JOIN') {
		$('#joinName').val(data.relation);
		$('#joinRel').val(data.input);
		$('#joinOpt').val(data.options);
		$('#joinRel2').val(data.input2);
		$('#joinOpt2').val(data.options2);
		hideScriptDialogSlow();
		setOperationDialog('join');
	} else if(data.operation =='PROJECTION') {
		$('#projectionName').val(data.relation);
		$('#projectionRel').val(data.input);
		$('#projectionOpt').val(data.options);
		hideScriptDialogSlow();
		setOperationDialog('projection');
	} else if(data.operation=='SCRIPT') {
		$('#scriptTextarea').val(data.options);
		setOperationDialog('script');
		setFormContainerOperation('user defined script');
	}
	
	if($('#helpBtn').hasClass('btn-info')) {
		if(data.operation != 'SCRIPT') {
			setOperationRelatedHelpContent(data.operation.toLowerCase());
		} else {
			setOperationRelatedHelpContent('user defined script');
		}
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
	if(dialog != 'projection') {
		$('#projectionDialog').hide('slow');
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
 * Additionally the option to select a reference file gets shown.
 */
function hideTxtSeparatorOptions() {
	$('#loadFiletypeSeparator.btn-group').css('display','none');
	$('#referenceFileOption.btn-group').css('display','inline-block');
}


/**
 * Function is used to show the additional text separator options
 * in the operations dialog when a load operation is selected.
 * Additionally the option to select a reference file gets hidden.
 */
function showTxtSeparatorOptions() {
	$('#loadFiletypeSeparator.btn-group').css('display','inline-block');
	$('#referenceFileOption.btn-group').css('display','none');
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
 * Function is used to open up a dialog, when
 * the user clicks on the download script button
 * but the workflow definition was not saved yet.
 */
function openUnsavedModal() {
	$('#unsavedModal').modal('show');
}


/**
 * Function is used to close the 
 * download unsaved modal dialog.
 */
function closeUnsavedModal() {
	$('#unsavedModal').modal('hide');
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


/**
 * Function is used to implement a toggle functionality
 * for the help button in the formContainer.
 */
function toggleHelpBtn() {
	if($('#helpBtn').hasClass('btn-info')) {
		$('#helpBtn').removeClass('btn-info');
		$('#helpBtnIcon').removeClass('icon-white');
		$('#explanationDialog').hide('slow');
	} else {
		showHelp();
	}
}


/**
 * Function is used to display the explanation when 
 * the help button is activated.
 */
function showHelp() {
	setHelpDialogContent('A standard pig script consists of different operations representing a data flow. Please have a look at the existing exemplary workflows by pressing the "open" button.');
	$('#helpBtn').addClass('btn-info');
	$('#helpBtnIcon').addClass('icon-white');
	$('#explanationDialog').show('slow');
}


/**
 * Function is used to set the operation dependend helper text.
 * @param operation name
 */
function setOperationRelatedHelpContent(operation) {
	var body = '';
	if(operation == 'register') {
		body = 'Apache Pig loads all necessary operations from existing jar files. The register field is an optional field that is only needed when you want to use non-standard operations.';
	} else if(operation == 'load') {
		body = 'This is the main entry point for Apache Pig scripts. Specify a name to reference the operation within the workflow and the folder or file name to analyze.';
	} else if(operation == 'filter') {
		body = 'Different filters can be applied to loaded data. Specify a name to reference the operation within the workflow, the relation to filter and the desired filter options. All possible Pig filter options are allowed.';
	} else if(operation == 'join') {
		body = 'A join is an operation to combine two relations. Specify a name to reference the operation within the workflow and the relations and columns you want to match. Multiple columns per relation are separated by comma.';
	} else if(operation == 'projection') {
		body = 'This operation helps to remove not needed columns from a relation. Specify the names of the columns you want to remove and seperate multiple names by comma. If you don\'t know the name of the columns you can use "$number" (number starting from zero) to select the column you want to remove.';
	} else if(operation == 'user defined script') {
		body = 'User Defined Scripts allow it to implement advanced operations, which are defined in optional files. All operations (REGISTER, LOAD, STORE, FILTER, JOIN) can be used.';
	} else if(operation == 'store') {
		body = 'Relations containing the results of a workflow can be saved to a specified file.';
	}
	setHelpDialogContent(body);
}


/**
 * Function is used to set the body text content
 * of the help dialog, that shows up on user request.
 * @param body text
 */
function setHelpDialogContent(body) {
	$('#explanationMsg').html(body);
}


/**
 * Function is used to toggle the display 
 * of the site notice popover.
 */
function toggleSiteNoticePopover() {
	if($('#logoContainer').hasClass('pop')) {
		$('#logoContainer').popover('hide').removeClass('pop');
	} else {
		var popContent = 'author: <a href="mailto:clemens.banas@student.uibk.ac.at?subject=pigGene%20project">Clemens Banas</a><br> created: April, 2013';
		$('#logoContainer').attr('data-content', popContent).popover('show').addClass('pop');
	}
}


/**
 * Function is used to set an artificially constructed relation name 
 * into the input field of the corresponding operation.
 * @param operation
 */
function setArtificialRelationName(operation) {
	if (operation == 'load') {
		$('#loadName').val(getArtificialName());
	} else if (operation == 'filter') {
		$('#filtName').val(getArtificialName());
	} else if (operation == 'join') {
		$('#joinName').val(getArtificialName());
	} else if (operation == 'projection') {
		$('#projectionName').val(getArtificialName());
	}
	//else: ignore
}


/**
 * Funktion is used to show the problem modal.
 */
function showWorkflowProblemModal() {
	$('#wfProblemHeader').html('Problems in the workflow definition!');
	$('#wfProblemBody').html('Your workflow definition contains problems regarding the use of relation names. Those are higlighted red.<b> Do you really want to ignore the problems?</b>');
	$('#workflowProblemsModal').modal('show');
}


/**
 * Resets the standard options after each load operation.  
 */
function resetLoadSpecifier() {
	$('#loadFiletypeSeparator.btn-group').css('display','none');
	$('#referenceFileOption.btn-group').css('display','inline-block');
	$('#loadVcf').addClass('active');
	$('#loadTxt').removeClass('active');
	$('#tabSeparator').addClass('active');
	$('#spaceSeparator').removeClass('active');
	$('#commaSeparator').removeClass('active');
	$('#stdFileBtn').addClass('active');
	$('#refFileBtn').removeClass('active');
}