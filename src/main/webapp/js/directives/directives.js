/**
 * Directive to set scroll positioning
 *
 *   scroll-element: DOM element(s) to be scrolled upon (comma-separated)
 *  scroll-to-pixel: Scroll to this number of pixels from the top of the scroll-element
 *     scroll-speed: Speed at which to scroll
 *        scroll-to: Scroll to a location (e.g. "top", "bottom")
 *         focus-on: Focus on an element after scrolling
 */
app.directive('scrollOnClick', ['$document', '$window', function($document, $window) {
    return {
        restrict: 'A',
        scope: {
            scrollElement: '@',
            scrollToPixel: '@',
            scrollSpeed: '@',
            scrollTo: '@',
            focusOn: '@'
        },
        link: function(scope, element) {
            var context = scope.scrollElement || 'body, html';
            var pixels = scope.scrollToPixel || 0;
            var speed = scope.scrollSpeed || 'slow';
            var scrollTo = scope.scrollTo;

            if (scrollTo === 'top') {
                pixels = 0;
            } else if (scrollTo === 'bottom') {
                pixels = $document.height() + $document.innerHeight() + $document.outerHeight();
            }

            element.on('click', function() {
                $(context).animate({scrollTop: pixels }, speed);
                if (scope.focusOn) {
                    $(scope.focusOn)[0].focus();
                }
            });
        }
    }
}]);

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
        '<a href emotion ng-model="emotion"><img ng-src="/img/{{::emotion.name | lowercase}}.png"></a>' +
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
        template: '<button class="btn btn-{{buttonStyle}} btn-question" type="button">{{::question}}</button>',
        scope: {
            question: '=',
            buttonStyle: '='
        },
        link: function (scope, element, attrs, questionsCtrl) {
            element.bind('click', function () {
                questionsCtrl.select(scope.question);
            });
        }
    }
});

app.directive('rants', ['$timeout', 'QR_DATA', 'DialogService', function ($timeout, QR_DATA, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-stream.html',
        scope: {
            page: '=',
            activeView: '=',
            views: '=',
            popularRants: '='
        },
        controller: function($scope) {

            $scope.replyToRant = {};

            this.replyToRant = function(rant) {
                $timeout(function() {
                    $scope.activeView = $scope.views.REPLY;
                    $scope.replyToRant = rant;
                });
            };

            $scope.isLastRant = function(index) {
                return index == $scope.page.getRantCount() - 1
            };

            $scope.isLastPopularRant = function(index) {
                return index === $scope.popularRants.length - 1;
            };

            $scope.getNextPage = function() {
                $scope.page.getNextPage()
                    .done(function(page) {
                        $timeout(function() {
                            $scope.page = page;
                        });
                    });
            };

            $scope.showPolledRants = function() {
                $scope.page.applyPolledRants();
            };

            $scope.showShare = function() {
                DialogService.open({
                    templateUrl: '/templates/modals/share.html',
                    scope: QR_DATA.shareUrls
                });
            };

        }
    }
}]);


app.directive('rant', ['RantService',function(RantService) {
    return {
        restrict: 'E',
        require: '^rants',
        templateUrl: '/templates/directives/rant.html',
        scope: {
            rant: '='
        },
        link: function (scope, element, attrs, rantsCtrl) {
            scope.replyTo = function(rant) {
                rantsCtrl.replyToRant(rant);
            };
        }
    }
}]);

app.directive('rantForm', ['$timeout', 'QR_CONST', 'RantService', 'DialogService', function ($timeout, QR_CONST, RantService, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-form.html',
        scope: {
            rant: '=',
            emotions: '=',
            selection: '=',
            page: '='
        },
        controller: function ($scope) {

            $scope.rant.allowComments = false;

            $scope.getPanelStyleForEmotion = function(selection) {
                var emotionKey = selection.emotion;
                var emotion = $scope.emotions[emotionKey];
                return emotion.styles.panel;
            };

            $scope.getCharactersLeft = function(rant) {
                if (!rant.text) return;
                return QR_CONST.RESTRICTIONS.MAX_CHAR - rant.text.length;
            };

            $scope.postRant = function(rant) {
                if (!rant || !rant.text) return;
                RantService.postRant(rant, $scope.selection)
                    .done(function(postedRant) {
                        $timeout(function() {
                            $scope.page.addPostedRant(postedRant);
                        });
                    })
                    .fail(function(error) {
                        DialogService.error(error.message);
                    })
                    .always(function() {
                        $timeout(function() {
                            $scope.rant = {};
                        });
                    });
            };

            $scope.$watch('form.text.$error', function(errors) {
                $scope.error = {};
                _.each(errors, function(value, key) {
                    if (value === true) {
                        $scope.error[key] = true;
                    }
                });
            }, true);

        }
    }
}]);

app.directive('replyToRant', ['$timeout', 'QR_CONST', 'RantService', 'DialogService', function ($timeout, QR_CONST, RantService, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-reply.html',
        scope: {
            rant: '='
        },
        controller: function ($scope) {

            $scope.showForm = true;

            function _addCommentToRant(comment) {
                $scope.rant.comments.push(comment);
                $scope.rant.commentCount += 1;
            }

            function _resetForm() {
                $scope.form.location = undefined;
                $scope.form.name = undefined;
                $scope.form.text = undefined;
            }

            $scope.saveComment = function(form) {
                var comment = RantService.createNewComment(form);
                if (!comment || !comment.text) return;
                RantService.saveComment(comment, $scope.rant.id)
                    .done(function(comment) {
                        _addCommentToRant(comment);
                        _resetForm();
                    })
                    .always(function() {
                        $timeout(function() {
                            $scope.showForm = false;
                        });
                    });
            };

            $scope.getCharactersLeft = function(text) {
                if (!text) return;
                return QR_CONST.RESTRICTIONS.MAX_CHAR - text.length;
            };

            $scope.$watch('form.text.$error', function(errors) {
                $scope.error = {};
                _.each(errors, function(value, key) {
                    if (value === true) {
                        $scope.error[key] = true;
                    }
                });
            }, true);
        }
    }
}]);

app.directive('verticalStatisticsList', function() {

    var templateHtml =
        '<div class="col-lg-2 col-md-2 col-sm-2 hidden-xs sidebar text-right">' +
        '<div class="page-info">' +
        '<div ng-repeat="stat in data" class="media">' +
        '<div class="media-left"><h2>{{stat.value}}</h2></div>' +
        '<div class="media-body">{{stat.label}}</div>' +
        '</div>' +
        '</div>' +
        '</div>';

    return {
        restrict: 'E',
        template: templateHtml,
        scope: {
            data: '='
        },
        link: function(scope) {

        }
    }

});