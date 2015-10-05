app.service('StatisticsService', ['DataAccessService', function(DataAccessService) {

    return {
        getEmotionStatistics: function() {
            return DataAccessService.get('/statistics/emotion');
        },
        getQuestionStatistics: function() {
            return DataAccessService.get('/statistics/question');
        },
        generateStatistics: function(page, data) {
            var statisticsMap = this.generateStatisticsMap(page, data);
            var statisticsList = _.sortBy(statisticsMap, function(statistic) {
                return statistic.sortOrder;
            });
            return { statisticsMap: statisticsMap, statisticsList: statisticsList };
        },
        generateStatisticsMap: function(page, data) {

            var rantCount = page.getRantCount();
            var totalRants = data.totalElements;
            var currentPage = data.number + 1;
            var totalPages = data.totalPages;

            return {
                'TOTAL_RANTS': {
                    sortOrder: 0,
                    label: 'total rants',
                    value: totalRants
                },
                'RANT_COUNT': {
                    sortOrder: 1,
                    label: 'rants viewed',
                    value: rantCount
                },
                'RANTS_TO_VIEW': {
                    sortOrder: 2,
                    label: 'rants left to view',
                    value: totalRants - rantCount
                },
                'CURRENT_PAGE': {
                    sortOrder: 3,
                    label: 'current page',
                    value: currentPage
                },
                'PAGES_TO_VIEW': {
                    sortOrder: 4,
                    label: 'pages left to view',
                    value: totalPages - currentPage
                },
                'TOTAL_PAGES': {
                    sortOrder: 5,
                    label: 'total pages',
                    value: totalPages
                }
            }
        }
    }

}]);