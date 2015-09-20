app.factory('RantPageFactory', ['$interval', 'QR_CONST', 'RantService', 'StatisticsService', function($interval, QR_CONST, RantService, StatisticsService) {

    function RantPage() {};                     // Empty constructor

    function newRantPage() {
        var deferred = $.Deferred();
        var rantPage = new RantPage();

        rantPage._init()
            .done(function() {
                deferred.resolve(rantPage);
            })
            .fail(function(error) {
                deferred.reject(error);
            });

        return deferred.promise();
    }

    angular.extend(RantPage.prototype, {
        _init: function() {
            var page = this;

            var deferred = $.Deferred();

            this.rants = [];                    // List of user rants 
            this.polledRants = [];              // Rants polled from the server
            this.statisticsMap = {};            // Map of statistics keyed by statistic type 
            this.statisticsList = [];           // List of statistics in sort order

            this.lastPage = false;              // Determine if the current page is the last page
            this.newestRantDate = {};           // Date of the newest rate, used for polling

            RantService.getFirstPage()
                .done(function(data) {
                    page._initPageData(data, QR_CONST.UPDATE_ACTIONS.ADD_PAGE);
                    page._initPolling(page);
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });


            return deferred.promise();
        },
        _initPageData: function(data, updateAction) {

            var rants = data.content;
            var push = true;

            if (!rants) {
                rants = data;
                push = false;
            }

            this.addRants(rants, push);
            this.setNewestRantDate(_.first(this.getRants()).createdDate);

            switch (updateAction) {
                case QR_CONST.UPDATE_ACTIONS.ADD_PAGE: {
                    this.lastPage = data.last;
                    this._setStatistics(data);
                    break;
                }
                case QR_CONST.UPDATE_ACTIONS.ADD_RANTS: {
                    this.incrementStatisticValueByType(QR_CONST.STATISTICS.TOTAL_RANTS, this.getPolledRantCount());
                    this.incrementStatisticValueByType(QR_CONST.STATISTICS.RANT_COUNT, this.getPolledRantCount());
                    this.polledRants = [];
                    break;
                }
                default: {
                    console.log('Unsupported update action: ' + updateAction);
                    break;
                }
            }
        },
        _initPolling: function() {
            var page = this;
            $interval(function() {
                var date = page.getNewestRantDate();
                if (date) {
                    RantService.getRantsCreatedAfter(date)
                        .done(function(rants) {
                            page.polledRants = rants;
                        });
                }
            }, QR_CONST.POLLING.RANTS);

        },
        _setStatistics: function(data) {
            var stats = StatisticsService.generateStatistics(this, data);
            this.statisticsMap = stats.statisticsMap;
            this.statisticsList = stats.statisticsList;
        },
        setNewestRantDate: function(date) {
            this.newestRantDate = date;
        },
        getRants: function() {
            return this.rants;
        },
        hasRants: function() {
            return !_.isEmpty(this.getRants());
        },
        getRantCount: function() {
            return this.rants.length
        },
        pushRant: function(page, rant) {
            page.rants.push(rant);
        },
        unshiftRant: function(page, rant) {
            page.rants.unshift(rant);
        },
        addRants: function(rants, push) {
            if (!this.hasRants()) {
                this.rants = rants;
            } else {
                var page = this;
                var action = push ? this.pushRant : this.unshiftRant;
                _.each(rants, function(rant) {
                    action(page, rant);
                }, this);
            }
        },
        getPolledRants: function() {
            return this.polledRants;
        },
        hasPolledRants: function() {
            return !_.isEmpty(this.getPolledRants());
        },
        getPolledRantCount: function() {
            return this.polledRants.length
        },
        getStatisticsList: function() {
            return this.statisticsList;
        },
        getStatisticsMap: function() {
            return this.statisticsMap;
        },
        getStatisticByType: function(type) {
            return this.getStatisticsMap()[type];
        },
        getStatisticLabelByType: function(type) {
            return this.getStatisticsMap()[type].label;
        },
        getStatisticValueByType: function(type) {
            return this.getStatisticsMap()[type].value;
        },
        incrementStatisticValueByType: function(type, value) {
            var statistic = this.getStatisticByType(type);
            statistic.value += value;
        },
        getTotalRants: function() {
            return this.getStatisticValueByType(QR_CONST.STATISTICS.TOTAL_RANTS);
        },
        getRantsToView: function() {
            return this.getStatisticValueByType(QR_CONST.STATISTICS.RANTS_TO_VIEW);
        },
        getCurrentPage: function() {
            return this.getStatisticValueByType(QR_CONST.STATISTICS.CURRENT_PAGE);
        },
        getPagesToView: function() {
            return this.getStatisticValueByType(QR_CONST.STATISTICS.PAGES_TO_VIEW);
        },
        getTotalPages: function() {
            return this.getStatisticValueByType(QR_CONST.STATISTICS.TOTAL_PAGES);
        },
        hasNextPage: function() {
            return !this.lastPage;
        },
        getNextPage: function() {
            var page = this;
            var deferred = $.Deferred();
            RantService.getPageByPageNumber(this.getCurrentPage())
                .done(function(newPage) {
                    page._initPageData(newPage, QR_CONST.UPDATE_ACTIONS.ADD_PAGE);
                    deferred.resolve(page);
                })
                .fail(function() {
                    deferred.reject(deferred);
                });
            return deferred.promise();
        },
        getNewestRantDate: function() {
            return this.newestRantDate;
        },
        applyPolledRants: function() {
            this._initPageData(this.getPolledRants(), QR_CONST.UPDATE_ACTIONS.ADD_RANTS);
        }
    });

    return newRantPage;

}]);