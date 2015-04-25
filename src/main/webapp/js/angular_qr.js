var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('MainController', ['$scope', '$timeout', '$interval', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', 'ModalService', function ($scope, $timeout, $interval, QR_DATA, QR_CONST, SessionService, RantService, ModalService) {

    var _qr = $scope.app = {
        emotions: QR_DATA.emotions,
        shareUrls: QR_DATA.shareUrls,
        views: QR_CONST.VIEWS,
        showPreview: false,
        defaults: {
            name: QR_CONST.DEFAULT_VALUE.NAME,
            location: QR_CONST.DEFAULT_VALUE.LOCATION
        },
        restrictions: QR_CONST.RESTRICTIONS
    };

    $scope.rant = {
        allowComments: false
    };

    var _init = function() {
        _getRants(1)
            .done(function(data) {
                $scope.$apply(function() {
                    setRants(data.rants);
                    setPage(data.page);
                });
            })
            .fail(_logError)
            .always(function() {
                $scope.$apply(function() {
                    setView(_qr.views.LIVE_STREAM);
                })
            })
    };

    /**
     * Return a promise that corresponds to getting paginated rants
     * @param pageNumber
     * @returns {*}
     * @private
     */
    var _getRants = function(pageNumber) {
        return RantService.getPaginatedRants(pageNumber);
    };

    /**
     * Return a promise that corresponds to getting the popular rant list
     * @returns {*}
     * @private
     */
    var _getPopularRants = function() {
        return RantService.getPopularRants();
    };

    /**
     * Return a promise that corresponds to posting a rant
     * @param rant
     * @returns {*}
     * @private
     */
    var _postRant = function (rant) {
        return RantService.postRant(rant);
    };

    /**
     * Return a promise that corresponds to opening a modal window
     * @param options
     * @returns {*|Window}
     * @private
     */
    var _openModal = function(options) {
        return ModalService.open(options);
    };

    /**
     * Insert a rant into rants at the first position
     * @param data
     * @private
     */
    function addRant(data) {
        $scope.rants.unshift(data.rant);
    }

    /**
     * Set the page page
     * @param page
     * @private
     */
    function setPage(page) {
        $scope.page = page;
    }

    /**
     * Set the current view
     * @param view
     */
    function setView(view) {
        $scope.app.view = view;
    }

    /**
     * Set the rants object; optional: concatenate to existing rants
     * @param rants
     * @param concatenate
     * @private
     */
    function setRants(rants, concatenate) {
        $scope.rants = concatenate ? $scope.rants.concat(rants) : rants;
    }

    /**
     * Set popular rants
     * @param rants
     */


    /**
     * Show popular rants
     */
    function showPopularRants() {
        _getPopularRants()
            .done(function (rants) {
                $scope.$apply(function() {
                    $scope.popularRants = rants;
                });
            })
            .fail(_logError);
    }

    function showRantPosted(data) {
        _openModal({
            templateUrl: '/templates/modals/rant-posted.html',
            scope: $scope.$new(),
            data: {id: data.response.id}
        });
    }

    /**
     * Helper method for logging errors
     * @param error
     * @private
     */
    var _logError = function(error) {
        console.error(error.message);
    };

    /**
     * Post a rant
     * @param rant
     */
    $scope.postRant = function (rant) {
        _postRant(rant)
            .done(function(data) {
                $scope.rant = {};
                addRant(data);
                showRantPosted(data);
                setView(_qr.views.LIVE_STREAM);
            })
            .fail(_logError);
    };

    /**
     * Determine if more rants can be fetched
     * @param rants
     * @param page
     * @returns {boolean}
     */
    $scope.hasNextPage = function(rants, page) {
        if (!rants || !page) return false;
        return rants.length < page.totalElements;
    };

    /**
     * Get the next page of rants
     * @param pageNumber
     */
    $scope.getNextPage = function(pageNumber) {
        _getRants(pageNumber + 1)
            .done(function(data) {
                $scope.$apply(function() {
                    $scope.rant = {};
                    setPage(data.page);
                    setRants(data.rants, true);
                });
            })
            .fail(_logError);
    };

    /**
     * Watch for changes to the view
     */
    $scope.$watch('app.view', function(newView, oldView) {
        if (newView === oldView) return;
        if (newView === _qr.views.POPULAR) {
            showPopularRants();
        }
    });

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

    $scope.showAbout = function() {
        _openModal({
            templateUrl: '/templates/modals/about.html',
            size: 'lg',
            scope: $scope.$new()
        });
    };

    $scope.showBeta = function() {
        _openModal({
            templateUrl: '/templates/modals/beta.html',
            scope: $scope.$new()
        });
    };

    $scope.showShare = function() {
        _openModal({
            templateUrl: '/templates/modals/share.html',
            data: $scope.shareUrls,
            scope: $scope.$new(),
            backdrop: true
        });
    };

    $scope.openRant = function(rant) {
        _openModal({
            templateUrl: '/templates/modals/rant.html',
            data: rant,
            size: 'lg',
            scope: $scope.$new(),
            windowClass: 'rant-comment',
            backdrop: 'static'
        });
    };

    _init();

}]);
