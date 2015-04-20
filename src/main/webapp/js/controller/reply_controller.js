app.controller('ReplyController', ['$scope', '$timeout', 'RantService', function($scope, $timeout, RantService) {
    $scope.close = function () {
        $scope.$dismiss();
    };

    var rantId = $scope.data.id;

    $scope.showReplyForm = true;

    $scope.saveReply = function(reply) {
        RantService.saveReply(reply, rantId)
            .done(function(_reply) {
                $scope.$apply(function () {
                    if (!$scope.data.replies) {
                        $scope.data.replies = [];
                    }
                    $scope.data.replies.push(_reply);
                    $('.modal-body').animate({scrollTop: $('.modal-body')[0].scrollHeight }, 'slow');
                    _reset();
                });
            })
            .fail(function(error) {
                console.error(error.message);
            })
            .always(function() {
                $timeout(function() {
                    $scope.showReplyForm = false;
                });
            });
    };

    function _reset() {
        $scope.form.location = undefined;
        $scope.form.name = undefined;
        $scope.form.reply = undefined;
    }

}]);