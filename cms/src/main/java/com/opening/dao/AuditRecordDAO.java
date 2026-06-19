package com.opening.dao;

import com.opening.entity.AuditRecord;
import com.opening.utils.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditRecordDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public AuditRecord findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT r.*, c.real_name AS customerName, c.customer_no AS customerNo, u.real_name AS auditorName FROM audit_record r LEFT JOIN customer c ON r.customer_id = c.id LEFT JOIN sys_user u ON r.audit_by = u.id WHERE r.id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(AuditRecord.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<AuditRecord> findByCustomerId(Long customerId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT r.*, c.real_name AS customerName, c.customer_no AS customerNo, u.real_name AS auditorName FROM audit_record r LEFT JOIN customer c ON r.customer_id = c.id LEFT JOIN sys_user u ON r.audit_by = u.id WHERE r.customer_id = ? ORDER BY r.create_time DESC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(AuditRecord.class), customerId);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<AuditRecord> findByPage(int pageNum, int pageSize, String keyword, Integer auditStatus) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT r.*, c.real_name AS customerName, c.customer_no AS customerNo, u.real_name AS auditorName FROM audit_record r LEFT JOIN customer c ON r.customer_id = c.id LEFT JOIN sys_user u ON r.audit_by = u.id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (c.real_name LIKE ? OR c.customer_no LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (auditStatus != null) {
                sql.append(" AND r.audit_status = ?");
                params.add(auditStatus);
            }

            sql.append(" ORDER BY r.create_time DESC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(AuditRecord.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String keyword, Integer auditStatus) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM audit_record r LEFT JOIN customer c ON r.customer_id = c.id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (c.real_name LIKE ? OR c.customer_no LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (auditStatus != null) {
                sql.append(" AND r.audit_status = ?");
                params.add(auditStatus);
            }

            return queryRunner.query(conn, sql.toString(), new ScalarHandler<>(), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int save(AuditRecord record) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO audit_record (customer_id, audit_type, audit_status, audit_result, audit_by, audit_time, create_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    record.getCustomerId(),
                    record.getAuditType(),
                    record.getAuditStatus(),
                    record.getAuditResult(),
                    record.getAuditBy(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(AuditRecord record) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE audit_record SET customer_id=?, audit_type=?, audit_status=?, audit_result=?, audit_by=?, audit_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    record.getCustomerId(),
                    record.getAuditType(),
                    record.getAuditStatus(),
                    record.getAuditResult(),
                    record.getAuditBy(),
                    LocalDateTime.now(),
                    record.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM audit_record WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
