function descriptionButtonsHandling() {
		$('#workflowDescription').addClass('hide');
		$('#descriptionIcon').removeClass('icon-minus-sign').addClass('icon-plus-sign');
	}

	function removeTableRowLabeling(label) {
		$('#tableContainer tr').each(function(){
			$(this).removeClass(label);
		});
	}

	function hideScriptContainer() {
		$('#scriptDialog').addClass('hide');
	}

	/**
	 * Hides all input-dialog-forms except the form given to the function.
	 */
	function hideInputDialogs(elem) {
		if(elem != 'register') {
			$('#registerDialog').hide('slow');
		}
		if(elem != 'load') {
			$('#loadDialog').hide('slow');
		}
		if(elem != 'store') {
			$('#storeDialog').hide('slow');
		}
		if(elem != 'filter') {
			$('#filterDialog').hide('slow');
		}
		if(elem != 'join') {
			$('#joinDialog').hide('slow');
		}
		if(elem != 'script') {
			$('#scriptDialog').hide('slow');
		}
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
	
	function modifyDialog(operation) {
		var obj = '#' + operation + 'Dialog';
		resetStandardBehavior(operation);
		$(obj).children('input[type=text]').val('');
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
		hideInputDialogs(operation);
		setCurrentOperation(operation.toUpperCase());
		$('#lineDetails').removeClass('hide');
		modifyContainerHeight();
	}
	
	function resetStandardBehaviorForAll() {
		resetStandardBehavior('register');
		resetStandardBehavior('load');
		resetStandardBehavior('store');
		resetStandardBehavior('filter');
		resetStandardBehavior('join');
		resetStandardBehavior('script');
	}
	
	/**
	 * Removes the highlighting from the selected table row and changes the visibility of the
	 * "standard" submit and the "modification" submit buttons. Also hides all input dialogs.
	 */
	function resetStandardBehavior(operation) {
		if(~highlightedRowIndex) {
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').removeClass('warning');
		}
		
		var submitBtn = '#' + operation + 'Submit';
		var submitChangeBtn = '#' + operation + 'SubmitChange';
		var deleteBtn = '#' + operation + 'Delete';
		var dialog = '#' + operation + 'Dialog';
		
		$(submitBtn).removeClass('hide');
		$(submitChangeBtn).removeClass('modification');
		$(submitChangeBtn).addClass('hide');
		$(deleteBtn).addClass('hide');
		$(dialog).addClass('hide');
		hideInputDialogs('all');
		$('#lineDetails').addClass('hide');
		if(operation == 'load') {
			$('#loadFiletypeSeparator.btn-group').css('display','none');
			$('#loadVcf').addClass('active');
			$('#loadTxt').removeClass('active');
		}
		if(operation == 'script') {
			$('#scriptDialog').addClass('hide');
		}
	}
	
	function setCurrentOperation(operation) {
		$('#stepAction').addClass('hide');
		$('#workflowOps').html(operation);
	}
	
	function toggleSaveStateVisualisation() {
		if($('#saveState').hasClass('saved')) {
			$('#saveState').html('');
		} else {
			$('#saveState').html(' *');
		}
	}
	
	function cleanModificationDialogs() {
		modifyDialog('register');
		modifyDialog('load');
		modifyDialog('store');
		modifyDialog('filter');
		modifyDialog('join');
		modifyDialog('script');
		$('#inputError').hide();
		$('#showWfBtn').popover('hide').removeClass('pop');
		$('#deleteWfBtn').popover('hide').removeClass('pop');
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
		$('#removeRowBtn').on('click', function() {
			$('#removeCheckModal').modal('hide');
			deleteTableRow();
			finishReset(obj);
		});
	}
	
	function showDiscardChangesAlert() {
		$('#discardChangesCheckModal').modal('show');
		$('#discardWfChangesBtn').on('click', function() {
			$('#discardChangesCheckModal').modal('hide');
			initializeNewWorkflow();
		});
	}
	
	function deleteTableRow() {
		$('#inputError').hide();
		workflow.splice(highlightedRowIndex,1);
		if(workflow.length == 0) {
			resetInitialState();
		} else {
			showTable();
		}
	}
	
	
	function finishReset(obj) {
		var buttonName = $(obj).attr('id');
		if(buttonName.indexOf('register') == 0) {
			resetStandardBehavior('register');
		} else if(buttonName.indexOf('load') == 0) {
			resetStandardBehavior('load');
		} else if (buttonName.indexOf('store') == 0) {
			resetStandardBehavior('store');
		} else if (buttonName.indexOf('filter') == 0) {
			resetStandardBehavior('filter');
		} else if (buttonName.indexOf('join') == 0) {
			resetStandardBehavior('join');
		} else if (buttonName.indexOf('script') == 0) {
			resetStandardBehavior('script');
		}
		$('#comments').val('');
		resetFormContainer();
		$('#inputError').hide();
		modifyContainerHeight();
	}
	
	/**
	 * Calls a helper function to convert the workflow-object into a html table and displays the result.
	 */
	function showTable() {
		var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
		$('#tableContainer').html(tab);
	}