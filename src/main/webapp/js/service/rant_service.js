app.service('RantService', ['$log', 'DataAccessService', function ($log, DataAccessService) {

    var basePath = '/rants';

    return {
        getFirstPage: function() {
            var deferred = $.Deferred();
            this.getPageByPageNumber(0)
                .done(function(page) {
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });
            return deferred.promise();
        },
        getPageByPageNumber: function (pageNumber) {
            if (pageNumber < 0) throw new Error('Page number cannot be less than zero');
            var deferred = $.Deferred();
            DataAccessService.get(basePath + '/page/' + pageNumber)
                .done(function(page) {
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });
            return deferred.promise();
        }
    };

}]);