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
	
	$('#loadDialog').on('submit',function() {
		var values = $('#loadDialog').serializeArray();
		var oper = 'LOAD';
		var name = values[0].value;
		var rel = values[1].value;
		
		if(name == null || name == '') {
			name = getArtificialName();
		}
		
		workflow.push({name:name, relation:rel, operation:oper, relation2:'-', options:'-', options2:'-'});
		showTable();
		showSaveWorkflowButton();
		$(this).hide('slow');
		//TODO: clear the input fields...
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
		showTable();
		showSaveWorkflowButton();
		$(this).hide('slow');
		//TODO: clear the input fields...
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
		showTable();
		showSaveWorkflowButton();
		$(this).hide('slow');
		//TODO: clear the input fields...
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
		showTable();
		showSaveWorkflowButton();
		$(this).hide('slow');
		//TODO: clear the input fields...
		return false;
	});
	
	function getArtificialName() {
		return 'R' + nameCounter++;
	}
	
	function showTable() {
		var tab = convertJsonToTable(workflow, 'operationTable', 'table table-striped table-hover');
		$('#tableContainer').html(tab);
	}
	
	function showSaveWorkflowButton() {
		$('#saveWorkflow').show("fast");
	}
	
	/**
	 * send data to the server
	 */
	$('#saveWorkflow').on('click',function() {
		var data = JSON.stringify(workflow);
		console.log(data);

		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/ser',
    	    data: data,
    	    dataType: 'json',
    	    success: function(response) {
    	    	if(response.success) {
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
	});
	
	/**
	 * loads data from the server
	 */
	$('#loadWorkflow').on('click', function() {
		$.ajax({
    		type: 'POST',
    	    url: 'http://localhost:8080/ld',
    	    data: null,
    	    success: function(response) {
    	    	if(response.success) {
    	    		//TODO: load workflow in background
    	    		
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
	}); 
    
});