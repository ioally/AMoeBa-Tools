package com.ioally.amoeba.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioally.amoeba.dto.AMoeBaLogDto;
import com.ioally.amoeba.dto.BaseRequestDto;
import com.ioally.amoeba.utils.other.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿米巴会话类
 */
public class AMoeBaSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(AMoeBaSession.class);

    private String id;

    /**
     * 操作类型：新增日志
     */
    public static final String ADD_ACTION = "add";
    /**
     * 操作类型：删除日志
     */
    public static final String DELETE_ACTION = "delete";
    /**
     * 操作类型：更新日志
     */
    public static final String UPDATE_ACTION = "update";

    /**
     * 阿米巴地址
     */
    private String address;

    /**
     * Cookie
     */
    private CookieStore cookieStore;

    /**
     * 用户id
     */
    private String userEmployeeId;

    /**
     * 用户名-工号
     */
    private String userEmployeeName;

    /**
     * 用户的工号
     */
    private String userId;

    /**
     * 是否已经登陆
     */
    private boolean isLogin;

    private boolean isDeleteing;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public AMoeBaSession(String address) {
        this.address = address;
        cookieStore = new BasicCookieStore();
        this.id = UUID.randomUUID().toString();
    }

    /**
     * 登陆
     *
     * @param userName 用户名
     * @param password 密码
     * @return 登陆结果
     * @throws IOException
     */
    public boolean login(String userName, String password) throws Exception {
        clear();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute("http.cookie-store", this.cookieStore);
        HttpPost httppost = new HttpPost(this.address + "/login?year=&month=");
        StringEntity reqEntity = new StringEntity("loginName=" + userName + "&password=" + password);
        reqEntity.setContentType("application/x-www-form-urlencoded");
        httppost.setEntity(reqEntity);
        HttpResponse response = HttpClientBuilder.create().build().execute(httppost);
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; ++i) {
            Header h = headers[i];
            String name = h.getName();
            String value = h.getValue();
            if ("Set-Cookie".equalsIgnoreCase(name)) {
                String[] strs = value.split(";");
                String[] s = strs;
                for (int j = 0; j < strs.length; ++j) {
                    String str = s[j];
                    String[] cookies = str.split("=");
                    this.cookieStore.addCookie(new BasicClientCookie(cookies[0], cookies.length >= 2 ? cookies[1] : ""));
                }
            }
        }
        HttpEntity entity = response.getEntity();
        String responseHTML = IOUtils.toString(entity.getContent(), Charset.forName("UTF-8"));
        Document document = Jsoup.parse(responseHTML);
        Element body = document.body();
        Elements findUserCentElements = body.getElementsByAttributeValue("class", "user_cent");
        if (findUserCentElements.size() > 0) {
            Element userCentElement = body.getElementsByAttributeValue("class", "user_cent").get(0);
            Elements findUserNameElements = userCentElement.getElementsByAttributeValue("class", "name");
            if (findUserNameElements.size() > 0) {
                Element userNameElement = findUserNameElements.get(0);
                userEmployeeName = userNameElement.html();
                if (responseHTML.indexOf("userEmployeeId = \"") >= 0) {
                    this.userEmployeeId = (String) responseHTML.subSequence(responseHTML.indexOf("\"", responseHTML.indexOf("userEmployeeId = \"") + 1) + 1, responseHTML.indexOf("\"", responseHTML.indexOf("\"", responseHTML.indexOf("userEmployeeId = \"") + 1) + 1));
                }
            }
        }
        if (StringUtils.isNotEmpty(userEmployeeId)) {
            LOGGER.info("登陆成功，用户名:{},用户id:{}", userEmployeeName, userEmployeeId);
            userId = userName;
            return this.isLogin = true;
        } else {
            this.isLogin = false;
            throw new Exception("用户名或密码错误，请重新登录 !");
        }
    }

    /**
     *
     *
     */
    public void adminLogin(String userName) {
        this.isLogin = true;
        this.userEmployeeId = "AMoeBa Tools Administrator";
        this.userEmployeeName = "系统管理员-"+userName;
    }


    /**
     * 查询当年所有的日志
     *
     * @return AMoeBaLogDtos
     */
    public Map<String, Collection<AMoeBaLogDto>> getAmoebaLogItems() throws IOException {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        try {
            Date start = sdf.parse(year + "-01-01");
            Date end = sdf.parse(year + "-12-31");
            return getAmoebaLogItems(start, end);
        } catch (ParseException e) {
            LOGGER.error("日期转换异常", e);
        }
        return null;
    }

    /**
     * 查询指定时间区间的日志
     *
     * @param start Date开始时间
     * @param end   Date结束时间
     * @return AMoeBaLogDtos
     */
    public Map<String, Collection<AMoeBaLogDto>> getAmoebaLogItems(Date start, Date end) throws IOException, ParseException {
        String startStr = sdf.format(start);
        String endStr = sdf.format(end);
        return getAmoebaLogItems(startStr, endStr);
    }

    /**
     * 查询指定时间区间的日志
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return AMoeBaLogDtos
     */
    public Map<String, Collection<AMoeBaLogDto>> getAmoebaLogItems(String start, String end) throws IOException, ParseException {
        StringBuilder path = new StringBuilder(this.address);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = sdf.parse(end);
        // 由于阿米巴系统默认是查询不包含结束日期，所以这里把结束日期向后延一天
        Date date = DateUtil.calculateDate(DateUtil.DAY_OF_YEAR, endDate, 1);
        end = sdf.format(date);
        path.append("/scheduler/scheduler/initScheduler?userId=&ra=0.3400165785011644&timeshift=-480&from=")
                .append(start).append("&to=").append(end);
        String res = null;
        try {
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI(path.toString()));
            httpGet.setHeader("Cookie", getCookieStr());
            HttpResponse response = HttpClientBuilder.create().build().execute(httpGet);
            res = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
        } catch (Exception e) {
            LOGGER.error("查询日志失败!", e);
        }
        List<AMoeBaLogDto> aMoeBaLogDtos = null;

        aMoeBaLogDtos = objectMapper.readValue(res, new TypeReference<List<AMoeBaLogDto>>() {
        });

        TreeSet<AMoeBaLogDto> resAmSet = new TreeSet<AMoeBaLogDto>((o1, o2) -> o1.getDateStart().compareTo(o2.getDateStart()) != 0 ? o1.getDateStart().compareTo(o2.getDateStart()) : -1);
        TreeSet<AMoeBaLogDto> resPmSet = new TreeSet<AMoeBaLogDto>((o1, o2) -> o1.getDateStart().compareTo(o2.getDateStart()) != 0 ? o1.getDateStart().compareTo(o2.getDateStart()) : -1);
        Map<String, Collection<AMoeBaLogDto>> resMap = new HashMap<>();
        if (aMoeBaLogDtos != null && !aMoeBaLogDtos.isEmpty()) {
            SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            aMoeBaLogDtos.forEach(aMoeBaLogDto -> {
                Date dateStart = aMoeBaLogDto.getDateStart();
                Date dateEnd = aMoeBaLogDto.getDateEnd();
                aMoeBaLogDto.setDateRange("[" + _sdf.format(dateStart) + "] - [" + _sdf.format(dateEnd) + "]");
                if (DateUtil.isAM(dateStart)) {
                    resAmSet.add(aMoeBaLogDto);
                } else {
                    resPmSet.add(aMoeBaLogDto);
                }
            });
            resMap.put("am", resAmSet);
            resMap.put("pm", resPmSet);
        }
        return resMap;
    }

    /**
     * 提交日志操作
     *
     * @param aMoeBaLogDto 要操作的日志内容
     * @param type         操作类型
     * @return
     */
    public boolean executeAMoeBaLog(AMoeBaLogDto aMoeBaLogDto, String type) {
        if (!ADD_ACTION.equals(type) && !DELETE_ACTION.equals(type) && !UPDATE_ACTION.equals(type)) {
            throw new NullPointerException("操作类型不正确！");
        }
        String path = this.address + "/scheduler/scheduler/" + type + "SchedulerEvent";
        if (aMoeBaLogDto.getId() == null) {
            aMoeBaLogDto.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
        }
        aMoeBaLogDto.setUserEmployeeId(this.userEmployeeId);
        aMoeBaLogDto.setIsSync("0");
        String responseStr = null;
        try {
            String httpString = aMoeBaLogDto.toHttpString();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(path));
            httpPost.setHeader("Cookie", getCookieStr());
            StringEntity reqEntity = new StringEntity(httpString);
            reqEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(reqEntity);
            // 防止批量任务session有效期内无法执行完成，每次提交请求前刷新session时间
            Session session = BaseRequestDto.sessionThreadLocal.get();
            if (session != null) {
                session.setLastTime(new Date());
            }
            LOGGER.info("{}请求参数:{}", type, httpString);
            HttpResponse response = HttpClientBuilder.create().build().execute(httpPost);
            responseStr = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            LOGGER.info("{}操作成功:{}", type, responseStr);
            return true;
        } catch (Exception e) {
            LOGGER.error("{}操作失败,请检查日志内容是否有误：{}", type, aMoeBaLogDto, e);
            return false;
        }
    }

    /**
     * 获取cookie字符串
     *
     * @return CookieStr
     */
    public String getCookieStr() {
        StringBuilder cookieStr = new StringBuilder();
        if (cookieStore != null) {
            List<Cookie> cookies = cookieStore.getCookies();
            if (cookies != null && cookies.size() > 0) {
                cookieStore.getCookies().forEach(cookie ->
                        cookieStr.append(cookie.getName()).append("=").append(cookie.getValue()).append(";")
                );
            }
        }
        return cookieStr.toString();
    }

    /**
     * 清除session的cookie和登陆信息
     */
    public void clear() {
        cookieStore.clear();
        userEmployeeId = null;
        userEmployeeName = null;
        isLogin = false;
    }

    /**
     * 校验是否登陆，未登录则抛出异常
     */
    private void checkIsLogin() {
        if (!isLogin
                || StringUtils.isEmpty(userEmployeeId)
                || StringUtils.isEmpty(userEmployeeName)
                || StringUtils.isEmpty(getCookieStr())) {
            throw new RuntimeException("请登录后再进行相关操作！");
        }
    }

    /**
     * 比较两个实例是否相等，id一致则认为两个实例相同
     *
     * @param o 待比较的实例
     * @return 比较结果
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AMoeBaSession that = (AMoeBaSession) o;
        return Objects.equals(id, that.id);
    }


    /**
     * id的hash值
     *
     * @return hash值
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getUserEmployeeId() {
        return userEmployeeId;
    }

    public String getUserEmployeeName() {
        return userEmployeeName;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isDeleteing() {
        return isDeleteing;
    }

    public void setDeleteing(boolean deleteing) {
        isDeleteing = deleteing;
    }
}
