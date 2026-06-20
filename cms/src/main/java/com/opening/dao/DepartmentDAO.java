package com.opening.dao;

import com.opening.entity.Department;
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

public class DepartmentDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public Department findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM department WHERE id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Department.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public Department findByDeptNo(String deptNo) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM department WHERE dept_no = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Department.class), deptNo);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Department> findAll() throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM department ORDER BY create_time DESC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Department.class));
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Department> findByPage(int pageNum, int pageSize, String deptName, String province, String city, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT * FROM department WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (deptName != null && !deptName.trim().isEmpty()) {
                sql.append(" AND dept_name LIKE ?");
                params.add("%" + deptName.trim() + "%");
            }

            if (province != null && !province.trim().isEmpty()) {
                sql.append(" AND province = ?");
                params.add(province.trim());
            }

            if (city != null && !city.trim().isEmpty()) {
                sql.append(" AND city = ?");
                params.add(city.trim());
            }

            if (status != null) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            sql.append(" ORDER BY create_time DESC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(Department.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String deptName, String province, String city, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM department WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (deptName != null && !deptName.trim().isEmpty()) {
                sql.append(" AND dept_name LIKE ?");
                params.add("%" + deptName.trim() + "%");
            }

            if (province != null && !province.trim().isEmpty()) {
                sql.append(" AND province = ?");
                params.add(province.trim());
            }

            if (city != null && !city.trim().isEmpty()) {
                sql.append(" AND city = ?");
                params.add(city.trim());
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

    public int save(Department department) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO department (dept_no, dept_name, address, phone, introduction, province, city, status, create_by, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    department.getDeptNo(),
                    department.getDeptName(),
                    department.getAddress(),
                    department.getPhone(),
                    department.getIntroduction(),
                    department.getProvince(),
                    department.getCity(),
                    department.getStatus(),
                    department.getCreateBy(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(Department department) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE department SET dept_no=?, dept_name=?, address=?, phone=?, introduction=?, province=?, city=?, status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    department.getDeptNo(),
                    department.getDeptName(),
                    department.getAddress(),
                    department.getPhone(),
                    department.getIntroduction(),
                    department.getProvince(),
                    department.getCity(),
                    department.getStatus(),
                    LocalDateTime.now(),
                    department.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM department WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
