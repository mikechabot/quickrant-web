var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('AppController', ['$scope', function ($scope) {
    $scope.$safeApply = function (fn) {
        var phase = this.$root.$$phase;
        if (phase == '$apply' || phase == '$digest') {
            if (fn && (typeof(fn) === 'function')) {
                fn();
            }
        } else {
            this.$apply(fn);
        }
    };
}]);

app.controller('MainController', ['$scope', '$timeout', 'QR_DATA', 'QR_CONST', 'QuickrantFactory', function ($scope, $timeout, QR_DATA, QR_CONST, QuickrantFactory) {

    var _initQuickrant = function() {

        $scope.quickrant = {};

        QuickrantFactory()
            .done(function(quickrant) {
                $timeout(function() {
                    $scope.quickrant = quickrant;
                });
            });

        $scope.isLoaded = function() {
            return !_.isEmpty($scope.quickrant);
        };

    };

    _initQuickrant();

}]);