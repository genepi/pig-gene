$(document).ready(function() {
	
	$(window).load(function(){
		stdContent = $('#tableContainer').html();
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
	
	$('#registerDialog').on('submit', function() {
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
	
	$('#workflowContainer').on('click', function(e) {
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
			$('#downloadScript').attr('href', 'http://localhost:8080/dwld/' + filename);
		} else {
			$('#downloadScript').removeAttr('download').removeAttr('href');
			forceDownload = true;
			$('#saveWfBtn').trigger('click');
		}
	});
	
	$('#closeSuccModal').on('click', function() {
		$('#successModal').modal('hide');
	});
	
});