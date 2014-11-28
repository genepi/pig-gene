//controllers manage an object $scope in AngularJS (this is the view model)
pigGeneApp.controller('PlumbCtrl', function($scope) {

	$scope.redraw = function() {
		jsPlumb.detachEveryConnection();
	};
	
	$scope.init = function() {
		jsPlumb.bind("ready", function() {
			console.log("Set up jsPlumb listeners (should be only done once)");
			jsPlumb.bind("connection", function (info) {
				$scope.$apply(function () {
					console.log("Possibility to push connection into array");
				});
			});
		});
	}
});

pigGeneApp.directive('postRender', [ '$timeout', function($timeout) {
	var def = {
			restrict : 'A', 
			terminal : true,
			transclude : true,
			link : function(scope, element, attrs) {
				$timeout(scope.redraw, 0);  //Calling a scoped method
			}
	};
	return def;
}]);


//directives link user interactions with $scope behaviours
//now we extend html with <div plumb-item>, we can define a template <> to replace it with "proper" html, or we can 
//replace it with something more sophisticated, e.g. setting jsPlumb arguments and attach it to a double-click 
//event
pigGeneApp.directive('plumbItem', function() {
	return {
		replace: true,
		controller: 'PlumbCtrl',
		link: function (scope, element, attrs) {
			console.log("Add plumbing for the 'item' element");

			jsPlumb.draggable(element, {
				//containment: element.parent
			});


			
			console.log(scope.component);
			
		}
	};
});

pigGeneApp.directive('plumbSource', function() {
	return {
		replace: true,
		link: function (scope, element, attrs) {
			console.log("Add plumbing for the 'connect' element");

			jsPlumb.makeSource(element, {
				//parent: $(element).parent().parent(),
				anchor: 'RightMiddle',
				container: 'workflow-graph'

			});
		}
	};
});

pigGeneApp.directive('plumbTarget', function() {
	return {
		replace: true,
		link: function (scope, element, attrs) {
			console.log("Add plumbing for the 'connect' element");

			jsPlumb.makeTarget(element, {
				//parent: $(element).parent().parent(),
				anchor: 'LeftMiddle',
				container: 'workflow-graph'
			});
		}
	};
});

pigGeneApp.directive('droppable', function($compile) {
	return {
		restrict: 'A',
		link: function(scope, element, attrs){
			console.log("Make this element droppable");

			element.droppable({
				/*drop:function(event,ui) {
					// angular uses angular.element to get jQuery element, subsequently data() of jQuery is used to get
					// the data-identifier attribute
					var dragIndex = angular.element(ui.draggable).data('identifier'),
					dragEl = angular.element(ui.draggable),
					dropEl = angular.element(this);

					// if dragged item has class menu-item and dropped div has class drop-container, add module 
					if (dragEl.hasClass('menu-item') && dropEl.hasClass('drop-container')) {
						console.log("Drag event on " + dragIndex);
						var x = event.pageX - scope.module_css.width / 2;
						var y = event.pageY - scope.module_css.height / 2;

						scope.addModuleToSchema(dragIndex, x, y);
					}

					scope.$apply();
				}*/
			});
		}
	};
});


