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
var sampleWorkflows = ["filterExample","joinExample"];

//is used to indicate that the workflow is defined and
//contains problems regarding use/naming of the relations
var workflowProblem = false;

//saves the workflow definition
var description = '';

//index of highlighted line in the workflow table
var highlightedRowIndex = -1;

var inputPrefix = 'input';
var outputPrefix = 'output';

var inputCounter = 0;
var outputCounter = 0;

//indicates that unsaved workflow should be downloaded
var forceDownload = false;

//indicates that unsafed workflow should be run on cloudgene
var forceRun = false;

//saves the name of the workflow that should be loaded/deleted
var loadDeleteWorkflowName = '';