app.directive('application', ['APP_DATA', function (APP_DATA) {
    return {
        restrict: 'A',
        scope: true,
        controller: function($scope, $element, $attrs) {
            this.setEmotion = function(emotion) {
                $scope.emotion = emotion;
            };
            this.getEmotion = function() {
                return $scope.emotion;
            };
            this.setQuestion = function(question) {
                $scope.question = question;
            };
            this.getQuestion = function() {
                return $scope.question;
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

app.directive('introSection', function () {
    var templateHtml =
        '<div class="section intro">' +
            '<div class="container">' +
                '<div class="row">' +
                    '<div class="one-half column">' +
                        '<h4 class="intro-heading">How are you feeling today?</h4>' +
                        '<div class="mobile-toggle-lg decline-rant-lg">' +
                            '<i class="fa fa-lg fa-hand-o-right"></i>&nbsp;&nbsp;<a href="#blank">None of your business.</a>' +
                        '</div>' +
                    '</div>' +
                    '<div class="one-half column">' +
                        '<a ng-repeat="emotion in emotions" href="#questions" class="facial-emotion" ng-click="setEmotion(emotion)"><img ng-src="/images/{{emotion.name}}.gif"></a>' +
                    '</div>'+
                '</div>' +
                '<div class="row mobile-toggle-sm decline-rant-sm">' +
                    '<div class="twelve columns">' +
                        '<i class="fa fa-lg fa-hand-o-right"></i>&nbsp;&nbsp;<a href="#blank">None of your business.</a>' +
                    '</div>' +
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


app.directive('questionSection', function () {

    var templateHtml =
        '<div class="section questions">' +
            '<div class="container">' +
                '<div class="row">' +
                    '<div class="twelve columns">' +
                        '<h4>I feel <span class="bold-400">{{emotion.name}}</span> right now, and...</h4>' +
                    '</div>' +
                '</div>' +
                '<div class="row">' +
                    '<div class="two columns">' +
                        '<a href="/#/" class="facial-emotion"><img ng-src="/images/{{emotion.name}}.gif"></a>' +
                    '</div>' +
                    '<div class="ten columns">' +
                        '<a href="#rant"><button class="question" ng-repeat="question in questions" ng-click="setQuestion(question)">{{question}}</button></a>' +
                    '</div>' +
                '</div>' +
                '<div class="row back-to-intro">' +
                    '<div class="twelve columns">' +
                        '<i class="fa fa-lg fa-hand-o-right"></i>&nbsp;&nbsp;<a href="/#/">Wait, I don\'t feel {{emotion.name}}.</a>' +
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

app.directive('rantFormSection', function () {

    var templateHtml =
        '<div class="section rant-form-section">' +
            '<div class="container">' +
                '<div class="row">' +
                    '<div class="twelve columns">' +
                        '<h4>I feel <span class="bold-400">{{emotion.name}}</span> right now, and <em>{{question | decapitalize}}</em></h4>' +
                    '</div>' +
                '</div>' +
                '<form name="form">' +
                    '<div class="row">' +
                        '<div class="one-half column">' +
                            '<input class="u-full-width" type="text" placeholder="Name (Optional)" id="name">' +
                        '</div>' +
                        '<div class="one-half column">' +
                            '<input class="u-full-width" type="text" placeholder="Location (Optional)" id="location">' +
                        '</div>' +
                    '</div>' +
                    '<div class="row">' +
                        '<div class="twelve columns">' +
                            '<textarea class="u-full-width" placeholder="Say something..." id="rant"></textarea>' +
                        '</div>' +
                    '</div>' +
                    '<input type="submit" value="Submit">' +
                '</form>' +
                '<div class="row back-to-intro">' +
                    '<div class="twelve columns">' +
                        '<i class="fa fa-lg fa-hand-o-right"></i>&nbsp;&nbsp;<a href="#questions">Wait, I don\'t feel this way.</a>' +
                    '</div>' +
                '</div>' +
            '</div>' +
        '</div>';

    return {
        restrict: 'EA',
        require: '^application',
        template: templateHtml,
        link: function (scope, element, attrs, AppCtrl) {
            scope.emotion = AppCtrl.getEmotion();
            scope.question = AppCtrl.getQuestion();
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
