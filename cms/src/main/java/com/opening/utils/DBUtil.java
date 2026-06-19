package com.opening.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {

    private static DruidDataSource dataSource;

    static {
        try {
            InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties props = new Properties();
            props.load(is);

            Properties druidProps = new Properties();
            druidProps.setProperty("driverClassName", props.getProperty("db.driverClassName"));
            druidProps.setProperty("url", props.getProperty("db.url"));
            druidProps.setProperty("username", props.getProperty("db.username"));
            druidProps.setProperty("password", props.getProperty("db.password"));
            druidProps.setProperty("initialSize", props.getProperty("db.initialSize"));
            druidProps.setProperty("maxActive", props.getProperty("db.maxActive"));
            druidProps.setProperty("minIdle", props.getProperty("db.minIdle"));
            druidProps.setProperty("maxWait", props.getProperty("db.maxWait"));
            druidProps.setProperty("timeBetweenEvictionRunsMillis", props.getProperty("db.timeBetweenEvictionRunsMillis"));
            druidProps.setProperty("minEvictableIdleTimeMillis", props.getProperty("db.minEvictableIdleTimeMillis"));
            druidProps.setProperty("validationQuery", props.getProperty("db.validationQuery"));
            druidProps.setProperty("testWhileIdle", props.getProperty("db.testWhileIdle"));
            druidProps.setProperty("testOnBorrow", props.getProperty("db.testOnBorrow"));
            druidProps.setProperty("testOnReturn", props.getProperty("db.testOnReturn"));
            druidProps.setProperty("poolPreparedStatements", props.getProperty("db.poolPreparedStatements"));
            druidProps.setProperty("maxPoolPreparedStatementPerConnectionSize", props.getProperty("db.maxPoolPreparedStatementPerConnectionSize"));

            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(druidProps);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库连接池失败", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    public static DruidDataSource getDataSource() {
        return dataSource;
    }
}
