var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('MainController', ['$scope', '$timeout', '$interval', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', 'ModalService', function ($scope, $timeout, $interval, QR_DATA, QR_CONST, SessionService, RantService, ModalService) {

    var app = $scope.app = {
        emotions: QR_DATA.emotions,
        shareUrls: QR_DATA.shareUrls,
        views: QR_CONST.VIEWS,
        view: QR_CONST.VIEWS.LIVE_STREAM,
        defaults: {
            name: QR_CONST.DEFAULT_VALUE.NAME,
            location: QR_CONST.DEFAULT_VALUE.LOCATION
        }
    };

    $scope.rant = {};
    $scope.allowReplies = false;

    var _postRant = function (rant) {
        return RantService.postRant(rant)
            .done(_unshiftRants)
            .done(_openRantPosted)
            .fail(_logError);
    };

    var _unshiftRants = function(data) {
        $scope.rants.unshift(data.rant);
    };

    var _logError = function(error) {
        console.error(error.message);
    };

    $scope.postRant = function (rant) {
        _postRant(rant)
            .always(function() {
                $timeout(function() {
                    app.view = app.views.LIVE_STREAM;
                    $scope.mostActive = undefined;
                    $scope.rant = {};
                });
            })
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
        if ($scope.rants && $scope.page) {
            return $scope.rants.length < $scope.page.totalElements;
        }
    };

    $scope.showView = function(view) {
        app.view = view;
        if (view === app.views.POPULAR) {
            _getMostActiveRants();
        }
    };

    function _getMostActiveRants() {
        $scope.loading = true;
        RantService.getMostActiveRants()
            .done(function(mostActive) {
                $scope.$apply(function() {
                    $scope.mostActive = mostActive;
                    $scope.loading = false;
                });
            });
    }

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

    var _openRantPosted = function(data) {
        ModalService.open({
            templateUrl: '/templates/modals/rant-posted.html',
            scope: $scope.$new(),
            data: {id: data.response.id}
        });
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

    $scope.share = function() {
        ModalService.open({
            templateUrl: '/templates/modals/share.html',
            data: $scope.shareUrls,
            scope: $scope.$new(),
            backdrop: true
        });
    };

    $scope.openRant = function(rant, searchTerm) {
        var options = {};
        if (rant) {
            options = {
                templateUrl: '/templates/modals/rant.html',
                data: rant,
                size: 'lg',
                windowClass: 'rant-comment',
                backdrop: 'static'
            }
        } else {
            options = { templateUrl: '/templates/modals/rant-not-found.html',
                data: searchTerm
            }
        }
        if (rant) {
            options.scope = $scope.$new();
            ModalService.open(options);
        }
    };

    //authenticate();
    $scope.currentPage = 1;
    loadRants($scope.currentPage);

}]);
