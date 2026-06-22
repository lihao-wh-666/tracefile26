package com.opening.dao;

import com.opening.entity.Region;
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

public class RegionDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public Region findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM region WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Region.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public Region findByRegionCode(String regionCode) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM region WHERE region_code = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Region.class), regionCode);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Region> findByParentId(Long parentId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM region WHERE parent_id = ? ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Region.class), parentId);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Region> findByLevel(Integer level) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM region WHERE level = ? ORDER BY sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Region.class), level);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Region> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM region ORDER BY level ASC, sort ASC, id ASC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Region.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Region> findByPage(int pageNum, int pageSize, String regionName, Integer level, Long parentId, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT * FROM region WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (regionName != null && !regionName.trim().isEmpty()) {
                sql.append(" AND region_name LIKE ?");
                params.add("%" + regionName.trim() + "%");
            }

            if (level != null) {
                sql.append(" AND level = ?");
                params.add(level);
            }

            if (parentId != null) {
                sql.append(" AND parent_id = ?");
                params.add(parentId);
            }

            if (status != null) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            sql.append(" ORDER BY level ASC, sort ASC, id ASC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(Region.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String regionName, Integer level, Long parentId, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM region WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (regionName != null && !regionName.trim().isEmpty()) {
                sql.append(" AND region_name LIKE ?");
                params.add("%" + regionName.trim() + "%");
            }

            if (level != null) {
                sql.append(" AND level = ?");
                params.add(level);
            }

            if (parentId != null) {
                sql.append(" AND parent_id = ?");
                params.add(parentId);
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

    public int save(Region region) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO region (region_code, region_name, level, parent_id, sort, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    region.getRegionCode(),
                    region.getRegionName(),
                    region.getLevel(),
                    region.getParentId(),
                    region.getSort(),
                    region.getStatus(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(Region region) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE region SET region_code=?, region_name=?, level=?, parent_id=?, sort=?, status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    region.getRegionCode(),
                    region.getRegionName(),
                    region.getLevel(),
                    region.getParentId(),
                    region.getSort(),
                    region.getStatus(),
                    LocalDateTime.now(),
                    region.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM region WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long countByParentId(Long parentId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM region WHERE parent_id = ?";
            return queryRunner.query(conn, sql, new ScalarHandler<>(), parentId);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
