/**
 * Listener functions - handle user events.
 * 
 * @author Clemens Banas
 * @date April 2013
 */

$(document).ready(function() {
	
	/**
	 * Function is used to save the standard explanation in a global variable. 
	 * This value is used again if the user deletes the currently used workflow.
	 * Also the typeahead values (saved workflow names) are set.
	 */
	$(window).load(function(){
		preSaveStdContent();
//		updateTypeaheadSaved();
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
	$('#selectLink').on('click', function() {
		processOperationLinkRequest('select');
	});
	//TODO
	$('#groupLink').on('click', function() {
		processOperationLinkRequest('group');
	});
	//TODO
	$('#orderLink').on('click', function() {
		processOperationLinkRequest('order');
	});
	$('#userScriptLink').on('click', function() {
		setFormContainerOperation('user defined script');
		setOperationRelatedHelpContent('user defined script');
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
	$('#selectDialog').on('submit', function() {
		processSelectOperation();
		return false;
	});
	$('#groupDialog').on('submit', function() {
		processGroupOperation();
		return false;
	});
	$('#orderDialog').on('submit', function() {
		processOrderOperation();
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
	
	
	/**
	 * Function to process clicks that confirm the deletion of a table row.
	 */
	$('#removeRowBtn').on('click', function() {
		deleteRowAndDisplayTable();
	});
	
	
	/**
	 * Function saves the workflow description in the global description variable,
	 * displays the 'unsaved'-save state and collapses the description.
	 */	
	$('#workflowDescrSubmit').on('click', function() {
		description = $('#description').val();
		setSaveStateUnsavedAndDisplayStatus();
		descriptionButtonHandling();
	});

	
	//TODO needed?
//	/**
//	 * Function resets the workflow description from the global
//	 * description variable and collapses the description.
//	 */
//	$('#workflowDescrClear').on('click', function() {
//		resetDescription();
//		descriptionButtonHandling();
//	});
	
	
	/**
	 * Function saves the user-given comment and displays a blink
	 * effect to visualize the saving. Finally modifies the save state.
	 */
	$('#lineCommentSubmit').on('click', function() {
		saveCommentInWorkflow($('#comments').val());
		blinkEffectComments();
		setSaveStateUnsavedAndDisplayStatus();
	});
	
	
	/**
	 * Function resets the line comment.
	 */
	$('#lineCommentClear').on('click', function() {
		resetSavedLineComment();
	});
	
	
	/**
	 * Function reacts when user presses a key on the keyboard.
	 */
	$('html').keydown(function(e) {
		handleKeydownEvent(e);
	});
	
	
	//TODO umbauen
//	/**
//	 * Function reacts on a user click within the workflow container.
//	 * Resets the table row highlighting and the display of the 
//	 * operation dialog by calling a helper method.
//	 */
//	$('#workflowContainer').on('click', function(e) {
//		checkAndResetRowHighlighting($(e.target));
//	});
	
	
//	/**
//	 * Function is used to react on a user click within a table row.
//	 * The row gets highlighted and the corresponding information
//	 * gets displayed in the operation dialog by calling a helper function.
//	 */
//	$('#tableContainer').on('click', 'tr', function() {
//		processTableRowClick($(this));
//	});

	
	//TODO
//	$('#workflowContainer').on('click', 'div.container', function() {
//		$(this).addClass('workflowHighlighting');
//		highlightedWorkflowIndex = $(this).index();
//		$('#workflowSorting').removeClass('hide');
//	});
	
	
	/**
	 * Function is used to show/hide the site notice of the web page.
	 */
	$('#logoContainer').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		toggleSiteNoticePopover();
	});
	
	
	/**
	 * Function shows the new workflow view dependent of the save state
	 * of the current workflow - not saved: modal dialog to ensure that
	 * the user wants to discard the changes.
	 */
	$('#newWfBtn').on('click', function() {
		processNewWfRequest();
	});
	
	
	/**
	 * Function is used to process the discard changes approval.
	 */
	$('#discardWfChangesBtn').on('click', function() {
		processDiscardChanges();
	});
	
	
	/**
	 * Function shows the save dialog.
	 */
	$('#saveWfBtn').on('click', function() {
		//TODO: vorschalten einer Abfrage... soll als comp oder als wf gespeichert werden
		//open new modal
//		showSaveOptionModal();
		//processSaveWfRequest();
		processSaveWfRequest();
	});
	
	//TODO
//	$('#saveAsWf').on('click', function() {
//	});
	
	//TODO
//	$('#saveAsWfComp').on('click', function() {
//		processSaveWfCompRequest();
//	});
	
	
	/**
	 * Function processes the save instruction by calling a
	 * helper function.
	 */
	$('#saveWorkflow').on('submit', function() {
		save();
		return false;
	});
	
	//TODO
	$('#saveWorkflowComp').on('submit', function() {
		saveWfComp();
		return false;
	});
	
	
	/**
	 * Function is used to save the workflow after the user clicked
	 * the button to ensure that he wants to override the workflow.
	 */
	$('#overrideBtn').on('click', function() {
		$('#saveCheckModal').modal('hide');
		if($('#saveCheckModal').hasClass('component')) {
			$('#saveCheckModal').removeClass('component');
			saveWorkflowComp($('#overrideFilename').html());
		} else {
			saveWorkflow($('#overrideFilename').html());
		}
	});
	
	
	/**
	 * Function handles a users download request by clicking the download button.
	 */
	$('#downloadScript').on('click', function(e) {
		processDownloadRequest(e);
	});
	
	/**
	 * Function to run the specified data processing on the cloudgene platform.
	 */
	$('#runJob').on('click', function(e) {
		processRunJobRequest(e);
	});
	
	
	$('#wfProblemIgnoreBtn').on('click', function() {
		handleProblemIgnore();
	});
	
	/**
	 * Function opens up the save dialog when download script was clicked,
	 * but the workflow was not saved yet and the user selects the option
	 * to open up the save dialog.
	 */
	$('#openSaveDialogBtn').on('click', function() {
		closeUnsavedModal();
		$('#saveWfBtn').trigger('click');
	})
	
	
	/**
	 * Functions are used to handle hovers over the line 'reordering' icons.
	 */
	$('#orderUp').hover(function() {
		hoverOverArrowAction('#up');
	});
	$('#orderDown').hover(function() {
		hoverOverArrowAction('#down');
	});
	
	
	//TODO
	$('#wfUp').hover(function() {
		hoverOverArrowAction('#wfUpArrow');
	});
	$('#wfDown').hover(function() {
		hoverOverArrowAction('#wfDownArrow');
	});
	
	/**
	 * Functions are used to rearrange two table lines if the 
	 * user clicks on the up or down links in the line dialog.
	 */
	$('#orderUp').on('click', function() {
		orderUpHandling();
	});
	$('#orderDown').on('click', function(){
		orderDownHandling();
	});
	
	//TODO
	$('#wfUp').on('click', function() {
		wfOrderUpHandling();
	});
	$('#wfDown').on('click', function() {
		wfOrderDownHandling();
	});
	
	
	/**
	 * Functions are used to toggle the display of additional text separator options
	 * in the operations dialog when a load operation is selected.
	 */
	$('#loadFastQ').on('click', function() {
		hideTxtSeparatorOptions();
		hideReadAttrOptions();
		hideRefFileOptions();
	});
	$('#loadBam').on('click', function() {
		hideTxtSeparatorOptions();
		hideRefFileOptions();
		showReadAttrOptions();
	});
	$('#loadSam').on('click', function() {
		hideTxtSeparatorOptions();
		hideRefFileOptions();
		showReadAttrOptions();
	});
	$('#loadVcf').on('click', function() {
		hideTxtSeparatorOptions();
		hideReadAttrOptions();
		showRefFileOptions();
	});
	$('#loadTxt').on('click', function() {
		hideReadAttrOptions();
		hideRefFileOptions();
		showTxtSeparatorOptions();
	});

	
	/**
	 * Function is used to process a users load workflow 
	 * and delete workflow request.
	 */
	$('#actionBtns').on('click', 'a.fileNames', function() {
		loadDeleteWfRequest($(this).html());
	});
	
	
	/**
	 * Function is used to display/hide the popovers that show the existing workflows.
	 * If a popover gets displayed - the existing workflows are fetched from the server.
	 */
	$('#showWfBtn').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		$('#showWfCompBtn').popover('hide').removeClass('pop').removeClass('showWfCompBtnPopover');
		handleWorkflowRequest('#showWfBtn');
	});
	
	//TODO
	$('#showWfCompBtn').popover({trigger: 'manual', html: true, placement: 'bottom'}).click(function() {
		$('#showWfBtn').popover('hide').removeClass('pop').removeClass('showWfBtnPopover');
		handleWfCompRequest('#showWfCompBtn');
	});
	
	/**
	 * Function is used to delete the opened workflow. (Showing a warning bevore deletion!)
	 */
	$('#deleteWfBtn').on('click', function() {
		loadDeleteWfRequest($('#workflowName').html());
	});
	
	
	//TODO
	/**
	 * Function handles a click on the description 
	 * button by calling a helper function.
	 */
	$('#workflowContainer').on('click', 'a.descBtn', function() {
		processDescriptionBtnClick(this);
	});
	
	
	//TODO
	$('#workflowContainer').on('click', 'a.compDetBtn', function() {
		toggleWorkflowDetails(this);
	});
	
	
	//TODO
	$('#workflowContainer').on('submit','form.inputFileNames', function() {
		saveInputFileNames(this);
		return false;
	});
	
	/**
	 * Function handles the deletion of a workflow.
	 */
	$('#removeWfBtn').on('click', function() {
		processWorkflowDeletion();
	})

	
	/**
	 * Function is used to toggle the display of an
	 * optional user-help functionality.
	 */
	$('#helpBtn').on('click', function() {
		toggleHelpBtn();
	})
	
});