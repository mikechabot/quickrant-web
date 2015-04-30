var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('MainController', ['$scope', '$timeout', '$interval', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', 'ModalService', function ($scope, $timeout, $interval, QR_DATA, QR_CONST, SessionService, RantService, ModalService) {

    var _qr = $scope.app = {
        emotions: QR_DATA.emotions,
        shareUrls: QR_DATA.shareUrls,
        views: QR_CONST.VIEWS,
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
     * Return a promise that corresponds to getting a single rant by id
     * @param id
     * @returns {*}
     * @private
     */
    var _getRantById = function(id) {
        var deferred = $.Deferred();
        RantService.getRantById(id)
            .done(function (rant) {
                rant ?
                    deferred.resolve(rant) :
                    deferred.reject({message: 'Unable to locate rant with id: ' + id});
            })
            .fail(function() {
                deferred.reject({message: 'Error occurred while getting rant with id: ' + id});
            });
        return deferred.promise();
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
     * Helper method for logging errors
     * @param error
     * @private
     */
    var _logError = function(error) {
        console.error(error.message);
    };

    /**
     * Insert a rant into rants at the first position
     * @param data
     * @private
     */
    function addRant(data) {
        $scope.page.totalElements++;
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

    /**
     * Show rant posted modal
     * @param data
     */
    function showRantPosted(data) {
        _openModal({
            templateUrl: '/templates/modals/rant-posted.html',
            scope: $scope.$new(),
            data: {id: data.response.id}
        });
    }

    /**
     * Return the number of new rants;
     * @param data
     * @returns {*}
     */
    function getNewRantCount(data) {
        if (!data) return false;
        return data.page.totalElements - $scope.page.totalElements;
    }

    /**
     * Store off any new data in anticipation of the user selecting 'Show N new rants'
     * @param data
     */
    function storeNewRants(count, data) {
        $scope.newRantCount = count;
        $scope.newData = data;
    }

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

    /**
     * Fetch new rants every N seconds
     */
    $interval(function() {
        _getRants(1)
            .done(function(data) {
                var count = getNewRantCount(data);
                if (count > 0) {
                    storeNewRants(count, data);
                }
            })
            .fail(_logError);
    }, QR_CONST.POLLING.RANTS);

    /**
     * Show new rants
     * @param data
     */
    $scope.showNewRants = function(data) {
        setPage(data.page);
        setRants(data.rants);
        $scope.newData = undefined;
        $scope.newRantCount = undefined;
    };

    /**
     * Find a rant by id
     * @param id
     */
    $scope.findRantById = function(id) {
        _getRantById(id)
            .done(function(rant) {
               $scope.openRant(rant);
            })
            .fail(function(error) {
                console.warn(error.message);
                _openModal({
                    templateUrl: '/templates/modals/rant-not-found.html',
                    data: id,
                    scope: $scope.$new()
                });
            });
    };

    /**
     * Open About modal
     */
    $scope.showAbout = function() {
        _openModal({
            templateUrl: '/templates/modals/about.html',
            size: 'lg',
            scope: $scope.$new()
        });
    };

    /**
     * Open Beta modal
     */
    $scope.showBeta = function() {
        _openModal({
            templateUrl: '/templates/modals/beta.html',
            scope: $scope.$new()
        });
    };

    /**
     * Open Share modal
     */
    $scope.showShare = function() {
        _openModal({
            templateUrl: '/templates/modals/share.html',
            data: _qr.shareUrls,
            scope: $scope.$new(),
            backdrop: true
        });
    };

    /**
     * Open single rant modal
     * @param rant
     */
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
