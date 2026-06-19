package com.opening.service;

import com.opening.dao.AuditRecordDAO;
import com.opening.entity.AuditRecord;
import com.opening.utils.PageResult;

import java.sql.SQLException;
import java.util.List;

public class AuditRecordService {

    private AuditRecordDAO auditRecordDAO = new AuditRecordDAO();

    public AuditRecord findById(Long id) throws SQLException {
        return auditRecordDAO.findById(id);
    }

    public List<AuditRecord> findByCustomerId(Long customerId) throws SQLException {
        return auditRecordDAO.findByCustomerId(customerId);
    }

    public PageResult<AuditRecord> findByPage(int pageNum, int pageSize, String keyword, Integer auditStatus) throws SQLException {
        long total = auditRecordDAO.count(keyword, auditStatus);
        List<AuditRecord> list = auditRecordDAO.findByPage(pageNum, pageSize, keyword, auditStatus);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public boolean save(AuditRecord record) throws SQLException {
        return auditRecordDAO.save(record) > 0;
    }

    public boolean update(AuditRecord record) throws SQLException {
        return auditRecordDAO.update(record) > 0;
    }

    public boolean delete(Long id) throws SQLException {
        return auditRecordDAO.delete(id) > 0;
    }
}
