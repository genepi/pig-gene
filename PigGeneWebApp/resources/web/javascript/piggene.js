$(document).ready(function() {
	
	//TODO: check if all values are given correctly
	//TODO: when deleting a line - check if workflow array is empty - if so: hide save button
	
	var workflow = [];
	var nameCounter = 1;
	
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
	
	$("button[type='reset']").on('click', function() {
		hideInputDialogs('all');
		$('#inputError').hide();
	});
	
	function showInputErrorMsg(errText) {
		$('#inputErrMsg').html(errText);
		$('#inputError').show('slow');
	}
	
    $('input[type=text]').on('change invalid', function() {
        var textfield = $(this).get(0);
        textfield.setCustomValidity('');
        
        if (!textfield.validity.valid) {
          textfield.setCustomValidity('this field cannot be left blank');  
        }
    }); 
	
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
		
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'});
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
		
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'});
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
		
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:opt, options2:'-'});
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
		
		workflow.push({name:name, relation:rel1, operation:oper, relation2:rel2, options:opt1, options2:opt2});
		finalizeSubmit(this);
		return false;
	});
	
	function getArtificialName() {
		return 'R' + nameCounter++;
	}
	
	function inputLongEnough(input) {
		if(input.length < 2) {
			return false;
		}
		return true;
	}
	
	function finalizeSubmit(obj) {
		$('#inputError').hide();
		showTable();
		$('#saveWorkflow').show("fast");
		$(obj).hide('slow');
		$(obj).children('input[type=text]').val('');
	}
	
	function showTable() {
		var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
		$('#tableContainer').html(tab);
	}
	
	/**
	 * send data to the server
	 */
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
			content += '<a href="javascript:setSelectedFileName(\''+name+'\');" class="fileNames">'+name+'</a><br>'
		}
		return content;
	}
	

	
	/**
	 * loads data from the server
	 */
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
		$('#saveWorkflowName').val('');
		$('#saveWorkflow').show("fast");
		return false;
	}); 
	
	function initializeLoadedWorkflow(data) {
		workflow = data;
		nameCounter = workflow.length;
		showTable();
	}
    
});

function setSelectedFileName(fileName) {
	$('#loadWorkflowName').val(fileName);
	$('#showWfBtn').popover('hide').removeClass('pop');
}