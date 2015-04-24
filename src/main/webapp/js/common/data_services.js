/**
 * Service to perform AJAX calls
 */
app.service('AjaxService', function () {
    return {
        request: function (options) {
            return $.ajax(options);
        }
    }
});

/**
 * Service to perform generic GET/POST actions
 */
app.service('DataAccessService', ['AjaxService', function (AjaxService) {

    /**
     * AJAX request with response handling
     * @param type
     * @param url
     * @param data
     * @returns The original promise is not returned, but rather a promise that wraps it.
     *          This means we can handle cases where a successful response was sent from the server (200 OK),
     *          but contained an error condition (i.e. unable to save record)
     * @private
     */
    var _request = function (type, url, data) {
        var options = {
            type: type,
            url: '/spring' + url
        };
        if (data) {
            options.data = JSON.stringify(data);
            options.contentType = 'application/json';
        }

        var deferred = $.Deferred();
        AjaxService.request(options)
            .done(function (response) {
                if (response.status === 'SUCCESS') {
                    if (response.message) {
                        console.log(response.message);
                    }
                    deferred.resolve(response.data);
                } else {
                    deferred.reject(response);
                }
            })
            .fail(function (response) {
                deferred.reject(response)
            });
        return deferred;
    };

    return {
        get: function (url) {
            return _request('GET', url, null);
        },
        post: function (url, data) {
            return _request('POST', url, data);
        }
    }

}]);