app.factory('QuickrantFactory', ['QR_CONST', 'QR_DATA', 'RantPageFactory', 'RantService', function(QR_CONST, QR_DATA, RantPageFactory, RantService) {

    function _initPage() {
        var deferred = $.Deferred();

        var promises = [];
        promises.push(RantPageFactory());
        promises.push(RantService.getPopularRants());

        $.when.apply(this, promises)
            .done(function(page, popularRants) {
                deferred.resolve({ page: page, popularRants: popularRants });
            })
            .fail(function(errorPage, errorPopular){
                deferred.reject(errorPage, errorPopular);
            });
        return deferred.promise();
    }

    function newQuickrant() {
        var deferred = $.Deferred();
        var quickrant = new Quickrant();

        quickrant._init()
            .done(function() {
                deferred.resolve(quickrant);
            })
            .fail(function(error) {
                deferred.reject(error);
            });

        return deferred.promise();
    }

    function Quickrant() {};                // Empty constructor

    angular.extend(Quickrant.prototype, {
        _init: function() {

            var quickrant = this;

            var deferred = $.Deferred();

            this.page = {};                 // Holds paginated user rants & page statistics
            this.popularRants = []          // Array of popular user rants
            this.views = {};                // Map of toggleable views
            this.activeView = {};           // Active view presented to user
            this.selection = {};            // Holds user selections (e.g. emotion, question)
            this.rant = {};                 // Holds rant information (e.g name, location, commentsAllowed)
            this.emotions = {};             // Map of static emotion objects
            this.defaults = {};             // Map of default values
            this.restrictions = {};         // Map of restrictions (e.g min/max length)

            _initPage(this)
                .done(function(data) {
                    quickrant.page = data.page;
                    quickrant.popularRants = data.popularRants;
                    quickrant.views = QR_CONST.VIEWS;
                    quickrant.activeView = QR_CONST.VIEWS.LIVE_STREAM;
                    quickrant.emotions = QR_DATA.emotions;
                    quickrant.defaults = QR_CONST.DEFAULT_VALUES;
                    quickrant.restrictions = QR_CONST.RESTRICTIONS;
                    deferred.resolve(quickrant);
                })
                .fail(function(error) {
                    deferred.reject(error);
                });

            return deferred.promise();
        },
        getPage: function() {
            return this.page;
        },
        getViews: function() {
            return this.views;
        },
        getActiveView: function() {
            return this.activeView;
        },
        getEmotions: function() {
            return this.emotions;
        },
        getSelection: function() {
            return this.selection;
        },
        getRant: function() {
            return this.rant;
        },
        getDefaults: function() {
            return this.defaults;
        },
        getRestrictions: function() {
            return this.restrictions;
        },
        hasPage: function() {
            return !_.isEmpty(this.getPage());
        },
        isEmotionSelected: function() {
            return angular.isDefined(this.getSelection().emotion);
        },
        isQuestionSelected: function() {
            return angular.isDefined(this.getSelection().question);
        }

    });

    return newQuickrant;

}]);