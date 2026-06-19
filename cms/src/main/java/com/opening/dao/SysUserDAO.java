package com.opening.dao;

import com.opening.entity.SysUser;
import com.opening.utils.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SysUserDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public SysUser findByUsername(String username) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM sys_user WHERE username = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(SysUser.class), username);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public SysUser findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM sys_user WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(SysUser.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<SysUser> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM sys_user ORDER BY create_time DESC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(SysUser.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<SysUser> findByPage(int pageNum, int pageSize) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            String sql = "SELECT * FROM sys_user ORDER BY create_time DESC LIMIT ?, ?";
            return queryRunner.query(conn, sql, new BeanListHandler<>(SysUser.class), offset, pageSize);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM sys_user";
            return queryRunner.query(conn, sql, new ScalarHandler<>());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int save(SysUser user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO sys_user (username, password, real_name, role, status, phone, email, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getRealName(),
                    user.getRole(),
                    user.getStatus(),
                    user.getPhone(),
                    user.getEmail(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(SysUser user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE sys_user SET username=?, password=?, real_name=?, role=?, status=?, phone=?, email=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getRealName(),
                    user.getRole(),
                    user.getStatus(),
                    user.getPhone(),
                    user.getEmail(),
                    LocalDateTime.now(),
                    user.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int updateLoginInfo(Long id, String ip) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE sys_user SET last_login_time=?, last_login_ip=? WHERE id=?";
            return queryRunner.update(conn, sql, LocalDateTime.now(), ip, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM sys_user WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
