app.factory('AjaxResponseFactory', ['AJAX_CONST', function(AJAX_CONST) {

    var _getRequestStatusFromJqXHRStatus = function(jqXHR) {
        if (!jqXHR) return;
        var status = jqXHR.status;
        if (status === AJAX_CONST.JQXHR_STATUS.SUCCESS) {
            return AJAX_CONST.REQUEST_STATUS.SUCCESS;
        } else if (status === AJAX_CONST.JQXHR_STATUS.SUCCESS) {
            return AJAX_CONST.REQUEST_STATUS.ERROR;
        }
        return 'Unable to map jqXHR status to request status: ' + status;
    };

    function AjaxResponse() { }

    angular.extend(AjaxResponse.prototype, {
        _init: function (response, status, jqXHR, error) {

            this.data = undefined;          // Holds the data object sent back from the server (e.g. list of users)
            this.message = undefined;       // Holds a message from the server or the AJAX response object (i.e. jqXHR)
            this.status = undefined;        // Holds the status of the business request (e.g. success, fail, error)
            this.statusCode = undefined;    // Holds the HTTP response code

            if (!response && !status && !jqXHR && !error) {
                return this;
            }

            this.statusCode = jqXHR.status;

            if (error && !response) {
                this.status = AJAX_CONST.REQUEST_STATUS.ERROR;
                this.message = error;
                return this;
            }

            if (response) {
                this.status = response.status;
                this.data = response.data;
                this.message = (this.isSuccess() || response.message)
                    ? response.message
                    : this.isError()
                        ? error
                        : 'Failed with no additional detail from the server';
            } else {
                this.status = _getRequestStatusFromJqXHRStatus(jqXHR);
                this.message = error || jqXHR.statusText;
            }

            return this;
        },
        getData: function() {
            return this.data;
        },
        getMessage: function() {
            return this.message;
        },
        getStatus: function() {
            return this.status;
        },
        getStatusCode: function() {
            return this.statusCode;
        },
        hasMessage: function() {
            return angular.hasValue(this.getMessage());
        },
        hasData: function() {
            return angular.hasValue(this.getData());
        },
        isSuccess: function() {
            return this.getStatus() === AJAX_CONST.REQUEST_STATUS.SUCCESS;
        },
        isFail: function() {
            return this.getStatus() === AJAX_CONST.REQUEST_STATUS.FAIL;
        },
        isError: function() {
            return this.getStatus() === AJAX_CONST.REQUEST_STATUS.ERROR;
        }
    });

    return function newAjaxResponse(response, status, jqXHR, error) {
        return new AjaxResponse()._init(response, status, jqXHR, error);
    };

}]);