app.controller('CommentController', ['$scope', '$timeout', 'RantService', function($scope, $timeout, RantService) {

    $scope.showForm = true;

    $scope.saveComment = function(comment) {
        RantService.saveComment(comment, $scope.data.id)
            .done(function(_comment) {
                $scope.$apply(function () {
                    if (!$scope.data.comments) {
                        $scope.data.comments = [];
                    }
                    $scope.data.comments.push(_comment);
                    $scope.data.commentCount = $scope.data.comments.length;
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