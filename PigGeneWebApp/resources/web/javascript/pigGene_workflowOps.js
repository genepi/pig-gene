/**
 * 
 * @author Clemens Banas
 * @date April 2013
 */

function loadWorkflow(filename) {
		var data = '{"filename":"' + filename + '"}';
		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/ld',
    	    data: data,
    	    dataType:'json',
    	    success: function(response) {
    	    	if(response.success) {
    	    		initializeLoadedWorkflow(response.data);
    	    		$('#workflowName').html(filename);
    	    		$('#modalHeaderContent').html('<h3>Loading...</h3>');
    	    		$('#msg').html('Your workflow was loaded successfully!');
    	    		setStandardBehaviorSuccessModal();
					$('#saveState').addClass('saved');
					toggleSaveStateVisualisation();
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
	    		$('#modalHeaderContent').html('<h3>saving done</h3>');
	    		$('#msg').html('Your workflow was saved successfully!');
	    		if(forceDownload) {
	    			forceDownload = false;
	    			setModificationBehaviorSuccessModal(filename);
	    		} else {
	    			setStandardBehaviorSuccessModal();
	    		}
	    		resetOperationDialog($('#workflowOps').html().toLowerCase());
	    		resetFormContainerOperation();
	    		$('#workflowName').html(filename);
				$('#saveState').addClass('saved');
				toggleSaveStateVisualisation();
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

function deleteWorkflow(filename) {
	var data = '{"filename":"' + filename + '"}';
	$.ajax({
		type: 'POST',
	    url: 'http://localhost:8080/del',
	    data: data,
	    dataType:'json',
	    success: function(response) {
	    	if(response.success) {
	    		$('#modalHeaderContent').html('<h3>Deletion complete</h3>');
	    		$('#msg').html('Selected workflow was deleted successfully!');
				setStandardBehaviorSuccessModal();
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


/**
 * Method is called to perform a cleanup after the submit action has taken place.
 * Hides the input error dialog and the input form, reloads the workflow table and 
 * clears the input fields. Displays the save form.
 */
function finalizeSubmit(obj) {
	$('#inputError').hide();
	displayTable();
	$('#saveWfBtn').removeClass('hide');
	$('#downloadScript').removeClass('hide');
	$('#descriptionBtn').removeClass('hide');
	$('#saveWorkflow').removeClass('hide');
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
	$(obj).hide('slow');
	$(obj).children('input[type=text]').val('');
	$('#saveState').removeClass('saved');
	toggleSaveStateVisualisation();
	$('#comments').val('');
	if($('#workflowName').hasClass('new')) {
		$('#workflowName').removeClass('new');
		$('#workflowName').html(' unnamed');
	}
}

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