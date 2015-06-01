var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('AppController', ['$scope', function ($scope) {
    $scope.$safeApply = function (fn) {
        var phase = this.$root.$$phase;
        if (phase == '$apply' || phase == '$digest') {
            if (fn && (typeof(fn) === 'function')) {
                fn();
            }
        } else {
            this.$apply(fn);
        }
    };
}]);

app.controller('MainController', ['$scope', '$timeout', '$interval', '$log', 'QR_DATA', 'QR_CONST', 'SessionService', 'RantService', 'DialogService',
    function ($scope, $timeout, $interval, $log, QR_DATA, QR_CONST, SessionService, RantService, DialogService) {

        var _qr = $scope.app = {
            emotions: QR_DATA.emotions,
            shareUrls: QR_DATA.shareUrls,
            views: QR_CONST.VIEWS,
            notify: QR_DATA.notify,
            defaults: {
                name: QR_CONST.DEFAULT_VALUE.NAME,
                location: QR_CONST.DEFAULT_VALUE.LOCATION
            },
            restrictions: QR_CONST.RESTRICTIONS
        };

        $scope.rant = {
            allowComments: false
        };

        /**
         * Return a promise that corresponds to getting paginated rants
         * @param pageNumber
         * @returns {*}
         * @private
         */
        var _getRants = function (pageNumber) {
            return RantService.getPaginatedRants(pageNumber);
        };

        /**
         * Return a promise that corresponds to getting the popular rant list
         * @returns {*}
         * @private
         */
        var _getPopularRants = function () {
            return RantService.getPopularRants();
        };

        /**
         * Return a promise that corresponds to posting a rant
         * @param rant
         * @returns {*}
         * @private
         */
        var _postRant = function (rantForm) {
            var rant = RantService.createRantFromFormSubmission(rantForm);
            return RantService.postRant(rant);
        };

        /**
         * Return a promise that corresponds to getting a single rant by id
         * @param id
         * @returns {*}
         * @private
         */
        var _getRantById = function (id) {
            var deferred = $.Deferred();
            RantService.getRantById(id)
                .done(function (rant) {
                    rant ?
                        deferred.resolve(rant) :
                        deferred.reject({message: 'Unable to locate rant with id: ' + id});
                })
                .fail(function () {
                    deferred.reject({message: 'Error occurred while getting rant with id: ' + id});
                });
            return deferred.promise();
        };

        /**
         * Insert a rant into rants at the first position
         * @param data
         * @private
         */
        function addRant(rant) {
            $scope.page.totalRants++;
            $scope.rants.unshift(rant);
        }

        function setPageData(pageData, concatRants) {
            $scope.rants = concatRants ? $scope.rants.concat(pageData.rants) : pageData.rants;
            $scope.page = pageData.page;
        };

        /**
         * Set the current view
         * @param view
         */
        function setView(view) {
            $scope.app.view = view;
        }

        /**
         * Fetch and display the popular rants
         */
        function showPopularRants() {
            _getPopularRants()
                .done(function (rants) {
                    $scope.$safeApply(function () {
                        $scope.popularRants = rants;
                    });
                });
        }

        /**
         * Return the number of new rants;
         * @param data
         * @returns {*}
         */
        function getNewRantCount(data) {
            if (!data) return false;
            return data.page.totalRants - $scope.page.totalRants;
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
                .done(function (data) {
                    addRant(data);
                    setView(_qr.views.LIVE_STREAM);
                })
                .fail(function (error) {
                    $log.error(error);
                })
                .always(function () {
                    $scope.$safeApply(function () {
                        $scope.rant = {};
                    });
                });
        };

        /**
         * Determine if more rants can be fetched
         * @param rants
         * @param page
         * @returns {boolean}
         */
        $scope.hasNextPage = function (rants, page) {
            if (!rants || !page) return false;
            return rants.length < page.totalRants;
        };

        /**
         * Get the next page of rants
         * @param pageNumber
         */
        $scope.getNextPage = function (pageNumber) {
            _getRants(pageNumber + 1)
                .done(function (data) {
                    $scope.$safeApply(function () {
                        $scope.rant = {};
                        setPageData(data, true);
                    });
                })
        };

        /**
         * Show new rants
         * @param data
         */
        $scope.showNewRants = function (data) {
            setPageData(data);
            $scope.newData = undefined;
            $scope.newRantCount = undefined;
        };

        /**
         * Find a rant by id
         * @param id
         */
        $scope.findRantById = function (id) {
            _getRantById(id)
                .done(function (rant) {
                    RantService.openRant(rant);
                })
                .fail(function () {
                    DialogService.notify(

                    );
                });
        };

        /**
         * Open About modal
         */
        $scope.showAbout = function () {
            DialogService.open({
                templateUrl: '/templates/modals/about.html',
                size: 'lg'
            });
        };

        /**
         * Open Beta modal
         */
        $scope.showBeta = function () {
            DialogService.notify(_qr.notify.beta.body, _qr.notify.beta.title);
        };

        /**
         * Open Share modal
         */
        $scope.showShare = function () {
            DialogService.open({
                templateUrl: '/templates/modals/share.html',
                scope: _qr.shareUrls
            });
        };

        /**
         * Open a rant
         * @param rant
         */
        $scope.openRant = function (rant) {
            RantService.openRant(rant);
        };

        /**
         * Watch for changes to the view. We only need to take
         * action when the popular view is displayed, since
         * the fetch interval will ensure we have current
         * rant data
         */
        $scope.$watch('app.view', function (newView, oldView) {
            if (newView === oldView) return;
            if (newView === _qr.views.POPULAR) {
                showPopularRants();
            }
        });

        /**
         * Fetch new rants every N seconds
         */
        var _pollForRants = function () {
            $interval(function () {
                _getRants(1)
                    .done(function (data) {
                        var count = getNewRantCount(data);
                        if (count > 0) {
                            storeNewRants(count, data);
                        }
                    })
            }, QR_CONST.POLLING.RANTS);
        };

        /**
         * Initialize quickrant
         * @private
         */
        var _init = function () {
            _getRants(1)
                .done(function (data) {
                    $scope.$safeApply(function () {
                        setPageData(data);
                    });
                })
                .always(function () {
                    $scope.$safeApply(function () {
                        setView(_qr.views.LIVE_STREAM);
                    });
                    _pollForRants();
                });
        };

        _init();

    }]);
