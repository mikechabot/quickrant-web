/**
 * Service to perform jQuery AJAX calls
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
app.service('DataAccessService', ['AjaxService', 'QR_CONST', function (AjaxService, QR_CONST) {

    /**
     * AJAX request with response handling
     * @param type
     * @param url
     * @param data
     * @returns a promise wrapper
     * @private
     */
    var _request = function (type, url, data) {
        var options = {
            type: type,
            url: '/spring' + url,
            dataType: 'json'
        };
        if (data) {
            options.data = JSON.stringify(data);
            options.contentType = 'application/json';
        }

        var deferred = $.Deferred();
        AjaxService.request(options)
            .done(function (response) {
                if (response.status === QR_CONST.STATUS.SUCCESS) {
                    if (response.message) {
                        console.log(response.message);
                    }
                    deferred.resolve(response.data);
                }
            })
            .fail(function (jqXHR, status, error) {
                console.warn(error + ': ' + jqXHR.responseJSON.message);
                deferred.reject(jqXHR.responseJSON);
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