define([
    'app',
    'constants',
    'moment',
], function (app, constants, moment) {
    app.registerController('showLogCtrl', ['$scope', '$rootScope', '$state', '$$finder', '$timeout', 'layer',
        function ($scope, $rootScope, $state, $$finder, $timeout, layer) {
            $rootScope.access("showLog");
            let nowDate = new Date();
            let fullYear = nowDate.getFullYear();
            let month = nowDate.getMonth();

            $scope.selectLogs = [];

            $scope.queryInfo = {
                startDate: new Date(fullYear, month, 1),
                endDate: nowDate,
                startDateMin: new Date(fullYear - 2, month, 1),
                startDateMax: nowDate,
                endDateMin: new Date(fullYear, month, 1),
                endDateMax: nowDate,
            };

            $scope.$watch('queryInfo.startDate', function () {
                let _fullYear = $scope.queryInfo.startDate.getFullYear();
                let _month = $scope.queryInfo.startDate.getMonth();
                let _date = $scope.queryInfo.startDate.getDate();
                $scope.queryInfo.endDateMin = new Date(_fullYear, _month, _date + 1);
                if ($scope.queryInfo.endDate <= $scope.queryInfo.startDate) {
                    $scope.queryInfo.endDate = $scope.queryInfo.endDateMin;
                }
            });

            $scope.stylePage = {
                selectAll: ''
            };

            $scope.logContentAm = [];
            $scope.logContentPm = [];

            $scope.selectLog = function (item) {
                if ($scope.determinateDis) {
                    item.switch = !item.switch;
                    if (item.switch) {
                        $scope.selectLogs.push(item.id);
                    } else {
                        let index = $scope.selectLogs.indexOf(item.id);
                        if (index > -1) {
                            $scope.selectLogs.splice(index, 1);
                        }
                    }
                    if ($scope.selectLogs.length == ($scope.logContentAm.length + $scope.logContentPm.length)) {
                        $scope.stylePage.selectAll = 'md-primary'
                    } else {
                        $scope.stylePage.selectAll = ''
                    }
                }
            };

            $scope.selectAll = function () {
                if ($scope.logContentAm.length + $scope.logContentPm.length > 0) {
                    if ($scope.stylePage.selectAll === 'md-primary') {
                        angular.forEach($scope.logContentAm, function (item) {
                            item.switch = false;
                        });
                        angular.forEach($scope.logContentPm, function (item) {
                            item.switch = false;
                        });
                        $scope.stylePage.selectAll = '';
                        $scope.selectLogs = [];
                    } else {
                        $scope.selectLogs = [];
                        angular.forEach($scope.logContentAm, function (item) {
                            item.switch = true;
                            $scope.selectLogs.push(item.id);
                        });
                        angular.forEach($scope.logContentPm, function (item) {
                            item.switch = true;
                            $scope.selectLogs.push(item.id);
                        });
                        $scope.stylePage.selectAll = 'md-primary'
                    }
                }
            };

            $scope.reset = function () {
                $scope.selectLogs = [];
                $scope.logContentAm = [];
                $scope.logContentPm = [];
                $scope.stylePage.selectAll = '';
                $rootScope.determinateDis = true;
            };

            $scope.queryLog = function () {
                $scope.reset();
                $rootScope.determinateDis = false;
                $$finder.post('getLog', {
                    startDate: moment($scope.queryInfo.startDate).format(constants.reg.datePicker),
                    endDate: moment($scope.queryInfo.endDate).format(constants.reg.datePicker)
                }, {
                    success: function (data) {
                        $rootScope.determinateDis = true;
                        if (data.statusCode == 200) {
                            $scope.logContentAm = data.content.am || [];
                            $scope.logContentPm = data.content.pm || [];
                            if ($scope.logContentAm.length + $scope.logContentPm.length < 1) {
                                layer.alert("所选日期区间没有填写日志！");
                            }
                        } else {
                            layer.alert("查询失败！");
                        }
                    },
                    error: function (e) {
                        $rootScope.determinateDis = true;
                        console.error(e);
                    }
                });
            };

            $scope.longClick = function (item) {
                let content = "开始时间：" + item.start_date + "&nbsp;&nbsp;结束时间：" + item.end_date + "<br><br>日志内容：" + item.text + "<br><br>日志ID：" + item.id;
                layer.alert_bar("日志详情", content);
            };

            $scope.batchDelete = function () {
                if ($scope.selectLogs.length > 0) {
                    layer.confirm_bar('操作确认', '注意：删除操作不可恢复，请确认是否继续删除？', {
                        ok: function () {
                            $rootScope.determinateDis = false;
                            $$finder.post('batchDelete', $scope.selectLogs, {
                                success: function (data) {
                                    if (data.statusCode == 200) {
                                        let copyAm = angular.copy($scope.logContentAm);
                                        let copyPm = angular.copy($scope.logContentPm);
                                        $scope.logContentAm = [];
                                        $scope.logContentPm = [];
                                        angular.forEach(copyAm, function (item) {
                                            if ($scope.selectLogs.indexOf(item.id) < 0) {
                                                $scope.logContentAm.push(item);
                                            }
                                        });
                                        angular.forEach(copyPm, function (item) {
                                            if ($scope.selectLogs.indexOf(item.id) < 0) {
                                                $scope.logContentPm.push(item);
                                            }
                                        });
                                        $scope.stylePage.selectAll = '';

                                        if ($rootScope.config.multiThread) {
                                            layer.alert("成功提交[" + $scope.selectLogs.length + "]条日志删除请求，请勿再重复提交！");
                                        } else {
                                            layer.alert("成功删除[" + $scope.selectLogs.length + "]条日志！");
                                        }

                                        $scope.selectLogs = [];
                                    } else {
                                        layer.alert("操作失败！" + data.message);
                                    }
                                    $rootScope.determinateDis = true;
                                },
                                error: function (e) {
                                    console.error(e);
                                    if (e.status == 504) {
                                        layer.alert("删除队列任务较多，正在后台努力删除中....请稍后查看！");
                                        $scope.reset();
                                    }
                                    $rootScope.determinateDis = true;
                                }
                            })
                        }
                    });
                } else {
                    layer.alert("至少选择一条才能删除！");
                }
            };


        }]
    )
});