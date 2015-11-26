var app = angular.module('quickrant', ['ngRoute', 'ngAnimate', 'yaru22.angular-timeago']);

app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            template : '<intro-section></intro-section>'
        })
        .when('/questions', {
            template : '<question-section></question-section>'
        })
        .when('/rant', {
            template: '<rant-form-section></rant-form-section>'
        })
        .when('blank', {
            template: ''
        });
});

app.filter('decapitalize', function() {
    return function(input) {
        if (angular.isString(input) && angular.hasValue(input)) {
            return input.charAt(0).toLowerCase() + input.slice(1);
        }
        return input;
    }
});


