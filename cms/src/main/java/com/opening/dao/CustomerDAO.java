package com.opening.dao;

import com.opening.entity.Customer;
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

public class CustomerDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public Customer findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM customer WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Customer.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public Customer findByCustomerNo(String customerNo) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM customer WHERE customer_no = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Customer.class), customerNo);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public Customer findByIdNo(String idNo) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM customer WHERE id_no = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Customer.class), idNo);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Customer> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM customer ORDER BY create_time DESC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Customer.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Customer> findByPage(int pageNum, int pageSize, String keyword, Integer auditStatus) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT * FROM customer WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (real_name LIKE ? OR customer_no LIKE ? OR phone LIKE ? OR id_no LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (auditStatus != null) {
                sql.append(" AND audit_status = ?");
                params.add(auditStatus);
            }

            sql.append(" ORDER BY create_time DESC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(Customer.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String keyword, Integer auditStatus) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM customer WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (real_name LIKE ? OR customer_no LIKE ? OR phone LIKE ? OR id_no LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (auditStatus != null) {
                sql.append(" AND audit_status = ?");
                params.add(auditStatus);
            }

            return queryRunner.query(conn, sql.toString(), new ScalarHandler<>(), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long countByAuditStatus(Integer auditStatus) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM customer WHERE audit_status = ?";
            return queryRunner.query(conn, sql, new ScalarHandler<>(), auditStatus);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int save(Customer customer) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO customer (customer_no, real_name, id_type, id_no, gender, birth_date, phone, email, address, occupation, nationality, status, audit_status, id_verified, face_verified, remark, create_by, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    customer.getCustomerNo(),
                    customer.getRealName(),
                    customer.getIdType(),
                    customer.getIdNo(),
                    customer.getGender(),
                    customer.getBirthDate(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress(),
                    customer.getOccupation(),
                    customer.getNationality(),
                    customer.getStatus(),
                    customer.getAuditStatus(),
                    customer.getIdVerified(),
                    customer.getFaceVerified(),
                    customer.getRemark(),
                    customer.getCreateBy(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(Customer customer) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE customer SET customer_no=?, real_name=?, id_type=?, id_no=?, gender=?, birth_date=?, phone=?, email=?, address=?, occupation=?, nationality=?, status=?, audit_status=?, id_verified=?, face_verified=?, remark=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    customer.getCustomerNo(),
                    customer.getRealName(),
                    customer.getIdType(),
                    customer.getIdNo(),
                    customer.getGender(),
                    customer.getBirthDate(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress(),
                    customer.getOccupation(),
                    customer.getNationality(),
                    customer.getStatus(),
                    customer.getAuditStatus(),
                    customer.getIdVerified(),
                    customer.getFaceVerified(),
                    customer.getRemark(),
                    LocalDateTime.now(),
                    customer.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int updateAuditStatus(Long id, Integer auditStatus, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE customer SET audit_status=?, status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql, auditStatus, status, LocalDateTime.now(), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM customer WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
