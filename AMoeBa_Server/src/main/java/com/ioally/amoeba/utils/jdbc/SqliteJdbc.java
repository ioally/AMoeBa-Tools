package com.ioally.amoeba.utils.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
@Order(1)
public class SqliteJdbc {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqliteJdbc.class);
    private static final Logger SQL_LOGGER = LoggerFactory.getLogger("ShowSQL");

    private static final String URL_PREFIX = "jdbc:sqlite:";

    @Value("${sqliteDataBsae.driver-class-name}")
    private String driverClass;

    @Value("${sqliteDataBsae.dataFilePath}")
    private String dataFilePath;

    /**
     * 批量执行sql，事务自动提交
     *
     * @param singleSql 多条sql
     * @return 执行后影响的行数
     */
    public int[] executeBatch(String[] singleSql) throws SQLException {
        int[] ints = null;
        Connection connection = getConnection();
        if (connection != null) {
            try {
                ints = executeBatch(connection, singleSql);
                connection.commit();
            } catch (SQLException e) {
                ints = null;
                LOGGER.error("executeBatch SQLException:{}", e.getMessage());
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    LOGGER.error("executeBatch rollback SQLException:{}", e1.getMessage());
                }
            } finally {
                connection.close();
            }
        }
        return ints;
    }

    /**
     * 批量执行sql，不会自动提交事务不关闭连接
     *
     * @param connection 数据库连接
     * @param singleSql  多条sql
     * @return 执行后影响的行数
     * @throws SQLException
     */
    public int[] executeBatch(Connection connection, String[] singleSql) throws SQLException {
        Statement statement = connection.createStatement();
        for (String sqlStr : singleSql) {
            SQL_LOGGER.info(sqlStr);
            statement.addBatch(sqlStr);
        }
        return statement.executeBatch();
    }

    /**
     * 执行单条更新操作的sql，自动提交事务
     *
     * @param sql 更新操作的sql
     * @return 影响的行数
     */
    public int executeUpdate(String sql) throws SQLException {
        int i = 0;
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
                i = executeUpdate(connection, sql);
                connection.commit();
            } catch (SQLException e) {
                LOGGER.error("executeUpdate SQLException", e);
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    LOGGER.error("executeUpdate rollback SQLException", e1);
                }
                throw e;
            } finally {
                connection.close();
            }
        }

        return i;
    }


    /**
     * 执行单条更新操作的sql，不自动提交事务不关闭连接
     *
     * @param connection 数据库连接
     * @param sql        更新操作的sql
     * @return 影响的行数
     * @throws SQLException
     */
    public int executeUpdate(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        SQL_LOGGER.info(sql);
        return statement.executeUpdate(sql);
    }

    public ResultSet executeQuery(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        SQL_LOGGER.info(sql);
        return statement.executeQuery(sql);
    }

    public boolean execute(String sql) throws SQLException {
        boolean b = false;
        Connection connection = getConnection();
        if (connection != null) {
            try {
                b = execute(connection, sql);
                connection.commit();
            } catch (SQLException e) {
                b = false;
                LOGGER.error("execute SQLException:{}", e.getMessage());
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    LOGGER.error("execute rollback SQLException:{}", e.getMessage());
                }
                throw e;
            } finally {
                connection.close();
            }
        }
        return b;
    }

    public boolean execute(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        SQL_LOGGER.info(sql);
        return statement.execute(sql);
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found:" + driverClass);
        }
        Connection connection = DriverManager.getConnection(URL_PREFIX + dataFilePath);
        connection.setAutoCommit(false);
        return connection;
    }

}
