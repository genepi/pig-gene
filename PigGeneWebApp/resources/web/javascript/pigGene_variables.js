/**
 * Globally used variables.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

//saves the information-content of the table container
var stdContent = '';

//saves the complete workflow definition
var workflow = [];

//saves the names of the relations used by the typeahead feature
var typeaheadRelations = [];

//saves the names of the "used" relation names - to find indicate
//that "unused" Statements are useless for the user
var usedRelations = [];

//saves the names of the pre installed workflows - they do not
//show up in the "delete" section, therefore they cannot be deleted
var undeletableWorkflows = ["filterExample","joinExample"];

//saves the workflow definition
var description = '';

//index of highlighted line in the workflow table
var highlightedRowIndex = -1;

//indicates that unsaved workflow should be downloaded
var forceDownload = false;

//saves the name of the workflow that should be loaded/deleted
var loadDeleteWorkflowName = '';