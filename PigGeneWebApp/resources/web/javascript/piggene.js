$(document).ready(function() {
	var workflow = [];
	var description = "";
	
	var nameCounter = 1;
	var highlightedRowIndex = -1;
	var downloadPossible = false;
	var stdContent = $('#tableContainer').html();
	
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
	
	$('#userScriptLink').on('click', function() {
		setCurrentOperation('USER SCRIPT');
		cleanModificationDialogs();
		$('#scriptDialog').show('slow');
		$('#scriptDialog').removeClass('hide');
		hideInputDialogs('script');
		modifyContainerHeight();
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
		modifyDialog('script');
		$('#inputError').hide();
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
			$('#inputError').hide();
			workflow.splice(highlightedRowIndex,1);
			if(workflow.length == 0) {
				$('#tableContainer').html(stdContent);
				$('#workflowName').html('Workflow');
				$('#saveWorkflowName').val('');
				$('#saveWorkflow').addClass('hide');
				$('#downloadScript').addClass('hide');
				$('#workflowDescription').addClass('hide');
				$('#descriptionBtn').addClass('hide');
				description = '';
				$('#description').val(description);
			} else {
				showTable();
			}
		} else {
			$(this).removeClass('modification');
		}
		
		var buttonName = $(this).attr('id');
		if(buttonName.indexOf('register') == 0) {
			resetStandardBehavior('register');
		} else if(buttonName.indexOf('load') == 0) {
			resetStandardBehavior('load');
			$('#loadFiletypeSeparator.btn-group').css('display','none');
			$('#loadVcf').addClass('active');
			$('#loadTxt').removeClass('active');
		} else if (buttonName.indexOf('store') == 0) {
			resetStandardBehavior('store');
		} else if (buttonName.indexOf('filter') == 0) {
			resetStandardBehavior('filter');
		} else if (buttonName.indexOf('join') == 0) {
			resetStandardBehavior('join');
		} else if (buttonName.indexOf('script') == 0) {
			$('#scriptDialog').addClass('hide');
			resetStandardBehavior('script');
		}
		$('#comments').val('');
		$('#stepAction').removeClass('hide');
		$('#workflowOps').html('OPERATIONS');
		$('#inputError').hide();
	});
	
	function showInputErrorMsg(errText) {
		$('#inputErrMsg').html(errText);
		$('#inputError').show('slow');
	}
	
	$('#registerDialog').on('submit', function() {
		downloadPossible = false;
		var values = $('#registerDialog').serializeArray();
		var oper = 'REGISTER';
		var rel = values[0].value;
		var comm = $('#comments').val();
		if(!inputLongEnough(rel)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
			return false;
		}
		if(comm == null || comm == ''){
			comm = '-';
		}
		
		if($('#registerSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:'-', relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm};
			resetStandardBehavior('register');
		} else {
			workflow.push({name:'-', relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#loadVcf').on('click', function() {
		$('#loadFiletypeSeparator.btn-group').css('display','none');
	});
	
	$('#loadTxt').on('click', function() {
		$('#loadFiletypeSeparator.btn-group').css('display','inline-block');
	});
	
	$('#loadDialog').on('submit', function() {
		downloadPossible = false;
		var values = $('#loadDialog').serializeArray();
		var oper = 'LOAD';
		var name = values[0].value;
		var rel = values[1].value;
		var comm = $('#comments').val();
		if(!inputLongEnough(rel)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
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
			resetStandardBehavior('load');
		} else {
			workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:opt2, comment:comm});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#storeDialog').on('submit', function() {
		downloadPossible = false;
		var values = $('#storeDialog').serializeArray();
		var oper = 'STORE';
		var name = values[0].value;
		var rel = values[1].value;
		var comm = $('#comments').val();
		if(!inputLongEnough(name) || !inputLongEnough(rel)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
			return false;
		}
		if(comm == null || comm == ''){
			comm = '-';
		}
		
		if($('#storeSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm};
			resetStandardBehavior('store');
		} else {
			workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-', comment:comm});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#filterDialog').on('submit', function() {
		downloadPossible = false;
		var values = $('#filterDialog').serializeArray();
		var oper = 'FILTER';
		var name = values[0].value;
		var rel = values[1].value;
		var opt = values[2].value;
		var comm = $('#comments').val();
		if(!inputLongEnough(rel) || !inputLongEnough(opt)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
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
			resetStandardBehavior('filter');
		} else {
			workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:'-', comment:comm});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#joinDialog').on('submit', function() {
		downloadPossible = false;
		var values = $('#joinDialog').serializeArray();
		var oper = 'JOIN';
		var name = values[0].value;
		var rel1 = values[1].value;
		var opt1 = values[2].value;
		var rel2 = values[3].value;
		var opt2 = values[4].value;
		var comm = $('#comments').val();
		if(!inputLongEnough(rel1) || !inputLongEnough(opt1) || !inputLongEnough(rel2) || !inputLongEnough(opt2)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please change short input and press the add button again.');
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
			resetStandardBehavior('join');
		} else {
			workflow.push({name:name, relation:rel1, operation:oper, relation2:rel2, options:opt1, options2:opt2, comment:comm});
		}
		finalizeSubmit(this);
		return false;
	});
	
	$('#scriptDialog').on('submit', function() {
		var script = $('#scriptTextarea').val();
		var oper = 'SCRIPT';
		var comm = $('#comments').val();
		if(comm == null || comm == ''){
			comm = '-';
		}
		if($('#scriptSubmitChange').hasClass('modification')) {
			workflow[highlightedRowIndex] = {name:'script', relation:'-', operation:oper, relation2:'-', options:script, options2:'-', comment:comm};
			resetStandardBehavior('script');
		} else {
			workflow.push({name:'script', relation:'-', operation:oper, relation2:'-', options:script, options2:'-', comment:comm});
		}
		finalizeSubmit(this);
		$('#scriptDialog').addClass('hide');
		modifyContainerHeight();
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
		var dialog = '#' + operation + 'Dialog';
		
		$(submitBtn).removeClass('hide');
		$(submitChangeBtn).removeClass('modification');
		$(submitChangeBtn).addClass('hide');
		$(deleteBtn).addClass('hide');
		$(dialog).addClass('hide');
		hideInputDialogs('all');
		$('#lineDetails').addClass('hide');
		modifyContainerHeight();
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
		$('#descriptionBtn').removeClass('hide');
		$('#downloadScript').removeClass('hide');
		$('#saveWorkflow').removeClass('hide');
		$('#stepAction').removeClass('hide');
		$('#workflowOps').html('OPERATIONS');
		$(obj).hide('slow');
		$(obj).children('input[type=text]').val('');
		$('#comments').val('');
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
	
	$('#tableContainer').on('click', function(e) {
		var target = $(e.target);
		if(target != null && target.context != null && target.context.tagName != null && target.context.tagName == 'DIV') {
			$('#registerClear').trigger('click');
		}
	});
	
	/**
	 * Modification-Handling of table row.
	 */
	$('#tableContainer').on('click', 'tr', function() {
		removeTableRowLabeling('warning');
		hideLargeContainers();
		$(this).addClass('warning');
		highlightedRowIndex = $(this).index();
		var data = workflow[highlightedRowIndex];
		var comment = data.comment;
		if(comment == '-') {
			$('#comments').val('');
		} else {
			$('#comments').val(comment);
		}
	
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
		} else if(data.operation=='SCRIPT') {
			$('#scriptTextarea').val(data.options);
			setModificationBehavior('script');
		}
	});
	
	function removeTableRowLabeling(label) {
		$('#tableContainer tr').each(function(){
			$(this).removeClass(label);
		});
	}
	
	function hideLargeContainers() {
		$('#scriptDialog').addClass('hide');
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
	
	$('#orderUp').hover(function() {
		$('#up').toggleClass('icon-white');
	});
	
	$('#orderUp').on('click', function() {
		if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
			downloadPossible = false;
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
			downloadPossible = false;
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
		return false;
	});
	
	function showSecurityAlert(filename) {
		$('#saveCheckModal').modal('show');
		$('#overrideBtn').on('click', function() {
			$('#saveCheckModal').modal('hide');
			saveWorkflow(filename);
		});
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
    	    		resetStandardBehavior($('#workflowOps').html().toLowerCase());
    	    		$('#stepAction').removeClass('hide');
    	    		$('#workflowOps').html('workflow operations');
    	    		$('#workflowName').html(filename);
    	    		$('#modalHeaderContent').html('<h3>Saving...</h3>');
    	    		$('#msg').html('Your workflow was saved successfully!');
					$('#successModal').modal('show');
					downloadPossible = true;
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
	
	$('#descriptionBtn').on('click', function() {
		if($('#workflowDescription').hasClass('hide')) {
			$('#workflowDescription').removeClass('hide');
			$('#description').val(description);
			modifyContainerHeight();
		} else {
			$('#workflowDescrClear').trigger('click');
		}
	});
	
	$('#workflowDescrClear').on('click', function() {
		$('#description').val(description);
		descriptionButtonsHandling();
	});
	
	$('#workflowDescrSubmit').on('click', function() {
		description = $('#description').val();
		downloadPossible = false;
		descriptionButtonsHandling();
	});
	
	function descriptionButtonsHandling() {
		$('#workflowDescription').addClass('hide');
		modifyContainerHeight();
	}
	
	$('#lineCommentClear').on('click', function() {
		var oldComment = workflow[highlightedRowIndex].comment;
		if(oldComment == '-') {
			$('#comments').val('');
		} else {
			$('#comments').val(oldComment);
		}
	});
	
	$('#lineCommentSubmit').on('click', function() {
		blinkEffectComments();
		var newValue = $('#comments').val();
		if(newValue == '') {
			workflow[highlightedRowIndex].comment = '-';
		} else {
			workflow[highlightedRowIndex].comment = newValue;
		}
		downloadPossible = false;
	});
	
	function blinkEffectComments() {
		setTimeout(function() {
			$('#comments').focus();
		}, 100);
		setTimeout(function() {
			$('#comments').blur();
		}, 350);
	}

	function modifyContainerHeight() {
		if($('#workflowDescription').hasClass('hide') && $('#lineDetails').hasClass('hide') && $('#scriptDialog').hasClass('hide')) {
			//smallest possible illustration
			$('#tableContainer.well').css('min-height','288px');
			$('#formContainer.well').css('height','175px');
		} else if($('#workflowDescription').hasClass('hide') && $('#lineDetails').hasClass('hide')) {
			//larger because of script dialog window
			$('#tableContainer.well').css('min-height','396px');
			$('#formContainer.well').css('height','284px');
		} else if($('#workflowDescription').hasClass('hide') && $('#scriptDialog').hasClass('hide')) {
			//table container higher because of the lineDetails window
			$('#tableContainer.well').css('min-height','495px');
			$('#formContainer.well').css('height','175px');
		} else if($('#workflowDescription').hasClass('hide')) {
			//table container even higher - lineDetails and script dialog window
			$('#tableContainer.well').css('min-height','604px');
			$('#formContainer.well').css('height','284px');
		} else if($('#lineDetails').hasClass('hide') && $('#scriptDialog').hasClass('hide')) {
			//table container higher because of the workflowDescription window
			$('#tableContainer.well').css('min-height','466px');
			$('#formContainer.well').css('height','175px');
		} else if($('#lineDetails').hasClass('hide')) {
			//table container even higher - workflowDescription and script dialog window
			$('#tableContainer.well').css('min-height','575px');
			$('#formContainer.well').css('height','284px');
		} else {
			//highest - every window open
			$('#tableContainer.well').css('min-height','673px');
		}
	}
	
	$('#processElements').on('click', 'a.fileNames', function() {
		var fileName = $(this).html();
		loadWorkflow(fileName);
		$('#saveWorkflowName').val(fileName);
		$('#showWfBtn').popover('hide').removeClass('pop');
		$('#description').val(description);
		$('#comments').val('');
		$('#lineDetails').addClass('hide');
		$('#tableContainer.well').css('min-height','288px');
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
					downloadPossible = true;
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
		description = data.description;
		workflow = data.workflow;
		nameCounter = workflow.length;
		showTable();
	}
	
	$('#downloadScript').on('click', function() {
		if(downloadPossible) {
			var filename = $('#workflowName').html();
			$(this).attr('download', filename + '.pig');
			$(this).attr('href', 'http://localhost:8080/dwld/' + filename);
		} else {
			showInputErrorMsg('You have to save your workflow first.');
			return false;
		}
	});
    
});