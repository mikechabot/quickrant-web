app.controller('ReplyController', ['$scope', '$timeout', 'RantService', function($scope, $timeout, RantService) {
    $scope.close = function () {
        $scope.$dismiss();
    };

    var rantId = $scope.data.id;

    $scope.saveReply = function(reply) {
        RantService.saveReply(reply, rantId)
            .done(function(_reply) {
                $scope.$apply(function () {
                    if (!$scope.data.replies) {
                        $scope.data.replies = [];
                    }
                    $scope.data.replies.push(_reply);
                    _reset();
                });
            })
            .fail(function(error) {
                console.error(error.message);
            })
            .always(function() {
                $timeout(function() {
                    $scope.hideReply = true;
                });
            });
    };

    function _reset() {
        $scope.form.location = undefined;
        $scope.form.name = undefined;
        $scope.form.reply = undefined;
    }

}]);