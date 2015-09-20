app.controller('RantDialogController', ['$scope', '$timeout', 'RantService', 'QR_CONST', function($scope, $timeout, RantService, QR_CONST) {

    $scope.showForm = true;

    function _addCommentToRant(comment) {
        $scope.rant.comments.push(comment);
        $scope.rant.commentCount += 1;
    }

    function _resetForm() {
        $scope.form.location = undefined;
        $scope.form.name = undefined;
        $scope.form.text = undefined;
    }

    $scope.saveComment = function(form) {
        var comment = RantService.createNewComment(form);
        if (!comment || !comment.text) return;
        RantService.saveComment(comment, $scope.rant.id)
            .done(function(comment) {
                _addCommentToRant(comment);
                _resetForm();
                //TODO: move this to a directive
                $('.modal-body').animate({scrollTop: $('.modal-body')[0].scrollHeight }, 'slow');
            })
            .always(function() {
                $timeout(function() {
                    $scope.showForm = false;
                });
            });
    };

    $scope.getCharactersLeft = function(text) {
        if (!text) return;
        return QR_CONST.RESTRICTIONS.MAX_CHAR - text.length;
    };

    $scope.$watch('form.text.$error', function(errors) {
        $scope.error = {};
        _.each(errors, function(value, key) {
            if (value === true) {
                $scope.error[key] = true;
            }
        });
    }, true);

}]);