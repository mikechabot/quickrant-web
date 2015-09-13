/**
 * Directive to set scroll positioning
 *
 *   scroll-element: DOM element(s) to be scrolled upon (comma-separated)
 *  scroll-to-pixel: Scroll to this number of pixels from the top of the scroll-element
 *     scroll-speed: Speed at which to scroll
 *         focus-on: Focus on an element after scrolling
 */
app.directive('scrollOnClick', function() {
    return {
        restrict: 'A',
        scope: {
            scrollElement: '@',
            scrollToPixel: '@',
            scrollSpeed: '@',
            focusOn: '@'
        },
        link: function(scope, element) {
            var context = scope.scrollElement || 'body, html';
            var pixels = scope.scrollToPixel || 0;
            var speed = scope.scrollSpeed || 'slow';
            element.on('click', function() {
                $(context).animate({scrollTop: pixels }, speed);
                if (scope.focusOn) {
                    $(scope.focusOn)[0].focus();
                }
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

/**
 * Directive to focus on an element
 *
 *  focus-trigger: Element is focused when the trigger is defined
 *
 */
app.directive('focus', ['$timeout', function ($timeout) {
    return {
        restrict: 'A',
        scope: {
          focusTrigger: '='
        },
        link: function (scope, elem) {
            elem[0].focus();
            scope.$watch('focusTrigger', function(newValue) {
                $timeout(function() {
                    if (newValue === true) {
                        elem[0].focus();
                    }
                });
            });
        }
    }
}]);

app.directive('navigation', ['QR_DATA', 'DialogService', function (QR_DATA, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/navigation.html',
        scope: true,
        controller: function ($scope) {

            var notify = QR_DATA.notify;

            $scope.showAbout = function() {
                DialogService.open({
                    templateUrl: '/templates/modals/about.html',
                    size: 'lg'
                });
            };

            $scope.showBeta = function() {
                DialogService.notify(notify.beta.body, notify.beta.title);
            };

        }
    }
}]);

app.directive('emotions', ['$timeout', function ($timeout) {

    var templateHtml =
        '<div class="row margin-bottom-lg text-center"> ' +
        '<div ng-repeat="emotion in emotions" class="col-lg-4 col-sm-4 col-xs-4">' +
        '<a href emotion ng-model="emotion"><img ng-src="/img/{{::emotion.name}}.png"></a>' +
        '</div>' +
        '</div>';

    return {
        restrict: 'E',
        template: templateHtml,
        scope: {
            emotions: '=',
            selection: '=',
            rant: '='
        },
        controller: function ($scope) {
            this.select = function (emotion) {
                $timeout(function () {
                    _alwaysClear();
                    !_alreadySelected(emotion)
                        ? $scope.selection.emotion = emotion.name
                        : delete $scope.selection.emotion;
                });
            };
            function _alreadySelected(emotion) {
                return emotion.name === $scope.selection.emotion;
            }
            function _alwaysClear() {
                delete $scope.selection.question;
                $scope.rant = {};
            }
        }
    }
}]);

app.directive('emotion', function () {
    return {
        restrict: 'A',
        require: '^emotions',
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
    var templateHtml =
        '<div ng-repeat="emotion in emotions" class="text-center">' +
        '<div ng-if="showQuestionsForEmotion(emotion, selection)" class="row margin-bottom-lg">' +
        '<div class="col-lg-offset-1 col-lg-10">' +
        '<question ng-repeat="question in emotion.questions" question="question" button-style="emotion.styles.button"></question>' +
        '</div>' +
        '</div>';
    return {
        restrict: 'E',
        template: templateHtml,
        scope: {
            emotions: '=',
            selection: '='
        },
        controller: function ($scope) {
            this.select = function (question) {
                $timeout(function() {
                    !_alreadySelected(question)
                        ? $scope.selection.question = question
                        : delete $scope.selection.question;
                });
            };
            function _alreadySelected(question) {
                return question === $scope.selection.question;
            }
            $scope.showQuestionsForEmotion = function(emotion, selection) {
                if (_.isEmpty(selection)) return false;
                return emotion.name === selection.emotion;
            }
        }
    };
}]);

app.directive('question', function () {
    return {
        restrict: 'E',
        require: '^questions',
        scope: {
            question: '=',
            buttonStyle: '='
        },
        template: '<button class="btn btn-{{buttonStyle}} btn-question" type="button">{{::question}}</button>',
        link: function (scope, $element, $attrs, questionsCtrl) {
            $element.bind('click', function () {
                questionsCtrl.select(scope.question);
            });
        }
    }
});

app.directive('rants', ['$timeout', 'RantService', 'DialogService', function ($timeout, RantService, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rants.html',
        scope: true,
        link: function (scope) {

            var _getRantsByPageNumber = function(pageNumber) {
                return RantService.getRantsByPageNumber(pageNumber)
            };

            var _initQuickrant = function() {
                _getRantsByPageNumber(0)
                    .done(function (page) {
                        $timeout(function() {
                            scope.rants = page.rants;
                            scope.pageInfo = page.pageInfo;
                        });

                    })
                    .fail(function(error) {
                        DialogService.error(error.message);
                    });
            };

            _initQuickrant();

        }
    }
}]);

app.directive('rantForm', ['QR_CONST', function (QR_CONST) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-form.html',
        scope: {
            rant: '=',
            emotions: '=',
            selection: '='
        },
        controller: function ($scope) {

            var validationWatch;
            var emotions = $scope.emotions;

            $scope.getPanelStyleForSelection = function(selection) {
                var emotionKey = selection.emotion;
                var emotion = emotions[emotionKey];
                return emotion.styles.panel;
            };

            $scope.getCharactersLeft = function(rant) {
                if (!rant.text) return;
                return QR_CONST.RESTRICTIONS.MAX_CHAR - rant.text.length;
            };

            function _buildValidationWatch() {
                if (validationWatch) {
                    console.log('killing watch');
                    validationWatch();
                }
                validationWatch = $scope.$watch('form.text.$error', function(properties) {
                    $scope.error = {};
                    _.each(properties, function(value, key) {
                        if (value === true) {
                            $scope.error[key] = true;
                        }
                    });
                }, true);
            }

            _buildValidationWatch();
        }
    }
}]);