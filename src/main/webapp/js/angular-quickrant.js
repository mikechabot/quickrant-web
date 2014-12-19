var app = angular.module('quickrant', ['ngCookies', 'firebase', 'ui.bootstrap']);

app.controller('MainCtrl', ['$scope', '$timeout', 'SessionService', function($scope, $timeout, SessionService) {

  $scope.model = {};
  $scope.user = {};
  $scope.user.session = SessionService.getSession();

  var init = function() {
    SessionService.authenticate()
      .then(function (data) {
        $timeout(function () {
          $scope.user.session = data.data.success ? SessionService.getSession() : $scope.user.session + '-X';
        }, 100);
      })
      .finally(function () {
        $scope.navigation = 'navigation.html';
        $scope.form = 'form.html';
        $scope.model.data = data;
      });
  };

  init();

}]);

app.directive('userControls', function() {
  return {
    restrict: 'A',
    link: function($scope) {

      $scope.ui = {};
      $scope.ui.visitor = 'Anonymous';
      $scope.ui.location = 'Earth';
      $scope.ui.showPreview = false;

      var showQuestions = function(show) {
        $scope.ui.showQuestions = show;
        if (!show) showForm(false);
      };

      var showForm = function(showForm) {
        $scope.ui.showForm = showForm;
      }

      var setCurrentEmotion = function(emotion) {
        $scope.ui.emotion = emotion;
      };

      var setQuestion = function(question) {
        $scope.ui.question = question;
      }

      var setPanelStyle = function(style) {
        $scope.ui.panelStyle = 'panel-' + style;
      }

      $scope.$watch('ui.showPreview', function(newVal, oldVal) {
//        $scope.ui.previewPopover = newVal ? "Show Preview" : "Hide Preview";
        $scope.ui.previewPopover = newVal ? "Hide Preview" : "Show Preview";
      });

      $scope.$on('faceSelected', function(event, data) {
        $scope.$apply(function() {
          showQuestions(data.showQuestions);
          setCurrentEmotion(data.emotion);
          if (!data.selected) {
            showForm(false);
          }
        });
      });

      $scope.$on('questionSelected', function(event, data) {
        $scope.$apply(function() {
          showForm(data.showForm);
          setQuestion(data.question);
          setPanelStyle(data.style);
        });
      });

    }
  }
});

app.directive('faces', function() {
  return {
    restrict: 'E',
    require: '^controls',
    template: '<div class="row margin-bottom-lg ">'
            + '  <div ng-repeat="face in model.data" class="col-lg-4 col-sm-4 col-xs-4">'
            + '    <face emotion="{{::face.emotion}}"></face>'
            + '  </div>'
            + '</div>',
    controller: function ($scope) {
      var faces = [];

      this.addFace = function (face) {
        faces.push(face);
      };

      var deselect = function (face) {
        face.selected = false;
      }

      var emit = function (data) {
        $scope.$emit('faceSelected', data);
      }

      this.select = function (face, emotion) {
        if (face.selected) {
          deselect(face);
          emit({showQuestions: false});
          return;
        }
        angular.forEach(faces, function (face) {
          deselect(face);
        });
        face.selected = true;
        emit({showQuestions: true, emotion: emotion});
      };

    }
  }
});

app.directive('face', function() {
  return {
    restrict: 'E',
    require: '^faces',
    template: '<a href><img class="img-circle" ng-src="/img/{{::face.emotion}}.gif"></a>',
    link: function($scope, $element, $attrs, facesCtrl) {
      $element.bind('click', function(){
        facesCtrl.select($scope, $attrs.emotion);
      });
      facesCtrl.addFace($scope);
    }
  }
});

app.directive('questions', function() {
  return {
    restrict: 'E',
    require: '^controls',
    template: '<div ng-repeat="face in model.data" >'
            + '  <div ng-if="face.emotion === ui.emotion" class="row margin-bottom-lg">'
            + '    <div class="col-lg-offset-1 col-lg-10">'
            + '      <question ng-repeat="question in face.questions" panel-style="{{face.altStyle || face.style}}"></question>'
            + '    </div>'
            + '  </div>'
            + '</div>',
    controller: function($scope) {
      var questions = [];

      this.addQuestion = function (question) {
        questions.push(question);
      };

      var deselect = function (question) {
        question.selected = false;
      }

      var emit = function (selected) {
        $scope.$emit('questionSelected', selected);
      }

      this.select = function (question, style) {
        if (question.selected) {
          deselect(question);
          emit({showForm: false});
          return;
        }
        angular.forEach(questions, function (question) {
          deselect(question);
        });
        question.selected = true;
        emit({showForm: true, question: question.question.text, style: style});
      };

    }
  };
});

app.directive('question', function() {
   return {
     restrict: 'E',
     require: '^questions',
     template: '<button class="btn btn-{{::face.style}} question" type="button">{{::question.text}}</button>',
     link: function($scope, $element, $attrs, questionsCtrl) {
       $element.bind('click', function(){
         questionsCtrl.select($scope, $attrs.panelStyle);
       });
       questionsCtrl.addQuestion($scope);
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
      $scope.defaultVisitor = $scope.ui.visitor;
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
      $scope.defaultLocation = $scope.ui.location;
    },
    link: function($scope, $element, $attrs, ngModelCtrl) {
      ngModelCtrl.$viewChangeListeners.push(function(){
        $scope.ui.location =  ngModelCtrl.$viewValue.length > 0 ? ngModelCtrl.$viewValue : $scope.defaultLocation;
      });
    }
  }
});