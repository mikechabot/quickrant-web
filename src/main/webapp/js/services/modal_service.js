/**
 * Open a modal window.
 */
app.service('ModalService', ['$modal', '$document', function($modal, $document) {
    var body = angular.element($document[0].body);
    return {
        open: function(options) {
            body.addClass('modal-open');
            return $modal.open(options)
                .always(function() {
                    body.removeClass('modal-open');
                })
        }
    }
}]);

/**
 * Open a modal window with some content
 */
app.service('DialogService', ['$rootScope', 'ModalService', function($rootScope, ModalService) {

    /**
     * Get a new scope object
     * @param isolate
     * @returns {Object|*}
     * @private
     */
    var _newScope = function(isolate) {
        return $rootScope.$new(isolate);
    };

    return {
        open: function(options) {

            // This scope is an object, not a legitimate scope instance
            if (options.scope) {
                var data = options.scope;
                var scope = _newScope(true);
                options.scope = scope;
                copyObject(data, scope);
            }

            return ModalService.open(options);
        },
        notify: function(body, title) {

            var options = {
                templateUrl: '/templates/modals/notify.html',
                scope:  _newScope(true)
            };

            options.scope.body = body;
            if (title) {
                options.scope.title = title;
            }

            return ModalService.open(options);
        }
    }

}]);