app.service('PromiseService', function() {

    /**
     * Convert an Angular promise to a JQuery promise
     */
    return {
        convertAngularPromiseToJQueryPromise: function(angularPromise) {
            var deferred = $.Deferred();

            angularPromise.then(function success(data) {
                deferred.resolve(data);
            }, function fail(error) {
                deferred.reject(error);
            });

            return deferred.promise();
        }
    }

});
