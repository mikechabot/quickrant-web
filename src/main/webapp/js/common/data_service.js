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
app.service('DataAccessService', ['$log', 'AjaxService', 'QR_DATA', 'QR_CONST', 'HTTP_CONST', 'DialogService',
    function ($log, AjaxService, QR_DATA, QR_CONST, HTTP_CONST, DialogService) {

        var _urlPrefix = '/spring';

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

        var _success = function(deferred, response) {
            deferred.resolve(response.data);
            if (response.message) $log.info(response.message);
        };

        var _fail = function(deferred, response) {
            deferred.reject(response);
            $log.error(response);
        };

        var _error = function(deferred, jqXHR, error) {
            if (jqXHR.status === HTTP_CONST.STATUS.FORBIDDEN) {
                DialogService.notify(
                    QR_DATA.notify.noSession.body,
                    QR_DATA.notify.noSession.title
                );
            }
            deferred.reject(jqXHR.responseJSON);
            $log.error(error + ': ' + jqXHR.responseJSON.message);
        };

        var _isSuccess = function(response) {
            return response && (response.status === QR_CONST.STATUS.SUCCESS);
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