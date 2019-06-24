/**
 * 对$http的轻量封装向后端服务发起请求
 */
define([
    'angular',
    'serviceRouter'
], function (angular, serviceRouter) {
    angular.module('finder', [])
        .factory('$$finder', ['$http', 'layer', '$rootScope',
            function ($http, layer, $rootScope) {
                return {

                    method: {
                        dataType: "JSON",
                        contentType: "application/json; charset=UTF-8",
                        headers: {}
                    },

                    /**
                     * 向后端服务发起post请求
                     * @param {string}target 目标服务名称
                     * @param {*}keywords 请求参数
                     * @param {object}options 请求之后的回调函数必须包含success和error两个function
                     */
                    post: function (target, keywords, options) {
                        this.method.method = 'POST';
                        this.method.data = keywords;
                        this.method.url = serviceRouter.getIp() + serviceRouter.getBaseUrl() + serviceRouter.getUrl(target);
                        $http(this.method)
                            .then(function (result) {
                                $rootScope.determinateDis = true;
                                if (options && options.success && typeof (options.success) == 'function') {
                                    if (result.data.statusCode == 401) {
                                        $rootScope.restMenu();
                                        layer.alert("请先登录以后再操作！");
                                    } else if (result.data.statusCode == 203) {
                                        layer.alert("系统繁忙，暂无法处理请求，请稍后再试！");
                                    } else {
                                        options.success(result.data);
                                    }
                                }
                            })
                            .catch(function (result) {
                                $rootScope.determinateDis = true;
                                if (options && options.error && typeof (options.error) == 'function') {
                                    options.error(result);
                                    if (result.status == 502) {
                                        layer.alert("服务或网络异常!")
                                    }
                                }
                            });
                    },

                    /**
                     * 向后端服务发起get请求
                     * @param {string}target 目标服务名称
                     * @param {*}keywords 请求参数
                     * @param {object}options 请求之后的回调函数必须包含success和error两个function
                     */
                    get: function (target, keywords, options) {
                        this.method.method = 'GET';
                        this.method.data = keywords;
                        this.method.url = serviceRouter.getIp() + serviceRouter.getBaseUrl() + serviceRouter.getUrl(target);
                        $http(this.method)
                            .then(function (result) {
                                if (options && options.success && typeof (options.success) == 'function')
                                    options.success(result.data);
                            })
                            .catch(function (result) {
                                if (options && options.error && typeof (options.error) == 'function')
                                    options.error(result);
                            });
                    }
                }
            }
        ])
});