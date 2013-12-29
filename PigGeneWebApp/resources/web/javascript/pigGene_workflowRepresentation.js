

function renderWorkflowArray(workflow) {
	var components = '';
	if(workflow != null && workflow != []) {
		for(var i=0; i<workflow.length; i++) {
			components += '<div id="component' + i + '" class="span10 container">'
			
			components += '<div id="title">';
			
			components += '<a id="descriptionBtn" class="btn btn-link" title="workflow description"><i class="icon-plus-sign" id="descriptionIcon"></i></a>'; //TODO alles link...
			
			components += '<h3 id="workflowName">' + workflow[i].name + '</h3>';
			components += '<h3 class="saved" id="saveState"></h3>';
			components += '<a id="showCompDetails" class="btn" title="showCompDetails"><i class="icon-download"></i> show details</a>';
			
			components += '<div class="well hide" id="workflowDescription">';
			components += '<h4 id="descrText">DESCRIPTION</h4>';
			components += '<textarea id="description" placeholder="type your workflow description" title="workflow description">' + workflow[i].description + '</textarea>';
			components += '<button class="btn" id="workflowDescrClear">cancel</button>';
			components += '<button class="btn btn-success" id="workflowDescrSubmit">add description</button>';
			components += '</div>';
			
			components += '</div>';
			
			components += '<div id="tableContainer" class="">';
			components += convertJsonToTable(workflow[i].data,'componentTable','table table-striped table-hover');
			components += '</div>';
			components += '</div>';
		}
	}
	return components;
}