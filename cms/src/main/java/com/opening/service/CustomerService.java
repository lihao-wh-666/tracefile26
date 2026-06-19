package com.opening.service;

import com.opening.dao.AuditRecordDAO;
import com.opening.dao.CustomerDAO;
import com.opening.entity.AuditRecord;
import com.opening.entity.Customer;
import com.opening.utils.PageResult;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerService {

    private CustomerDAO customerDAO = new CustomerDAO();
    private AuditRecordDAO auditRecordDAO = new AuditRecordDAO();

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    private String generateCustomerNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = SEQUENCE.incrementAndGet();
        if (seq > 9999) {
            SEQUENCE.set(1);
            seq = 1;
        }
        return "C" + datePart + String.format("%04d", seq);
    }

    public Customer findById(Long id) throws SQLException {
        return customerDAO.findById(id);
    }

    public Customer findByCustomerNo(String customerNo) throws SQLException {
        return customerDAO.findByCustomerNo(customerNo);
    }

    public Customer findByIdNo(String idNo) throws SQLException {
        return customerDAO.findByIdNo(idNo);
    }

    public List<Customer> findAll() throws SQLException {
        return customerDAO.findAll();
    }

    public PageResult<Customer> findByPage(int pageNum, int pageSize, String keyword, Integer auditStatus) throws SQLException {
        long total = customerDAO.count(keyword, auditStatus);
        List<Customer> list = customerDAO.findByPage(pageNum, pageSize, keyword, auditStatus);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public Map<String, Long> getStatistics() throws SQLException {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", customerDAO.count(null, null));
        stats.put("pending", customerDAO.countByAuditStatus(0));
        stats.put("auditing", customerDAO.countByAuditStatus(1));
        stats.put("passed", customerDAO.countByAuditStatus(2));
        stats.put("rejected", customerDAO.countByAuditStatus(3));
        return stats;
    }

    public boolean save(Customer customer) throws SQLException {
        if (customer.getCustomerNo() == null || customer.getCustomerNo().isEmpty()) {
            customer.setCustomerNo(generateCustomerNo());
        }
        if (customer.getStatus() == null) {
            customer.setStatus(0);
        }
        if (customer.getAuditStatus() == null) {
            customer.setAuditStatus(0);
        }
        if (customer.getIdVerified() == null) {
            customer.setIdVerified(0);
        }
        if (customer.getFaceVerified() == null) {
            customer.setFaceVerified(0);
        }
        if (customer.getNationality() == null || customer.getNationality().isEmpty()) {
            customer.setNationality("中国");
        }
        return customerDAO.save(customer) > 0;
    }

    public boolean update(Customer customer) throws SQLException {
        Customer existCustomer = customerDAO.findById(customer.getId());
        if (existCustomer == null) {
            return false;
        }
        if (customer.getCustomerNo() == null || customer.getCustomerNo().isEmpty()) {
            customer.setCustomerNo(existCustomer.getCustomerNo());
        }
        return customerDAO.update(customer) > 0;
    }

    public boolean audit(Long id, Integer auditStatus, String auditResult, Long auditBy) throws SQLException {
        Customer customer = customerDAO.findById(id);
        if (customer == null) {
            return false;
        }
        Integer customerStatus = 0;
        if (auditStatus == 1) {
            customerStatus = 1;
        } else if (auditStatus == 2) {
            customerStatus = 2;
        }
        int result = customerDAO.updateAuditStatus(id, auditStatus, customerStatus);
        if (result > 0) {
            AuditRecord record = new AuditRecord();
            record.setCustomerId(id);
            record.setAuditType("CUSTOMER_INFO");
            record.setAuditStatus(auditStatus);
            record.setAuditResult(auditResult);
            record.setAuditBy(auditBy);
            auditRecordDAO.save(record);
            return true;
        }
        return false;
    }

    public boolean delete(Long id) throws SQLException {
        return customerDAO.delete(id) > 0;
    }
}
