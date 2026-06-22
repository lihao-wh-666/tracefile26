package com.opening.dao;

import com.opening.entity.City;
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

public class CityDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public City findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM city WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(City.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public City findByCityCode(String cityCode) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM city WHERE city_code = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(City.class), cityCode);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<City> findByProvinceId(Long provinceId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM city WHERE province_id = ? ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(City.class), provinceId);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<City> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM city ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(City.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<City> findByPage(int pageNum, int pageSize, String cityName, Long provinceId, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT * FROM city WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (cityName != null && !cityName.trim().isEmpty()) {
                sql.append(" AND city_name LIKE ?");
                params.add("%" + cityName.trim() + "%");
            }

            if (provinceId != null) {
                sql.append(" AND province_id = ?");
                params.add(provinceId);
            }

            if (status != null) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            sql.append(" ORDER BY sort ASC, id ASC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(City.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String cityName, Long provinceId, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM city WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (cityName != null && !cityName.trim().isEmpty()) {
                sql.append(" AND city_name LIKE ?");
                params.add("%" + cityName.trim() + "%");
            }

            if (provinceId != null) {
                sql.append(" AND province_id = ?");
                params.add(provinceId);
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

    public int save(City city) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO city (city_code, city_name, province_id, sort, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    city.getCityCode(),
                    city.getCityName(),
                    city.getProvinceId(),
                    city.getSort(),
                    city.getStatus(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(City city) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE city SET city_code=?, city_name=?, province_id=?, sort=?, status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    city.getCityCode(),
                    city.getCityName(),
                    city.getProvinceId(),
                    city.getSort(),
                    city.getStatus(),
                    LocalDateTime.now(),
                    city.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM city WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long countByProvinceId(Long provinceId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM city WHERE province_id = ?";
            return queryRunner.query(conn, sql, new ScalarHandler<>(), provinceId);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
