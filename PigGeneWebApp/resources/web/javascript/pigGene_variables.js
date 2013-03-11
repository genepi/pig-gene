/**
 * Globally used variables.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

//saves the information-content of the table container
var stdContent = '';

//saves the name of the workflow that should be loaded/deleted
var loadDeleteWorkflowName = '';

//saves the complete workflow definition
var workflow = [];

//saves the workflow definition
var description = '';

//number is used to 'automatically' name relations 
//if the user does not provide a name
var nameCounter = 1;

//index of highlighted line in the workflow table
var highlightedRowIndex = -1;

//indicates that unsaved workflow should be downloaded
var forceDownload = false;