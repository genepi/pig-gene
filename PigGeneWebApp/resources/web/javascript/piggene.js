$(document).ready(function() {
	
	/**
	 * Sends an ajax-Request to the server. The returned json-object is parsed  
	 * into a html-table with the help of the convertJsonToTable-function.
	 */
    $("#myForm").on('submit', function() {
    	
//    	console.log($("#name"));
    	var myJSONString = '[{"relation":"a","operation":"FILTER","relation2":"-","options":"chrom==20"},{"relation":"c","operation":"JOIN","relation2":"d","options":"c.id==d.id"},{"relation":"f","operation":"FILTER","relation2":"-","options":"pos>10000"}]';
    	
    	$.ajax({
    		type: "POST",
//    	    url: "http://localhost:8080/jobs",
    	    url: "http://localhost:8080/ser",
    	    data: myJSONString,
//    	    data: $(this).serialize(),
    	    dataType: "json",
    	    success: function(data) {
    	    	if(data.success) {
    	    		
    	    	}
    	    	
    	    	
    	    	$("#tableContainer").html(data);
//    	    	var tab = convertJsonToTable(data, "operationTable", "table table-striped table-hover");
//    	    	$("#tableContainer").html(tab);
    	    }
    	});
    	return false;
    });
    
    /**
     * Convert a Javascript Object array or String array to an HTML table.
     * JSON parsing has to be made before function call; that allows use 
     * of other JSON parsing methods like jQuery.parseJSON.
     * 
     * @param parsedJson JSON data
     * @param tableId - String
     * @param tableClassName table css class name - String
     * 
     * original:
     * @author Afshin Mehrabani <afshin dot meh at gmail dot com>
     * 
     * modified by:
     * @author Clemens Banas
     * 
     * @return String - converted JSON to HTML table
     */
    function convertJsonToTable(parsedJson, tableId, tableClassName) {
        //Pattern for table                          
    	var idMarkup = tableId ? ' id="' + tableId + '"' : '';
        var classMarkup = tableClassName ? ' class="' + tableClassName + '"' : '';
        var tbl = '<table' + idMarkup + classMarkup + '>{0}{1}</table>';

        //Patterns for table content
        var th = '<thead>{0}</thead>';
        var tb = '<tbody>{0}</tbody>';
        var tr = '<tr>{0}</tr>';
        var thRow = '<th>{0}</th>';
        var tdRow = '<td>{0}</td>';
        var thCon = '';
        var tbCon = '';
        var trCon = '';

        if (parsedJson) {
            var isStringArray = typeof(parsedJson[0]) == 'string';
            var headers;

            // Create table header from JSON data
            if(isStringArray) { // If JSON data is a string array a single table header is created
                thCon += thRow.format('value');
            } else { // If JSON data is an object array, headers are computed
                if(typeof(parsedJson[0]) == 'object') {
                    headers = array_keys(parsedJson[0]);
                    for (i = 0; i < headers.length; i++) {
                        thCon += thRow.format(headers[i]);
                    }
                }
            }
            th = th.format(tr.format(thCon));
            
            // Create table rows from JSON data
            if(isStringArray) { //JSON data is a string array
                for (i = 0; i < parsedJson.length; i++) {
                    tbCon += tdRow.format(parsedJson[i]);
                    trCon += tr.format(tbCon);
                    tbCon = '';
                }
            } else {
                if(headers) { //JSON data is an object array
                    for (i = 0; i < parsedJson.length; i++) {
                        for (j = 0; j < headers.length; j++) {
                            var value = parsedJson[i][headers[j]];
                            if(value) {
                            	if(typeof(value) == 'object') {
                            		//for supporting nested tables
                            		tbCon += tdRow.format(ConvertJsonToTable(eval(value.data), value.tableId, value.tableClassName, value.linkText));
                            	} else {
                            		tbCon += tdRow.format(value);
                            	}
                                
                            } else {    // If value == null we format it like PhpMyAdmin NULL values
                                tbCon += tdRow.format(italic.format(value).toUpperCase());
                            }
                        }
                        trCon += tr.format(tbCon);
                    	tbCon = '';
                    }
                }
            }
            tb = tb.format(trCon);
            tbl = tbl.format(th, tb);
            return tbl;
        }
        return null;
    }
    
    
    /**
     * Return just the keys from the input array, optionally only for the specified search_value
     * version: 1109.2015
     *  discuss at: http://phpjs.org/functions/array_keys
     *  +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
     *  +      input by: Brett Zamir (http://brett-zamir.me)
     *  +   bugfixed by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
     *  +   improved by: jd
     *  +   improved by: Brett Zamir (http://brett-zamir.me)
     *  +   input by: P
     *  +   bugfixed by: Brett Zamir (http://brett-zamir.me)
     *  *     example 1: array_keys( {firstname: 'Kevin', surname: 'van Zonneveld'} );
     *  *     returns 1: {0: 'firstname', 1: 'surname'}
     */
    function array_keys(input, search_value, argStrict) {
        var search = typeof search_value !== 'undefined', tmp_arr = [], strict = !!argStrict, include = true, key = '';
        if (input && typeof input === 'object' && input.change_key_case) { // Duck-type check for our own array()-created PHPJS_Array
            return input.keys(search_value, argStrict);
        }
     
        for (key in input) {
            if (input.hasOwnProperty(key)) {
                include = true;
                if (search) {
                    if (strict && input[key] !== search_value) {
                        include = false;
                    } else if (input[key] != search_value) {
                        include = false;
                    }
                } 
                if (include) {
                    tmp_arr[tmp_arr.length] = key;
                }
            }
        }
        return tmp_arr;
    }
    
    /**
     * JavaScript format string function
     */
    String.prototype.format = function() {
      var args = arguments;
      return this.replace(/{(\d+)}/g, function(match, number) {
        return typeof args[number] != 'undefined' ? args[number] : '{' + number + '}';
      });
    };
    
});