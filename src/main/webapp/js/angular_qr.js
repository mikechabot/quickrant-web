var app = angular.module('quickrant', ['ngCookies', 'firebase', 'ui.bootstrap']);

app.controller('MainController', ['$scope', '$timeout', 'DATA', 'SessionService', 'RantService', function ($scope, $timeout, DATA, SessionService, RantService) {

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

    $scope.currentPage = 1;

    $scope.quickrant.submit = function (form) {
        if (form.$valid) {
            postRant({
                selection: {
                    emotion: quickrant.selection.face.emotion,
                    question: quickrant.selection.question
                },
                ranter: {
                    /* TODO: Don't do this like this */
                    name: getDefaultString(form.visitor, quickrant.user.defaultName),
                    location: getDefaultString(form.location, quickrant.user.defaultName)
                },
                rant: form.rant
            });
        }
    };

    function postRant(data) {
        if (!data) return;
        RantService.postRant(data)
            .fail(function (response) {
                console.error(response);
            })
            .always(function () {
                quickrant.selection = {};
                loadRants(1);
            });
    }

    function loadRants(page) {
        $scope.loading = true;
        RantService.getRants(page)
            .done(function (response) {
                console.log(response);
                if ($scope.quickrant.rants) {
                    var rants = $scope.quickrant.rants.content;
                    rants = rants.concat(response.content);
                    response.content = rants;
                }
                $timeout(function () {
                    $scope.quickrant.rants = response;
                });
            })
            .fail(function() {
                console.log('Unable to load rants');
            })
            .always(function () {
                $timeout(function () {
                    $scope.loading = false;
                });
            });
    }

    $scope.$watch('currentPage', function (newPage, oldPage) {
        if (newPage === oldPage) return;
        loadRants(newPage);
    });

    $scope.charsLeft = function (rant) {
        if (!rant) return restrictions.maxChars;
        return subtract(restrictions.maxChars, rant.length);
    };

    $scope.charsToGo = function (rant) {
        if (!rant) return restrictions.minChars;
        return subtract(restrictions.minChars, rant.length);
    };

    $scope.nextPage = function () {
        $scope.currentPage += 1;
    };

    //authenticate();
    loadRants($scope.currentPage);

}]);

app.directive('faces', ['$timeout', function ($timeout) {
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
                $timeout(function () {
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

app.directive('questions', ['$timeout', function ($timeout) {
    return {
        restrict: 'A',
        scope: {
            selection: '='
        },
        controller: function ($scope) {
            this.select = function (question) {
                $scope.$apply(function () {
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

app.directive('visitor', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            scope.quickrant.user.name = scope.quickrant.user.defaultName;
            ctrl.$viewChangeListeners.push(function () {
                scope.quickrant.user.name = ctrl.$viewValue.length > 0 ? ctrl.$viewValue : scope.quickrant.user.defaultName;
            });
        }
    }
});

app.directive('location', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            scope.quickrant.user.location = scope.quickrant.user.defaultLocation;
            ctrl.$viewChangeListeners.push(function () {
                scope.quickrant.user.location = ctrl.$viewValue.length > 0 ? ctrl.$viewValue : scope.quickrant.user.defaultLocation;
            });
        }
    }

});

app.controller('MyMainCtrl', ['TemplateService', function(TemplateService) {
    /* Specify files to be loaded */
    var templates = [ '/navigation.html', '/bar.html' ];

    /* Fetch and cache each file */
    _.each(templates, function(template) {
       TemplateService.loadTemplate(template)
           .done(function() {
                console.log('Loaded template: ' + template);
           });
    });
}]);