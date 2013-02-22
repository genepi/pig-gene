function initializeLoadedWorkflow(data) {
	description = data.description;
	workflow = data.workflow;
	nameCounter = workflow.length;
	showTable();
}

function initializeButtons() {
	$('#downloadScript').removeClass('hide');
	$('#saveWfBtn').removeClass('hide');
	$('#descriptionBtn').removeClass('hide');
}

function prepareContainers() {
	$('#workflowContainer').removeClass('span12').addClass('span10');
	$('#formContainer').removeClass('hide');
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
	resetStandardBehaviorForAll();
	$('#saveState').addClass('saved');
	toggleSaveStateVisualisation();
}

function initializeNewWorkflow() {
	$('#workflowName').addClass('new');
	prepareContainers();
	resetFormContainer();
	resetWorkflow();
}

function resetWorkflow() {
	workflow = [];
	$('#saveWfBtn').addClass('hide');
	$('#downloadScript').addClass('hide');
	$('#workflowDescription').addClass('hide');
	$('#descriptionBtn').addClass('hide');
	$('#description').val('');
	$('#workflowName').html('workflow');
	$('#tableContainer').html(stdContent);
	$('#saveState').removeClass('saved');
	$('#saveState').html('');
	resetStandardBehaviorForAll();
	$('#workflowContainer.well').css('min-height','193px');
}

function resetFormContainer() {
	$('#stepAction').removeClass('hide');
	$('#workflowOps').html('OPERATIONS');
}