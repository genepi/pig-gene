/**
 * Workflow operation functions - load, save, delete.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

/**
 * Function is called to perform a cleanup after the submit action has taken place.
 * Hides the input error dialog and the input form, reloads the workflow table and 
 * clears the input fields. Displays the save form.
 */
function finalizeSubmit(obj) {
	hideInputErrors();
	displayTable();
	$('#saveWfBtn').removeClass('hide');
	$('#downloadScript').removeClass('hide');
	$('#descriptionBtn').removeClass('hide');
	$('#saveWorkflow').removeClass('hide');
	resetFormContainerOperation();
	$(obj).hide('slow');
	$(obj).children('input[type=text]').val('');
	$('#saveState').removeClass('saved');
	toggleSaveStateVisualisation();
	clearCommentTextbox();
	hideLineDetailDialog();
	if($('#workflowName').hasClass('new')) {
		$('#workflowName').removeClass('new');
		setWorkflowName(' unnamed')
	}
}


/**
 * Function is used to check if the given workflow-name is long enough.
 * If it is too short an error message gets displayed otherwise an ajax
 * request function is called to save the workflow on server side.
 */
function save() {
	var filename = $('#saveDialogInput').val();
	$('#saveNameModal').modal('hide');
	if(!inputLongEnough(filename)) {
		showErrorMessageShortInput();
		return false;
	}
	ajaxRequestSaveWorkflow(filename);
	return false;
}


/**
 * Function is used to transfer the filename to the server. If the server returns true to indicate that the name
 * is already in use then an alert is shown to double check if the user really wants to override the old workflow.
 * Otherwise the workflow gets saved by calling a assynchronic helper function.
 * @param filename
 */
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


/**
 * Function is used to send the workflow data to the server with an ajax request.
 * The server processes the data and saves the user defined workflow.
 * @param filename
 */
function saveWorkflow(filename) {
	//last two elements just needed for the ajax request, remove afterwards
	workflow.push(description);
	workflow.push(filename); 
	var data = JSON.stringify(workflow);
	workflow.splice(-2,2);
	
	$('#inputError').hide('slow');
	$('#comments').val('');
	
	$.ajax({
		type: 'POST',
	    url: 'http://localhost:8080/ser',
	    data: data,
	    dataType: 'json',
	    success: function(response) {
	    	if(response.success) {
	    		$('#modalHeaderContent').html('<h3>Saving completed</h3>');
	    		$('#msg').html('Your workflow was saved successfully!');
	    		if(forceDownload) {
	    			forceDownload = false;
	    			setModificationBehaviorSuccessModal(filename);
	    		} else {
	    			setStandardBehaviorSuccessModal();
	    		}
	    		resetOperationDialog($('#workflowOps').html().toLowerCase());
	    		resetFormContainerOperation();
	    		setWorkflowName(filename);
	    		setSaveStateSavedAndDisplayStatus();
	    		updateTypeaheadSaved();
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
	return false;
}


/**
 * Function is used to load the workflow that matches the given file name.
 * After successfully loading the workflow it gets initialized and displayed
 * by calling several helper functions. Also surrounding information like
 * additional operation buttons get displayed. 
 * @param fileName
 */
function loadWorkflow(fileName) {
		var data = '{"filename":"' + fileName + '"}';
		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/ld',
    	    data: data,
    	    dataType:'json',
    	    success: function(response) {
    	    	if(response.success) {
    	    		initializeAndDisplayLoadedWorkflow(response.data);
    	    		setWorkflowName(fileName);
    	    		$('#modalHeaderContent').html('<h3>Loading completed</h3>');
    	    		$('#msg').html('Your workflow was loaded successfully!');
    	    		setStandardBehaviorSuccessModal();
					$('#saveState').addClass('saved');
					toggleSaveStateVisualisation();
					initializeTypeaheadRelations();
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
		prepareContainers();
		initializeButtons();
		return false;
}


/**
 * Function is used to delete the workflow that matches the given file name.
 * @param fileName
 */
function deleteWorkflow(fileName) {
	var data = '{"filename":"' + fileName + '"}';
	$.ajax({
		type: 'POST',
	    url: 'http://localhost:8080/del',
	    data: data,
	    dataType:'json',
	    success: function(response) {
	    	if(response.success) {
	    		$('#modalHeaderContent').html('<h3>Deletion completed</h3>');
	    		$('#msg').html('Selected workflow was deleted successfully!');
				setStandardBehaviorSuccessModal();
				updateTypeaheadSaved();
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
	return false;
}


/**
 * Function is used to load all existing workflow names from the server if no popup is present.
 * These names are displayed as links in a popup that shows up on the button the user clicked.
 * Otherwise the existing popup gets closed.
 * @param buttonName
 */
function handleWorkflowRequest(buttonName) {
	var id = buttonName.substring(1,buttonName.length) + 'Popover';
	if($(buttonName).hasClass('pop')) {
		$(buttonName).popover('hide').removeClass('pop').removeClass(id);
	} else {
		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/wf',
    	    data: null,
    	    dataType:'json',
    	    success: function(response) {
    	    	if(response.success) {
    	    		var popContent = convertFilenamesToLinks(response.data);
    	    		hideOtherPopups(buttonName);
    	    		$(buttonName).attr('data-content', popContent).popover('show').addClass('pop').addClass(id);
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
}