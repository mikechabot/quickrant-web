(function() {

  var app = angular.module('quickrant', ['ngCookies']);

  app.controller('UuidController', function($cookies) {
    this.value = $cookies['quickrant-uuid'] || 'No cookies?';
  });

  app.directive('rants', function() {
    return {
      restrict: 'E',
      templateUrl: 'rants.html',
      controllerAs: 'rantsCtrl',
      controller: function($scope, $element, $attrs) {
        this.rants = rants;
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
        $scope.select = function(option, emotion) {
          /* Return if already selected */
          if (option.selected) {
            option.selected = false;
            $scope.legend = undefined;
            return;
          }
          /* Set the 'selected' flag whether a face or a question */
          if (angular.isDefined(option.id)) {
            angular.forEach(emotions, function(emotion) {
              emotion.selected = false;
            });
            $scope.legend = undefined;
          } else {
            $scope.legend = option.text;
            angular.forEach(emotion.questions, function(question) {
              question.selected = false;
            });
            $scope.bgStyle = emotion.bgStyle || emotion.style;
          }
          option.selected = true;
        };

        $scope.isSelected = function(option) {
          return option.selected;
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

  var rants = [{
    "id": 1,
    "visitor": "John",
    "location": "Boston, MA",
    "created": "2014-08-10 21:49:58.495",
    "rant": "Weekends - they're just fantastic.",
    "emotion": "happy",
    "question": "You know what's cool?"
  }, {
    "id": 2,
    "visitor": "John",
    "location": "Boston, MA",
    "created": "2014-08-10 21:49:58.495",
    "rant": "The end of a damn fine weekend.",
    "emotion": "sad",
    "question": "You know what sucks?"
  }, {
    "id": 3,
    "visitor": "Anonymous",
    "location": "Earth",
    "created": "2014-08-10 21:49:58.495",
    "rant": "When I'm thinking I'm making good time, but then I hit a ton of traffic, and all is lost - all is lost...",
    "emotion": "angry",
    "question": "You know what makes me mad?"
  }, {
    "id": 4,
    "visitor": "Some Guy",
    "location": "Earth",
    "created": "2014-08-10 21:49:58.495",
    "rant": "Ice cream, tons of it - with caramel and chocolate and strawberries",
    "emotion": "happy",
    "question": "You know what's cool?"
  }];

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