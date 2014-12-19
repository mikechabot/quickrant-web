app.service('SessionService', function($cookies, $http) {

    var sessionId = 'quickrant-uuid';

    /**
     * Get the current session
     * @returns {*}
     */
    var getSession = function() {
      return $cookies[sessionId];
    };

    /* Server appends '*' to sessionId
    *  This can be spoofed, but it won't do any good
    */
    var isAuthenticated = function(cookie) {
      return cookie.match('\\*$');
    };

    var authenticate = function() {
      var request = {
        method: 'POST',
        url: '/session/auth',
        data: {
          screen_height: window.screen.availHeight,
          screen_width: window.screen.availWidth,
          screen_color: window.screen.colorDepth
        }
      }
      return $http(request).
        success(function (data, status) {
          return { data: data, status: status };
        }).
        error(function (data, status) {
          console.error(data.message);
          return { data: data, status: status };
        });
    };

    /* Return API */
    return ({
              getSession: getSession,
         isAuthenticated: isAuthenticated,
            authenticate: authenticate
    });

  }
);
