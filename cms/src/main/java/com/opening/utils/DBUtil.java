package com.opening.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {

    private static DruidDataSource dataSource;
    private static final String DEFAULT_CONFIG = "db.properties";

    static {
        InputStream is = null;
        try {
            String configPath = System.getProperty("db.config");
            if (configPath != null && !configPath.trim().isEmpty()) {
                File configFile = new File(configPath);
                if (configFile.exists() && configFile.isFile()) {
                    is = new FileInputStream(configFile);
                    System.out.println("[DBUtil] Loading config from external path: " + configPath);
                } else {
                    System.out.println("[DBUtil] External config not found: " + configPath + ", fallback to classpath");
                }
            }

            if (is == null) {
                is = DBUtil.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG);
                if (is == null) {
                    throw new RuntimeException("Cannot find db.properties in classpath: " + DEFAULT_CONFIG);
                }
                System.out.println("[DBUtil] Loading config from classpath: " + DEFAULT_CONFIG);
            }

            Properties props = new Properties();
            props.load(is);

            Properties druidProps = new Properties();
            setPropertyIfExists(druidProps, "driverClassName", props, "db.driverClassName");
            setPropertyIfExists(druidProps, "url", props, "db.url");
            setPropertyIfExists(druidProps, "username", props, "db.username");
            setPropertyIfExists(druidProps, "password", props, "db.password");
            setPropertyIfExists(druidProps, "initialSize", props, "db.initialSize");
            setPropertyIfExists(druidProps, "maxActive", props, "db.maxActive");
            setPropertyIfExists(druidProps, "minIdle", props, "db.minIdle");
            setPropertyIfExists(druidProps, "maxWait", props, "db.maxWait");
            setPropertyIfExists(druidProps, "timeBetweenEvictionRunsMillis", props, "db.timeBetweenEvictionRunsMillis");
            setPropertyIfExists(druidProps, "minEvictableIdleTimeMillis", props, "db.minEvictableIdleTimeMillis");
            setPropertyIfExists(druidProps, "validationQuery", props, "db.validationQuery");
            setPropertyIfExists(druidProps, "testWhileIdle", props, "db.testWhileIdle");
            setPropertyIfExists(druidProps, "testOnBorrow", props, "db.testOnBorrow");
            setPropertyIfExists(druidProps, "testOnReturn", props, "db.testOnReturn");
            setPropertyIfExists(druidProps, "poolPreparedStatements", props, "db.poolPreparedStatements");
            setPropertyIfExists(druidProps, "maxPoolPreparedStatementPerConnectionSize", props, "db.maxPoolPreparedStatementPerConnectionSize");
            setPropertyIfExists(druidProps, "connectionInitSqls", props, "db.connectionInitSqls");

            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(druidProps);
            System.out.println("[DBUtil] DataSource initialized successfully, url=" + props.getProperty("db.url"));
        } catch (Exception e) {
            System.err.println("[DBUtil] Failed to initialize DataSource: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("初始化数据库连接池失败: " + e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    private static void setPropertyIfExists(Properties target, String targetKey, Properties source, String sourceKey) {
        String value = source.getProperty(sourceKey);
        if (value != null && !value.trim().isEmpty()) {
            target.setProperty(targetKey, value.trim());
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
