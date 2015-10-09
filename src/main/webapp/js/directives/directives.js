/**
 * Directive to set scroll positioning
 *
 *   scroll-element: DOM element(s) to be scrolled upon (comma-separated)
 *  scroll-to-pixel: Scroll to this number of pixels from the top of the scroll-element
 *     scroll-speed: Speed at which to scroll
 *        scroll-to: Scroll to a location (e.g. 'top", "bottom")
 *         focus-on: Focus on an element after scrolling
 */
app.directive('scrollOnClick', ['$document', '$window', function($document, $window) {
    return {
        restrict: 'A',
        scope: {
            scrollElement: '@',
            scrollToPixel: '@',
            scrollSpeed: '@',
            scrollTo: '@',
            focusOn: '@'
        },
        link: function(scope, element) {
            var context = scope.scrollElement || 'body, html';
            var pixels = scope.scrollToPixel || 0;
            var speed = scope.scrollSpeed || 'slow';
            var scrollTo = scope.scrollTo;

            if (scrollTo === 'top') {
                pixels = 0;
            } else if (scrollTo === 'bottom') {
                pixels = $document.height() + $document.innerHeight() + $document.outerHeight();
            }

            element.on('click', function() {
                $(context).animate({scrollTop: pixels }, speed);
                if (scope.focusOn) {
                    $(scope.focusOn)[0].focus();
                }
            });
        }
    }
}]);

/**
 * Directive that copies a scope variable to another
 *
 *       ngModel: Value to be copied
 *   destination: Copy ngModel to this scope variable
 * default-value: If ngModel model is null or undefined, use this value
 */
app.directive('copyable', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
            destination: '=',
            defaultValue: '='
        },
        link: function (scope, elem, attr, ctrl) {
            ctrl.$viewChangeListeners.push(function () {
                scope.destination = hasValue(ctrl.$viewValue) ? ctrl.$viewValue : scope.defaultValue;
            });
        },
        controller: function($scope) {
            $scope.destination = $scope.defaultValue;
        }
    }
});

/**
 * Directive to focus on an element
 *
 *  focus-trigger: Element is focused when the trigger is defined
 *
 */
app.directive('focus', ['$timeout', function ($timeout) {
    return {
        restrict: 'A',
        scope: {
          focusTrigger: '='
        },
        link: function (scope, elem) {
            elem[0].focus();
            scope.$watch('focusTrigger', function(newValue) {
                $timeout(function() {
                    if (newValue === true) {
                        elem[0].focus();
                    }
                });
            });
        }
    }
}]);

app.directive('navigation', ['QR_DATA', 'DialogService', function (QR_DATA, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/navigation.html',
        scope: true,
        controller: function ($scope) {

            var notify = QR_DATA.notify;

            $scope.showAbout = function() {
                DialogService.open({
                    templateUrl: '/templates/modals/about.html',
                    size: 'lg'
                });
            };

            $scope.showBeta = function() {
                DialogService.notify(notify.beta.body, notify.beta.title);
            };

        }
    }
}]);

app.directive('emotions', ['$timeout', function ($timeout) {

    var templateHtml =
        '<div class="row margin-bottom-lg text-center"> ' +
        '<div ng-repeat="emotion in emotions" class="col-lg-4 col-sm-4 col-xs-4">' +
        '<a href emotion ng-model="emotion"><img ng-src="/img/{{::emotion.name | lowercase}}.png"></a>' +
        '</div>' +
        '</div>';

    return {
        restrict: 'E',
        template: templateHtml,
        scope: {
            emotions: '=',
            selection: '=',
            rant: '='
        },
        controller: function ($scope) {
            this.select = function (emotion) {
                $timeout(function () {
                    _alwaysClear();
                    !_alreadySelected(emotion)
                        ? $scope.selection.emotion = emotion.name
                        : delete $scope.selection.emotion;
                });
            };
            function _alreadySelected(emotion) {
                return emotion.name === $scope.selection.emotion;
            }
            function _alwaysClear() {
                delete $scope.selection.question;
                $scope.rant = {};
            }
        }
    }
}]);

app.directive('emotion', function () {
    return {
        restrict: 'A',
        require: '^emotions',
        scope: {
            ngModel: '='
        },
        link: function (scope, element, attrs, facesCtrl) {
            element.bind('click', function () {
                facesCtrl.select(scope.ngModel);
            });
        }
    }
});

app.directive('questions', ['$timeout', function ($timeout) {
    var templateHtml =
        '<div ng-repeat="emotion in emotions" class="text-center">' +
        '<div ng-if="showQuestionsForEmotion(emotion, selection)" class="row margin-bottom-lg">' +
        '<div class="col-lg-offset-1 col-lg-10">' +
        '<question ng-repeat="question in emotion.questions" question="question" button-style="emotion.styles.button"></question>' +
        '</div>' +
        '</div>';
    return {
        restrict: 'E',
        template: templateHtml,
        scope: {
            emotions: '=',
            selection: '='
        },
        controller: function ($scope) {
            this.select = function (question) {
                $timeout(function() {
                    !_alreadySelected(question)
                        ? $scope.selection.question = question
                        : delete $scope.selection.question;
                });
            };
            function _alreadySelected(question) {
                return question === $scope.selection.question;
            }
            $scope.showQuestionsForEmotion = function(emotion, selection) {
                if (_.isEmpty(selection)) return false;
                return emotion.name === selection.emotion;
            }
        }
    };
}]);

app.directive('question', function () {
    return {
        restrict: 'E',
        require: '^questions',
        template: '<button class="btn btn-{{buttonStyle}} btn-question" type="button">{{::question}}</button>',
        scope: {
            question: '=',
            buttonStyle: '='
        },
        link: function (scope, element, attrs, questionsCtrl) {
            element.bind('click', function () {
                questionsCtrl.select(scope.question);
            });
        }
    }
});

app.directive('rants', ['$timeout', 'QR_DATA', 'DialogService', function ($timeout, QR_DATA, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-stream.html',
        scope: {
            page: '=',
            activeView: '=',
            views: '=',
            popularRants: '='
        },
        controller: function($scope) {

            $scope.replyToRant = {};

            this.replyToRant = function(rant) {
                $timeout(function() {
                    $scope.activeView = $scope.views.REPLY;
                    $scope.replyToRant = rant;
                });
            };

            $scope.isLastRant = function(index) {
                return index == $scope.page.getRantCount() - 1
            };

            $scope.isLastPopularRant = function(index) {
                return index === $scope.popularRants.length - 1;
            };

            $scope.getNextPage = function() {
                $scope.page.getNextPage()
                    .done(function(page) {
                        $timeout(function() {
                            $scope.page = page;
                        });
                    });
            };

            $scope.showPolledRants = function() {
                $scope.page.applyPolledRants();
            };

        }
    }
}]);


app.directive('rant', ['RantService',function(RantService) {
    return {
        restrict: 'E',
        require: '^rants',
        templateUrl: '/templates/directives/rant.html',
        scope: {
            rant: '=',
            flash: '='
        },
        link: function (scope, element, attrs, rantsCtrl) {
            scope.replyTo = function(rant) {
                rantsCtrl.replyToRant(rant);
            };
        }
    }
}]);

app.directive('rantForm', ['$timeout', 'QR_CONST', 'RantService', 'DialogService', function ($timeout, QR_CONST, RantService, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-form.html',
        scope: {
            rant: '=',
            emotions: '=',
            selection: '=',
            page: '='
        },
        controller: function ($scope) {

            $scope.rant.allowComments = true;

            $scope.getPanelStyleForEmotion = function(selection) {
                var emotionKey = selection.emotion;
                var emotion = $scope.emotions[emotionKey];
                return emotion.styles.panel;
            };

            $scope.getCharactersLeft = function(rant) {
                if (!rant.text) return;
                return QR_CONST.RESTRICTIONS.MAX_CHAR - rant.text.length;
            };

            $scope.postRant = function(rant) {
                if (!rant || !rant.text) return;
                RantService.postRant(rant, $scope.selection)
                    .done(function(postedRant) {
                        $timeout(function() {
                            $scope.page.addPostedRant(postedRant);
                            $scope.selection.emotion = undefined;
                            $scope.selection.question = undefined;
                        });
                    })
                    .fail(function(error) {
                        DialogService.error(error.message);
                    });
            };

            $scope.$watch('form.text.$error', function(errors) {
                $scope.error = {};
                _.each(errors, function(value, key) {
                    if (value === true) {
                        $scope.error[key] = true;
                    }
                });
            }, true);

        }
    }
}]);

app.directive('replyToRant', ['$timeout', 'QR_CONST', 'RantService', 'DialogService', function ($timeout, QR_CONST, RantService, DialogService) {
    return {
        restrict: 'E',
        templateUrl: '/templates/directives/rant-reply.html',
        scope: {
            rant: '='
        },
        controller: function ($scope) {

            $scope.showForm = true;

            function _addCommentToRant(comment) {
                $scope.rant.comments.push(comment);
                $scope.rant.commentCount += 1;
            }

            function _resetForm() {
                $scope.form.location = undefined;
                $scope.form.name = undefined;
                $scope.form.text = undefined;
            }

            $scope.saveComment = function(form) {
                var comment = RantService.createNewComment(form);
                if (!comment || !comment.text) return;
                RantService.saveComment(comment, $scope.rant.id)
                    .done(function(comment) {
                        _addCommentToRant(comment);
                        _resetForm();
                    })
                    .always(function() {
                        $timeout(function() {
                            $scope.showForm = false;
                        });
                    });
            };

            $scope.hasComments = function() {
                return $scope.rant.comments.length > 0;
            };

            $scope.showDirectionals = function() {
                if (!$scope.rant.comments) return;
                return $scope.rant.comments.length > 3;
            };

            $scope.getCharactersLeft = function(text) {
                if (!text) return;
                return QR_CONST.RESTRICTIONS.MAX_CHAR - text.length;
            };

            $scope.$watch('form.text.$error', function(errors) {
                $scope.error = {};
                _.each(errors, function(value, key) {
                    if (value === true) {
                        $scope.error[key] = true;
                    }
                });
            }, true);
        }
    }
}]);


app.directive('myVis', function($timeout) {
    return {
        restrict: 'E',
        template: '<div id="stat"></div>',
        scope: {
            data: '='
        },
        link: function(scope, element, attrs) {

            var data = _.map(_.range(200), function(i) {
                return Math.random() * 50;
            });

            var margin = { top: 55, right: 40, bottom: 35, left: 40 };
            var height = 480 - margin.top - margin.bottom;
            var width = 640 - margin.left - margin.right;

            var yScale = d3.scale.linear()
                .domain([0, d3.max(data) * 1.1])
                .range([0, height]);

            var xScale = d3.scale.ordinal()
                .domain(data)
                .rangeBands([0, width], 0.1, 0);

            var colorScale = d3.scale.linear()
                .domain([0, d3.max(data)])
                .range(['black', 'blue']);

            // Initialize the svg canvas
            var svg = d3.select('#stat')
                .append('div')
                .classed('svg-container', true)
                .append('svg')
                .attr('preserveAspectRatio', 'xMinYMin meet')
                .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
                .classed('svg-content-responsive', true)
                .append('g')
                .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');

            svg.selectAll('rect')
                .data(data)
                .enter()
                .append('rect')
                .attr('class', 'vis-style')
                .attr('x', function(d) {
                    return xScale(d);
                })
                .attr('y', function(d) {
                    return height - yScale(d);
                })
                .attr('width', xScale.rangeBand())
                .attr('height', function(d) {
                    return yScale(d);
                })
                .attr('fill', colorScale)


        }
    }
});

app.directive('statistic', ['ArrayService', function(ArrayService) {
    return {
        restrict: 'E',
        template: '<code class="grey-0 margin-bottom text-large">{{title}} | <button ng-click="toggleData()">Update</button></code><div id="{{id}}"></div>',
        scope: {
            data: '=',
            margin: '@',
            id: '@',
            title: '@'
        },
        link: function (scope, element, attrs) {
            if (!scope.data || !scope.id) return;

            var _data = angular.copy(scope.data);
            var _keys = _.keys(_data);
            var _activeData = _data[_.last(_keys)];

            scope.toggleData = function() {
                var i = Math.floor(Math.random() * _keys.length);
                var random = scope.data[_keys[i]];
                if (random) {
                    ArrayService.popUntil(_getActiveData(), random.length);
                    _.each(random, function(d, i) {
                        var existing = _getActiveData()[i];
                        if (existing) {
                            existing.label = d.label;
                            existing.value = d.value;
                        } else {
                            _getActiveData().push(d);
                        }
                    });
                    svg
                        .selectAll('rect')
                        .call(_renderBars);
                    _renderXAxis(svg);
                    _renderYAxis(svg);
                }
            };

            // Set margins
            var margin = { top: 0, right: 0, bottom: 0, left: 0 };
            if (scope.margin) {
                var keys = _.keys(margin);
                var margins = scope.margin.split(" ");
                _.each(margins, function(each, i) {
                    margin[keys[i]] = each;
                });
            }

            var margin = { top: 30, right: 40, bottom: 220, left: 75 };
            var height = 480 - margin.top - margin.bottom;
            var width = 640 - margin.left - margin.right;

            //var _legendData = [
            //    ['angry','#CB3B37'],
            //    ['happy', '#3D9BCB'],
            //    ['sad', '#555555']
            //];

            //var colorScale = d3.scale.quantile()
            //    .domain([0, numAngry, data.length-numSad, data.length])
            //    .range(['#CB3B37', '#3D9BCB', '#555555']);

            // Initialize the svg canvas
            var svg = d3.select('#' + scope.id)
                .append('div')
                .classed('svg-container', true)
                .append('svg')
                .attr('preserveAspectRatio', 'xMinYMin meet')
                .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
                .classed('svg-content-responsive', true)
                .append('g')
                .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');

            //// Generate legend
            //var legend = svg.append('g')
            //    .attr('class', 'stat-emotion-legend')
            //    .attr('transform', 'translate(' + margin.right + ', -' + (margin.top - 15) + ')');
            //
            //legend.selectAll('circle').call(_initLegendCircles);
            //legend.selectAll('text').call(_initLegendText);

            _initXAxis(svg);
            _initYAxis(svg);

            _renderXAxis(svg);
            _renderYAxis(svg);

            svg
                .selectAll('rect')
                .call(_initBars);

            svg
                .selectAll('rect')
                .call(_renderBars);

                //.attr('fill', function(d, i) {
                //    return colorScale(i)
                //});

            //function _initLegendCircles(selection) {
            //    selection
            //        .data(_legendData)
            //        .enter()
            //        .append('circle')
            //        .attr('cx', function(d, i) {
            //            return margin.left + ((i/_legendData.length) * width);
            //        })
            //        .attr('cy', 5)
            //        .attr('r', 5)
            //        .style('fill', function(d) {
            //            return d[1];
            //        });
            //}

            //function _initLegendText(selection) {
            //    selection
            //        .data(_legendData)
            //        .enter()
            //        .append('text')
            //        .attr('x', function(d, i){
            //            return margin.left + ((i/_legendData.length) * width) + 10;
            //        })
            //        .attr('y', 10)
            //        .text(function(d) {
            //            return d[0];
            //        });
            //}

            function _renderBars(selection) {
                var xScale = _XScale();
                var yScale = _YScale();
                selection
                    .transition()
                    .duration(750)
                    .attr('x', function(d, i) {
                        return xScale(i);
                    })
                    .attr('y', function(d) {
                        return yScale(d.value);
                    })
                    .attr('width', function(d) {
                        return (width / _getActiveDataLength()) - 1;
                    })
                    .attr('height', function(d) {
                        return height - yScale(d.value);
                    });
            }

            function _initBars(selection) {
                selection
                    .data(_getActiveData())
                    .enter()
                    .append('rect')
                    .attr('class', 'stat' + scope.id)
            }

            function _initYAxis(svg) {
                svg
                    .append('g')
                    .attr('class', 'y axis')
                    .attr('transform', 'translate(0,0)');
            }

            function _renderYAxis(svg) {
                svg.select('.y.axis')
                    .call(_YAxis());
            }

            function _initXAxis(svg) {
                svg
                    .append('g')
                    .attr('class', 'x axis')
                    .attr('transform', 'translate(0, ' + (height) + ')');
            }

            function _renderXAxis(svg) {
                svg.select('.x.axis')
                    .call(_XAxis())
                    .selectAll("text")
                    .style("text-anchor", "end")
                    .attr("dx", "-.1em")
                    .attr("transform", "rotate(-65)" )
                    .text(function(i) {
                        return _getActiveData()[i] ? _getActiveData()[i].label : '';
                    });
            }

            function _XScale() {
                return d3.scale.linear()
                    .domain([0, _getActiveDataLength()])
                    .range([0, width]);
            }

            function _YScale() {
                return d3.scale.linear()
                    .domain([0, _getMax(_getActiveData()) * 1.1])
                    .range([height, 0]);
            }

            function _XAxis() {
                return d3.svg.axis()
                    .scale(_XScale())
                    .tickSize(2)
                    .ticks(_getActiveDataLength())
                    .orient('bottom');
            }

            function _YAxis() {
                return d3.svg.axis()
                    .scale(_YScale())
                    .tickSize(2)
                    .orient('left');
            }

            function _getActiveData() {
                return _activeData;
            }

            function _getActiveDataLength() {
                return _getActiveData().length;
            }

            function _getMax(data) {
                return d3.max(data, function(d) {
                    return d.value;
                });
            }

        }
    }
}]);

app.directive('questionStatistics', ['StatisticsService', function(StatisticsService) {
    return {
        restrict: 'E',
        template: '<code class="grey-0 margin-bottom text-large">Rants By Question</code><div id="question-stats"></div>',
        scope: {
            data: '='
        },
        link: function(scope, element, attrs) {

            var margin = { top: 30, right: 40, bottom: 220, left: 75 };
            var height = 480 - margin.top - margin.bottom;
            var width = 640 - margin.left - margin.right;

            var _legendData = [
                ['angry','#CB3B37'],
                ['happy', '#3D9BCB'],
                ['sad', '#555555']
            ];

            StatisticsService.getQuestionStatistics()
                .done(function(data) {

                    var numAngry  = _.filter(data, function(d) {
                        return d.emotion === 'ANGRY';
                    }).length;

                    var numSad  = _.filter(data, function(d) {
                        return d.emotion === 'SAD';
                    }).length;

                    var yScale = d3.scale.linear()
                        .domain([0, d3.max(data, function(d) {
                            return d.value;
                        }) * 1.1])
                        .range([height, 0]);

                    var xScale = d3.scale.linear()
                        .domain([0, data.length])
                        .range([0, width]);

                    var colorScale = d3.scale.quantile()
                        .domain([0, numAngry, data.length-numSad, data.length])
                        .range(['#CB3B37', '#3D9BCB', '#555555']);

                    // Initialize the svg canvas
                    var svg = d3.select('#question-stats')
                        .append('div')
                        .classed('svg-container', true)
                        .append('svg')
                        .attr('preserveAspectRatio', 'xMinYMin meet')
                        .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
                        .classed('svg-content-responsive', true)
                        .append('g')
                        .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');

                    // Generate legend
                    var legend = svg.append('g')
                        .attr('class', 'stat-emotion-legend')
                        .attr('transform', 'translate(' + margin.right + ', -' + (margin.top - 15) + ')');

                    legend.selectAll('circle').call(_initLegendCircles);
                    legend.selectAll('text').call(_initLegendText);


                    var xAxis = d3.svg.axis()
                        .scale(xScale)
                        .tickSize(2)
                        .ticks(data.length-1)
                        .orient('bottom');

                    // Add X axis
                    svg
                        .append('g')
                        .attr('class', 'stat-emotion-axis')
                        .attr('transform', 'translate(0, ' + (height) + ')')
                        .call(xAxis)
                        .selectAll("text")
                        .style("text-anchor", "end")
                        .attr("dx", "-.1em")
                        .attr("transform", "rotate(-65)" )
                        .text(function(i) {
                            return data[i] ? data[i].label : '';
                        });

                    var yAxis = d3.svg.axis()
                        .scale(yScale)
                        .tickSize(2)
                        .orient('left');

                    // Add Y axis
                    svg
                        .append('g')
                        .attr('class', 'stat-emotion-axis')
                        .attr('transform', 'translate(0,0)')
                        .call(yAxis);

                    svg.selectAll('rect')
                        .data(data)
                        .enter()
                        .append('rect')
                        .attr('class', 'vis-style')
                        .attr('x', function(d, i) {
                            return xScale(i);
                        })
                        .attr('y', function(d) {
                            return yScale(d.value);
                        })
                        .attr('width', function(d) {
                            return (width / data.length) - 1;
                        })
                        .attr('height', function(d) {
                            return height - yScale(d.value);
                        })
                        .attr('fill', function(d, i) {
                            return colorScale(i)
                        })

                })

            function _initLegendCircles(selection) {
                selection
                    .data(_legendData)
                    .enter()
                    .append('circle')
                    .attr('cx', function(d, i) {
                        return margin.left + ((i/_legendData.length) * width);
                    })
                    .attr('cy', 5)
                    .attr('r', 5)
                    .style('fill', function(d) {
                        return d[1];
                    });
            }

            function _initLegendText(selection) {
                selection
                    .data(_legendData)
                    .enter()
                    .append('text')
                    .attr('x', function(d, i){
                        return margin.left + ((i/_legendData.length) * width) + 10;
                    })
                    .attr('y', 10)
                    .text(function(d) {
                        return d[0];
                    });
            }

        }
    }
}]);



app.directive('emotionStatistics', ['StatisticsService', function(StatisticsService) {
    return {
        restrict: 'E',
        template: '<code class="grey-0 margin-bottom text-large">Rants By Emotion</code><div id="emotion-stats"></div>',
        link: function(scope, element, attrs) {

            // Dummy object for transitions
            var _data = [
                { label: 'angry', value: 0 },
                { label: 'happy', value: 0 },
                { label: 'sad', value: 0 }
            ];

            var _legendData = [
                ['angry','#CB3B37'],
                ['happy', '#3D9BCB'],
                ['sad', '#555555']
            ];

            var margin = { top: 55, right: 40, bottom: 35, left: 40 };
            var height = 480 - margin.top - margin.bottom;
            var width = 640 - margin.left - margin.right;

            // Initialize the svg canvas
            var canvas = d3.select('#emotion-stats')
                .append('div')
                .classed('svg-container', true)
                .append('svg')
                .attr('preserveAspectRatio', 'xMinYMin meet')
                .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
                .classed('svg-content-responsive', true)
                .append('g')
                .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');

            // Initialize the chart
            canvas
                .selectAll('rect')
                .call(_initBars);

            // Generate legend
            var legend = canvas.append('g')
                .attr('class', 'stat-emotion-legend')
                .attr('transform', 'translate(' + margin.right + ', -' + (margin.top - 15) + ')');

            legend.selectAll('circle').call(_initLegendCircles);
            legend.selectAll('text').call(_initLegendText);

            StatisticsService.getEmotionStatistics()
                .done(function(data) {

                    _.each(_data, function(datum, i) {
                        datum.value = data[i].value;
                    });

                    // Add X axis
                    canvas
                        .append('g')
                        .attr('class', 'stat-emotion-axis')
                        .attr('transform', 'translate(0, ' + (height) + ')')
                        .call(_getXAxis());

                    // Add Y axis
                    canvas
                        .append('g')
                        .attr('class', 'stat-emotion-axis')
                        .attr('transform', 'translate(0,0)')
                        .call(_getYAxis());

                    // Regenerate bar chart with updated data
                    canvas
                        .selectAll('rect')
                        .call(_paintBars);

                });

            function _paintBars(selection) {

                var xScale = _getXScale();
                var yScale = _getYScale();
                var colorScale = _getColorScale();

                selection
                    .transition()
                    .duration(250)
                    .delay(function(d, i) {
                        return i * 125;
                    })
                    .ease('quad')
                    .attr('x', function(d) {
                        return xScale(d.value);
                    })
                    .attr('y', function(d) {
                        return yScale(d.value);
                    })
                    .attr('width', xScale.rangeBand())
                    .attr('height', function(d) {
                        return height - yScale(d.value);
                    })
                    .attr('fill', function(d, i) {
                        return colorScale(i);
                    })
                    .attr('fill-opacity', 1);

            }

            function _initBars(selection) {
                selection
                    .data(_data)
                    .enter()
                    .append('rect')
                    .attr('class', 'stat-emotion')
                    .attr('y', height)
                    .attr('height', 0)
                    .attr('fill', 'gray')
                    .attr('fill-opacity',0)
                    .on('mouseover', function(d) {
                        d3.select(this).classed('active-bar-' + d.label, true);
                    })
                    .on('mouseout', function(d) {
                        d3.select(this).classed('active-bar-' + d.label, false);
                    });
            }

            function _initLegendCircles(selection) {
                selection
                    .data(_legendData)
                    .enter()
                    .append('circle')
                    .attr('cx', function(d, i) {
                        return margin.left + ((i/_legendData.length) * width);
                    })
                    .attr('cy', 5)
                    .attr('r', 5)
                    .style('fill', function(d) {
                        return d[1];
                    });
            }

            function _initLegendText(selection) {
                selection
                    .data(_legendData)
                    .enter()
                    .append('text')
                    .attr('x', function(d, i){
                        return margin.left + ((i/_legendData.length) * width) + 10;
                    })
                    .attr('y', 10)
                    .text(function(d) {
                        return d[0];
                    });
            }

            function _getXAxis() {
                return d3.svg.axis()
                    .scale(_getXScale())
                    .tickSize(2)
                    .orient('bottom');
            }

            function _getYAxis() {
                return d3.svg.axis()
                    .scale(_getYScale())
                    .tickSize(2)
                    .orient('left')
            }

            function _getXScale() {
                return d3.scale.ordinal()
                    .domain(_getDataValues())
                    .rangeBands([0, width], 0.1, 0);
            }

            function _getYScale() {
                return d3.scale.linear()
                    .domain([0, d3.max(_getDataValues())])
                    .range([height, 0]);
            }

            function _getColorScale() {
                return d3.scale.quantile()
                    .domain([0, _data.length])
                    .range(['#CB3B37', '#3D9BCB', '#555555']);
            }

            function _getDataValues() {
                return _.pluck(_data, 'value');
            }

        }
    }
}]);
