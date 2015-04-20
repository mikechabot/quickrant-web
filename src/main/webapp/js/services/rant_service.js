app.service('RantService', ['DataAccessService', 'QR_CONST', function (DataAccessService, QR_CONST) {

    /**
     * Generate Rant object from form input
     * @param rant
     * @returns {{rant: (*|.scope.rant|$scope.rant|.quickrant.rant), selection: {emotion: (*|.emotions.happy.emotion|.emotions.angry.emotion|.emotions.sad.emotion), question: (*|.scope.question|$scope.rant.question)}, ranter: {name: (*|.DEFAULT_VALUE.NAME|m.selectors.match.NAME|m.selectors.find.NAME), location: (*|$scope.default.location|number|DOMLocator|Location|String|$scope.form.location)}, allowReplies: (*|$scope.allowReplies)}}
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
            allowReplies: rant.allowReplies
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

    return ({
        postRant: function postRant(rant) {
            var deferred = $.Deferred();
            if (rant.rant) {
                var _rant = _createRantObject(rant);
                DataAccessService.post('/rants', _rant)
                    .done(function(response) {
                        deferred.resolve(response.data);
                    })
                    .fail(function (response) {
                        deferred.reject({message: response.message});
                    });
            } else {
                deferred.reject({message: 'Cannot post null rant'});
            }
            return deferred;
        },
        getRantById: function getRantById(id) {
            var deferred = $.Deferred();
                DataAccessService.post('/rants/' + id)
                    .done(function(data) {
                        deferred.resolve(data.data);
                    })
                    .fail(function(error) {
                        deferred.reject({message: error.message});
                    });
            return deferred;
        },
        saveComment: function saveComment(comment, rantId) {
            var deferred = $.Deferred();
            var _comment = _createCommentObject(comment);
            DataAccessService.post('/rants/comment/' + rantId, _comment)
                .done(function(response) {
                    console.log(response.message);
                    deferred.resolve(response.data);
                })
                .fail(function(response) {
                    deferred.reject({message: response.message});
                });
            return deferred;
        },
        //TODO: fix this; don't pass the rants in
        getPaginatedRants: function getRants(rants, pageNumber) {
            function getPage(data) {
                return {
                    size: data.size,
                    number: data.number,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    numberOfElements: data.numberOfElements
                };
            }

            var deferred = $.Deferred();
            DataAccessService.get('/rants/page/' + pageNumber)
                .done(function (response) {

                    var newRants = response.data.content;
                    var page = getPage(response.data);

                    rants = pageNumber > 1 ? rants.concat(newRants) : newRants;

                    deferred.resolve({rants: rants, page: page});
                })
                .fail(function () {
                    deferred.reject({message: 'Unable to load rants'});
                });

            return deferred;
        },
        getMostActiveRants: function getRants() {
            var deferred = $.Deferred();
            DataAccessService.post('/rants/mostactive')
                .done(function (response) {
                    deferred.resolve({rants: response.data});
                })
                .fail(function () {
                    deferred.reject({message: 'Unable to get most active rants'});
                });
            return deferred;
        }
    });
}]);
