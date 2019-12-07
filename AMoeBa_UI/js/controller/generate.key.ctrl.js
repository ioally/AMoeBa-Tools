/**
 * 生成密钥的控制器
 */
define([
    'app',
    'constants'
], function (app) {
    app.registerController('generateKeyCtrl', ['$scope', '$rootScope', '$$finder', 'layer',
        function ($scope, $rootScope, $$finder, layer) {
            $rootScope.access("generateKey");

            $scope.generateKey = {
                userName: null,
                key: null
            };

            /**
             * 调用生成key的服务
             */
            $scope.generate = function ($event) {
                if (!$scope.generateKey.userName) {
                    layer.alert("用户名不能为空！", null, $event);
                    return;
                }
                $$finder.post("generateAndIgnoreExistKey", $scope.generateKey, {
                    success: function (data) {
                        if (data.statusCode == 200) {
                            $scope.generateKey.key = data.content;
                            layer.alert("生成密钥成功，请使用【" + $scope.generateKey.userName + "】账号登陆！", null, $event);
                        } else {
                            layer.alert("生成密钥失败：" + data.message, null, $event);
                        }
                    },
                    error: function (e) {
                        console.error(e);
                    }
                })
            };

            /**
             * 验证key是否可用
             */
            $scope.verify = function ($event) {
                if (!$scope.generateKey.userName) {
                    layer.alert("用户名不能为空！", null, $event);
                    return;
                }
                if (!$scope.generateKey.key) {
                    layer.alert("密钥不能为空！", null, $event);
                    return;
                }
                $$finder.post("verifyKey", $scope.generateKey, {
                    success: function (data) {
                        console.log(data);
                        if (data.statusCode == 200) {
                            layer.alert("密钥验证通过，有效期至【" + data.content + "】", null, $event);
                        } else {
                            layer.alert(data.message, null, $event);
                        }
                    },
                    error: function (e) {
                        console.error(e);
                    }
                })
            };

            $scope.queryKey = function () {
                if (!$scope.generateKey.key) {
                    $$finder.post("getKeyByUserName", {userName: $scope.generateKey.userName}, {
                        success: function (data) {
                            if (data.statusCode == 200) {
                                $scope.generateKey.key = data.content;
                            }
                        },
                        error: function (e) {
                            console.error(e);
                        }
                    })
                }
            };

            $scope.cleanCache = function () {
                $scope.generateKey.key = null;
            }


        }
    ]);
});