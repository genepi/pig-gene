/**
 * JSON to HTML table conversion.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

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
    //pattern for table                          
	var idMarkup = tableId ? ' id="' + tableId + '"' : '';
    var classMarkup = tableClassName ? ' class="' + tableClassName + '"' : '';
    var tbl = '<table' + idMarkup + classMarkup + '>{0}{1}</table>';

    //patterns for table content
    var counter = 1;
    var th = '<thead>{0}</thead>';
    var tb = '<tbody>{0}</tbody>';
    var tr = '<tr class="tablerow">{0}</tr>';
    var thRow = '<th>{0}</th>';
    var tdRow = '<td>{0}</td>';
    var tdRow2 = '<td colspan={0}>{1}</td>';
    var thCon = '';
    var tbCon = '';
    var trCon = '';
    
    //guarantees fixed column order
    var headers = new Array();
    headers.push('name');
    headers.push('relation');
    headers.push('operation');
    headers.push('relation2');
    headers.push('options');
    headers.push('options2');

    for (i = 0; i < headers.length; i++) {
        thCon += thRow.format(headers[i]);
    }

    for (i = 0; i < parsedJson.length; i++) {
        for (j = 0; j < headers.length; j++) {
            var value = parsedJson[i][headers[j]];
            if(value == 'script') {
            	tbCon += tdRow.format('script' + counter++);
            	for(k = j; k < headers.length-1; k++) {
            		tbCon += tdRow.format('');
            	}
            	j = headers.length;
            } else {
            	tbCon += tdRow.format(value);
            }
        }
        trCon += tr.format(tbCon);
    	tbCon = '';
    }
    
    th = th.format(tr.format(thCon));
    tb = tb.format(trCon);
    tbl = tbl.format(th, tb);
    return tbl;
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