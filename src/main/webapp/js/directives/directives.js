/**
 * Directive to set scroll positioning
 */
app.directive('scroll', function() {
    return {
        restrict: 'A',
        scope: {
            scrollTo: '@',
            speed: '@'
        },
        link: function(scope, element) {
            element.on('click', function() {
                $("body, html").animate({scrollTop: scope.scrollTo }, scope.speed || 'slow');
            });
        }
    }
});