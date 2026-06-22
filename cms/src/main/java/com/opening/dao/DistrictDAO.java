package com.opening.dao;

import com.opening.entity.District;
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

public class DistrictDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public District findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM district WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(District.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public District findByDistrictCode(String districtCode) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM district WHERE district_code = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(District.class), districtCode);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<District> findByCityId(Long cityId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM district WHERE city_id = ? ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(District.class), cityId);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<District> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM district ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(District.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<District> findByPage(int pageNum, int pageSize, String districtName, Long cityId, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT * FROM district WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (districtName != null && !districtName.trim().isEmpty()) {
                sql.append(" AND district_name LIKE ?");
                params.add("%" + districtName.trim() + "%");
            }

            if (cityId != null) {
                sql.append(" AND city_id = ?");
                params.add(cityId);
            }

            if (status != null) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            sql.append(" ORDER BY sort ASC, id ASC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(District.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String districtName, Long cityId, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM district WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (districtName != null && !districtName.trim().isEmpty()) {
                sql.append(" AND district_name LIKE ?");
                params.add("%" + districtName.trim() + "%");
            }

            if (cityId != null) {
                sql.append(" AND city_id = ?");
                params.add(cityId);
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

    public int save(District district) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO district (district_code, district_name, city_id, sort, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    district.getDistrictCode(),
                    district.getDistrictName(),
                    district.getCityId(),
                    district.getSort(),
                    district.getStatus(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(District district) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE district SET district_code=?, district_name=?, city_id=?, sort=?, status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    district.getDistrictCode(),
                    district.getDistrictName(),
                    district.getCityId(),
                    district.getSort(),
                    district.getStatus(),
                    LocalDateTime.now(),
                    district.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM district WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long countByCityId(Long cityId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM district WHERE city_id = ?";
            return queryRunner.query(conn, sql, new ScalarHandler<>(), cityId);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
