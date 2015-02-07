app.service('DataAccessService', ['AjaxService', function(AjaxService) {

  var request = function(type, url, data) {
    var options = {
      type: type,
      url: url
    };
    if (data) {
      options.data = JSON.stringify(data);
      options.contentType = 'application/json';
    }
    return AjaxService.request(options);
  };

  return {
    get: function(url) {
      return request('GET', url);
    },
    post: function(url, data) {
      return request('POST', url, data);
    }
  }

}]);

app.service('AjaxService', function() {
  return {
    request: function(options) {
      return $.ajax(options);
    }
  }
});