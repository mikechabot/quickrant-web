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
    controller: function($scope, $element, $attrs, rantService) {

      $scope.rants = [];
      loadRants();

      function setRants(newRants) {
        $scope.rants = newRants;
      }

      function loadRants() {
        rantService.getRants().then(function(data) {
          setRants(data);
        });
      }

    }
  };
});