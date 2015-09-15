app.service('RantService', ['$log', 'QR_CONST', 'QR_DATA', 'DataAccessService', 'DialogService', function ($log, QR_CONST, QR_DATA, DataAccessService, DialogService) {

    function _createNewRant(rant, selection) {
        return {
            text: rant.text,
            name: rant.name || QR_CONST.DEFAULT_VALUES.NAME,
            location: rant.location || QR_CONST.DEFAULT_VALUES.LOCATION,
            allowComments: rant.allowComments,
            emotion: selection.emotion,
            question: selection.question,
            comments: []
        }
    }

    function _createNewPageRequest(pageNumber, numberOfRantsViewed) {
        return {
            pageNumber: pageNumber,
            numberOfRantsViewed: numberOfRantsViewed
        }
    }

    function _createCommentObject(comment) {
        return {
            ranter: {
                name: comment.name || QR_CONST.DEFAULT_VALUE.NAME,
                location: comment.location || QR_CONST.DEFAULT_VALUE.LOCATION
            },
            comment: comment.comment
        }
    }
    return {
        getRantsByPageNumber: function getRants(pageNumber, numberOfRantsViewed) {
            if (pageNumber < 0) throw new Error('Page number cannot be less than zero')
            var deferred = $.Deferred();
            DataAccessService.post('/rants/page', _createNewPageRequest(pageNumber, numberOfRantsViewed))
                .done(function(page) {
                    deferred.resolve(page);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });
            return deferred.promise();
        },
        postRant: function postRant(rant, selection) {
            if (!rant || !selection) throw new Error('Rant and/or selection cannot be null');

            var deferred = $.Deferred();
            DataAccessService.post('/rants', _createNewRant(rant, selection))
                .done(function(rant) {
                    deferred.resolve(rant);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });
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
        },
        getPageStatisticsMap: function(page) {
            if (!page) return;
            return page.pageStatistics;
        },
        getPageStatistics: function(statsMap) {
            if (!statsMap) return;
            return  _.sortBy(statsMap, function(stat) {
                return stat.sortOrder;
            });
        }
    };

}]);
