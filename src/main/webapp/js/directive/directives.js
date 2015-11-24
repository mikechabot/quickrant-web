app.directive('application', ['APP_DATA', function (APP_DATA) {
    return {
        restrict: 'A',
        scope: true,
        controller: function($scope, $element, $attrs) {
            this.setEmotion = function(emotion) {
                $scope.emotion = emotion;
            };
            this.getEmotions = function() {
                return APP_DATA.EMOTIONS;
            };
            this.getQuestions = function() {
                if ($scope.emotion) {
                    return $scope.emotion.questions;
                }
            }
        }
    }
}]);

app.directive('introduction', function () {
    var templateHtml =
        '<div class="section hero">' +
            '<div class="container">' +
                '<div class="row">' +
                    '<div class="one-half column">' +
                        '<div>' +
                            '<h4 class="hero-heading">How are you feeling today?</h4>' +
                        '</div>' +
                    '</div>' +
                    '<div class="one-half column">' +
                        '<a ng-repeat="emotion in emotions" href="#questions" class="facial-emotion" ng-click="setEmotion(emotion)"><img ng-src="/images/{{emotion.name}}.gif"></a>' +
                    '</div>'+
                '</div>' +
            '</div>'+
        '</div>';

    return {
        restrict: 'EA',
        require: '^application',
        template: templateHtml,
        link: function (scope, element, attrs, AppCtrl) {
            scope.emotions = AppCtrl.getEmotions();
            scope.setEmotion = function(emotion) {
                AppCtrl.setEmotion(emotion);
            }
        }
    }
});


app.directive('questions', function () {

    var templateHtml =
        '<div class="section questions">' +
            '<div class="container">' +
                '<h4>I feel {{emotion.name}} right now, and...</h4>' +
                '<div class="row">' +
                    '<div class="two columns">' +
                        '<a href="/#/" class="facial-emotion"><img ng-src="/images/{{emotion.name}}.gif"></a><h5></h5>' +
                    '</div>' +
                    '<div class="ten columns">' +
                        '<button class="question" ng-repeat="question in questions">{{question}}</button>' +
                    '</div>' +
                '</div>' +
            '</div>' +
        '</div>';

    return {
        restrict: 'EA',
        require: '^application',
        template: templateHtml,
        link: function (scope, element, attrs, AppCtrl) {
            scope.questions = AppCtrl.getQuestions();
            scope.setQuestion = function(question) {
                AppCtrl.setQuestion(question);
            }
        }
    }
});

app.directive('rants', ['$timeout','RantPageFactory', function($timeout, RantPageFactory) {

    var templateHtml =
        '<div class="section rants">' +
            '<div class="container">' +
                '<blockquote ng-repeat="rant in page.rants">' +
                    '<span class="rant-question">{{::rant.question}}</span>' +
                    '<p>{{::rant.text}}' +
                    '<span class="rant-details">- {{::rant.name}} <span class="rant-details-sm">{{::rant.location}} <span class="rant-details-xs">{{::rant.createdDate | timeAgo}}</span></span>' +
                    '</p>' +
                '</blockquote>' +
            '</div>' +
        '</div>';

    return {
        restrict: 'E',
        template: templateHtml,
        scope: true,
        link: function(scope, element, attrs) {
            RantPageFactory()
                .done(function(page) {
                    scope.page = page;
                    //scope.rants = _.last(page.getRants());
                });
        }
    }
}]);
