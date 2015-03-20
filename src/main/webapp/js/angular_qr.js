var app = angular.module('quickrant', ['ngCookies', 'ui.bootstrap', 'ngAnimate']);

app.controller('MainController', ['$scope', '$timeout', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', function ($scope, $timeout, QR_DATA, QR_CONST, SessionService, RantService) {

    var quickrant = $scope.quickrant = {
        data: QR_DATA,
        templates: {
            navigation: 'navigation.html',
            rants: 'rants.html'
        }
    };

    $scope.default = {
        name: QR_CONST.DEFAULT_VALUE.NAME,
        location: QR_CONST.DEFAULT_VALUE.LOCATION
    }

    $scope.rant = {};

    $scope.quickrant.submit = function (form) {
        if (form.$valid) {
            RantService.postRant($scope.rant)
                .done(function() {
                    _reset();
                })
                .fail(function (error) {
                    console.error(error.message);
                })
                .always(function () {
                    $timeout(function() {
                        loadRants(1)
                    });
                });
        }
    };

    function loadRants(pageNumber) {
        $scope.loading = true;
        RantService.getPaginatedRants($scope.rants, pageNumber)
            .done(function (response) {
                $scope.$apply(function() {
                    $scope.rants = response.rants;
                    $scope.page = response.page;
                });
            })
            .fail(function(error) {
                console.error(error.message);
            })
    }

    $scope.$watch('currentPage', function (newPage, oldPage) {
        if (newPage === oldPage) return;
        loadRants(newPage);
    });

    $scope.charsLeft = function (rant) {
        return subtract(QR_CONST.RESTRICTIONS.MAX_CHAR, rant ? rant.length : 0);
    };

    $scope.charsToGo = function (rant) {
        return subtract(QR_CONST.RESTRICTIONS.MIN_CHAR,  rant ? rant.length : 0);
    };

    $scope.nextPage = function () {
        $scope.currentPage += 1;
    };

    function _reset() {
        delete quickrant.rants;
        $scope.rant = {};
    }

    //authenticate();
    loadRants(1);

}]);

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
        }
    }
});

/**
 * Directive that allows for one a variable
 * to be copied to another variable.
 *
 * destination: Copy ngModel to this variable
 * default-value: If ngModel model is null or undefined,
 *              then set the destination to this variable
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