(function() {

  var app = angular.module('quickrant', ['ngCookies', 'firebase']);
  
  app.controller('UuidController', function($cookies) {
    this.value = $cookies['quickrant-uuid'] || 'No cookies?';
  });

  app.directive('rants', function() {
    return {
      restrict: 'E',
      templateUrl: 'rants.html',
      controller: function($scope, $element, $attrs, rantService) {
    	  
    	var url = 'https://blazing-heat-6301.firebaseio.com/';
	    var dataRef = new Firebase(url);
	   
	    dataRef.on('child_added', function (snapshot) {
	      var rant = snapshot.val();
	      console.log("visitor: " + rant.visitor);
	      console.log("rant: " + rant.rant);
	    });

        $scope.rants = [];
        loadRants();

        function setRants(newRants) {
	      $scope.rants = newRants;
	      console.log($scope.rants);
        }
        
        function loadRants() {
          rantService.getRants().then(function(data) {
            setRants(data); 
          }); 
	    }
        
      }
    };
  });

  app.directive('userInteractionContainer', function() {
    return {
      restrict: 'A',
      controllerAs: 'person',
      controller: function($scope, $element, $attrs) {
        /* Load the emotions */
        this.emotions = emotions;

        /* Select a face or a question */
        $scope.select = function(element, emotion) {
          /* Return if already selected */
          if (element.selected) {
        	element.selected = false;
            $scope.legend = undefined;
            return;
          }
          /* Set the 'selected' flag whether a face or a question */
          if (angular.isDefined(element.id)) {
            angular.forEach(emotions, function(emotion) {
              emotion.selected = false;
            });
            $scope.legend = undefined;
          } else {
            $scope.legend = element.text;
            angular.forEach(emotion.questions, function(question) {
              question.selected = false;
            });
            $scope.bgStyle = emotion.bgStyle || emotion.style;
          }
          element.selected = true;
        };

        $scope.isSelected = function(element) {
          return element.selected;
        };

        $scope.isQuestionSelected = function() {
          return angular.isDefined($scope.legend);
        }
      }
    };
  });

  app.directive('navigation', function() {
    return {
      restrict: 'E',
      templateUrl: 'navigation.html'
    };
  });

  app.directive('faces', function() {
    return {
      restrict: 'E',
      templateUrl: 'faces.html'
    };
  });

  app.directive('questions', function() {
    return {
      restrict: 'E',
      templateUrl: 'questions.html'
    };
  });

  app.directive('rantForm', function() {
    return {
      restrict: 'E',
      templateUrl: 'form.html'
    };
  });
  
  app.service("rantService", function($firebase) {
    
	return ({ getRants: getRants });
      
      function getRants() {
    	var url = 'https://blazing-heat-6301.firebaseio.com/';
    	var data = $firebase(new Firebase(url)).$asObject();
        return data.$loaded();
      }
      
      
      
    }
  );
  
  var emotions = [{
    "id": "happy",
    "image": "happy.gif",
    "style": "success",
    "questions": [{
      "text": "You know what I love?"
    }, {
      "text": "You know what I like?"
    }, {
      "text": "You know what's cool?"
    }, {
      "text": "You know what make me happy?"
    }, {
      "text": "You know what I can't live without?"
    }, {
      "text": "You know what's pretty good?"
    }]
  }, {
    "id": "angry",
    "image": "angry.gif",
    "style": "danger",
    "questions": [{
      "text": "You know what I hate?"
    }, {
      "text": "You know what's bullshit?"
    }, {
      "text": "You know what sucks?"
    }, {
      "text": "You know what I don't like?"
    }, {
      "text": "You know what I can't stand?"
    }, {
      "text": "You know what makes me angry?"
    }]
  }, {
    "id": "sad",
    "image": "sad.gif",
    "style": "primary",
    "bgStyle": "info",
    "questions": [{
      "text": "You know what makes me cry?"
    }, {
      "text": "You know what's depressing?"
    }, {
      "text": "You know what makes me sad?"
    }, {
      "text": "You know what I wish had happened?"
    }, {
      "text": "You know what sucks?"
    }, {
      "text": "You know what I miss?"
    }, {
      "text": "You know what I regret?"
    }]
  }];

})();