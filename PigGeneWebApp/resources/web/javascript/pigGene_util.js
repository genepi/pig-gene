/**
 * Util functions.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

/**
 * Function displays the operation dialog that was selected by calling some helper functions.
 */
function processOperationLinkRequest(operation) {
	if($('#helpBtn').hasClass('btn-info')) {
		setOperationRelatedHelpContent(operation);
	}
	var operationDialog = '#'+operation+'Dialog';
	setFormContainerOperation(operation);
	showInputDialogSlow(operationDialog);
}


/**
 * Function is used to check which containers currently get displayed 
 * and modifies their height depending on that information.
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


/**
 * Functions are used to check the input length and to put the input values into the global workflow variable. 
 * Finally the operation dialog gets reset to its standard behavior.
 */
function processRegisterOperation() {
	var values = getFormData('#registerDialog');
	var oper = 'REGISTER';
	var input = values[0].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(input)) {
		showErrorMessageShortInput();
		return false;
	}
	if(comm == null || comm == ''){
		comm = '-';
	}
	if($('#registerSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {relation:'-', input:input, operation:oper, input2:'-', options:'-', options2:'-', comment:comm};
		resetOperationDialog('register');
	} else {
		workflow.push({relation:'-', input:input, operation:oper, input2:'-', options:'-', options2:'-', comment:comm});
	}
	finalizeSubmit('#registerDialog');
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
		deleteTypeaheadAndUsedRelationByOperation(oper);
		workflow[highlightedRowIndex] = {relation:name, input:rel, operation:oper, input2:'-', options:opt, options2:opt2, comment:comm};
		resetOperationDialog('load');
	} else {
		workflow.push({relation:name, input:rel, operation:oper, input2:'-', options:opt, options2:opt2, comment:comm});
	}
	updateTypeaheadRelations(name);
	updateUsedRelations(rel);
	finalizeSubmit('#loadDialog');
}
function processStoreOperation() {
	var values = getFormData('#storeDialog');
	var oper = 'STORE';
	var rel = values[0].value;
	var name = values[1].value;
	var comm = $('#comments').val();
	if(!inputLongEnough(name) || !inputLongEnough(rel)) {
		showErrorMessageShortInput();
		return false;
	}
	if(comm == null || comm == '') {
		comm = '-';
	}
	
	if($('#storeSubmitChange').hasClass('modification')) {
		deleteTypeaheadAndUsedRelationByOperation(oper);
		workflow[highlightedRowIndex] = {relation:name, input:rel, operation:oper, input2:'-', options:'-', options2:'-', comment:comm};
		resetOperationDialog('store');
	} else {
		workflow.push({relation:name, input:rel, operation:oper, input2:'-', options:'-', options2:'-', comment:comm});
	}
	updateUsedRelations(name);
	updateUsedRelations(rel);
	finalizeSubmit('#storeDialog');
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
		deleteTypeaheadAndUsedRelationByOperation(oper);
		workflow[highlightedRowIndex] = {relation:name, input:rel, operation:oper, input2:'-', options:opt, options2:'-', comment:comm};
		resetOperationDialog('filter');
	} else {
		workflow.push({relation:name, input:rel, operation:oper, input2:'-', options:opt, options2:'-', comment:comm});
	}
	updateTypeaheadRelations(name);
	updateUsedRelations(rel);
	finalizeSubmit('#filterDialog');
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
		deleteTypeaheadAndUsedRelationByOperation(oper);
		workflow[highlightedRowIndex] = {relation:name, input:rel1, operation:oper, input2:rel2, options:opt1, options2:opt2, comment:comm};
		resetOperationDialog('join');
	} else {
		workflow.push({relation:name, input:rel1, operation:oper, input2:rel2, options:opt1, options2:opt2, comment:comm});
	}
	updateTypeaheadRelations(name);
	updateUsedRelations(rel1);
	updateUsedRelations(rel2);
	finalizeSubmit('#joinDialog');
}
function processScriptOperation() {
	var script = $('#scriptTextarea').val();
	var oper = 'SCRIPT';
	var comm = $('#comments').val();
	if(comm == null || comm == ''){
		comm = '-';
	}
	if($('#scriptSubmitChange').hasClass('modification')) {
		workflow[highlightedRowIndex] = {relation:'script', input:'-', operation:oper, input2:'-', options:script, options2:'-', comment:comm};
		resetOperationDialog('script');
	} else {
		workflow.push({relation:'script', input:'-', operation:oper, input2:'-', options:script, options2:'-', comment:comm});
	}
	finalizeSubmit('#scriptDialog');
	$('#scriptDialog').addClass('hide');
	$('#scriptTextarea').val('');
	modifyContainerHeight();
}


/**
 * Helper function to retrieve the input values of the given input form.
 * @returns form values as an array
 */
function getFormData(dialog) {
	return $(dialog).serializeArray();
}


/**
 * Method checks if the clicked button was a delete button or if it was 
 * a cancellation of the input. It eigther shows a security alert or resets 
 * the operation dialog by calling a helper function.
 * @param clicked button
 */
function processInputFormCancellation(button) {
	if(button.hasClass('delete')) {
		showSecurityAlertRemoveLine();
		return;
	}
	finishReset(button);
}


/**
 * Function is used to set the save state to 'saved' and 
 * to hide the *-symbol behind the workflow name.
 */
function setSaveStateSavedAndDisplayStatus() {
	$('#saveState').addClass('saved');
	toggleSaveStateVisualisation();
}


/**
 * Function is used to set the save state to 'unsaved' and 
 * to show the *-symbol behind the workflow name.
 */
function setSaveStateUnsavedAndDisplayStatus() {
	$('#saveState').removeClass('saved');
	toggleSaveStateVisualisation();
}


/**
 * Function is used to reset the description to the value 
 * saved in the global description variable.
 */
function resetDescription() {
	$('#description').val(description);
}


/**
 * Function is used to save the given comment in the global workflow variable.
 */
function saveCommentInWorkflow(comment) {
	if(comment == '' || comment == '-') {
		workflow[highlightedRowIndex].comment = '-';
	} else {
		workflow[highlightedRowIndex].comment = comment;
	}
}


/**
 * Function is used to reset the previous line comment that 
 * is saved in the global workflow variable
 */
function resetSavedLineComment() {
	var oldComment = workflow[highlightedRowIndex].comment;
	if(oldComment == '-') {
		$('#comments').val('');
	} else {
		$('#comments').val(oldComment);
	}
}


/**
 * Function is used to find out which key was pressed by
 * the user and to handle the further processing by calling
 * the proper helper function.
 * @param pressed key
 */
function handleKeydownEvent(e) {
	if(!isInputFormElement(e)) {
		switch(e.which) {
			case 38: highlightUpperRow(); break;
			case 40: highlightLowerRow(); break;
			case 46: showSecurityAlertRemoveLine(); break;
			default: break;
		}
	}
}


/**
 * Function checks if the given argument is an input or a textarea form element.
 * @param pressed key
 */
function isInputFormElement(e) {
	var elementName = e.target.nodeName.toLowerCase();
	if(elementName == 'input') {
		return true;
	} else if(elementName == 'textarea') {
		return true;
	}
	return false;
}


/**
 * Function is used to reset the highlighting of the table row by finding out which operation
 * is currently highlighted and by triggering the corresponding cancel button.
 * @param target
 */
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


/**
 * Function is used to remove the table row highlightings of all rows except the one that
 * was given to the function and to display the corresponding operation information by
 * calling a helper function.
 * @param tableRow
 */
function processTableRowClick(tableRow) {
	removeTableRowLabeling('warning');
	$(tableRow).addClass('warning');
	highlightedRowIndex = $(tableRow).index();
	displayCorrespondingContainerInfo();
}


/**
 * Function is used to check if the workflow has been saved bevore. If not an alert 
 * message is shown. Otherwise the new workflow gets initialized by calling a helper
 * function.
 */
function processNewWfRequest() {
	if(!$('#saveState').hasClass('saved')) {
		$('#discardFilename').html($('#workflowName').html());
		showDiscardChangesAlert();
		return;
	}
	initializeNewWorkflow();
}


/**
 * Function is used to show an error message to indicate that the input was too short.
 */
function showErrorMessageShortInput() {
	showInputErrorMsg('Inputs have to be at least 2 characters long. Modify your input and click save again.');
}


/**
 * Function is used to process a click on the description button.
 * If the description is hidden the description dialog gets displayed,
 * the previous saved description gets set into the textfield and the
 * icon of the button gets changed to indicate the expanded state.
 * Otherwise the cancel button gets triggered to close the dialog.
 */
function processDescriptionBtnClick() {
	if($('#workflowDescription').hasClass('hide')) {
		$('#workflowDescription').removeClass('hide');
		resetDescription();
		showExpandedDescriptionIcon();
	} else {
		$('#workflowDescrClear').trigger('click');
	}
}


/**
 * Function is used to reorder two table lines. The currently highlighted an the line above.
 * These changes are made directly to the global workflow variable. The previously highlighted
 * line stays highlighted, the save state changes to 'unsaved' and the changed table gets displayed.
 */
function orderUpHandling() {
	if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
		var tmp = workflow[highlightedRowIndex-1];
		workflow[highlightedRowIndex-1] = workflow[highlightedRowIndex];
		workflow[highlightedRowIndex] = tmp;
		displayTable();
		addTableRowHighlighting(highlightedRowIndex);
		highlightedRowIndex--;
		setSaveStateUnsavedAndDisplayStatus();
	}
}


/**
 * Function is used to reorder two table lines. The currently highlighted an the line below.
 * These changes are made directly to the global workflow variable. The previously highlighted
 * line stays highlighted, the save state changes to 'unsaved' and the changed table gets displayed.
 */
function orderDownHandling() {
	var rowCount = $('#operationTable tr').length;
	if (~highlightedRowIndex && highlightedRowIndex!=rowCount-2 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')')) {
		var tmp = workflow[highlightedRowIndex+1];
		workflow[highlightedRowIndex+1] = workflow[highlightedRowIndex];
		workflow[highlightedRowIndex] = tmp;
		displayTable();
		addTableRowHighlighting(highlightedRowIndex+2);
		highlightedRowIndex++;
		setSaveStateUnsavedAndDisplayStatus();
	}
}


/**
 * Function is used to check if the current workflow was saved. If it was saved then
 * the function places a link into the clicked downloadScript-button that enables
 * the user to directly download the requested workflow as a pigscript generated by
 * the server.
 */
function processDownloadRequest() {
	if($('#saveState').hasClass('saved')) {
		var filename = $('#workflowName').html();
		$('#downloadScript').attr('download', filename + '.pig');
		$('#downloadScript').attr('href', 'http://localhost:8080/dwld/' + filename);
		resetDialogsAndHighlightings();
	} else {
		$('#downloadScript').removeAttr('download').removeAttr('href');
		openDownloadUnsavedModal();
	}
}


/**
 * Function checks if workflow was saved - if not it shows
 * a modal dialog to avoid unwanted data loss.
 * @param fileName
 */
function loadDeleteWfRequest(fileName) {
	if(!$('#saveState').hasClass('saved') && $('#showWfBtn').hasClass('showWfBtnPopover')) {
		$('#discardFilename').html($('#workflowName').html());
		$('#discardFilename').addClass('load');
		loadDeleteWorkflowName = fileName;
		showDiscardChangesAlert();
		return;
	}
	processLoadDeleteWfRequest(fileName);
}

/**
 * Function is used to trigger a load or a delete 
 * request depending on the shown popover.
 * @param fileName
 */
function processLoadDeleteWfRequest(fileName) {
	hideLineDetailDialog();
	if($('#showWfBtn').hasClass('showWfBtnPopover')) {
		$('#workflowName').removeClass('new');
		loadWorkflow(fileName);
		hideShowWfBtnPopover();
		resetDescription();
	} else {
		showSecurityAlertRemoveWorkflow(fileName);
	}
}


/**
 * Function is used to delete a workflow.
 */
function processWorkflowDeletion() {
	var fileName = $('#deleteWfName').html();
	$('#removeWorkflowCheckModal').modal('hide');
	deleteWorkflow(fileName);
	hideDeleteWfBtnPopover();
	if(fileName == $('#workflowName').html().trim()) {
		resetWorkflow();
	}
}


/**
 * Function is used to dinstinguish between the loading of a
 * new workflow or the loading/deletion of an existing workflow.
 * It calls the corresponding helper functions and hides the
 * modal dialog.
 */
function processDiscardChanges() {
	$('#discardChangesCheckModal').modal('hide');
	if($('#discardFilename').hasClass('load')) {
		$('#discardFilename').removeClass('load');
		processLoadDeleteWfRequest(loadDeleteWorkflowName);
	} else {
		initializeNewWorkflow();
	}
}


/**
 * Function is used to convert the given filenames into html-links.
 * In case of a delete-action the content gets filtered.
 * @param data
 */
function convertFilenamesToLinks(buttonName, data) {
	var toRemove = '.yaml';
	var content = '';
	for(var i=0; i<data.length; i++) {
		var name = data[i].replace(toRemove,'');
		if(!(buttonName == '#deleteWfBtn' && $.inArray(name, undeletableWorkflows) > -1)) {
			content += '<a class="fileNames">'+name+'</a><br>';
		}
	}
	return content;
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


/**
 * Function is used to update the typeahead feature of the 
 * save dialog by sending an ajax request to the server.
 */
function updateTypeaheadSaved() {
	$.ajax({
		type: 'POST',
	    url: 'http://localhost:8080/wf',
	    data: null,
	    dataType:'json',
	    success: function(response) {
	    	if(response.success) {
	    		var savedNames = [];
	    		var toRemove = '.yaml';
	    		for(var i=0; i<response.data.length; i++) {
	    			var fileName = response.data[i].replace(toRemove,'');
	    			if($.inArray(fileName, undeletableWorkflows) < 0) {
	    				savedNames.push(fileName);
	    			}
	    		}
	    		savedNames.sort();
	    		$('#saveDialogInput').typeahead({source: savedNames, items: 2});
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


/**
 * Function is used to initialize the typeahead values. Therefor
 * it loads the needed data from the global workflow array.
 */
function initializeTypeaheadRelations() {
	typeaheadRelations = [];
	var relation; 
	for(var i=0; i<workflow.length; i++) {
		relation = workflow[i].relation;
		operation = workflow[i].operation;
		if(relation != '-' && relation != 'script' && operation != 'STORE') {
			typeaheadRelations.push(relation);
		}
	}
	sortTypeaheadRelationElements();
	performTypeaheadButtonUpdate();
}


/**
 * Function is used to reset and delete all globally 
 * saved typeahead values.
 */
function resetTypeaheadRelations() {
	typeaheadRelations = [];
	performTypeaheadButtonUpdate();
}


/**
 * Function is used to add a new value to the globally 
 * saved typeahead values.
 * @param relation name
 */
function updateTypeaheadRelations(relation) {
	if($.inArray(relation, typeaheadRelations) == -1) {
		typeaheadRelations.push(relation);
		sortTypeaheadRelationElements();
		performTypeaheadButtonUpdate();
	}
}


/**
 * Function is used to remove the given element from the typeahead
 * relations array because a line was deleted in the workflow.
 * @param element name to remove from array
 */
function removeTypeaheadRelationElement(name) {
	if($.inArray(name, typeaheadRelations) > -1) {
		typeaheadRelations.splice($.inArray(name, typeaheadRelations), 1);
		sortTypeaheadRelationElements();
		performTypeaheadButtonUpdate();
	}
}


/**
 * Function is used to sort the typeahead relation elements.
 */
function sortTypeaheadRelationElements() {
	typeaheadRelations.sort();
}


/**
 * Function is used to update all the buttons, that rely
 * on the globally saved typeahead values.
 */
function performTypeaheadButtonUpdate() {
	$('#filtRel').typeahead().data('typeahead').source = typeaheadRelations;
	$('#joinRel').typeahead().data('typeahead').source = typeaheadRelations;
	$('#joinRel2').typeahead().data('typeahead').source = typeaheadRelations;
	$('#relToStore').typeahead().data('typeahead').source = typeaheadRelations;
}


/**
 * Function is used to initialize the used relations 
 * when a workflow was loaded by the user.
 */
function initializeUsedRelations() {
	usedRelations = [];
	var operation;
	for(var i=0; i<workflow.length; i++) {
		operation = workflow[i].operation;
		if(operation == 'LOAD' || operation == 'FILTER') {
			updateUsedRelations(workflow[i].input);
		} else if(operation == 'JOIN') {
			updateUsedRelations(workflow[i].input);
			updateUsedRelations(workflow[i].input2);
		} else if(operation == 'STORE') {
			updateUsedRelations(workflow[i].input);
			updateUsedRelations(workflow[i].relation);
		}
	}
}


/**
 * Function is used to add the given name to the
 * list of used relations.
 * @param name
 */
function updateUsedRelations(name) {
	usedRelations.push(name);
}


/**
 * Function is used to remove the given name
 * from the list of used relations.
 * @param name
 */
function removeUsedRelationElement(name) {
	usedRelations.splice($.inArray(name, usedRelations), 1);
}


/**
 * Function is used to check if the given name is 
 * contained in the global "usedRelations" array.
 * @param relation name
 */
function relationIsUsed(name) {
	if($.inArray(name, usedRelations) > -1) {
		return true;
	}
	return false;
}


/**
 * Function is used to check if the given name is
 * contained in the global "typeaheadRelations" array.
 * @param relation name
 */
function relationExists(name) {
	if($.inArray(name, typeaheadRelations) > -1) {
		return true;
	}
	return false;
}


/**
 * Function is used to update the typeahead and used relations
 * in case of a modification or deletion of a workflow line.
 * @param operation
 */
function deleteTypeaheadAndUsedRelationByOperation(op) {
	removeTypeaheadRelationElement(workflow[highlightedRowIndex].relation);
	removeUsedRelationElement(workflow[highlightedRowIndex].input);

	var operation = op;
	if(op == '') {
		operation = $('#workflowOps').html();
	}
	if(operation.indexOf('STORE') == 0) {
		removeUsedRelationElement(workflow[highlightedRowIndex].relation);
	} else if(operation.indexOf('JOIN') == 0) {
		removeUsedRelationElement(workflow[highlightedRowIndex].input2);
	}
}