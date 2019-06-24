package com.ioally.amoeba.dao;

import com.ioally.amoeba.dto.FeedBackDto;
import com.ioally.amoeba.dto.LoginDto;
import com.ioally.amoeba.dto.MenuInfoDto;
import com.ioally.amoeba.dto.QueryResultDto;
import com.ioally.amoeba.utils.jdbc.SqliteJdbc;
import com.ioally.amoeba.utils.other.Str;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 操作sqlite数据库的实现
 */
@Repository("sqliteDao")
public class SqliteDao {

    @Autowired
    private SqliteJdbc sqliteJdbc;

    /**
     * 查询指定时间的假日
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     * @throws SQLException
     */
    public List<String> getHoliday(String startDate, String endDate) throws SQLException {
        List<String> holidayDates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        String nowMonthStr = Str.getCurNo((long) nowMonth, 2, "0");
        String sql = "select holidayDate from HolidayPlan where holidayId like '" + nowYear + nowMonthStr + "%' and holidayDate >= '" + startDate + "' and holidayDate <= '" + endDate + "'";
        Connection connection = sqliteJdbc.getConnection();
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, sql);
            while (resultSet.next()) {
                String holidayDate = resultSet.getString("holidayDate");
                holidayDates.add(holidayDate);
            }
        } finally {
            connection.close();
        }

        return holidayDates;
    }

    /**
     * 查询指定权限的菜单信息
     *
     * @param role 权限代码
     * @return
     * @throws SQLException
     */
    public List<MenuInfoDto> getMenuInfoDtos(String role) throws SQLException {
        List<MenuInfoDto> menuInfoDtos = new ArrayList<>();
        Connection connection = sqliteJdbc.getConnection();
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, "select menuName,targetURL,menuIcon from sysMenu where role='" + role + "' and validFlag ='1' order by menuId");
            while (resultSet.next()) {
                MenuInfoDto menuInfoDto = new MenuInfoDto();
                String menuName = resultSet.getString("menuName");
                String targetURL = resultSet.getString("targetURL");
                String menuIcon = resultSet.getString("menuIcon");
                menuInfoDto.setName(menuName);
                menuInfoDto.setTarget(targetURL);
                menuInfoDto.setIcon(menuIcon);
                menuInfoDtos.add(menuInfoDto);
            }
        } finally {
            connection.close();
        }
        return menuInfoDtos;
    }

    /**
     * 获取用户权限
     *
     * @param userName 用户名称（工号）
     * @return
     * @throws SQLException
     */
    public String getUserRole(String userName) throws SQLException {
        Connection connection = sqliteJdbc.getConnection();
        String role = null;
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, "select role from userInfo where id='" + userName + "'");
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
        } finally {
            connection.close();
        }
        return role;
    }

    /**
     * 添加一个指定权限的用户信息
     *
     * @param loginDto 用户信息
     * @param role     权限等级
     */
    public int addUser(LoginDto loginDto, String role) throws SQLException {
        String userName = loginDto.getUserName();
        String passWord = loginDto.getPassWord();
        String userEmployeeName = loginDto.getUserEmployeeName();
        String key = StringUtils.isEmpty(loginDto.getKey()) ? "" : loginDto.getKey();
        String sql = "insert into userInfo (id, userName, passWord, validFlag, role, key) " +
                "values ('" + userName + "', '" + userEmployeeName.split("-")[0] + "', '" + passWord + "', '1', '" + role + "', '" + key + "')";
        return sqliteJdbc.executeUpdate(sql);
    }

    /**
     * 按照主键查询用户信息
     *
     * @param userName 用户姓名（工号）
     * @return
     */
    public LoginDto queryUserByPK(String userName) throws SQLException {
        String sql = "select id, userName, key from userInfo where id='" + userName + "'";
        Connection connection = sqliteJdbc.getConnection();
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, sql);
            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String userCName = resultSet.getString("userName");
                String key = resultSet.getString("key");
                LoginDto loginDto = new LoginDto();
                loginDto.setUserName(id);
                loginDto.setUserEmployeeName(userCName + "-" + id);
                loginDto.setKey(key);
                return loginDto;
            }
        } finally {
            connection.close();
        }
        return null;
    }

    /**
     * 新增反馈信息
     *
     * @param feedBackDto 反馈信息
     * @return 更新行数
     * @throws SQLException
     */
    public int addFeedBack(FeedBackDto feedBackDto) throws SQLException {
        String sql = "insert into feedback (id, content, createBy, createTime, email, isSendFlag) " +
                "values ('" + feedBackDto.getId() + "', '"
                + feedBackDto.getContent() + "', '"
                + feedBackDto.getCreateBy() + "', '"
                + feedBackDto.getCreateTime() + "', '"
                + (StringUtils.isEmpty(feedBackDto.getEmail()) ? "" : feedBackDto.getEmail()) + "', '"
                + feedBackDto.getIsSendFlag() + "')";
        return sqliteJdbc.executeUpdate(sql);
    }

    /**
     * 校验用户是否有指定路径的访问权限
     *
     * @param userName  用户名
     * @param targetUrl 目标路径
     * @return
     * @throws SQLException
     */
    public long checkAccess(String userName, String targetUrl) throws SQLException {
        Connection connection = sqliteJdbc.getConnection();
        long sum = 0;
        try {
            String sql = "select count(1) from UserInfo u, SysMenu m"
                    + " where u.id = '" + userName + "'"
                    + " and u.role = m.role"
                    + " and m.targetUrl = '" + targetUrl + "'";
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, sql);
            if (resultSet.next()) {
                sum = resultSet.getLong(1);
            }
        } finally {
            connection.close();
        }
        return sum;
    }

    /**
     * 执行一条update语句
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public int executeUpdate(String sql) throws SQLException {
        return sqliteJdbc.executeUpdate(sql);
    }

    /**
     * 执行一条query语句
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public QueryResultDto executeQuery(String sql) throws SQLException {
        QueryResultDto queryResultDto = new QueryResultDto();
        Connection connection = sqliteJdbc.getConnection();
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String tableName = metaData.getTableName(1);
            queryResultDto.setTableName(tableName);
            List<Map<String, Object>> rowDate = new ArrayList<>();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames.add(columnName);
            }
            queryResultDto.setColumnNames(columnNames);
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                rowDate.add(row);
            }
            queryResultDto.setRowDate(rowDate);
        } finally {
            connection.close();
        }
        return queryResultDto;
    }
}
