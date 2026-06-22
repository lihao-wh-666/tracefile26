package com.opening.dao;

import com.opening.entity.Province;
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

public class ProvinceDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public Province findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM province WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Province.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public Province findByProvinceCode(String provinceCode) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM province WHERE province_code = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Province.class), provinceCode);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Province> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM province ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Province.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Province> findByPage(int pageNum, int pageSize, String provinceName, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT * FROM province WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (provinceName != null && !provinceName.trim().isEmpty()) {
                sql.append(" AND province_name LIKE ?");
                params.add("%" + provinceName.trim() + "%");
            }

            if (status != null) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            sql.append(" ORDER BY sort ASC, id ASC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(Province.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String provinceName, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM province WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (provinceName != null && !provinceName.trim().isEmpty()) {
                sql.append(" AND province_name LIKE ?");
                params.add("%" + provinceName.trim() + "%");
            }

            if (status != null) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            return queryRunner.query(conn, sql.toString(), new ScalarHandler<>(), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int save(Province province) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO province (province_code, province_name, sort, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    province.getProvinceCode(),
                    province.getProvinceName(),
                    province.getSort(),
                    province.getStatus(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(Province province) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE province SET province_code=?, province_name=?, sort=?, status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    province.getProvinceCode(),
                    province.getProvinceName(),
                    province.getSort(),
                    province.getStatus(),
                    LocalDateTime.now(),
                    province.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM province WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
