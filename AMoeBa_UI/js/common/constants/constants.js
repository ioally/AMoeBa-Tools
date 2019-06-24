/**
 * 用于声明系统级别的常量或方法（按照常量值的类型区分）
 */
define({
    // 正表达式类型常量
    reg: {
        // 日期格式
        date: 'yyyy-MM-dd',
        // 时间格式
        time: 'HH:mm',
        // 日期选择器的日期格式
        datePicker: 'YYYY-MM-DD',
    },
    // 字符类型常量
    str: {
        // 系统主页路由
        homeTarget: 'welcome',
    },
    // 数组类型常量
    arr: {
        // 日期选择器的月份显示
        shortMonths: [
            '1月', '2月', '3月', '4月', '5月', '6月',
            '7月', '8月', '9月', '10月', '11月', '12月'
        ],
        // 日期选择起的周显示
        shortDays: ['日', '一', '二', '三', '四', '五', '六'],
    },
    // 布尔类型常量
    boolean: {},
    // 数字类型常量
    number: {},
    // 复合对象类型常量
    obj: {
        // 上午上班时间
        amStartDate: new Date(2018, 0, 1, 8),
        // 上午下班时间
        amEndDate: new Date(2018, 0, 1, 12),
        // 下午上班时间
        pmStartDate: new Date(2018, 0, 1, 14, 30),
        // 下午下班时间
        pmEndDate: new Date(2018, 0, 1, 18, 30),
    }
});