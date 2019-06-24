define([
    'angular'
], function (angular) {
    angular.module('$ngOnhold', [])
        .directive("ngOnhold", ["$swipe", "$parse", function ($swipe, $parse) {
        //长按触发事件需要的时间
        var ON_HOLD_TIMEMS = 500;

        return function (scope, element, attr) {

            var onholdHandler = $parse(attr["ngOnhold"]);
            var run;

            $swipe.bind(element, {
                'start': function (coords, event) {
                    run = setTimeout(function () {
                        scope.$apply(function () {
                            element.triggerHandler("onhold");
                            onholdHandler(scope, {$event: event});
                        });
                    }, ON_HOLD_TIMEMS);
                },
                'cancel': function (event) {
                    if (run) clearTimeout(run);
                },
                'move': function (event) {
                    if (run) clearTimeout(run);
                },
                'end': function (coords, event) {
                    if (run) clearTimeout(run);
                }
            }, ['touch', 'mouse']);
        }
    }]);
});