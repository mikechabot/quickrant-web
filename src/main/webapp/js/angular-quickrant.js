var app = angular.module('quickrant', ['ngCookies', 'firebase']);

app.controller('QuickrantCtrl', function($scope) {
  $scope.navigation = 'navigation.html';
  $scope.model = {};
  $scope.model.data = data;
});

app.directive('controls', function() {
  return {
    restrict: 'A',
    link: function($scope) {

      $scope.controls = {};

      var setFaceSelected = function(selected) {
        $scope.isFaceSelected = selected;
      };

      var setQuestionSelected = function(selected) {
        $scope.isQuestionSelected = selected;
      }

      var setEmotion = function(emotion) {
        $scope.controls.emotion = emotion;
      };

      $scope.$on('faceSelected', function(event, data) {
        $scope.$apply(function() {
          setFaceSelected(data.selected);
          setEmotion(data.emotion);
        });
      });

      $scope.$on('questionSelected', function(event, isSelected) {
        $scope.$apply(function() {
          setQuestionSelected(isSelected)
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
            + '    <face id="{{face.emotion}}"></face>'
            + '  </div>'
            + '</div>',
    controller: function ($scope) {

      var faces = [];

      this.addFace = function (face) {
        faces.push(face);
      };

      var selected = function (face) {
        return face.selected;
      };

      var deselect = function (face) {
        face.selected = false;
      }

      var emitFaceSelected = function (data) {
        $scope.$emit('faceSelected', data);
      }

      this.select = function (face, emotion) {
        if (selected(face)) {
          deselect(face);
          emitFaceSelected({selected: false});
          return;
        }
        angular.forEach(faces, function (face) {
          deselect(face);
        });
        face.selected = true;
        emitFaceSelected({selected: true, emotion: emotion});
      };

    }
  }
});

app.directive('face', function() {
  return {
    restrict: 'E',
    require: '^faces',
    template: '<a href><img class="img-circle" ng-src="/img/{{face.emotion}}.gif"></a>',
    link: function($scope, $element, $attrs, facesCtrl) {
      $element.bind('click', function(){
        facesCtrl.select($scope, $attrs['id']);
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
            + '  <div ng-if="face.emotion == controls.emotion" class="row margin-bottom-lg">'
            + '    <div class="col-lg-offset-1 col-lg-10">'
            + '      <div style="display: inline;" ng-repeat="question in face.questions">'
            + '        <question></question>'
            + '      </div>'
            + '    </div>'
            + '  </div>'
            + '</div>',
    controller: function($scope) {
      var questions = [];

      this.addQuestion = function (question) {
        questions.push(question);
      };

      var selected = function (question) {
        return question.selected;
      };

      var deselect = function (question) {
        question.selected = false;
      }

      var emitQuestionSelected = function (selected) {
        $scope.$emit('questionSelected', selected);
      }

      this.select = function (question) {
        if (selected(question)) {
          deselect(question);
          emitQuestionSelected(false);
          return;
        }
        angular.forEach(questions, function (question) {
          deselect(question);
        });
        question.selected = true;
        emitQuestionSelected(true);
      };

    }
  };
});

app.directive('question', function() {
   return {
     restrict: 'E',
     require: '^questions',
     template: '<button class="btn btn-{{face.style}} question" type="button">{{question.text}}</button>',
     link: function($scope, $element, $attrs, questionsCtrl) {
       $element.bind("click", function(){
         questionsCtrl.select($scope);
       });
       questionsCtrl.addQuestion($scope);
     }
   }
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
  var dataRef = new Firebase('https://blazing-heat-6301.firebaseio.com/');
  var data = $firebase(dataRef);
    return data.$asObject().$loaded();
  }

  }
);

var data = [{
  "emotion": "happy",
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
  "emotion": "angry",
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
  "emotion": "sad",
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