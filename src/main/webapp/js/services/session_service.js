app.service('SessionService', ['$cookies','DataAccessService', function($cookies, DataAccessService) {

    var sessionId = 'quickrant-uuid';

    return ({
        getSessionCookie: function() {
          return $cookies[sessionId];
        },
        authenticate: function() {
          var data = {
            screen_height: window.screen.availHeight,
            screen_width: window.screen.availWidth,
            screen_color: window.screen.colorDepth
          };
          return DataAccessService.post('/session/auth', data);
        }
    });

}]);
