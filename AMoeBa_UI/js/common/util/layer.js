define([
    'angular'
], function (angular) {
    angular.module('$$layer', [])
        .factory('layer', ['$mdDialog',
            function ($mdDialog) {
                let param = {};
                let setParam = function (contents, buttonNumber, title, showProgressLinear) {
                    param.content = contents;
                    param.showProgressLinear = false;
                    if (buttonNumber < 2) {
                        param.buttons = [{
                            type: 'ok',
                            text: '确认',
                        }];
                    } else {
                        param.buttons = [{
                            type: 'ok',
                            text: '确认',
                        }, {
                            type: 'cancel',
                            text: '取消',
                        }];
                    }
                    if (title) {
                        param.showHead = true;
                        param.title = title;
                    } else {
                        param.showHead = false;
                    }
                    if (showProgressLinear) {
                        param.showProgressLinear = true;
                    }
                };
                return {
                    /**
                     * 打开一个弹框，并显示指定的内容
                     * @param dialogObj:{
                     *     title:标题,
                     *     showHead:是否显示toolbar,
                     *     content:提示内容
                     *     button:[]按钮详细，按钮做多有两个确认和取消，规则如下
                     *     {
                     *      type: 'ok',
                     *      text: '确认',
                     *      callback: function(){}
                     *     }
                     * }
                     */
                    open: function (dialogObj) {
                        if (dialogObj) {
                            let mdDialogParam = {};
                            mdDialogParam.templateUrl = 'template/message/dialog.html';
                            let targetEvent = dialogObj.ev;
                            if (targetEvent) {
                                mdDialogParam.targetEvent = targetEvent;
                            }
                            let _templateUrl = dialogObj.templateUrl;
                            if (_templateUrl) {
                                mdDialogParam.templateUrl = _templateUrl;
                            }
                            let title = dialogObj.title;
                            let showProgressLinear = dialogObj.showProgressLinear;
                            let showHead = dialogObj.showHead;
                            let content = dialogObj.content;
                            let buttons = dialogObj.buttons;

                            mdDialogParam.controller = function ($scope, $rootScope, $mdDialog) {
                                $scope.showHead = showHead;
                                $scope.title = title;
                                $scope.content = content;
                                $scope.showProgressLinear = showProgressLinear;
                                if (buttons) {
                                    if (buttons[0]) {
                                        let button1 = buttons[0];
                                        let type = button1.type;
                                        if (type == 'cancel') {
                                            $scope.cancelButton = button1;
                                        } else if (type == 'ok') {
                                            $scope.okButton = button1;
                                        }
                                    }
                                    if (buttons[1]) {
                                        let button2 = buttons[1];
                                        let type = button2.type;
                                        if (type == 'cancel') {
                                            $scope.cancelButton = button2;
                                        } else if (type == 'ok') {
                                            $scope.okButton = button2;
                                        }
                                    }
                                }

                                $scope.close = function () {
                                    $mdDialog.hide();
                                };

                                // 关闭窗口
                                $scope.closeDialog = function (callback) {
                                    $mdDialog.hide();
                                    if (typeof (callback) == "function") {
                                        callback();
                                    }
                                };

                                $scope.success = function (callback) {
                                    $mdDialog.hide();
                                    if (typeof (callback) == "function") {
                                        callback();
                                    }
                                };
                            };
                            $mdDialog.show(mdDialogParam);
                        }
                    },
                    /**
                     * 打开一个没有toolbar的提示框
                     * @param contents 提示信息
                     * @param callback 点击确定后的回调函数
                     * @param ev 事件来源
                     */
                    alert: function (contents, callback, ev) {
                        setParam(contents, 1);
                        param.buttons[0].callback = callback;
                        param.ev = ev;
                        this.open(param);
                    },
                    /**
                     * 打开一个没有toolbar的询问框
                     * @param contents 提示信息
                     * @param options ok-点击确定的回调函数，cancel-点击取消的回调函数
                     * @param ev 事件来源
                     */
                    confirm: function (contents, options, ev) {
                        setParam(contents, 2);
                        param.buttons[0].callback = options.ok;
                        param.buttons[1].callback = options.cancel;
                        param.ev = ev;
                        this.open(param);
                    },
                    /**
                     * 打开一个有toolbar的提示框
                     * @param title toolbar的标题
                     * @param contents 提示信息
                     * @param callback 点击确定后的回调函数
                     * @param ev 事件来源
                     */
                    alert_bar: function (title, contents, callback, ev) {
                        setParam(contents, 1, title);
                        param.buttons[0].callback = callback;
                        param.ev = ev;
                        this.open(param);
                    },
                    /**
                     * 打开一个有toolbar的询问框
                     * @param title toolbar的标题
                     * @param contents 提示信息
                     * @param options ok-点击确定的回调函数，cancel-点击取消的回调函数
                     * @param ev 事件来源
                     */
                    confirm_bar: function (title, contents, options, ev) {
                        setParam(contents, 2, title);
                        param.buttons[0].callback = options.ok;
                        param.buttons[1].callback = options.cancel;
                        param.ev = ev;
                        this.open(param);
                    },
                    /**
                     * 打开一个带有进度条的提示框
                     * @param contents 提示信息
                     * @param callback 点击确定后的回调函数
                     * @param ev 事件来源
                     */
                    alert_ProgressLinear: function (contents, callback, ev) {
                        setParam(contents, 1, undefined, true);
                        param.buttons[0].callback = callback;
                        param.ev = ev;
                        this.open(param);
                    }
                };
            }
        ])
});