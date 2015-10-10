var app = angular.module('quickrant', ['ui.bootstrap', 'ngAnimate', 'ngTimeago']);

app.controller('MainController', ['$scope', '$timeout', 'QR_DATA', 'QR_CONST', 'QuickrantFactory', 'DialogService',
    function ($scope, $timeout, QR_DATA, QR_CONST, QuickrantFactory, DialogService) {

        var _initQuickrant = function() {

            $scope.quickrant = {};

            $scope.showShare = function() {
                DialogService.open({
                    templateUrl: '/templates/modals/share.html',
                    scope: QR_DATA.shareUrls
                });
            };

            QuickrantFactory()
                .done(function(quickrant) {
                    $timeout(function() {
                        $scope.quickrant = quickrant;
                        $scope.page = $scope.quickrant.page;
                    });
                });

            $scope.isLoaded = function() {
                return !_.isEmpty($scope.quickrant);
            };

        };

        _initQuickrant();

    }
]);