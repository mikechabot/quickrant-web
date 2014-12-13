/**
 * Set the value of a cookie to a the scope (allows me to stick this anywhere)
 *
 */
app.directive('cookie', ['$cookies', function($cookies) {
  return {
    scope: true,
    link: function($scope, $element, $attrs) {
      $scope.cookie = $cookies[$attrs.cookie];
    }
  }
}]);

app.directive('rants', function() {
  return {
    restrict: 'E',
    templateUrl: 'rants.html',
    controller: function($scope, $element, $attrs, RantService) {

      $scope.rants = [];
      loadRants();

      function setRants(newRants) {
        $scope.rants = newRants;
      }

      function loadRants() {
        RantService.getRants().then(function(data) {
          setRants(data);
        });
      }

    }
  };
});