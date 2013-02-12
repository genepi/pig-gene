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
	
    $('#registerLink').on('click', function() {
    	setCurrentOperation('REGISTER');
		cleanModificationDialogs();
		$('#registerDialog').show('slow');
		hideInputDialogs('register');
	});
    
    $('#loadLink').on('click', function() {
    	setCurrentOperation('LOAD');
		cleanModificationDialogs();
		$('#loadDialog').show('slow');
		hideInputDialogs('load');
	});
	
	$('#storeLink').on('click', function() {
		setCurrentOperation('STORE');
		cleanModificationDialogs();
		$('#storeDialog').show('slow');
		hideInputDialogs('store');
	});

	$('#filterLink').on('click', function() {
		setCurrentOperation('FILTER');
		cleanModificationDialogs();
		$('#filterDialog').show('slow');
		hideInputDialogs('filter');
	});
	
	$('#joinLink').on('click', function() {
		setCurrentOperation('JOIN');
		cleanModificationDialogs();
		$('#joinDialog').show('slow');
		hideInputDialogs('join');
	});
	
	function setCurrentOperation(operation) {
		$('#stepAction').addClass('hide');
		$('#workflowOps').html(operation);
	}
	
	function cleanModificationDialogs() {
		modifyDialog('register');
		modifyDialog('load');
		modifyDialog('store');
		modifyDialog('filter');
		modifyDialog('join');
		$('#showWfBtn').popover('hide').removeClass('pop');
	}
	
	function modifyDialog(operation) {
		var obj = '#' + operation + 'Dialog';
		resetStandardBehavior(operation);
		$(obj).children('input[type=text]').val('');
	}
	
	/**
	 * Cancellation-Handling of input forms.
	 */
	$("button[type='reset']").on('click', function() {
		if($(this).hasClass('delete')) {
			workflow.splice(highlightedRowIndex,1);
			if(workflow.length == 0) {
				//TODO setzen des neuen contents...
				var content = '<h2>Workflow</h2>There is no workflow specified at the moment. Please create a new workflow by adding new operations in the panel on the left side or load an existing workflow if you want to modify it.<table id="operationTable"></table>';
				$('#tableContainer').html('<p>blub</p>');
				$('#saveWorkflow').addClass('hide');
			}
			$('#inputError').hide();
			showTable();
		} else {
			$(this).removeClass('modification');
		}
		
		var buttonName = $(this).attr('id');
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
		}	
		$('#stepAction').removeClass('hide');
		$('#workflowOps').html('workflow operations');
		$('#inputError').hide();
	});
	
	function showInputErrorMsg(errText) {
		$('#inputErrMsg').html(errText);
		$('#inputError').show('slow');
	}
	
	$('#registerDialog').on('submit', function() {
		var values = $('#registerDialog').serializeArray();
		var oper = 'REGISTER';
		var rel = values[0].value;
		if(!inputLongEnough(rel)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
			return false;
		}
		
		if($('#registerSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:'-', relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'};
			resetStandardBehavior('register');
		} else {
			workflow.push({name:'-', relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#loadDialog').on('submit', function() {
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
			resetStandardBehavior('load');
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
			resetStandardBehavior('store');
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
			resetStandardBehavior('filter');
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
			resetStandardBehavior('join');
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
	function resetStandardBehavior(operation) {
		if(~highlightedRowIndex) {
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').removeClass('warning');
		}
		
		var submitBtn = '#' + operation + 'Submit';
		var submitChangeBtn = '#' + operation + 'SubmitChange';
		var deleteBtn = '#' + operation + 'Delete';
		
		$(submitBtn).removeClass('hide');
		$(submitChangeBtn).removeClass('modification');
		$(submitChangeBtn).addClass('hide');
		$(deleteBtn).addClass('hide');
		hideInputDialogs('all');
		$('#orderBtns').addClass('hide');
		$('#tableContainer.well').css('min-height','288px');
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
		$('#downloadScript').removeClass('hide');
		$('#saveWorkflow').removeClass('hide');
		$('#stepAction').removeClass('hide');
		$('#workflowOps').html('workflow operations');
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
	
	function convertFilenamesToLinks(data) {
		var toRemove = '.yaml';
		var content = '';
		for(var i=0; i<data.length; i++) {
			var name = data[i].replace(toRemove,'');
			content += '<a class="fileNames">'+name+'</a><br>'
		}
		return content;
	}
	
	/**
	 * Modification-Handling of table row.
	 */
	$('#tableContainer').on('click', 'tr', function() {
		removeTableRowLabeling('warning');
		$(this).addClass('warning');
		highlightedRowIndex = $(this).index();
		var data = workflow[highlightedRowIndex];
	
		if(data.operation=='REGISTER') {
			$('#regFileName').val(data.relation);
			setModificationBehavior('register');
		} else if(data.operation=='LOAD') {
			$('#loadName').val(data.name);
			$('#fileName').val(data.relation);
			setModificationBehavior('load');
		} else if(data.operation=='STORE'){
			$('#storeName').val(data.name);
			$('#relToStore').val(data.relation);
			setModificationBehavior('store');
		} else if(data.operation=='FILTER') {
			$('#filtName').val(data.name);
			$('#filtRel').val(data.relation);
			$('#filtOpt').val(data.options);
			setModificationBehavior('filter');
		} else if(data.operation=='JOIN') {
			$('#joinName').val(data.name);
			$('#joinRel').val(data.relation);
			$('#joinOpt').val(data.options);
			$('#joinRel2').val(data.relation2);
			$('#joinOpt2').val(data.options2);
			setModificationBehavior('join');
		}
		
		//flow should not come to this point...
	});
	
	function removeTableRowLabeling(label) {
		$('#tableContainer tr').each(function(){
			$(this).removeClass(label);
		});
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
		hideInputDialogs(operation);
		setCurrentOperation(operation.toUpperCase());
		$('#orderBtns').removeClass('hide');
		$('#tableContainer.well').css('min-height','360px');
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
	}
	
	$('#orderUp').hover(function() {
		$('#up').toggleClass('icon-white');
	});
	
	$('#orderUp').on('click', function() {
		if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
			var tmp = workflow[highlightedRowIndex-1];
			workflow[highlightedRowIndex-1] = workflow[highlightedRowIndex];
			workflow[highlightedRowIndex] = tmp;
			showTable();
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex)+')').addClass('warning');
			highlightedRowIndex--;
		}
	});
	
	$('#orderDown').hover(function() {
		$('#down').toggleClass('icon-white');
	});
	
	$('#orderDown').on('click', function(){
		var rowCount = $('#operationTable tr').length;
		if (~highlightedRowIndex && highlightedRowIndex!=rowCount-2 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')')) {
			var tmp = workflow[highlightedRowIndex+1];
			workflow[highlightedRowIndex+1] = workflow[highlightedRowIndex];
			workflow[highlightedRowIndex] = tmp;
			showTable();
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+2)+')').addClass('warning');
			highlightedRowIndex++;
		}
		
	});
	
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
    	    	if(response.success) {
    	    		$('#workflowName').html(filename);
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
	
	$('#processElements').on('click', 'a.fileNames', function() {
		var fileName = $(this).html();
		loadWorkflow(fileName);
		$('#saveWorkflowName').val(fileName);
		$('#showWfBtn').popover('hide').removeClass('pop');
	});
	
	$('#downloadScript').on('click', function() {
		//TODO
	});
		
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

		$('#downloadScript').removeClass('hide');
		$('#saveWorkflow').removeClass('hide');
		return false;
	}
	
	function initializeLoadedWorkflow(data) {
		workflow = data;
		nameCounter = workflow.length;
		showTable();
	}
    
});