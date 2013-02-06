$(document).ready(function() {
	
	//TODO: check if all values are given correctly
	//TODO: when deleting a line - check if workflow array is empty - if so: hide save button
	
	var workflow = [];
	var nameCounter = 1;
	var highlightedRowIndex = -1;
	
	/**
	 * Shows an English error-message if form should be submitted 
	 * without specifying all required fields.
	 */
    $('input[type=text]').on('change invalid', function() {
        var textfield = $(this).get(0);
        textfield.setCustomValidity('');
        if (!textfield.validity.valid) {
          textfield.setCustomValidity('this field cannot be left blank');  
        }
    });
	
	$('#loadLink').on('click', function() {
		$('#loadDialog').show('slow');
		hideInputDialogs('load');
	});
	
	$('#storeLink').on('click', function() {
		$('#storeDialog').show('slow');
		hideInputDialogs('store');
	});

	$('#filterLink').on('click', function() {
		$('#filterDialog').show('slow');
		hideInputDialogs('filter');
	});
	
	$('#joinLink').on('click', function() {
		$('#joinDialog').show('slow');
		hideInputDialogs('join');
	});
	
	/**
	 * Cancellation-Handling of input forms.
	 */
	$("button[type='reset']").on('click', function() {
		$(this).removeClass('modification');
		var buttonName = $(this).attr('id');
		if(buttonName.indexOf('load') == 0) {
			resetStandardBehavior('#loadSubmit','#loadSubmitChange');
		} else if (buttonName.indexOf('store') == 0) {
			resetStandardBehavior('#storeSubmit','#storeSubmitChange');
		} else if (buttonName.indexOf('filter') == 0) {
			resetStandardBehavior('#filterSubmit','#filterSubmitChange');
		} else if (buttonName.indexOf('join') == 0) {
			resetStandardBehavior('#joinSubmit','#joinSubmitChange');
		}		
		$('#inputError').hide();
	});
	
	function showInputErrorMsg(errText) {
		$('#inputErrMsg').html(errText);
		$('#inputError').show('slow');
	}
	
	$('#loadDialog').on('submit',function() {
		var values = $('#loadDialog').serializeArray();
		var oper = 'LOAD';
		var name = values[0].value;
		var rel = values[1].value;
		if(!inputLongEnough(rel)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
			return false;
		}
		if(name == null || name == '') {
			name = getArtificialName();
		}

		if($('#loadSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'};
			resetStandardBehavior('#loadSubmit','#loadSubmitChange');
		} else {
			workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#storeDialog').on('submit',function() {
		var values = $('#storeDialog').serializeArray();
		var oper = 'STORE';
		var name = values[0].value;
		var rel = values[1].value;
		if(name == null || name == '') {
			name = getArtificialName();
		}
		
		if($('#storeSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'};
			resetStandardBehavior('#storeSubmit','#storeSubmitChange');
		} else {
			workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#filterDialog').on('submit',function() {
		var values = $('#filterDialog').serializeArray();
		var oper = 'FILTER';
		var name = values[0].value;
		var rel = values[1].value;
		var opt = values[2].value;
		if(name == null || name == '') {
			name = getArtificialName();
		}
		
		if($('#filterSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:'-'};
			resetStandardBehavior('#filterSubmit','#filterSubmitChange');
		} else {
			workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:'-'});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#joinDialog').on('submit',function() {
		var values = $('#joinDialog').serializeArray();
		var oper = 'JOIN';
		var name = values[0].value;
		var rel1 = values[1].value;
		var opt1 = values[2].value;
		var rel2 = values[3].value;
		var opt2 = values[4].value;
		if(name == null || name == '') {
			name = getArtificialName();
		}
		
		if($('#joinSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:name, relation:rel1, operation:oper, relation2:rel2, options:opt1, options2:opt2};
			resetStandardBehavior('#joinSubmit','#joinSubmitChange');
		} else {
			workflow.push({name:name, relation:rel1, operation:oper, relation2:rel2, options:opt1, options2:opt2});
		}
		finalizeSubmit(this);
		return false;
	});
	
	/**
	 * Removes the highlighting from the selected table row and changes the visibility of the
	 * "standard" submit and the "modification" submit buttons. Also hides all input dialogs.
	 */
	function resetStandardBehavior(submitBtn, submitChangeBtn) {
		if(~highlightedRowIndex) {
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').removeClass('warning');
		}
		$(submitBtn).removeClass('hide');
		$(submitChangeBtn).removeClass('modification');
		$(submitChangeBtn).addClass('hide');
		hideInputDialogs('all');
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
	 * Method is called to perform a cleanup after the submit action has taken place.
	 * Hides the input error dialog and the input form, reloads the workflow table and 
	 * clears the input fields. Displays the save form.
	 */
	function finalizeSubmit(obj) {
		$('#inputError').hide();
		showTable();
		$('#saveWorkflow').show("fast");
		$(obj).hide('slow');
		$(obj).children('input[type=text]').val('');
	}
	
	/**
	 * Calls a helper function to convert the workflow-object into a html table and displays the result.
	 */
	function showTable() {
		var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
		$('#tableContainer').html(tab);
	}
	
	/**
	 * Method handles a user request for already existing workflow definitions. Ajax request returns all
	 * existing workflow names. These names get converted into links and are shown in a popover. The user
	 * can select one of the links to avoid typing it manually.
	 */
	$('#showWfBtn').popover({ trigger: 'manual', html: true, placement: 'bottom', }).click(function() {
		if($(this).hasClass('pop')) {
			$(this).popover('hide').removeClass('pop');
		} else {
			$.ajax({
	    		type: 'POST',
	    	    url: 'http://localhost:8080/wf',
	    	    data: null,
	    	    dataType:'json',
	    	    success: function(response) {
	    	    	if(response.success) {
	    	    		var popContent = convertFilenamesToLinks(response.data);
	    	    		$('#showWfBtn').attr('data-content', popContent).popover('show').addClass('pop');
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
	});

	function convertFilenamesToLinks(data) {
		var toRemove = '.yaml';
		var content = '';
		for(var i=0; i<data.length; i++) {
			var name = data[i].replace(toRemove,'');
			content += '<a class="fileNames">'+name+'</a><br>'
		}
		return content;
	}
	
	$('#operationButtons').on('click', 'a.fileNames', function() {
		var fileName = $(this).html();
		$('#loadWorkflowName').val(fileName);
		$('#saveWorkflowName').val(fileName);
		$('#showWfBtn').popover('hide').removeClass('pop');
		$('#loadWorkflowName').select();
	});
	
	/**
	 * Modification-Handling of table row.
	 */
	$('#tableContainer').on('click', 'tr', function() {
		$(this).addClass('warning');
		highlightedRowIndex = $(this).index();
		var data = workflow[highlightedRowIndex];
	
		if(data.operation=='LOAD') {
			$('#loadName').val(data.name);
			$('#fileName').val(data.relation);
			setModificationBehavior('#loadSubmit','#loadSubmitChange','#loadDialog','load');
		} else if(data.operation=='STORE'){
			$('#storeName').val(data.name);
			$('#relToStore').val(data.relation);
			setModificationBehavior('#storeSubmit','#storeSubmitChange','#storeDialog','store');
		} else if(data.operation=='FILTER') {
			$('#filtName').val(data.name);
			$('#filtRel').val(data.relation);
			$('#filtOpt').val(data.options);
			setModificationBehavior('#filterSubmit','#filterSubmitChange','#filterDialog','filter');
		} else if(data.operation=='JOIN') {
			$('#joinName').val(data.name);
			$('#joinRel').val(data.relation);
			$('#joinOpt').val(data.options);
			$('#joinRel2').val(data.relation2);
			$('#joinOpt2').val(data.options2);
			setModificationBehavior('#joinSubmit','#joinSubmitChange','#joinDialog','join');
		}
		
		//flow should not come to this point...
	});
	
	function setModificationBehavior(submitBtn, submitChangeBtn, dialog, operation) {
		$(submitBtn).addClass('hide');
		$(submitChangeBtn).addClass('modification');
		$(submitChangeBtn).removeClass('hide');
		$(dialog).show('slow');
		hideInputDialogs(operation);
	}
	
	/**
	 * Hides all input-dialog-forms except the form given to the function.
	 */
	function hideInputDialogs(elem) {
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
	}
	
	$('#saveWorkflow').on('submit',function() {
		var filename = $('#saveWorkflowName').val();
		if(!inputLongEnough(filename)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
			return false;
		}
		
		$('#inputError').hide('slow');
		workflow.push(filename);
		var data = JSON.stringify(workflow);
		
		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/ser',
    	    data: data,
    	    dataType: 'json',
    	    success: function(response) {
    	    	console.log(response);
    	    	if(response.success) {
    	    		$('#modalHeaderContent').html('<h3>Saving...</h3>');
    	    		$('#msg').html('Your workflow was saved successfully!');
					$('#successModal').modal('show');
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
	});
		
	$('#loadWorkflow').on('submit', function() {
		var filename = $('#loadWorkflowName').val();
		if(!inputLongEnough(filename)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
			return false;
		}

		$('#inputError').hide('slow');
		var data = '{"filename":"' + filename + '"}';
		
		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/ld',
    	    data: data,
    	    dataType:'json',
    	    success: function(response) {
    	    	if(response.success) {
    	    		initializeLoadedWorkflow(response.data);
    	    		$('#modalHeaderContent').html('<h3>Loading...</h3>');
    	    		$('#msg').html('Your workflow was loaded successfully!');
					$('#successModal').modal('show');
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
		
		$('#loadWorkflowName').val('');
		$('#saveWorkflow').show("fast");
		return false;
	}); 
	
	function initializeLoadedWorkflow(data) {
		workflow = data;
		nameCounter = workflow.length;
		showTable();
	}
    
});