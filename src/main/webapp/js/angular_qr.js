var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('MainController', ['$scope', '$timeout', '$interval', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', 'ModalService', function ($scope, $timeout, $interval, QR_DATA, QR_CONST, SessionService, RantService, ModalService) {

    var app = $scope.app = {
        emotions: QR_DATA.emotions,
        shareUrls: QR_DATA.shareUrls,
        views: QR_CONST.VIEWS,
        defaults: {
            name: QR_CONST.DEFAULT_VALUE.NAME,
            location: QR_CONST.DEFAULT_VALUE.LOCATION
        }
    };

    $scope.rant = {};
    $scope.allowReplies = false;

    var _init = function() {
        _getRants(1)
            .done(function(data) {
                $scope.$apply(function() {
                    _setRants(data.rants);
                    _setPage(data.page);
                    _setView(app.views.LIVE_STREAM);
                });
            })
            .fail(_logError);
    };

    var _setPage = function(page) {
        if (page) console.log(page);
        $scope.page = page;
    };

    var _setRants = function(rants, concatenate) {
        $scope.rants = concatenate ? $scope.rants.concat(rants) : rants;
    };

    var _setView = function(view) {
        app.view = view;
        if (view === app.views.POPULAR) {
            //_getMostActiveRants();
        }
    };

    var _postRant = function (rant) {
        return RantService.postRant(rant);
    };

    var _getRants = function(pageNumber) {
        return RantService.getPaginatedRants(pageNumber);
    };

    var _openModal = function(options) {
        return ModalService.open(options);
    };

    var _addRant = function(data) {
        $scope.rants.unshift(data.rant);
    };

    var _logError = function(error) {
        console.error(error.message);
    };

    $scope.postRant = function (rant) {
        _postRant(rant)
            .done(function(data) {
                $scope.rant = {};
                _addRant(data);
                _openRantPosted(data);
                _setView(app.views.LIVE_STREAM);
            })
            .fail(_logError);
    };

    $scope.showView = function(view) {
        _setView(view);
    };

    $scope.hasNextPage = function(rants, page) {
        if (!rants || !page) return false;
        return rants.length < page.totalElements;
    };

    $scope.getNextPage = function(pageNumber) {
        _getRants(pageNumber + 1)
            .done(function(data) {
                $scope.$apply(function() {
                    _setPage(data.page);
                    _setRants(data.rants, true);
                });
            })
            .fail(_logError);
    };

    //$interval(function() {
    //    RantService.getPaginatedRants($scope.rants, 1)
    //        .done(function(data) {
    //            var diff = data.page.totalElements - $scope.page.totalElements;
    //            if (diff > 0) {
    //                $scope.newRants = diff;
    //                $scope.deltaData = data;
    //            }
    //        });
    //}, QR_CONST.POLLING.RANTS);

    //$scope.showNewRants = function(rants) {
    //    _setPageAndRants(rants);
    //    $scope.deltaData = undefined;
    //    $scope.newRants = undefined;
    //};

    //$scope.findRantById = function(searchTerm) {
    //    RantService.getRantById(searchTerm)
    //        .done(function(rant) {
    //            $scope.openRant(rant, searchTerm)
    //        })
    //        .fail(function(error) {
    //            console.error(error.message);
    //        });
    //};

    //$scope.canFetchMoreRants = function() {
    //    if ($scope.rants && $scope.page) {
    //        return $scope.rants.length < $scope.page.totalElements;
    //    }
    //};



    //function _getMostActiveRants() {
    //    $scope.loading = true;
    //    RantService.getMostActiveRants()
    //        .done(function(mostActive) {
    //            $scope.$apply(function() {
    //                $scope.mostActive = mostActive;
    //                $scope.loading = false;
    //            });
    //        });
    //}

    //function loadRants(pageNumber) {
    //    $scope.loading = true;
    //    RantService.getPaginatedRants($scope.rants, pageNumber)
    //        .done(function (data) {
    //            _setPageAndRants(data);
    //        })
    //        .fail(function(error) {
    //            console.error(error.message);
    //        })
    //        .always(function() {
    //            $timeout(function() {
    //                $scope.loading = false;
    //            });
    //        });
    //}

    //function _setPageAndRants(data) {
    //    $scope.rants = data.rants;
    //    $scope.page = data.page;
    //}

    //$scope.$watch('currentPage', function (newPage, oldPage) {
    //    if (newPage === oldPage) return;
    //    loadRants(newPage);
    //});

    //TODO: put this in a directive
    $scope.charsLeft = function (rant) {
        return subtract(QR_CONST.RESTRICTIONS.MAX_CHAR, rant ? rant.length : 0);
    };

    var _openRantPosted = function(data) {
        _openModal({
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
        _openModal({
            templateUrl: '/templates/modals/about.html',
            size: 'lg',
            scope: $scope.$new()
        });
    };

    $scope.beta = function() {
        _openModal({
            templateUrl: '/templates/modals/beta.html',
            scope: $scope.$new()
        });
    };

    $scope.share = function() {
        _openModal({
            templateUrl: '/templates/modals/share.html',
            data: $scope.shareUrls,
            scope: $scope.$new(),
            backdrop: true
        });
    };

    //$scope.openRant = function(rant, searchTerm) {
    //    var options = {};
    //    if (rant) {
    //        options = {
    //            templateUrl: '/templates/modals/rant.html',
    //            data: rant,
    //            size: 'lg',
    //            windowClass: 'rant-comment',
    //            backdrop: 'static'
    //        }
    //    } else {
    //        options = { templateUrl: '/templates/modals/rant-not-found.html',
    //            data: searchTerm
    //        }
    //    }
    //    if (rant) {
    //        options.scope = $scope.$new();
    //        ModalService.open(options);
    //    }
    //};

    //authenticate();
    //$scope.currentPage = 1;
    //loadRants($scope.currentPage);

    _init();

}]);
