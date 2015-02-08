app.service('RantService', ['$firebase', 'DataAccessService', function($firebase, DataAccessService) {
    return ({
      getFirebaseRants: function getFirebaseRants() {
        var dataRef = new Firebase('https://blazing-heat-6301.firebaseio.com/');
        var data = $firebase(dataRef);
        return data.$asObject().$loaded();
      },
      postRant: function postRant(rant) {
        return DataAccessService.post('/rant', rant);
      },
      getRants: function getRants() {
        return DataAccessService.get('/rant/get');
      }
    });
}]);
