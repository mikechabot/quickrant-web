app.service('RantService', ['DataAccessService', 'QR_CONST', function (DataAccessService, QR_CONST) {

    function _createRantObject(rant) {
        var now = moment().toDate();
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
            allowReplies: rant.allowReplies,
            createdDate: now,
            lastModifiedDate: now
        };
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
        getRepliesByRantId: function getRepliesByRantId(id) {
            var deferred = $.Deferred();
            DataAccessService.get('/replies/' + id)
                .done(function(data) {
                    deferred.resolve(data.data);
                })
                .fail(function(error) {
                    deferred.reject({message: error.message});
                });
            return deferred;
        },
        saveReply: function saveReply(rantId, reply) {
            var _reply = {
                ranter: {
                    name: reply.name || QR_CONST.DEFAULT_VALUE.NAME,
                    location: reply.location || QR_CONST.DEFAULT_VALUE.LOCATION
                },
                createdDate: moment().toDate(),
                rantId: rantId,
                reply: reply.reply
            };
            var deferred = $.Deferred();
            DataAccessService.post('/reply/', _reply)
                .done(function(data) {
                    deferred.resolve(data.data);
                })
                .fail(function(error) {
                    deferred.reject({message: error.message});
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
        }
    });
}]);
