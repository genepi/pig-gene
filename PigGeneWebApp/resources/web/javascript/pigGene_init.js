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

function resetInitialState() {
	$('#tableContainer').html(stdContent);
	$('#workflowName').html('workflow');
	$('#saveState').removeClass('saved');
	$('#saveState').html('');
	$('#saveWfBtn').addClass('hide');
	$('#downloadScript').addClass('hide');
	$('#descriptionBtn').addClass('hide');
	$('#workflowDescription').addClass('hide');
	$('#workflowContainer').removeClass('span10').addClass('span12');
	$('#formContainer').addClass('hide');
	description = '';
	$('#description').val(description);
}