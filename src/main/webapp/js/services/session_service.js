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
