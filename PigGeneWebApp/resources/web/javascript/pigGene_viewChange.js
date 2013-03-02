/**
 * 
 * @author Clemens Banas
 * @date April 2013
 */


function removeTableRowHighlighting(index) {
	$('#operationTable tbody tr:nth-child('+ index +')').removeClass('warning');
}

function addTableRowHighlighting(index) {
	$('#operationTable tbody tr:nth-child('+ index +')').addClass('warning');
}


function setFormContainerOperation(operation) {
	$('#stepAction').addClass('hide');
	$('#workflowOps').html(operation.toUpperCase());
}

function resetFormContainerOperation() {
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
}

function resetDialogsAndHighlightings() {
	resetAllOperationDialogs();
	$('#lineDetails').addClass('hide');
	hideNotSpecifiedInputDialogs('');
	hideInputErrors();
	closePopovers();
	if(~highlightedRowIndex) {
		removeTableRowHighlighting(highlightedRowIndex+1);
	}
}

function resetAllOperationDialogs() {
	resetOperationDialog('register');
	resetOperationDialog('load');
	resetOperationDialog('store');
	resetOperationDialog('filter');
	resetOperationDialog('join');
	resetOperationDialog('script');
}


///**
// * Removes the highlighting from the selected table row and changes the visibility of the
// * "standard" submit and the "modification" submit buttons. Also hides all input dialogs.
// */
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

function hideScriptDialog() {
	$('#scriptDialog').hide('slow');
	$('#scriptDialog').addClass('hide');
}

function showInputDialogSlow(dialog) {
	$(dialog).show('slow');
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







function descriptionButtonHandling() {
	$('#workflowDescription').addClass('hide');
	$('#descriptionIcon').removeClass('icon-minus-sign').addClass('icon-plus-sign');
}

	function removeTableRowLabeling(label) {
		$('#tableContainer tr').each(function(){
			$(this).removeClass(label);
		});
	}

	function hideScriptDialog() {
		$('#scriptDialog').addClass('hide');
	}
	
	function showScriptDialog() {
		$('#scriptDialog').removeClass('hide');
	}



	function blinkEffectComments() {
		setTimeout(function() {
			$('#comments').focus();
		}, 150);
		setTimeout(function() {
			$('#comments').blur();
		}, 500);
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

	
	function toggleSaveStateVisualisation() {
		if($('#saveState').hasClass('saved')) {
			$('#saveState').html('');
		} else {
			$('#saveState').html(' *');
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
	
	
	function showSecurityAlertRemove(obj) {
		$('#removeCheckModal').modal('show');
	}
	
	function showDiscardChangesAlert() {
		$('#discardChangesCheckModal').modal('show');
	}
	
	function deleteRowAndDisplayTable() {
		$('#removeCheckModal').modal('hide');
		$('#inputError').hide();
		workflow.splice(highlightedRowIndex,1);
		if(workflow.length == 0) {
			resetDialogsAndHighlightings();
			resetWorkflow();
		} else {
			resetDialogsAndHighlightings();
			displayTable();
		}
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
	
	function finishReset(obj) {
		var buttonName = $(obj).attr('id');
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
		hideLineDetailDialog();
		hideInputErrors();
		removeTableRowLabeling('warning');
		resetFormContainerOperation();
		modifyContainerHeight();
	}
	
	function clearCommentTextbox() {
		$('#comments').val('');
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
