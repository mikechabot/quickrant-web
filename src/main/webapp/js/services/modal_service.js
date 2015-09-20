/**
 * Open a modal window.
 */
app.service('ModalService', ['$modal', '$document', 'PromiseService', function($modal, $document, PromiseService) {
    return {
        open: function(options) {
            return PromiseService.convertAngularPromiseToJQueryPromise($modal.open(options).result);
        }
    }
}]);

/**
 * Open a modal window with some content
 */
app.service('DialogService', ['$rootScope', 'ModalService', function($rootScope, ModalService) {

    return {
        open: function(options) {

            if (hasValue(options.scope)) {
                if (options.scope.constructor.name !== 'Scope') {
                    var temp = options.scope;
                    options.scope = $rootScope.$new();
                    copyObject(temp, options.scope);
                }
            }

            return ModalService.open(options);
        },
        notify: function(body, title) {

            var options = {
                templateUrl: '/templates/modals/notify.html',
                scope:  $rootScope.$new()
            };

            options.scope.body = body;
            if (title) {
                options.scope.title = title;
            }

            return ModalService.open(options);
        },
        error: function(body) {
            return this.notify(body, "Error");
        }
    }

}]);