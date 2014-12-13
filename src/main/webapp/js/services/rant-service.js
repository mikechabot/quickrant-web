app.service('RantService', function($firebase) {
    return ({ getRants: getRants });
    function getRants() {
      var dataRef = new Firebase('https://blazing-heat-6301.firebaseio.com/');
      var data = $firebase(dataRef);
      return data.$asObject().$loaded();
    }
  }
);
