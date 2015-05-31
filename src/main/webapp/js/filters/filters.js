/**
 * Trust HTML
 */
app.filter('trusted', ['$sce', function($sce) {
    return function(input) {
        return $sce.trustAsHtml(input);
    }
}]);