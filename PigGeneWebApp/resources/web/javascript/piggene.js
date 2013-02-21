$(document).ready(function() {
	var workflow = [];
	var description = "";
	
	var nameCounter = 1;
	var highlightedRowIndex = -1;
//	var downloadPossible = false;
	var forceDownload = false;
	var stdContent = $('#tableContainer').html();
	
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
		setCurrentOperation('USER DEFINED SCRIPT');
		cleanModificationDialogs();
		$('#scriptDialog').show('slow');
		$('#scriptDialog').removeClass('hide');
		hideInputDialogs('script');
		modifyContainerHeight();
	});
	
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
	
	$('#registerDialog').on('submit', function() {
//		downloadPossible = false;
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
//		downloadPossible = false;
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
//		downloadPossible = false;
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
//		downloadPossible = false;
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
//		downloadPossible = false;
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
//		downloadPossible = false;
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
		$('#scriptTextarea').val('');
		modifyContainerHeight();
		return false;
	});
	
	$('#saveWfBtn').on('click', function() {
		var workflowName = $('#workflowName').html().trim();
		$('#saveDialogInput').val('');
		if('unnamed' != workflowName) {
			$('#saveDialogInput').val(workflowName);
		}
		$('#saveNameModal').modal('show');
	});
	
	$('#saveWorkflow').on('submit', function() {
		var filename = $('#saveDialogInput').val();
		$('#saveNameModal').modal('hide');
		if(!inputLongEnough(filename)) {
			showInputErrorMsg('Inputs have to be at least 2 characters long. Please click the save button again and type <br>a longer name.');
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
	
	$('#newWfBtn').on('click', function() {
		$('#workflowName').addClass('new');
		prepareContainers();
	});
	
	$('#actionBtns').on('click', 'a.fileNames', function() {
		var fileName = $(this).html();
		$('#comments').val('');
		$('#lineDetails').addClass('hide');
		if($('#showWfBtn').hasClass('showWfBtnPopover')) {
			loadWorkflow(fileName);
			$('#showWfBtn').popover('hide').removeClass('pop').removeClass('showWfBtnPopover');
			$('#description').val(description);
		} else {
			deleteWorkflow(fileName);
			$('#deleteWfBtn').popover('hide').removeClass('pop').removeClass('deleteWfBtnPopover');
			if(fileName == $('#workflowName').html().trim()) {
				resetInitialState();
				resetStandardBehaviorForAll();
//				downloadPossible = false;
				workflow = [];
			}
		}
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
    	    		setStandardBehaviorSuccessModal();
//					downloadPossible = true;
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
	
	function setStandardBehaviorSuccessModal() {
		$('#closeSuccModal').removeAttr('download').removeAttr('href');
		$('#successModal').modal('show');
	}
	
	function setModificationBehaviorSuccessModal(filename) {
		$('#closeSuccModal').attr('download', filename + '.pig');
		$('#closeSuccModal').attr('href', 'http://localhost:8080/dwld/' + filename);
		$('#successModal').modal('show');
	}
	
	$('#closeSuccModal').on('click', function() {
		$('#successModal').modal('hide');
	});
	
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
	
	function prepareContainers() {
		$('#workflowContainer').removeClass('span12').addClass('span10');
		$('#formContainer').removeClass('hide');
		$('#stepAction').removeClass('hide');
		$('#workflowOps').html('OPERATIONS');
		resetStandardBehaviorForAll();
		$('#saveState').addClass('saved');
		toggleSaveStateVisualisation();
	}
	
	function initializeButtons() {
		$('#downloadScript').removeClass('hide');
		$('#saveWfBtn').removeClass('hide');
		$('#descriptionBtn').removeClass('hide');
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
    	    		resetStandardBehavior($('#workflowOps').html().toLowerCase());
    	    		$('#stepAction').removeClass('hide');
    	    		$('#workflowOps').html('OPERATIONS');
    	    		$('#workflowName').html(filename);
					$('#saveState').addClass('saved');
					toggleSaveStateVisualisation();
//					downloadPossible = true;
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
	
//	$('#closeSuccModal').on('click', function() {
//		
//	});
	
	/**
	 * Cancellation-Handling of input forms.
	 */
	$("button[type='reset']").on('click', function() {
		if($(this).hasClass('delete')) {
			$('#inputError').hide();
//			$('#saveState').removeClass('saved');
//			toggleSaveStateVisualisation();
			workflow.splice(highlightedRowIndex,1);
			if(workflow.length == 0) {
				resetInitialState();
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
		$('#stepAction').removeClass('hide');
		$('#workflowOps').html('OPERATIONS');
		$('#inputError').hide();
		modifyContainerHeight();
	});
	
	function resetInitialState() {
		$('#tableContainer').html(stdContent);
		$('#workflowName').html('workflow');
		$('#saveState').removeClass('saved');
		$('#saveState').html('');
		$('#saveWfBtn').addClass('hide');
		$('#downloadScript').addClass('hide');
		$('#descriptionBtn').addClass('hide');
		$('#workflowDescription').addClass('hide');
		$('#workflowContainer').removeClass('span10').addClass('span12');
		$('#formContainer').addClass('hide');
		description = '';
		$('#description').val(description);
	}
	
	/**
	 * Method handles a user request for already existing workflow definitions. Ajax request returns all
	 * existing workflow names. These names get converted into links and are shown in a popover. The user
	 * can select one of the links to avoid typing it manually.
	 */
	$('#showWfBtn').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		handleWorkflowRequest('#showWfBtn');
	});
	
	$('#deleteWfBtn').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		handleWorkflowRequest('#deleteWfBtn');
	});
	
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
	
	$('#descriptionBtn').on('click', function() {
		if($('#workflowDescription').hasClass('hide')) {
			$('#workflowDescription').removeClass('hide');
			$('#description').val(description);
			$('#descriptionIcon').removeClass('icon-plus-sign').addClass('icon-minus-sign');
		} else {
			$('#workflowDescrClear').trigger('click');
		}
	});
	
	$('#workflowDescrSubmit').on('click', function() {
		description = $('#description').val();
//		downloadPossible = false;
		$('#saveState').removeClass('saved');
		toggleSaveStateVisualisation();
		descriptionButtonsHandling();
	});

	$('#workflowDescrClear').on('click', function() {
		$('#description').val(description);
		descriptionButtonsHandling();
	});
	
	$('#lineCommentSubmit').on('click', function() {
		blinkEffectComments();
		var newValue = $('#comments').val();
		if(newValue == '') {
			workflow[highlightedRowIndex].comment = '-';
		} else {
			workflow[highlightedRowIndex].comment = newValue;
		}
//		downloadPossible = false;
		$('#saveState').removeClass('saved');
		toggleSaveStateVisualisation();
	});
	
	$('#lineCommentClear').on('click', function() {
		var oldComment = workflow[highlightedRowIndex].comment;
		if(oldComment == '-') {
			$('#comments').val('');
		} else {
			$('#comments').val(oldComment);
		}
	});
	
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
			setCurrentOperation('USER DEFINED SCRIPT');
		}
	});
	
	$('#orderUp').hover(function() {
		$('#up').toggleClass('icon-white');
	});
	
	$('#orderUp').on('click', function() {
		if(~highlightedRowIndex && highlightedRowIndex!=0 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')').hasClass('warning')) {
//			downloadPossible = false;
			var tmp = workflow[highlightedRowIndex-1];
			workflow[highlightedRowIndex-1] = workflow[highlightedRowIndex];
			workflow[highlightedRowIndex] = tmp;
			showTable();
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex)+')').addClass('warning');
			highlightedRowIndex--;
			$('#saveState').removeClass('saved');
			toggleSaveStateVisualisation();
		}
	});
	
	$('#orderDown').hover(function() {
		$('#down').toggleClass('icon-white');
	});
	
	$('#orderDown').on('click', function(){
		var rowCount = $('#operationTable tr').length;
		if (~highlightedRowIndex && highlightedRowIndex!=rowCount-2 && $('#operationTable tbody tr:nth-child('+(highlightedRowIndex+1)+')')) {
//			downloadPossible = false;
			var tmp = workflow[highlightedRowIndex+1];
			workflow[highlightedRowIndex+1] = workflow[highlightedRowIndex];
			workflow[highlightedRowIndex] = tmp;
			showTable();
			$('#operationTable tbody tr:nth-child('+(highlightedRowIndex+2)+')').addClass('warning');
			highlightedRowIndex++;
			$('#saveState').removeClass('saved');
			toggleSaveStateVisualisation();
		}
	});
	
	$('#downloadScript').on('click', function() {
		if($('#saveState').hasClass('saved')) {
			var filename = $('#workflowName').html();
			$('#downloadScript').attr('download', filename + '.pig');
			$('#downloadScript').attr('href', 'http://localhost:8080/dwld/' + filename).click();
		} else {
			$('#downloadScript').removeAttr('download').removeAttr('href');
			forceDownload = true;
			$('#saveWfBtn').trigger('click');
		}
	});
	
	
	/**
	 * UTIL functions
	 */

	function initializeLoadedWorkflow(data) {
		description = data.description;
		workflow = data.workflow;
		nameCounter = workflow.length;
		showTable();
	}

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

	function descriptionButtonsHandling() {
		$('#workflowDescription').addClass('hide');
		$('#descriptionIcon').removeClass('icon-minus-sign').addClass('icon-plus-sign');
	}

	function removeTableRowLabeling(label) {
		$('#tableContainer tr').each(function(){
			$(this).removeClass(label);
		});
	}

	function hideLargeContainers() {
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
	 * Calls a helper function to convert the workflow-object into a html table and displays the result.
	 */
	function showTable() {
		var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
		$('#tableContainer').html(tab);
	}

	/**
	 * Method is called to perform a cleanup after the submit action has taken place.
	 * Hides the input error dialog and the input form, reloads the workflow table and 
	 * clears the input fields. Displays the save form.
	 */
	function finalizeSubmit(obj) {
		$('#inputError').hide();
		showTable();
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
    
});