app.service('RantService', ['$firebase', 'DataAccessService', function($firebase, DataAccessService) {
    return ({
      postRant: function postRant(rant) {
        return DataAccessService.post('/rants', rant);
      },
      getRants: function getRants(page) {
        return DataAccessService.get('/rants/page/' + page);
      }
    });
}]);
