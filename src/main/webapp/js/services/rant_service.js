app.service('RantService', ['$log', 'QR_CONST', 'QR_DATA', 'DataAccessService', 'DialogService', function ($log, QR_CONST, QR_DATA, DataAccessService, DialogService) {

    var basePath = '/rants';

    function _createNewRant(rant, selection) {
        return {
            text: rant.text,
            name: _getName(rant.name),
            location: _getLocation(rant.location),
            allowComments: rant.allowComments,
            emotion: selection.emotion,
            question: selection.question,
            comments: []
        }
    }

    function _createNewComment(form) {
        return {
            name: _getName(form.name),
            location: _getLocation(form.location),
            text: form.text
        }
    }

    function _getName(name) {
        return name || QR_CONST.DEFAULT_VALUES.NAME;
    }

    function _getLocation(location) {
        return location || QR_CONST.DEFAULT_VALUES.LOCATION;
    }

    return {
        getFirstPage: function() {
            var deferred = $.Deferred();
            this.getPageByPageNumber(0)
                .done(function(page) {
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });
            return deferred.promise();
        },
        getPageByPageNumber: function (pageNumber) {
            if (pageNumber < 0) throw new Error('Page number cannot be less than zero');
            var deferred = $.Deferred();
            DataAccessService.get(basePath + '/page/' + pageNumber)
                .done(function(page) {
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });
            return deferred.promise();
        },
        getRantsCreatedAfter: function(date) {
            return DataAccessService.get(basePath + '/since/' + date);
        },
        postRant: function(rant, selection) {
            if (!rant || !selection) throw new Error('Rant and/or selection cannot be null');
            var newRant =  _createNewRant(rant, selection);
            return DataAccessService.post(basePath, newRant);
        },
        getRantById: function (id) {
            return DataAccessService.get(basePath + '/' + id);
        },
        saveComment: function (comment, rantId) {
            var deferred = $.Deferred();
            if (!hasValue(comment.text)) {
                deferred.reject("Comment cannot be empty");
            } else {
                deferred = DataAccessService.post(basePath + '/comment/' + rantId, comment);
            }
            return deferred.promise();

        },
        getPopularRants: function() {
            return DataAccessService.get(basePath + '/popular');
        },
        getRantsByQuestion: function(question) {
            return DataAccessService.get(basePath + '/question', question);
        },
        openRant: function(rant) {
            var options = {
                templateUrl: '/templates/directives/rant-reply.html',
                size: 'lg',
                windowClass: 'rant-comment',
                backdrop: 'static',
                animation: false,
                scope: {
                    rant: rant
                }
            };
            return DialogService.open(options)
        },
        createNewRant: function(rant,selection) {
            return _createNewRant(rant, selection);
        },
        createNewComment: function(form) {
            return _createNewComment(form);
        }
    };

}]);
