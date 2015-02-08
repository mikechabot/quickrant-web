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
      url: url
    };
    if (data) {
      options.data = JSON.stringify(data);
      options.contentType = 'application/json';
    }

    /* Wrap the original promise */
    var wrapper = $.Deferred();
    AjaxService.request(options)
      .done(function(response) {
        if (response.success === true) {
          wrapper.resolve(response.data);
        } else {
          wrapper.reject(response.message);
        }
      })
      .fail(function(response) {
        wrapper.reject(response)
      });

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