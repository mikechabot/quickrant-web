app.controller('CommentController', ['$scope', '$timeout', 'RantService', 'QR_CONST', function($scope, $timeout, RantService, QR_CONST) {

    $scope.showForm = true;

    $scope.restrictions = QR_CONST.RESTRICTIONS;

    $scope.saveComment = function(comment) {
        RantService.saveComment(comment, $scope.rant.id)
            .done(function(_comment) {
                $scope.$apply(function () {
                    if (!$scope.rant.comments) {
                        $scope.rant.comments = [];
                    }
                    $scope.rant.comments.push(_comment);
                    $scope.rant.commentCount = $scope.rant.comments.length;
                    //TODO: move this to a directive
                    $('.modal-body').animate({scrollTop: $('.modal-body')[0].scrollHeight }, 'slow');
                    _reset();
                });
            })
            .fail(function(error) {
                console.error(error.message);
            })
            .always(function() {
                $timeout(function() {
                    $scope.showForm = false;
                });
            });
    };

    function _reset() {
        $scope.form.location = undefined;
        $scope.form.name = undefined;
        $scope.form.comment = undefined;
    }

}]);