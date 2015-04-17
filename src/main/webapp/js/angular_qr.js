var app = angular.module('quickrant', ['ngCookies', 'ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('MainController', ['$scope', '$timeout', '$interval', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', 'ModalService', function ($scope, $timeout, $interval, QR_DATA, QR_CONST, SessionService, RantService, ModalService) {

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
    };

    $scope.rant = {};

    $scope.quickrant.submit = function (form) {
        if (form.$valid) {
            RantService.postRant($scope.rant)
                .done(function(data) {
                    ModalService.open({
                        templateUrl: '/templates/modals/rant_posted.html',
                        scope: $scope.$new(),
                        data: {id: data.id}
                    });
                    $timeout(function() {
                        delete quickrant.rants;
                        $scope.rant = {};
                    });
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

    $interval(function() {
        RantService.getPaginatedRants($scope.rants, 1)
            .done(function(data) {
                var diff = data.page.totalElements - $scope.page.totalElements;
                if (diff > 0) {
                    $scope.newRants = diff;
                    $scope.deltaData = data;
                }
            });
    }, QR_CONST.POLLING.RANTS);

    $scope.showNewRants = function(rants) {
        _setPageAndRants(rants);
        $scope.deltaData = undefined;
        $scope.newRants = undefined;
    };

    $scope.findRantById = function(searchTerm) {
        RantService.getRantById(searchTerm)
            .done(function(rant) {
                $scope.openRant(rant, searchTerm)
            })
            .fail(function(error) {
                console.error(error.message);
            });
    };

    function loadRants(pageNumber) {
        $scope.loading = true;
        RantService.getPaginatedRants($scope.rants, pageNumber)
            .done(function (data) {
                _setPageAndRants(data);
            })
            .fail(function(error) {
                console.error(error.message);
            })
            .always(function() {
                $timeout(function() {
                    $scope.loading = false;
                });
            });
    }

    function _setPageAndRants(data) {
        $scope.rants = data.rants;
        $scope.page = data.page;
    }

    $scope.$watch('currentPage', function (newPage, oldPage) {
        if (newPage === oldPage) return;
        loadRants(newPage);
    });

    //TODO: put this in a directive
    $scope.charsLeft = function (rant) {
        return subtract(QR_CONST.RESTRICTIONS.MAX_CHAR, rant ? rant.length : 0);
    };

    //TODO: put this in a directive
    $scope.charsToGo = function (rant) {
        return subtract(QR_CONST.RESTRICTIONS.MIN_CHAR,  rant ? rant.length : 0);
    };

    $scope.nextPage = function () {
        if ($scope.currentPage)
        $scope.currentPage += 1;
    };

    $scope.showAbout = function() {
        ModalService.open({
            templateUrl: '/templates/modals/about.html',
            size: 'lg',
            scope: $scope.$new()
        });
    };

    $scope.beta = function() {
        ModalService.open({
            templateUrl: '/templates/modals/beta.html',
            scope: $scope.$new()
        });
    };

    $scope.openRant = function(rant, searchTerm) {
        var options = {};
        if (rant) {
            options = {
                templateUrl: '/templates/modals/rant.html',
                data: rant,
                size: 'lg',
                windowClass: 'rant-reply'
            }
        } else {
            options = { templateUrl: '/templates/modals/rant-not-found.html',
                data: searchTerm
            }
        }
        if (rant) {
            options.scope = $scope.$new();
            RantService.getRepliesByRantId(rant.id)
                .done(function(replies) {
                    options.data.replies = replies;
                    ModalService.open(options);
                })
                .fail(function(error) {
                    console.error(error.message);
                })
                .always(function() {
                    $timeout(function() {
                        $scope.hideReply = false;
                    });
                });
        }
    };

    //authenticate();
    $scope.currentPage = 1;
    loadRants($scope.currentPage);

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
            elem[0].focus();
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