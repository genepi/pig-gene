
//TODO add description of function
function renderWorkflowArray(workflow) {
	var components = '';
	if(workflow != null && workflow != []) {
		
		if(workflow.length > 1) {
			components += '<div id="workflowTitle">';
			components += '<a id="titleDescriptionBtn" class="btn btn-link titleDescrBtn" title="add workflow description"><i class="icon-plus-sign" id="descriptionIcon"></i></a>';
			components += '<h3 id="workflowName">' + workflowName + '</h3><h3 class="saved" id="saveState"></h3>';
			components += '<div class="well" id="workflowDescription">';
			components += '<h4 id="descrText">DESCRIPTION</h4>';
			components += '<textarea id="description" placeholder="type your workflow description" title="workflow description">description...</textarea>';
			components += '<button class="btn" id="workflowDescrClear">cancel</button>';
			components += '<button class="btn btn-success" id="workflowDescrSubmit">add description</button>';
			components += '</div></div>';
		}
			
		
		for(var i=0; i<workflow.length; i++) {
			components += '<div id="component' + i + '" index=' + i + ' class="span10 well container';
			if(highlightedWorkflowIndex == i) {
				components += ' workflowHighlighting';
			}
			components += '">';
			
			components += '<div id="title">';
			components += '<a id="descriptionBtn" class="btn btn-link descBtn" title="workflow description"><i class="icon-plus-sign" id="descriptionIcon"></i></a>'; //TODO alles link...
			components += '<h3 id="workflowName">' + workflow[i].name + '</h3>';
			components += '<h3 class="saved" id="saveState"></h3>';
			
			components += '<a id="showCompDetails" class="btn btn-inverse compDetBtn" title="compDetails">';
			var workflowDetails = workflowDetailDisplay[i];
			if(!workflowDetails) {
				components += '<i class="icon-download icon-white"></i> show details';
			} else {
				components += '<i class="icon-upload icon-white"></i> hide details';
			}
			components += '</a></div>';
			
			components += '<div class="well';
			if(!workflowDescDisplay[i]) {
				components += ' hide';
			}
			components += '" id="workflowDescription">';
			
			components += '<h4 id="descrText">DESCRIPTION</h4>';
			components += '<textarea id="description" placeholder="type your workflow description" title="workflow description">' + workflow[i].description + '</textarea>';
			components += '<button class="btn" id="workflowDescrClear">cancel</button>';
			components += '<button class="btn btn-success" id="workflowDescrSubmit">add description</button>';
			components += '</div>';
			
			components += '<div id="tableContainer" ';
			if(!workflowDetails) {
				components += 'class="hide">';
			} else {
				components += '>';
			}
			
			components += convertJsonToTable(workflow[i].data,'componentTable','table table-striped table-hover');
			components += '</div>';
			components += createInputElements(workflow[i].data);
			components += '</div>';
		}
		
		
	}
	return components;
}


//TODO add description of function
function createInputElements(data) {
	var formElement = '<form class="inputFileNames">';
	var buttonElement = '<button class="btn btn-success" type="submit">save</button>';
	var inputElements = '';
	for(var i=0; i<data.length; i++) {
		if(data[i].operation == 'LOAD') {
			inputElements += '<input type="text" placeholder="inputfile" title="inputfile" autocomplete="off" value="">';
		}
	}
	if(inputElements != '') {
		return formElement + inputElements + buttonElement + '</form>';
	}
	return inputElements;
}