function modifyContainerHeight() {
	if($('#lineDetails').hasClass('hide') && $('#scriptDialog').hasClass('hide')) {
		$('#workflowContainer.well').css('min-height','193px');
		$('#formContainer.well').css('height','175px');
	} else if($('#scriptDialog').hasClass('hide')) {
		$('#workflowContainer.well').css('min-height','401px');
		$('#formContainer.well').css('height','175px');
	} else if($('#lineDetails').hasClass('hide')) {
		$('#workflowContainer.well').css('min-height','302px');
		$('#formContainer.well').css('height','284px');
	} else {
		$('#workflowContainer.well').css('min-height','510px');
		$('#formContainer.well').css('height','284px');
	}
}



/**
 * Returns a relation-name if the user has not specified one.
 */
function getArtificialName() {
	return 'R' + nameCounter++;
}

/**
 * Checks if the input is longer than 1 character.
 */
function inputLongEnough(input) {
	if(input.length < 2) {
		return false;
	}
	return true;
}

function convertFilenamesToLinks(data) {
	var toRemove = '.yaml';
	var content = '';
	for(var i=0; i<data.length; i++) {
		var name = data[i].replace(toRemove,'');
		content += '<a class="fileNames">'+name+'</a><br>'
	}
	return content;
}