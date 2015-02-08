var app = angular.module('quickrant', ['ngCookies', 'firebase', 'ui.bootstrap']);

app.controller('MainController', ['$scope', '$timeout', 'DATA', 'SessionService', 'RantService', function($scope, $timeout, DATA, SessionService, RantService) {

  var restrictions = {
    maxChars: 500,
    minChars: 2
  };

  var quickrant = $scope.quickrant = {
    data: DATA,
    restrictions: restrictions,
    templates: {
      navigation: 'navigation.html',
      rants: 'rants.html'
    },
    user: {
      defaultName: 'Anonymous',
      defaultLocation: 'Earth'
    }
  };

  function loadRants() {
    RantService.getRants()
      .done(function(response) {
        $timeout(function() {
          setRants(response.data);
        });
      });
  }

  function setRants(newRants) {
    $scope.quickrant.rants = newRants;
  }

  function authenticate() {
    SessionService.authenticate()
      .done(function(response) {
        quickrant.session = response.session;
      })
      .fail(function(response) {
        console.error(response);
      });
  }

  $scope.quickrant.submit = function(form) {
    if (form.$valid) {
      postRant({
        selection: {
          emotion: quickrant.selection.face.emotion,
          question: quickrant.selection.question
        },
        name: getDefaultString(form.visitor, quickrant.user.defaultName),
        location: getDefaultString(form.location, quickrant.user.defaultName),
        rant: form.rant
      });
    }
  };

  function postRant(data) {
    if (!data) return;
    RantService.postRant(data)
      .done(function(response) {
        quickrant.selection = {};
        loadRants();
      })
      .fail(function(response) {
        console.error(response);
      });
  }

  $scope.charsLeft = function(rant) {
    if (!rant) return restrictions.maxChars;
    return subtract(restrictions.maxChars, rant.length);
  };

  $scope.charsToGo = function(rant) {
    if (!rant) return restrictions.minChars;
    return subtract(restrictions.minChars, rant.length);
  };

  authenticate();
  loadRants();


}]);

app.directive('faces', ['$timeout', function($timeout) {
  return {
    restrict: 'A',
    scope: {
      data: '=',
      selection: '='
    },
    controller: function ($scope) {
      if (!$scope.selection) {
        $scope.selection = {};
      }
      this.select = function (face) {
        $timeout(function() {
          if (face === $scope.selection.face) {
            $scope.selection = {};
            return;
          }
          $scope.selection.face = face;
          $scope.selection.question = undefined;
        });
      };
    }
  }
}]);

app.directive('face', function() {
  return {
    restrict: 'A',
    require: '^faces',
    scope: {
      ngModel: '='
    },
    link: function(scope, element, attrs, facesCtrl) {
      element.bind('click', function() {
        facesCtrl.select(scope.ngModel);
      });
    }
  }
});

app.directive('questions', ['$timeout', function($timeout) {
  return {
    restrict: 'A',
    scope: {
      selection: '='
    },
    controller: function($scope) {
      this.select = function (question) {
        $scope.$apply(function() {
          if (question === $scope.selection.question) {
            $scope.selection.question = undefined;
            return;
          }
          $scope.selection.question = question;
        });
      };
    }
  };
}]);

app.directive('question', function() {
   return {
     restrict: 'E',
     require: '^questions',
     scope: {
       question: '=',
       customStyle: '='
     },
     template: '<button class="btn btn-{{customStyle}} question" type="button">{{::question}}</button>',
     link: function($scope, $element, $attrs, questionsCtrl) {
       $element.bind('click', function() {
         questionsCtrl.select($scope.question);
       });
     }
   }
});

app.directive('rantText', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, elem, attr, ctrl) {
      ctrl.$viewChangeListeners.push(function() {
        scope.quickrant.rant = ctrl.$viewValue;
      });
    }
  }
});

app.directive('visitor', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, elem, attr, ctrl) {
      scope.quickrant.user.name = scope.quickrant.user.defaultName;
      ctrl.$viewChangeListeners.push(function() {
        scope.quickrant.user.name =  ctrl.$viewValue.length > 0 ? ctrl.$viewValue : scope.quickrant.user.defaultName;
      });
    }
  }
});

app.directive('location', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, elem, attr, ctrl) {
      scope.quickrant.user.location = scope.quickrant.user.defaultLocation;
      ctrl.$viewChangeListeners.push(function() {
        scope.quickrant.user.location =  ctrl.$viewValue.length > 0 ? ctrl.$viewValue : scope.quickrant.user.defaultLocation;
      });
    }
  }

});