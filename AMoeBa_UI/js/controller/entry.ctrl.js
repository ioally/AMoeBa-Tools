/**
 * 批量录入功能的控制器
 */
define([
    'angular',
    'app',
    'constants',
    'moment',
    'material'
], function (angular, app, constants, moment) {
    app.registerController('entryCtrl', ['$scope', '$rootScope', '$timeout', 'layer', '$$finder',
        function ($scope, $rootScope, $timeout, layer, $$finder) {
            $rootScope.access("entry");

            let nowDate = new Date();

            // 最多可添加的记录数
            let maxNumber = 5;
            // 默认勾选跳过双休日
            let defaultSkipWeekend = true;

            // 从常量配置中读取默认的上下班时间
            $scope.amStartTime = constants.obj.amStartDate;
            $scope.amEndTime = constants.obj.amEndDate;
            $scope.pmStartTime = constants.obj.pmStartDate;
            $scope.pmEndTime = constants.obj.pmEndDate;
            // 是否展示进度条
            $scope.showProgressLinear = false;

            let fullYear = nowDate.getFullYear();
            let month = nowDate.getMonth();
            let date = nowDate.getDate();
            let monthOfFirstDay = new Date(fullYear, month, 1);
            let monthOfLastDay = new Date(fullYear, month, moment(nowDate, "YYYY-MM").daysInMonth());

            // 数据行新增模板
            let rowData = {
                startDate: monthOfFirstDay,
                endDate: nowDate,
                content: null,
                skipWeekend: defaultSkipWeekend
            };


            // 页面数据函数初始化
            $scope.entry = {
                // 数据行
                workData: [angular.copy(rowData)],
                // 是否显示新增按钮
                showAddButton: true,
                // 是否显示删除按钮
                showDeleteButton: false,
                buttonText: '重置',
                // 剩余可操作数量
                remaining: maxNumber - 1,
                minDate: new Date(fullYear, month, date - 45),
                maxDate: monthOfLastDay,
                /**
                 * 新增一行
                 */
                addRow: function () {
                    this.workData.push(angular.copy(rowData));
                    allRegistered();
                    if (this.remaining <= 1) {
                        this.showAddButton = false;
                    }
                    if (this.workData.length > 1) {
                        this.showDeleteButton = true;
                        this.buttonText = '删除';
                    }
                    updateRemaining();
                },

                /**
                 * 删除指定行
                 * @param index 行索引
                 * @param isDelete 是否删除操作
                 */
                deleteRow: function (index, isDelete) {
                    if (isDelete) {
                        this.workData.splice(index, 1);
                        allRegistered();
                        if (this.workData.length <= maxNumber) {
                            this.showAddButton = true;
                        }
                        if (this.workData.length == 1) {
                            this.showDeleteButton = false;
                            this.buttonText = '重置';
                        }
                        updateRemaining();
                    } else {
                        this.resetData(index);
                    }
                },

                /**
                 * 重置所有的行数据
                 */
                resetData: function (index) {
                    let workDatum = this.workData[index];
                    workDatum.startDate = monthOfFirstDay;
                    workDatum.endDate = nowDate;
                    workDatum.content = null;
                    workDatum.skipWeekend = defaultSkipWeekend;
                }
            };

            /**
             * 更新剩余可增加数量
             */
            function updateRemaining() {
                $scope.entry.remaining = maxNumber - $scope.entry.workData.length;
            }

            /**
             * Material Design Lite 在动态网页使用时需要重新注册更新组件
             */
            function allRegistered() {
                $timeout(function () {
                    componentHandler.upgradeAllRegistered();
                });
            }

            /**
             * 开始日期校验
             * @param item 当前行数据
             */
            $scope.startDateChg = function (item, $event) {
                let sMoment = moment(item.startDate);
                let isAfter = sMoment.isAfter(item.endDate);
                if (isAfter) {
                    layer.alert("【开始日期】不能晚于【结束日期】，请修改！", function () {
                        item.startDate = item.endDate;
                    }, $event)
                }
            };

            /**
             * 结束日期校验
             * @param item 当前行数据
             */
            $scope.endDateChg = function (item, $event) {
                let sMoment = moment(item.startDate);
                let isAfter = sMoment.isAfter(item.endDate);
                if (isAfter) {
                    layer.alert("【结束日期】不能早于【开始日期】，请修改！", function () {
                        item.endDate = item.startDate;
                    }, $event)
                }
            };

            /**
             * 提交批量新增日志请求
             */
            $scope.submit = function ($event) {
                if (!$scope.entry.workData.length || $scope.entry.workData.length < 1) {
                    layer.alert("至少填写一条才能提交！", null, $event);
                } else {
                    let batchExecuteDto = {
                        amStartTime: moment($scope.amStartTime).format(constants.reg.time),
                        amEndTime: moment($scope.amEndTime).format(constants.reg.time),
                        pmStartTime: moment($scope.pmStartTime).format(constants.reg.time),
                        pmEndTime: moment($scope.pmEndTime).format(constants.reg.time),
                        logInfoDtos: []
                    };
                    let errorTemplate = "第{index}条数据，{type}为空，请检查！<br/>";
                    let checkRes = "";
                    let isCheck = true;

                    angular.forEach($scope.entry.workData, function (item, index) {
                        if (!item.startDate) {
                            checkRes += errorTemplate.replace("{index}", index + 1).replace("{type}", "【开始日期】");
                            isCheck = false;
                        }
                        if (!item.endDate) {
                            checkRes += errorTemplate.replace("{index}", index + 1).replace("{type}", "【结束日期】");
                            isCheck = false;
                        }
                        if (!item.content) {
                            checkRes += errorTemplate.replace("{index}", index + 1).replace("{type}", "【工作内容描述】");
                            isCheck = false;
                        }

                        if (isCheck) {
                            let copyDto = angular.copy(item);
                            copyDto.startDate = moment(item.startDate).format(constants.reg.datePicker);
                            copyDto.endDate = moment(item.endDate).format(constants.reg.datePicker);
                            batchExecuteDto.logInfoDtos.push(copyDto);
                        }
                    });
                    if (isCheck) {
                        $rootScope.determinateDis = false;
                        $$finder.post('batchSave', batchExecuteDto, {
                            success: function (data) {
                                $rootScope.determinateDis = true;
                                if (data.statusCode == 200) {
                                    if ($rootScope.config.multiThread) {
                                        layer.alert("日志新增请求提交成功，请勿重复操作！", function () {
                                            $scope.showProgressLinear = false;
                                        }, $event);
                                    } else {
                                        layer.alert("日志新增成功！", function () {
                                            $scope.showProgressLinear = false;
                                        }, $event);
                                    }
                                    $scope.entry.workData = [angular.copy(rowData)];
                                } else {
                                    layer.alert("日志新增请求处理失败！", function () {
                                        $scope.showProgressLinear = false;
                                    }, $event);
                                }
                            },
                            error: function (e) {
                                $rootScope.determinateDis = true;
                                if (e.status == 504) {
                                    layer.alert("删除队列任务较多，正在后台努力删除中....请稍后查看！", null, $event);
                                    $scope.showProgressLinear = false;
                                    $scope.entry.workData = [angular.copy(rowData)];
                                }
                            }
                        });
                        if (!$rootScope.config.multiThread) {
                            layer.alert_ProgressLinear("您的请求已经提交，稍后等片刻，请勿重复操作......<br/><br/>等不及可以过一会再来查询哦！", function () {
                                $scope.showProgressLinear = true;
                                $scope.entry.workData = [angular.copy(rowData)];
                            });
                        }
                    } else {
                        layer.alert(checkRes, null, $event);
                    }
                }
            };

        }
    ]);
});