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
app.service('DataAccessService', ['$log', 'AjaxService', 'QR_CONST', function ($log, AjaxService, QR_CONST) {

    var _urlPrefix = '/spring';

    var _success = function(deferred, response) {
        deferred.resolve(response.data);
        if (response.message) $log.info(response.message);
    };

    var _fail = function(deferred, response) {
        deferred.reject(response);
        $log.error(response);
    };

    var _error = function(deferred, jqXHR, error) {
        deferred.reject(jqXHR.responseJSON);
        $log.error(error + ': ' + jqXHR.responseJSON.message);
    };

    var _isSuccess = function(response) {
        return response && (response.status === QR_CONST.STATUS.SUCCESS);
    };

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
            url: _urlPrefix + url,
            dataType: 'json'
        };
        if (data) {
            options.data = JSON.stringify(data);
            options.contentType = 'application/json';
        }

        var deferred = $.Deferred();
        AjaxService.request(options)
            .done(function (response) {
                _isSuccess(response)
                    ? _success(deferred, response)
                    : _fail(deferred, response);
            })
            .fail(function (jqXHR, status, error) {
                _error(deferred, jqXHR, error);
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