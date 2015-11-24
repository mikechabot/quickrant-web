app.factory('RantPageFactory', ['$interval', '$timeout', 'APP_CONST', 'RantService', function($interval, $timeout, APP_CONST, RantService) {

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
            this.rants = [];

            RantService.getFirstPage()
                .done(function(data) {
                    page.rants = data.content;
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });


            return deferred.promise();
        },
        getRants: function() {
            return this.rants;
        }
    });

    return newRantPage;

}]);