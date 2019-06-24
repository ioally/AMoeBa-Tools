define([
    'jquery',
    'angular',
    'angular-couch-potato',
    'moment',
    'constants',
    'finder',
    'material',
    'angular-animate',
    'angular-aria',
    'angular-messages',
    'angular-material',
    'md-time-picker',
    'layer',
    'angular-cookies',
    'angular-touch',
    'onHoldDirective',
    'angular-sanitize'
], function ($, angular, couchPotato, moment, constants) {
    'use strict';
    var app = angular.module('mainApp', [
        'scs.couch-potato',
        'ui.router',
        'finder',
        'ngMessages',
        'ngMaterial',
        'ngAria',
        'md.time.picker',
        '$$layer',
        'ngCookies',
        'ngTouch',
        '$ngOnhold',
        'ngSanitize'
    ]);

    couchPotato.configureApp(app);

    app.config(function ($mdDateLocaleProvider) {

        $mdDateLocaleProvider.shortMonths = constants.arr.shortMonths;

        $mdDateLocaleProvider.shortDays = constants.arr.shortDays;

        $mdDateLocaleProvider.formatDate = function(date) {
            return date ? moment(date).format(constants.reg.datePicker) : '';
        };

        $mdDateLocaleProvider.parseDate = function(dateString) {
            let m = moment(dateString, constants.reg.datePicker, true);
            return m.isValid() ? m.toDate() : new Date(NaN);
        };
    });


    /**
     * @desc 主模块的运行块
     */
    app.run(['$couchPotato', '$rootScope', '$timeout',
        function ($couchPotato, $rootScope, $timeout) {
            app.lazy = $couchPotato;//懒装载
            // MDL组件中依赖js的效果，需要动态更新注册组件，否则没有动画效果
            $rootScope.$on('$viewContentLoaded', () => {
                $timeout(() => {
                    componentHandler.upgradeAllRegistered();
                })
            });
        }
    ]);
    return app;
});