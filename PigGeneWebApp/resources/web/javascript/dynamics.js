$(document).ready(function() {
	
	$('body').on('click', 'div.component', function() {
		var options = "options"
		
		if(!$(this).hasClass(options)) {
			var buttonGroup1 = $("<div class='btn-group optionBtns1'></div>");
			var buttonGroup2 = $("<div class='btn-group optionBtns2'></div>");
			
			var upBtn = $("<button type='button' class='btn btn-sm btn-default'><span class='glyphicon glyphicon-arrow-up'></span></button>");
			var downBtn = $("<button type='button' class='btn btn-sm btn-default'><span class='glyphicon glyphicon-arrow-down'></span></button>");
			var acceptBtn = $("<button type='button' class='btn btn-sm btn-success'><span class='glyphicon glyphicon-ok'></span></button>");
			var cancelBtn = $("<button type='button' class='btn btn-sm btn-warning'><span class='glyphicon glyphicon-remove'></span></button>");
			var deleteBtn = $("<button type='button' class='btn btn-sm btn-danger'><span class='glyphicon glyphicon-trash'></span></button>");
			
			$(buttonGroup1).append(upBtn);
			$(buttonGroup1).append(downBtn);
			
			$(buttonGroup2).append(acceptBtn);
			$(buttonGroup2).append(cancelBtn);
			$(buttonGroup2).append(deleteBtn);
			
			$(this.parentNode).append(buttonGroup1);
			$(this.parentNode).append(buttonGroup2);
			$(this).addClass(options);
		}
	});
	
});