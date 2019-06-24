define([
    'app'
], function (app) {
    app.registerProvider('routesDefault', [
        '$stateProvider',
        '$urlRouterProvider',
        '$couchPotatoProvider',
        '$locationProvider',
        '$provide',
        function ($stateProvider,
                  $urlRouterProvider,
                  $couchPotatoProvider) {
            this.$get = function () {
                return {};
            };
            $urlRouterProvider.when('', '/');
            $urlRouterProvider.otherwise('/welcome');

            $stateProvider
            // 欢迎页面
                .state('welcome', {
                    url: '/welcome',
                    views: {
                        'main': {
                            templateUrl: 'template/welcome.tpl.html'
                        }
                    }
                })
                // 查看日志页面
                .state('showLog', {
                    url: '/showLog',
                    resolve: {
                        dummy: $couchPotatoProvider.resolveDependencies([
                            'js/controller/showLog.ctrl.js'
                        ])
                    },
                    views: {
                        'main': {
                            templateUrl: 'template/showLog.tpl.html',
                            controller: 'showLogCtrl'
                        }
                    }
                })
                // 批量录入
                .state('entry', {
                    url: '/entry',
                    resolve: {
                        dummy: $couchPotatoProvider.resolveDependencies([
                            'js/controller/entry.ctrl.js'
                        ])
                    },
                    views: {
                        'main': {
                            templateUrl: 'template/entry.tpl.html',
                            controller: 'entryCtrl'
                        }
                    }
                })
                // 密钥生成
                .state('generateKey', {
                    url: '/generateKey',
                    resolve: {
                        dummy: $couchPotatoProvider.resolveDependencies([
                            'js/controller/generate.key.ctrl.js'
                        ])
                    },
                    views: {
                        'main': {
                            templateUrl: 'template/generateKey.tpl.html',
                            controller: 'generateKeyCtrl'
                        }
                    }
                })
                // 反馈信息
                .state('feedback', {
                    url: '/feedback',
                    resolve: {
                        dummy: $couchPotatoProvider.resolveDependencies([
                            'js/controller/feedback.ctrl.js'
                        ])
                    },
                    views: {
                        'main': {
                            templateUrl: 'template/feedback.tpl.html',
                            controller: 'feedbackCtrl'
                        }
                    }
                })
                // sql脚本控制台
                .state('sqlConsole', {
                    url: '/sqlConsole',
                    resolve: {
                        dummy: $couchPotatoProvider.resolveDependencies([
                            'js/controller/sqlConsole.ctrl.js'
                        ])
                    },
                    views: {
                        'main': {
                            templateUrl: 'template/sqlConsole.tpl.html',
                            controller: 'sqlConsoleCtrl'
                        }
                    }
                })

        }
    ]);
});