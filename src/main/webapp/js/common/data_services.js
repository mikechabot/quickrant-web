app.service('AjaxService', function() {
  return {
    request: function(options) {
      return $.ajax(options);
    }
  }
});

app.service('DataAccessService', ['AjaxService', function(AjaxService) {

  /**
   * AJAX request service with response handling
   * @param type
   * @param url
   * @param data
   * @returns The original promise is not returned, but rather a promise that wraps it.
   *          This means we can handle cases where a successful response was sent from the server (200 OK),
   *          but contained an error condition (i.e. unable to save record)
   */
  var request = function(type, url, data) {
    var options = {
      type: type,
      url: '/spring' + url
    };
    if (data) {
      options.data = JSON.stringify(data);
      options.contentType = 'application/json';
    }

    /* Wrap the original promise */
    var wrapper = $.Deferred();
    AjaxService.request(options)
      .done(function(response) {
        if (response.status === 'SUCCESS') {
          wrapper.resolve( _getResponseData(response));
        } else {
          wrapper.reject(response.message);
        }
      })
      .fail(function(response) {
        wrapper.reject(response)
      });

      function _getResponseData(response) {
          var data = _getData(response);
          var message = _getMessage(response);

          if (hasValue(data) && hasValue(message)) {
              data.message = message;
          } else if (hasValue(message)) {
              data = { message: message };
          }

          return data;
      }

      function _getData(response) {
          return response.data;
      }

      function _getMessage(response) {
          return response.message;
      }

    return wrapper;
  };

  return {
    get: function(url) {
      return request('GET', url, null);
    },
    post: function(url, data) {
      return request('POST', url, data);
    }
  }

}]);