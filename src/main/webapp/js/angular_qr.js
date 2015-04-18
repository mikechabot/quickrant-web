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

    $scope.allowReplies = false;

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

    $scope.canFetchMoreRants = function() {
        return $scope.rants.length < $scope.page.totalElements;
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
                windowClass: 'rant-reply',
                backdrop: 'static'
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
                    ModalService.open(options).result
                        .then(function(replyCount) {
                            rant.replyCount= replyCount;
                    });
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