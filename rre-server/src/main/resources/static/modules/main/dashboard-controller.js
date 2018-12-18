(function () {
    angular.module('myApp').controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', '$http', '$log', 'DataService', '$interval', 'ConfigService'];

    function DashboardController($scope, $http, $log, DataService, $interval, ConfigService) {
        var vm = this;

        // Scope vars
        vm.data = null;
        vm.isDetailOpen = [];
        // Filtering lists
        vm.metricList = [];
        vm.versionList = [];
        vm.corpusList = [];
        vm.topicList = [];
        vm.queryGroupList = [];

        // Methods
        vm.getMetricsCount = getMetricsCount;

        activate();

        ////////////

        /**
         * controller activation
         */
        function activate() {
            DataService.getData().then(
                function (response) {
                    vm.data = response;
                    updateFilterLists();
                },
                function (error) {
                    $log.error("DataService - Error while performing request:", error);
                }
            );
            $interval(function () {
                DataService.getData().then(
                    function (response) {
                        vm.data = response;
                    },
                    function (error) {
                        $log.error("DataService - Error while performing request:", error);
                    }
                );
            }, ConfigService.requestInterval);
            $scope.vm = vm;
            $log.log('DashboardController', 'starting');
        }


        function getMetricsCount() {
            if (vm.data != null) {
                return Object.keys(vm.data.metrics).length;
            }
            return 0;
        }

        updateFilterLists = function() {
            updateMetricList();
            updateVersionList();
            updateCorporaList();
        }

        updateMetricList = function() {
            DataService.getMetricList().then(
                function(data) {
                    for (var i = 0; i < data.length; i ++) {
                        if (!findByName(vm.metricList, data[i])) {
                            vm.metricList.push({ name: data[i], selected: true });
                        }
                    }
                },
                function(error) {
                    $log.error("DataService - error while fetching metrics: ", error);
                }
            );
        }

        $scope.toggleMetric = function(metric) {
            var idx = vm.currentMetrics.indexOf(metric);
            if (idx == -1) {
                vm.currentMetrics.push(metric);
            } else {
                vm.currentMetrics.splice(idx, 1);
            }
        }

        updateVersionList = function() {
            DataService.getVersionList().then(
                function(data) {
                    for (var i = 0; i < data.length; i ++) {
                        if (!findByName(vm.versionList, data[i])) {
                            vm.versionList.push({ name: data[i], selected: true });
                        }
                    }
                },
                function(error) {
                    $log.error("DataService - error while fetching versions: ", error);
                }
            );
        }

        findByName = function(objectArray, name) {
            var ret = null;
            for (i = 0; objectArray && i < objectArray.length; i ++) {
                if (objectArray[i].name == name) {
                    ret = objectArray[i];
                    break;
                }
            }
            return ret;
        }

        updateCorporaList = function() {
            DataService.getCorpusList().then(
                function(data) {
                    for (var i = 0; i < data.length; i ++) {
                        if (!findByName(vm.corpusList, data[i])) {
                            vm.corpusList.push({ name: data[i], selected: true });
                        }
                    }
                },
                function(error) {
                    $log.error("DataService - error while fetching corpora: ", error);
                }
            ).then(function() {
                updateTopicList();
            });
        }

        updateTopicList = function() {
            for (var i = 0; i < vm.corpusList.length; i ++) {
                if (vm.corpusList[i].selected) {
                    var corpus = vm.corpusList[i];
                    DataService.getTopicList(corpus.name).then(
                        function(data) {
                            for (var j = 0; j < data.length; j ++) {
                                if (!findByNameAndCorpus(vm.topicList, data[j], corpus.name)) {
                                    vm.topicList.push({
                                        name: data[j],
                                        corpus: corpus.name,
                                        selected: true,
                                        id: corpus.name + "_" + data[j]
                                    });
                                }
                            }
                        }.bind(this),
                        function(error) {
                            $log.error("DataService - error while fetching topics: ", error);
                        }
                    ).then(function() {
                        updateQueryGroupList();
                    });
                }
            }
        }

        findByNameAndCorpus = function(objectArray, name, corpus) {
            var ret = null;
            for (i = 0; objectArray && i < objectArray.length; i ++) {
                if (objectArray[i].name == name && objectArray[i].corpus && objectArray[i].corpus == corpus) {
                    ret = objectArray[i];
                    break;
                }
            }
            return ret;
        }

        updateQueryGroupList = function() {
            for (var i = 0; i < vm.corpusList.length; i ++) {
                for (var j = 0; j < vm.topicList.length; j ++) {
                    var corpus = vm.corpusList[i];
                    var topic = vm.topicList[j];
                    if (corpus.selected && topic.corpus == corpus.name && topic.selected) {
                        DataService.getQueryGroupList(corpus.name, topic.name).then(
                            function(data) {
                                for (i = 0; i < data.length; i ++) {
                                    if (!findQueryGroupInList(data[i], corpus.name, topic.name)) {
                                        vm.queryGroupList.push({
                                            name: data[i],
                                            selected: true,
                                            corpus: corpus.name,
                                            topic: topic.name,
                                            id: corpus.name + "_" + topic.name + "_" + data[i]
                                        });
                                    }
                                }
                            }.bind(this),
                            function(error) {
                            }
                        );
                    }
                }
            }
        }

        findQueryGroupInList = function(name, corpus, topic) {
            var ret = null;
            for (i = 0; vm.queryGroupList && i < vm.queryGroupList.length; i ++) {
                if (vm.queryGroupList[i].name == name
                    && vm.queryGroupList[i].corpus && vm.queryGroupList[i].corpus == corpus
                    && vm.queryGroupList[i].topic && vm.queryGroupList[i].topic == topic) {
                    ret = vm.queryGroupList[i];
                    break;
                }
            }
            return ret;
        }
    }
})();