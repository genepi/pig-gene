$(document).ready(function() {
	
	var workflow = [];
	var nameCounter = 1;
	
	
	//TODO: check if all values are given correctly
	/**
	 * get form data & show table update...
	 */
	$('#selectionForm').on('submit', function() {
		var values = $('#selectionForm').serializeArray();
		var oper = values[0].value;
		var name = values[1].value;
		var rel1 = values[2].value;
		var rel2 = values[3].value;
		var opt = values[4].value;
		var opt2 = values[5].value;
		
		if(name == null || name == "") {
			name = "R" + nameCounter;
		}
		nameCounter++;
		
		workflow.push({name:name, relation:rel1, operation:oper, relation2:rel2, options:opt, options2:opt2});
//		console.log(workflow);
		showTable();
		return false;
	});
	
	/**
	 * show Table
	 */
	function showTable() {
		var tab = convertJsonToTable(workflow, "operationTable", "table table-striped table-hover");
		$("#tableContainer").html(tab);
	}
	
	
	/**
	 * send data to the server
	 */
	$('#sendToServer').on('click',function() {
		var data = JSON.stringify(workflow);
		console.log(data);

		$.ajax({
    		type: "POST",
    	    url: "http://localhost:8080/ser",
    	    data: data,
    	    dataType: "json",
    	    success: function(response) {
    	    	if(response.success) {
					$("#successModal").modal('show');
    	    	} else {
    	    		$("#errormsg").html(response.message);
    	    		$("#errorModal").modal('show');
    	    	}
    	    },
    	    error: function (xhr, ajaxOptions, thrownError) {
    	    	$("#errormsg").html(xhr.responseText);
	    		$("#errorModal").modal('show');
    	   }
    	});
	});
			
	
	/*
	//TODO: check the javadoc...
	/**
	 * Sends an ajax-Request to the server. The returned json-object is parsed  
	 * into a html-table with the help of the convertJsonToTable-function.
	 
    $("#myForm").on('submit', function() {
    	var myJSONString = '[{"relName":"R1","relation":"a","operation":"FILTER","relation2":"-","options":"chrom==20"},{"relName":"R2","relation":"c","operation":"JOIN","relation2":"d","options":"id","options2":"id1"},{"relName":"R3","relation":"f","operation":"FILTER","relation2":"-","options":"pos>10000"}]';
   	
    	$.ajax({
    		type: "POST",
//    	    url: "http://localhost:8080/jobs",
    	    url: "http://localhost:8080/ser",
    	    data: myJSONString,
//    	    data: $(this).serialize(),
    	    dataType: "json",
    	    success: function(response) {
    	    	if(response.success) { // show table
    	    		var tab = convertJsonToTable(response.data, "operationTable", "table table-striped table-hover");
    	    		$("#tableContainer").html(tab);
    	    	} else { // show modal dialogue
    	    		$("#errormsg").html(response.message);
    	    		$("#errorModal").modal('show');
    	    	}
    	    }
    	
    		//add error...
    	});
    	return false;
    });
    */
    
    
});