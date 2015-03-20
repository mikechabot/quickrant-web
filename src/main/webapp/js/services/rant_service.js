app.service('RantService', ['DataAccessService', function (DataAccessService) {
    return ({
        postRant: function postRant(rant) {
            var deferred = $.Deferred();
            console.log(rant);
            if (rant.rant) {

                var rant = {
                    selection: {
                        emotion: rant.face.emotion,
                        question: rant.question
                    },
                    ranter: {
                        name: rant.name,
                        location: hasValue(rant.location) ? rant.location : QR_CONST.DEFAULT_VALUE.LOCATION
                    },
                    rant: rant.rant
                }

                deferred.resolve(DataAccessService.post('/rants', rant));
            } else {
                deferred.reject({message: 'Cannot post null rant'});
            }

            return deferred;
        },
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
