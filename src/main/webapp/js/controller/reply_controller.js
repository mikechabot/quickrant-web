app.controller('ReplyController', ['$scope', '$timeout', 'RantService', function($scope, $timeout, RantService) {
    $scope.close = function () {
        $scope.$close();
    };

    var rantId = $scope.data.id;

    $scope.saveReply = function(reply) {
        RantService.saveReply(rantId, reply)
            .done(function() {
                RantService.getRepliesByRantId(rantId)
                    .done(function(replies) {
                        $scope.$apply(function () {
                            $scope.data.replies = replies;
                            _reset();
                        });
                    })
                    .fail(function(error) {
                        console.error(error.message);
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