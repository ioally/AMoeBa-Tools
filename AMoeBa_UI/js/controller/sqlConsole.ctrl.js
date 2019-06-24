/**
 * sql 脚本控制台，提供管理员维护数据接口
 */
define([
    'app',
    'constants'
], function (app) {
    app.registerController('sqlConsoleCtrl', ['$scope', '$rootScope', '$$finder', 'layer',
        function ($scope, $rootScope, $$finder, layer) {
            $rootScope.access("sqlConsole");

            $scope.sqlConsoleDto = {
                sql: ""
            };

            $scope.resultMessage = "";
            $scope.resultColor = "blue-500";

            $scope.resultDate = {};

            $scope.submit = function ($event) {
                $scope.resultDate = {};
                $$finder.post("sqlExecute", $scope.sqlConsoleDto, {
                    success: function (data) {
                        if (data.statusCode == 200) {
                            $scope.resultColor = "blue-500";
                            let executeFlag = data.content.executeFlag;
                            $scope.resultDate = data.content.content;
                            $scope.resultDate.executeFlag = executeFlag;
                            if (executeFlag === 'update') {
                                let rowNum = data.content.content;
                                $scope.resultMessage = "更新操作成功！影响行数[" + rowNum + "]。";
                            } else {
                                $scope.resultMessage = "查询成功！";
                            }
                        } else {
                            $scope.resultColor = "pink-500";
                            $scope.resultMessage = data.message;
                        }
                        console.log($scope.resultDate);
                    },
                    error: function (e) {
                        $scope.resultColor = "pink-500";
                        layer.alert("服务处理失败！", null, $event);
                        $scope.resultMessage = "服务处理失败！"
                    }
                });
            }


        }
    ]);
});