require.config({
    baseUrl: '',
    urlArgs: 'v=' + window.CHERRY.version,
    paths: {
        // 环境依赖包引入
        'jquery': 'js/lib/jquery/jquery.min',
        'angular': 'js/lib/angular/angular.min',
        'angular-cookies': 'js/lib/angular-cookies/angular-cookies',
        'angular-ui-router': 'js/lib/angular-ui-router/angular-ui-router.min', // 路由配置依赖
        'angular-couch-potato': 'js/lib/angular-couch-potato/angular-couch-potato', // angular按需加载依赖
        'material': 'js/lib/materialDesign/material.min',
        'angular-animate': 'js/lib/angular-animate/angular-animate.min',
        'angular-aria': 'js/lib/angular-aria/angular-aria.min',
        'angular-messages': 'js/lib/angular-messages/angular-messages.min',
        'angular-material': 'js/lib/angular-material/angular-material.min',
        'md-time-picker': 'js/lib/angular-time-picker/md-time-picker',
        'angular-touch':'js/lib/angular-touch/angular-touch.min',
        'angular-sanitize':'js/lib/angular-sanitize/angular-sanitize.min',
        'moment': 'js/lib/moment/moment.min',
        // 项目开发的js
        'app': 'js/app',
        'finder': 'js/common/util/finder',
        'routesDefault': 'js/router.config',
        'mainCtrl': 'js/controller/main.ctrl',
        'constants': 'js/common/constants/constants',
        'serviceRouter': 'js/common/constants/service.router',
        'layer': 'js/common/util/layer',
        'onHoldDirective':'js/common/util/onHoldDirective'
    },
    shim: {
        'jquery': {
            exports: 'jquery'
        },
        'angular': {
            exports: 'angular'
        },
        'angular-cookies': {
            exports: 'angular'
        },
        'finder': {
            exports: 'angular'
        },
        'layer': {
            exports: 'angular'
        },
        'material': {
            exports: 'angular'
        },
        'angular-ui-router': {
            deps: ['angular']
        },
        'md-time-picker': {
            deps: ['angular']
        },
        'angular-touch': {
            deps: ['angular']
        },
        'onHoldDirective': {
            deps: ['angular']
        },
        'angular-sanitize': {
            deps: ['angular']
        },
        'app': {
            deps: ['angular']
        },
        'routesDefault': {
            deps: ['angular']
        },
        'mainCtrl': {
            deps: ['angular']
        }
    },
    priority: [
        'jquery',
        'angular',
        'app',
        'mainCtrl'
    ],
    waitSeconds: 250
});

require([
    'jquery',
    'angular',
    'app',
    'angular-ui-router',
    'mainCtrl',
    'routesDefault'
], function ($, angular) {
    angular.element().ready(function () {
        angular.bootstrap($('#myApp'), ['mainApp'])
    })
});
