var app = angular.module('quickrant', ['ngRoute', 'yaru22.angular-timeago']);

app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            template : '<introduction></introduction>'
        })
        .when('/questions', {
            template : '<questions></questions>'
        })
});







