/**
 * 
 * @author Clemens Banas
 * @date April 2013
 */

function modifyContainerHeight() {
	if($('#lineDetails').hasClass('hide') && $('#scriptDialog').hasClass('hide')) {
		$('#workflowContainer.well').css('min-height','193px');
		$('#formContainer.well').css('height','175px');
	} else if($('#scriptDialog').hasClass('hide')) {
		$('#workflowContainer.well').css('min-height','401px');
		$('#formContainer.well').css('height','175px');
	} else if($('#lineDetails').hasClass('hide')) {
		$('#workflowContainer.well').css('min-height','302px');
		$('#formContainer.well').css('height','284px');
	} else {
		$('#workflowContainer.well').css('min-height','510px');
		$('#formContainer.well').css('height','284px');
	}
}

function getFormData(dialog) {
	return $(dialog).serializeArray();
}

function processRegisterOperation() {
	var values = getFormData('#registerDialog');
	var oper = 'REGISTER';
	var rel = values[0].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(rel)) {
		showErrorMessageShortInput();
		return false;
	}
	if(comm == null || comm == ''){
		comm = '-';
	}
	if($('#registerSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {name:'-', relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm};
		resetOperationDialog('register');
	} else {
		workflow.push({name:'-', relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm});
	}
	finalizeSubmit(this);
}

function processLoadOperation() {
	var values = getFormData('#loadDialog');
	var oper = 'LOAD';
	var name = values[0].value;
	var rel = values[1].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(rel)) {
		showErrorMessageShortInput();
		return false;
	}
	if(name == null || name == '') {
		name = getArtificialName();
	}
	if(comm == null || comm == ''){
		comm = '-';
	}
	
	var opt = $('#loadFiletype.btn-group > button.btn.active').html();
	var opt2 = '-';
	if($('#loadTxt').hasClass('active')) {
		opt2 = $('#loadFiletypeSeparator.btn-group > button.btn.active').html();
	}

	if($('#loadSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:opt2, comment:comm};
		resetOperationDialog('load');
	} else {
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:opt2, comment:comm});
	}
	finalizeSubmit(this);
}

function processStoreOperation() {
	var values = getFormData('#storeDialog');
	var oper = 'STORE';
	var name = values[0].value;
	var rel = values[1].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(name) || !inputLongEnough(rel)) {
		showErrorMessageShortInput();
		return false;
	}
	if(comm == null || comm == ''){
		comm = '-';
	}
	
	if($('#storeSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm};
		resetOperationDialog('store');
	} else {
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm});
	}
	finalizeSubmit(this);
}

function processFilterOperation() {
	var values = getFormData('#filterDialog');
	var oper = 'FILTER';
	var name = values[0].value;
	var rel = values[1].value;
	var opt = values[2].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(rel) || !inputLongEnough(opt)) {
		showErrorMessageShortInput();
		return false;
	}
	if(name == null || name == '') {
		name = getArtificialName();
	}
	if(comm == null || comm == ''){
		comm = '-';
	}
	
	if($('#filterSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:'-', comment:comm};
		resetOperationDialog('filter');
	} else {
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:'-', comment:comm});
	}
	finalizeSubmit(this);
}

function processJoinOperation() {
	var values = getFormData('#joinDialog');
	var oper = 'JOIN';
	var name = values[0].value;
	var rel1 = values[1].value;
	var opt1 = values[2].value;
	var rel2 = values[3].value;
	var opt2 = values[4].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(rel1) || !inputLongEnough(opt1) || !inputLongEnough(rel2) || !inputLongEnough(opt2)) {
		showErrorMessageShortInput();
		return false;
	}
	if(name == null || name == '') {
		name = getArtificialName();
	}
	if(comm == null || comm == ''){
		comm = '-';
	}
	
	if($('#joinSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {name:name, relation:rel1, operation:oper, relation2:rel2, options:opt1, options2:opt2, comment:comm};
		resetOperationDialog('join');
	} else {
		workflow.push({name:name, relation:rel1, operation:oper, relation2:rel2, options:opt1, options2:opt2, comment:comm});
	}
	finalizeSubmit(this);
}

function processScriptOperation() {
	var values = getFormData('#scriptTextarea');
	var oper = 'SCRIPT';
	var comm = $('#comments').val();
	if(comm == null || comm == ''){
		comm = '-';
	}
	if($('#scriptSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {name:'script', relation:'-', operation:oper, relation2:'-', options:script, options2:'-', comment:comm};
		resetOperationDialog('script');
	} else {
		workflow.push({name:'script', relation:'-', operation:oper, relation2:'-', options:script, options2:'-', comment:comm});
	}
	finalizeSubmit(this);
	$('#scriptDialog').addClass('hide');
	$('#scriptTextarea').val('');
	modifyContainerHeight();
}

function processOperationLinkRequest(operation) {
	var operationDialog = '#'+operation+'Dialog';
	setFormContainerOperation(operation);
	resetDialogsAndHighlightings();
	showInputDialogSlow(operationDialog);
	hideNotSpecifiedInputDialogs(operation);
}

function showErrorMessageShortInput() {
	showInputErrorMsg('Inputs have to be at least 2 characters long. Please click the save button again and type <br>a longer name.');
}

function ajaxRequestSaveWorkflow(filename) {
	var data = '{"filename":"' + filename + '"}';
	$.ajax({
		type: 'POST',
	    url: 'http://localhost:8080/ex',
	    data: data,
	    dataType: 'json',
	    success: function(response) {
	    	if(response.success) {
				if(response.data) {
					showSecurityAlert(filename);
				} else {
					saveWorkflow(filename);
				}
	    	} else {
	    		$('#errmsg').html(response.message);
	    		$('#errorModal').modal('show');
	    	}
	    },
	    error: function (xhr, ajaxOptions, thrownError) {
	    	$('#errmsg').html(xhr.responseText);
    		$('#errorModal').modal('show');
	   }
	});
}

function processDownloadRequest() {
	if($('#saveState').hasClass('saved')) {
		var filename = $('#workflowName').html();
		$('#downloadScript').attr('download', filename + '.pig');
		$('#downloadScript').attr('href', 'http://localhost:8080/dwld/' + filename);
	} else {
		$('#downloadScript').removeAttr('download').removeAttr('href');
		forceDownload = true;
		$('#saveWfBtn').trigger('click');
	}
}



function orderUpHandling() {
	if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
		var tmp = workflow[highlightedRowIndex-1];
		workflow[highlightedRowIndex-1] = workflow[highlightedRowIndex];
		workflow[highlightedRowIndex] = tmp;
		displayTable();
		$('#operationTable tbody tr:nth-child('+(highlightedRowIndex)+')').addClass('warning');
		highlightedRowIndex--;
		$('#saveState').removeClass('saved');
		toggleSaveStateVisualisation();
	}
}

function processLoadDeleteWfRequest(fileName) {
	hideLineDetailDialog();
	if($('#showWfBtn').hasClass('showWfBtnPopover')) {
		loadWorkflow(fileName);
		hideShowWfBtnPopover();
		resetDescription();
	} else {
		deleteWorkflow(fileName);
		hideDeleteWfBtnPopover();
		if(fileName == $('#workflowName').html().trim()) {
			resetWorkflow();
		}
	}
}

function handleKeydownEvent(e) {
	var selector = 'tr:nth-child(' + (highlightedRowIndex+1) + ')';
	if(!isInputFormElement(e)) {
		switch(e.which) {
			case 38: highlightUpperRow(); break;
			case 40: highlightLowerRow(); break;
			case 46: showSecurityAlertRemove(); break;
			default: break;
		}
	}
}

function processTableRowClick(tableRow) {
	removeTableRowLabeling('warning');
	$(tableRow).addClass('warning');
	highlightedRowIndex = $(tableRow).index();
	displayCorrespondingContainerInfo();
}

function processNewWfRequest() {
	if(!$('#saveState').hasClass('saved')) {
		showDiscardChangesAlert();
		return;
	}
	initializeNewWorkflow();
}

function processDescriptionBtnClick() {
	if($('#workflowDescription').hasClass('hide')) {
		$('#workflowDescription').removeClass('hide');
		resetDescription();
		showExpandedDescriptionIcon();
	} else {
		$('#workflowDescrClear').trigger('click');
	}
}

function resetDescription() {
	$('#description').val(description);
}

function orderDownHandling() {
	var rowCount = $('#operationTable tr').length;
	if (~highlightedRowIndex && highlightedRowIndex!=rowCount-2 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')')) {
		var tmp = workflow[highlightedRowIndex+1];
		workflow[highlightedRowIndex+1] = workflow[highlightedRowIndex];
		workflow[highlightedRowIndex] = tmp;
		displayTable();
		$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+2)+')').addClass('warning');
		highlightedRowIndex++;
		setSavedStateUnsavedAndDisplayStatus();
	}
}


function setSaveStateSavedAndDisplayStatus() {
	$('#saveState').addClass('saved');
	toggleSaveStateVisualisation();
}

function setSavedStateUnsavedAndDisplayStatus() {
	$('#saveState').removeClass('saved');
	toggleSaveStateVisualisation();
}

function saveCommentInWorkflow(comment) {
	if(comment == '' || comment == '-') {
		workflow[highlightedRowIndex].comment = '-';
	} else {
		workflow[highlightedRowIndex].comment = comment;
	}
}

function isInputFormElement(e) {
	var elementName = e.target.nodeName.toLowerCase();
	if(elementName == 'input') {
		return true;
	} else if(elementName == 'textarea') {
		return true;
	}
	return false;
}

function checkAndResetRowHighlighting(target) {
	if(target != null && target.context != null && target.context.tagName != null && target.context.tagName == 'DIV') {
		var selector;
		var operation = $('#workflowOps').html().toLowerCase()
		if(operation == 'user defined script') {
			selector = '#scriptClear';
		} else {
			selector = '#' + operation + 'Clear';
		}
		$(selector).trigger('click');
	}
}

function resetSavedWorkflowComment() {
	var oldComment = workflow[highlightedRowIndex].comment;
	if(oldComment == '-') {
		$('#comments').val('');
	} else {
		$('#comments').val(oldComment);
	}
}

function processInputFormCancellation(button) {
	if(button.hasClass('delete')) {
		showSecurityAlertRemove(button);
		return;
	}
	button.removeClass('modification');
	finishReset(button);
}

/**
 * Returns a relation-name if the user has not specified one.
 */
function getArtificialName() {
	return 'R' + nameCounter++;
}

/**
 * Checks if the input is longer than 1 character.
 */
function inputLongEnough(input) {
	if(input.length < 2) {
		return false;
	}
	return true;
}

function convertFilenamesToLinks(data) {
	var toRemove = '.yaml';
	var content = '';
	for(var i=0; i<data.length; i++) {
		var name = data[i].replace(toRemove,'');
		content += '<a class="fileNames">'+name+'</a><br>'
	}
	return content;
}