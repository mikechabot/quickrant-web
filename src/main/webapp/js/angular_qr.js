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

app.controller('MainController', ['$scope', 'QR_DATA', 'QR_CONST', function ($scope, QR_DATA, QR_CONST) {

    $scope.isEmotionSelected = function(selection) {
        return selection && selection.emotion;
    };

    $scope.isQuestionSelected = function(selection) {
        return selection && selection.question;
    };

    var _initQuickrant = function() {
        $scope.quickrant = {
            defaults: QR_CONST.DEFAULT_VALUES,
            restrictions: QR_CONST.RESTRICTIONS
        };

        $scope.view = QR_CONST.VIEWS.LIVE_STREAM;
        $scope.views = QR_CONST.VIEWS;

        $scope.emotions = QR_DATA.emotions;

        $scope.selection = {};  // Holds user selections (e.g. emotion, question)
        $scope.rant = {};       // Holds rant information (e.g name, location, commentsAllowed)
    };

    _initQuickrant();

}]);