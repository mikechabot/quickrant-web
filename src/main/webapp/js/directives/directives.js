/**
 * Directive to set scroll positioning
 */
app.directive('scroll', function() {
    return {
        restrict: 'A',
        scope: {
            scrollTo: '@',
            speed: '@'
        },
        link: function(scope, element) {
            element.on('click', function() {
                $("body, html").animate({scrollTop: scope.scrollTo }, scope.speed || 'slow');
            });
        }
    }
});

/**
 * Directive that copies a scope variable to another
 *
 *       ngModel: Value to be copied
 *   destination: Copy ngModel to this scope variable
 * default-value: If ngModel model is null or undefined, use this value
 */
app.directive('copyable', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
            destination: '=',
            defaultValue: '='
        },
        link: function (scope, elem, attr, ctrl) {
            ctrl.$viewChangeListeners.push(function () {
                scope.destination = hasValue(ctrl.$viewValue) ? ctrl.$viewValue : scope.defaultValue;
            });
        },
        controller: function($scope) {
            $scope.destination = $scope.defaultValue;
        }
    }
});

app.directive('faces', ['$timeout', function ($timeout) {
    return {
        restrict: 'A',
        scope: {
            data: '=',
            rant: '='
        },
        controller: function ($scope) {
            this.select = function (face) {
                $timeout(function () {
                    if (face === $scope.rant.face) {
                        $scope.rant = {};
                        return;
                    }
                    $scope.rant.face = face;
                    $scope.rant.question = undefined;
                });
            };
        }
    }
}]);

app.directive('face', function () {
    return {
        restrict: 'A',
        require: '^faces',
        scope: {
            ngModel: '='
        },
        link: function (scope, element, attrs, facesCtrl) {
            element.bind('click', function () {
                facesCtrl.select(scope.ngModel);
            });
        }
    }
});

app.directive('questions', function () {
    return {
        restrict: 'A',
        scope: {
            rant: '='
        },
        controller: function ($scope) {
            this.select = function (question) {
                $scope.$apply(function () {
                    if (question === $scope.rant.question) {
                        $scope.rant.question = undefined;
                        return;
                    }
                    $scope.rant.question = question;
                });
            };
        }
    };
});

app.directive('question', function () {
    return {
        restrict: 'E',
        require: '^questions',
        scope: {
            question: '=',
            customStyle: '='
        },
        template: '<button class="btn btn-{{customStyle}} question" type="button">{{::question}}</button>',
        link: function ($scope, $element, $attrs, questionsCtrl) {
            $element.bind('click', function () {
                questionsCtrl.select($scope.question);
            });
        }
    }
});

app.directive('rantText', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            ctrl.$viewChangeListeners.push(function () {
                scope.quickrant.rant = ctrl.$viewValue;
            });
            elem[0].focus();
        }
    }
});