$(document).ready(function() {
	
	/**
	 * Sends an ajax-Request to the server. The returned json-object is parsed  
	 * into a html-table with the help of the convertJsonToTable-function.
	 */
    $("#myForm").on('submit', function() {
    	var myJSONString = '[{"relation":"a","operation":"FILTER","relation2":"-","options":"chrom==20"},{"relation":"c","operation":"JOIN","relation2":"d","options":"c.id==d.id"},{"relation":"f","operation":"FILTER","relation2":"-","options":"pos>10000"}]';
    	
    	$.ajax({
    		type: "POST",
//    	    url: "http://localhost:8080/jobs",
    	    url: "http://localhost:8080/ser",
    	    data: myJSONString,
//    	    data: $(this).serialize(),
    	    dataType: "json",
    	    success: function(response) {
    	    	console.log(response);
    	    	if(response.success) { // show table
    	    		var tab = convertJsonToTable(response.data, "operationTable", "table table-striped table-hover");
    	    		$("#tableContainer").html(tab);
    	    	} else { // show modal dialogue
    	    		
    	    	}
    	    }
    	
    		//add error...
    	});
    	return false;
    });
    
});