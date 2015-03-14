app.service('SessionService', ['DataAccessService', function (DataAccessService) {

    return ({
        authenticate: function() {
          var data = {
              window: {
                height: window.screen.availHeight,
                width: window.screen.availWidth,
                color: window.screen.colorDepth
              },
              browser: {
                userAgent: navigator.userAgent,
                platform: navigator.platform,
                language: navigator.language,
                cookieEnabled: navigator.cookieEnabled
              }
          };
          return DataAccessService.post('/session/auth', data);
        }
    });

}]);

app.service('TemplateService', ['$templateCache', function ($templateCache) {
    return {
        loadTemplate: function(template) {
            var deferred = new $.Deferred();

            /* Check if the template is already been loaded */
            if ($templateCache.get(template)) {
                deferred.resolve();
            } else {
                var options = {
                    async: false,
                    type: 'GET',
                    url: template
                };
                /* Fetch template */
                $.ajax(options)
                    .done(function (response) {
                        $templateCache.put(template, response);
                        deferred.resolve();
                    })
                    .fail(function () {
                        console.log('Unable to load template: ' + template);
                        deferred.reject();
                    });
            }
            return deferred.promise();
        }
    }
}]);

