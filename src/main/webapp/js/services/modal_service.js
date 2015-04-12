app.service('ModalService', ['$modal', function ($modal) {
    return {
        open: function open(options) {
            if (options.scope && options.data) {
                options.scope.data = options.data;
            }
            var modalInstance = $modal.open({
                templateUrl: options.templateUrl,
                scope: options.scope,
                size: options.size
            });
            return modalInstance;
        }
    };
}]);
