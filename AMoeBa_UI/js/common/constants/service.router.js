/**
 * 配置声明后端服务的ip，以及各个服务的url
 */
define({
    // 后端服务器ip，若前后端部署在同一台机器上，此ip设置为空字符即可
    ip: "",

    baseUrl: "/amoebaApi",

    // 后端服务的请求地址（格式为 名称：地址 ），新增配置请添加该服务的功能描述注释
    backend: {
        generateKey: "/security/generateKey", // 生成密钥
        verifyKey: "/security/verifyKey", // 密钥验证
        getMenu: "/menu/getMenu",// 查询菜单
        login: "/login",// 登录
        logout: "/logout",// 登出
        getAllLog: "/getAllLog", // 获取当年所有日志
        getLog: "/getLog", // 获取指定时间的日志
        queryLogin: "/queryLogin", // 查询登录状态
        batchDelete: "/batchDelete", // 批量删除日志
        batchSave: "/batchSave", // 批量保存日志
        configSys: "/config/sys", // 查询系统配置信息
        addFeedBack: "/addFeedBack", // 新增反馈信息
        checkAccess: "/checkAccess", // 校验是否有权限访问指定页面
        sqlExecute: "/sqlExecute", // 执行sql
    },

    /**
     * 获取基础url
     * @returns {string} 基础url
     */
    getBaseUrl: function () {
        return this.baseUrl;
    },

    /**
     * 获取后端服务ip
     * @returns {string} 后端服务的ip
     */
    getIp: function () {
        return this.ip;
    },

    /**
     * 获取指定名称的url
     * @param urlName url名称
     * @returns {string} url
     */
    getUrl: function (urlName) {
        if (!urlName) {
            throw 'Invalid urlName: ' + urlName;
        }
        let backendElement = this.backend[urlName];
        if (backendElement == undefined) {
            throw 'Not find url of urlName: ' + urlName;
        }
        return backendElement;
    }
});