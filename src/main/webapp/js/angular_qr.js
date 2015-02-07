var app = angular.module('quickrant', ['ngCookies', 'firebase', 'ui.bootstrap']);

app.controller('MainCtrl', ['$scope', '$timeout', 'DATA', 'SessionService', function($scope, $timeout, DATA, SessionService) {

  var quickrant = $scope.quickrant = {
    data: DATA,
    user: {
      name: 'Anonymous',
      location: 'Earth'
    },
    templates: {
      navigation: 'navigation.html',
      form: 'form.html'
    }
  };

  SessionService.authenticate()
    .done(function(response) {
      quickrant.session = SessionService.getSessionCookie();
    })
    .fail(function(response) {
      quickrant.session = 'no-session';
    });

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
       $element.bind('click', function(){
         questionsCtrl.select($scope.question);
       });
     }
   }
});

app.directive('rantTextarea', function($parse) {
  return {
    restrict: 'A',
    require: 'ngModel',
    controller: function($scope) {
      $scope.maxChars = 500;
      $scope.minChars = 2;
      $scope.rant = "";
      $scope.charsLeft = function() {
        return $scope.maxChars - $scope.rant.length;
      }
      $scope.charsToGo = function() {
        return $scope.minChars - $scope.rant.length;
      }
    },
    link: function($scope, $element, $attrs, ngModelCtrl) {
      $scope.minLengthErr = true;
      ngModelCtrl.$viewChangeListeners.push(function(){
        $parse($attrs.ngModel).assign($scope, ngModelCtrl.$viewValue);
        $scope.ui.rant = ngModelCtrl.$viewValue;
        $scope.minLengthErr = ngModelCtrl.$viewValue.length == 0 || ngModelCtrl.$error.minlength;
      });
    }
  }
});

app.directive('visitor', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    controller: function($scope) {
      $scope.defaultVisitor = $scope.quickrant.user.name;
    },
    link: function($scope, $element, $attrs, ngModelCtrl) {
      ngModelCtrl.$viewChangeListeners.push(function(){
        $scope.ui.visitor =  ngModelCtrl.$viewValue.length > 0 ? ngModelCtrl.$viewValue : $scope.defaultVisitor;
      });
    }
  }
});

app.directive('location', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    controller: function($scope) {
      $scope.defaultLocation = $scope.quickrant.user.location;
    },
    link: function($scope, $element, $attrs, ngModelCtrl) {
      ngModelCtrl.$viewChangeListeners.push(function(){
        $scope.ui.location =  ngModelCtrl.$viewValue.length > 0 ? ngModelCtrl.$viewValue : $scope.defaultLocation;
      });
    }
  }
});