app.service('RantService', ['$log', 'QR_CONST', 'QR_DATA', 'DataAccessService', 'DialogService', function ($log, QR_CONST, QR_DATA, DataAccessService, DialogService) {

    /**
     * Generate Rant object from form input
     * @param rant
     * @returns {{rant: (*|.scope.rant|$scope.rant|.quickrant.rant), selection: {emotion: (*|.emotions.happy.emotion|.emotions.angry.emotion|.emotions.sad.emotion), question: (*|.scope.question|$scope.rant.question)}, ranter: {name: (*|.DEFAULT_VALUE.NAME|m.selectors.match.NAME|m.selectors.find.NAME), location: (*|$scope.default.location|number|DOMLocator|Location|String|Location)}, allowComments: (*|$scope.allowComments)}}
     * @private
     */
    function _createRantObject(rant) {
        return{
            rant: rant.rant,
            selection: {
                emotion: rant.face.emotion,
                question: rant.question
            },
            ranter: {
                name: rant.name || QR_CONST.DEFAULT_VALUE.NAME,
                location: rant.location || QR_CONST.DEFAULT_VALUE.LOCATION
            },
            allowComments: rant.allowComments,
            createdDate: moment().toDate()
        };
    }

    /**
     * Generate Comment object from form input
     * @param comment
     * @returns {{ranter: {name: (*|.DEFAULT_VALUE.NAME|m.selectors.match.NAME|m.selectors.find.NAME), location: (*|$scope.default.location|number|DOMLocator|Location|String|Location)}, comment: (*|$scope.form.comment)}}
     * @private
     */
    function _createCommentObject(comment) {
        return {
            ranter: {
                name: comment.name || QR_CONST.DEFAULT_VALUE.NAME,
                location: comment.location || QR_CONST.DEFAULT_VALUE.LOCATION
            },
            comment: comment.comment
        }
    }

    /**
     * Generate an abridged Page object
     * @param data
     * @returns {{rants: (string|CSSStyleDeclaration.content|*|jQuery.content|c.DEFAULTS.content|.scope.content), page: {size: (*|modalInstance.size|size|number|Number|string), number: (*|inputType.number|.link.g.number|ld.number), totalPages: (*|totalPages), totalElements: *, numberOfElements: *}}}
     * @private
     */
    function _createPageObject(data) {
        return {
            rants: data.content,
            page: {
                size: data.size,
                number: data.number+1,
                totalPages: data.totalPages,
                totalElements: data.totalElements,
                numberOfElements: data.numberOfElements
            }
        };
    }

    var _isInvalidSession = function(error) {
        return error.message.indexOf("invalid session") > 0;
    };

    return {
        getPaginatedRants: function getRants(pageNumber) {
            var deferred = $.Deferred();
            DataAccessService.get('/rants/page/' + pageNumber)
                .done(function(paginated) {
                    deferred.resolve(_createPageObject(paginated));
                })
                .fail(function() {
                    deferred.reject();
                });
            return deferred.promise();
        },
        postRant: function postRant(rant) {
            var deferred = $.Deferred();
            if (rant.rant) {
                var _rant = _createRantObject(rant);
                var that = this;
                DataAccessService.post('/rants', _rant)
                    .done(function(rant) {
                        deferred.resolve(rant);
                        that.openRant(rant);
                    })
                    .fail(function (error) {
                        if (_isInvalidSession(error)) {
                            DialogService.notify(
                                QR_DATA.notify.noSession.body,
                                QR_DATA.notify.noSession.title
                            );
                        }
                        deferred.reject({message: error.message});
                    });
            } else {
                deferred.reject({message: 'Cannot post null rant'});
            }
            return deferred.promise();
        },
        getRantById: function getRantById(id) {
            var deferred = $.Deferred();
            DataAccessService.get('/rants/' + id)
                .done(function(data) {
                    deferred.resolve(data);
                })
                .fail(function() {
                    deferred.reject();
                });
            return deferred.promise();
        },
        saveComment: function saveComment(comment, rantId) {
            var deferred = $.Deferred();
            var _comment = _createCommentObject(comment);
            DataAccessService.post('/rants/comment/' + rantId, _comment)
                .done(function(response) {
                    deferred.resolve(response);
                })
                .fail(function() {
                    deferred.reject();
                });
            return deferred.promise();
        },
        getPopularRants: function getPopularRants() {
            var deferred = $.Deferred();
            DataAccessService.get('/rants/popular')
                .done(function (response) {
                    deferred.resolve(response);
                })
                .fail(function () {
                    deferred.reject({message: 'Unable to get popular rants'});
                });
            return deferred.promise();
        },
        getRantsByQuestion: function getRantsByQuestion(question) {
            var deferred = $.Deferred();
            DataAccessService.get('/rants/question', question)
                .done(function (response) {
                    deferred.resolve({rants: response});
                })
                .fail(function () {
                    deferred.reject({message: 'Unable to get most active rants'});
                });
            return deferred.promise();
        },
        openRant: function(rant) {
            var options = {
                templateUrl: '/templates/modals/rant.html',
                size: 'lg',
                windowClass: 'rant-comment',
                backdrop: 'static',
                scope: {
                    rant: rant
                }
            };
            return DialogService.open(options)

        }
    };

}]);
