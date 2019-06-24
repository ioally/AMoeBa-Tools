/**
 * 反馈信息
 */
define([
    'app',
    'constants'
], function (app) {
    app.registerController('feedbackCtrl', ['$scope', '$rootScope', '$$finder', 'layer',
        function ($scope, $rootScope, $$finder, layer) {
            $rootScope.access("feedback");

            $scope.feedbackDto = {
                content: null,
                createBy: null,
                email: null
            };

            $scope.submit = function ($event) {
                if (!$scope.feedbackDto.createBy) {
                    layer.alert("你的称呼不能为空！", null, $event);
                    return;
                }
                if (!$scope.feedbackDto.email) {
                    layer.alert("邮箱地址不能为空！", null, $event);
                    return;
                }
                if (!$scope.feedbackDto.content) {
                    layer.alert("反馈内容不能为空！", null, $event);
                    return;
                }
                let copyFeedbackDto = angular.copy($scope.feedbackDto);
                copyFeedbackDto.createBy += "(" + $rootScope.userInfo.userEmployeeName + ")";
                $$finder.post("addFeedBack", copyFeedbackDto, {
                    success: function (data) {
                        if (data.statusCode == 200 && data.content > 0) {
                            layer.alert("反馈成功！", function () {
                                $rootScope.goHome();
                            }, $event);
                        } else {
                            layer.alert("反馈失败！", function () {
                                $rootScope.goHome();
                            }, $event);
                        }
                    },
                    error: function (e) {
                        layer.alert("网络问题，反馈失败！", function () {
                            $rootScope.goHome();
                        }, $event);
                    }
                })
            };
        }
    ]);
});