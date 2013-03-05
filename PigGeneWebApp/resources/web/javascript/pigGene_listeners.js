/**
 * Listener functions.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

$(document).ready(function() {
	
	/**
	 * Function is used to save the standard explanation in a global variable. 
	 * This value is used again if the user deletes the currently used workflow.
	 */
	$(window).load(function(){
		preSaveStdContent();
	});
	
	
	/**
	 * Function to set an English error message if the user does 
	 * not specify a value in a form field that must not be blank.
	 */
    $('input[type=text]').on('change invalid', function() {
    	setMissingFormValueText($(this).get(0));
    });
    
    
    /**
     * Functions to process clicks that select the different 
     * operations in the menu on the right side.
     */
	$('#registerLink').on('click', function() {
		processOperationLinkRequest('register');
	});
    $('#loadLink').on('click', function() {
    	processOperationLinkRequest('load');
	});
	$('#storeLink').on('click', function() {
		processOperationLinkRequest('store');
	});
	$('#filterLink').on('click', function() {
		processOperationLinkRequest('filter');
	});
	$('#joinLink').on('click', function() {
		processOperationLinkRequest('join');
	});
	$('#userScriptLink').on('click', function() {
		setFormContainerOperation('user defined script');
		showScriptDialogSlow();
		modifyContainerHeight();
	});
	
	
	/**
	 * Functions to process clicks that submit the operation forms. No page reload!
	 */
	$('#registerDialog').on('submit', function() {
		processRegisterOperation();
		return false;
	});
	$('#loadDialog').on('submit', function() {
		processLoadOperation();
		return false;
	});
	$('#storeDialog').on('submit', function() {
		processStoreOperation();
		return false;
	});
	$('#filterDialog').on('submit', function() {
		processFilterOperation();
		return false;
	});
	$('#joinDialog').on('submit', function() {
		processJoinOperation();
		return false;
	});
	$('#scriptDialog').on('submit', function() {
		processScriptOperation();
		return false;
	});

	
	/**
	 * Function to process clicks that cancel inputs of operation forms.
	 */
	$("button[type='reset']").on('click', function() {
		processInputFormCancellation($(this));
	});
	
	
	
	
	
	$('#removeRowBtn').on('click', function() {
		deleteRowAndDisplayTable();
	});
	
	$('#workflowDescrSubmit').on('click', function() {
		description = $('#description').val();
		setSavedStateUnsavedAndDisplayStatus();
		descriptionButtonHandling();
	});

	$('#workflowDescrClear').on('click', function() {
		resetDescription();
		descriptionButtonHandling();
	});
	
	$('#lineCommentSubmit').on('click', function() {
		saveCommentInWorkflow($('#comments').val());
		blinkEffectComments();
		setSavedStateUnsavedAndDisplayStatus();
	});
	
	$('#lineCommentClear').on('click', function() {
		resetSavedWorkflowComment();
	});
	
	$('html').keydown(function(e) {
		handleKeydownEvent(e);
	});
	
	$('#workflowContainer').on('click', function(e) {
		checkAndResetRowHighlighting($(e.target));
	});
	
	$('#tableContainer').on('click', 'tr', function() {
		processTableRowClick($(this));
	});
	
	$('#newWfBtn').on('click', function() {
		processNewWfRequest();
	});
	
	$('#discardWfChangesBtn').on('click', function() {
		$('#discardChangesCheckModal').modal('hide');
		initializeNewWorkflow();
	});
	
	$('#saveWfBtn').on('click', function() {
		processSaveWfRequest();
	});
	
	$('#saveWorkflow').on('submit', function() {
		save();
		return false;
	});
	
	$('#downloadScript').on('click', function() {
		processDownloadRequest();
	});
	
	$('#orderUp').hover(function() {
		hoverOverArrowAction('#up');
	});
	
	$('#orderDown').hover(function() {
		hoverOverArrowAction('#down');
	});
	
	$('#orderUp').on('click', function() {
		orderUpHandling();
	});
	
	$('#orderDown').on('click', function(){
		orderDownHandling();
	});
	
	$('#loadVcf').on('click', function() {
		hideTxtSeparatorOptions();
	});
	$('#loadTxt').on('click', function() {
		showTxtSeparatorOptions();
	});
	
	$('#actionBtns').on('click', 'a.fileNames', function() {
		processLoadDeleteWfRequest($(this).html());
	});
	
	$('#showWfBtn').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		handleWorkflowRequest('#showWfBtn');
	});
	$('#deleteWfBtn').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		handleWorkflowRequest('#deleteWfBtn');
	});
	
	$('#descriptionBtn').on('click', function() {
		processDescriptionBtnClick();
	});

	$('#closeSuccModal').on('click', function() {
		$('#successModal').modal('hide');
	});
	
});