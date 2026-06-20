package com.opening.service;

import com.opening.dao.DepartmentDAO;
import com.opening.entity.Department;
import com.opening.utils.PageResult;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DepartmentService {

    private DepartmentDAO departmentDAO = new DepartmentDAO();

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    private String generateDeptNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = SEQUENCE.incrementAndGet();
        if (seq > 9999) {
            SEQUENCE.set(1);
            seq = 1;
        }
        return "D" + datePart + String.format("%04d", seq);
    }

    public Department findById(Long id) throws SQLException {
        return departmentDAO.findById(id);
    }

    public Department findByDeptNo(String deptNo) throws SQLException {
        return departmentDAO.findByDeptNo(deptNo);
    }

    public List<Department> findAll() throws SQLException {
        return departmentDAO.findAll();
    }

    public PageResult<Department> findByPage(int pageNum, int pageSize, String deptName, String province, String city, Integer status) throws SQLException {
        long total = departmentDAO.count(deptName, province, city, status);
        List<Department> list = departmentDAO.findByPage(pageNum, pageSize, deptName, province, city, status);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public boolean save(Department department) throws SQLException {
        if (department.getDeptNo() == null || department.getDeptNo().isEmpty()) {
            department.setDeptNo(generateDeptNo());
        }
        if (department.getStatus() == null) {
            department.setStatus(1);
        }
        return departmentDAO.save(department) > 0;
    }

    public boolean update(Department department) throws SQLException {
        Department existDept = departmentDAO.findById(department.getId());
        if (existDept == null) {
            return false;
        }
        if (department.getDeptNo() == null || department.getDeptNo().isEmpty()) {
            department.setDeptNo(existDept.getDeptNo());
        }
        return departmentDAO.update(department) > 0;
    }

    public boolean delete(Long id) throws SQLException {
        return departmentDAO.delete(id) > 0;
    }
}
