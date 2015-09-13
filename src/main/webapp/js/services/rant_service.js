app.service('RantService', ['$log', 'QR_CONST', 'QR_DATA', 'DataAccessService', 'DialogService', function ($log, QR_CONST, QR_DATA, DataAccessService, DialogService) {

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

    var _isInvalidSession = function(error) {
        return error.message.indexOf("invalid session") > 0;
    };

    return {
        createRantFromFormSubmission: function(rantForm) {
            if (rantForm.rant) {
                return{
                    rant: rantForm.rant,
                    selection: {
                        emotion: rantForm.face.emotion,
                        question: rantForm.question
                    },
                    ranter: {
                        name: rantForm.name || QR_CONST.DEFAULT_VALUE.NAME,
                        location: rantForm.location || QR_CONST.DEFAULT_VALUE.LOCATION
                    },
                    comments: [],
                    allowComments: rantForm.allowComments,
                    createdDate: moment().toDate()
                };
            }
        },
        getRantsByPageNumber: function getRants(pageNumber) {
            var deferred = $.Deferred();
            DataAccessService.get('/rants/page/' + pageNumber)
                .done(function(page) {
                    deferred.resolve(page);
                })
                .fail(function() {
                    deferred.reject();
                });
            return deferred.promise();
        },
        postRant: function postRant(rant) {
            var deferred = $.Deferred();
            if (!hasValue(rant)) {
                throw new Error('Rant cannot be null');
                deferred.reject();
            } else {
                var that = this;
                DataAccessService.post('/rants', rant)
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
