( function() {
	
	var app = angular.module('quickrant', ['ngCookies']);
	
	app.controller('UuidController', function($cookies) {
		this.value = $cookies['quickrant-uuid'] || 'No cookies?';
	});
	
	app.directive('navigation', function() {
		return {
			restrict: 'E',
			templateUrl: "navigation.html"
		};
	});	
	
	app.directive('faces', function() {
		return {
			restrict: 'E',
			templateUrl: "faces.html"	
		};
	});	
	
	app.directive('questions', function() {
		return {
			restrict: 'E',
			templateUrl: "questions.html"
		};
	});
	
	app.directive('rantForm', function() {
		return {
			restrict: 'E',
			templateUrl: "form.html"
		};
	});
	
	app.directive('personInteraction', function() {
		return {
			restrict: 'E',
			replace: 'true',
			controllerAs: 'person',
			controller: function($scope) {
				/* Load the emotions */
				this.emotions = emotions;
				
				/* Control the behavior of the face images */
				$scope.selectEmotion = function(emotion) {
					$scope.legend = undefined;
					if (emotion.selected) {
						emotion.selected = false;
						return;
					}
					angular.forEach(emotions, function(emotion) {
						emotion.selected = false;
					});
					emotion.selected = true;		
				};

				/* Control the behavior of the question buttons */
				$scope.selectQuestion = function(question, emotion) {
					$scope.legend = question.text;
					angular.forEach(emotion.questions, function(question) {
						question.selected = false;
					});
					$scope.bgStyle = emotion.bgStyle || emotion.style;
					question.selected = true;
				};
				
				/* See if a face has been clicked */
				$scope.isEmotionSelected = function(image) {
					return image.selected;
				};
				
				/* See if a question has been clicked */
				$scope.isQuestionSelected = function () {
					return angular.isDefined($scope.legend);
				};
			}
		};
	});

	
	
	var emotions = [
			    {
				  "id": "happy",
				  "image": "happy.gif",
				  "style": "success",
				  "questions": [
				    { "text": "You know what I love?" },
				    { "text": "You know what I like?" },
				    { "text": "You know what's cool?" },
				    { "text": "You know what make me happy?" },
				    { "text": "You know what I can't live without?" },
				    { "text": "You know what's pretty good?" }
				  ]
				},
			    {
			      "id": "angry",
			      "image": "angry.gif",
			      "style": "danger",
			      "questions": [
					{ "text": "You know what I hate?" },
					{ "text": "You know what's bullshit?" },
					{ "text": "You know what sucks?" },
					{ "text": "You know what I don't like?" },
					{ "text": "You know what I can't stand?" },
					{ "text": "You know what makes me angry?" }
			      ]
			    },
			    {
			      "id": "sad",
			      "image": "sad.gif",
			      "style": "primary",
			      "bgStyle": "info",
			      "questions": [
					{ "text": "You know what makes me cry?" },
					{ "text": "You know what's depressing?" },
					{ "text": "You know what makes me sad?" },
					{ "text": "You know what I wish had happened?" },
					{ "text": "You know what sucks?" },
					{ "text": "You know what I miss?" },
					{ "text": "You know what I regret?" }
			      ]
			    }
			  ];
	
})();
