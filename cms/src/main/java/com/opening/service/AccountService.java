package com.opening.service;

import com.opening.dao.AccountDAO;
import com.opening.dao.AuditRecordDAO;
import com.opening.dao.CustomerDAO;
import com.opening.entity.Account;
import com.opening.entity.AuditRecord;
import com.opening.entity.Customer;
import com.opening.utils.PageResult;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private AuditRecordDAO auditRecordDAO = new AuditRecordDAO();

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    private String generateAccountNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = SEQUENCE.incrementAndGet();
        if (seq > 9999) {
            SEQUENCE.set(1);
            seq = 1;
        }
        return "A" + datePart + String.format("%04d", seq);
    }

    public Account findById(Long id) throws SQLException {
        return accountDAO.findById(id);
    }

    public Account findByAccountNo(String accountNo) throws SQLException {
        return accountDAO.findByAccountNo(accountNo);
    }

    public List<Account> findByCustomerId(Long customerId) throws SQLException {
        return accountDAO.findByCustomerId(customerId);
    }

    public PageResult<Account> findByPage(int pageNum, int pageSize, String keyword, Integer status) throws SQLException {
        long total = accountDAO.count(keyword, status);
        List<Account> list = accountDAO.findByPage(pageNum, pageSize, keyword, status);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public Map<String, Object> getStatistics() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        long total = accountDAO.count(null, null);
        long active = accountDAO.count(null, 1);
        long frozen = accountDAO.count(null, 2);
        stats.put("total", total);
        stats.put("active", active);
        stats.put("frozen", frozen);
        stats.put("pending", accountDAO.count(null, 0));
        return stats;
    }

    public boolean save(Account account, Long auditBy) throws SQLException {
        Customer customer = customerDAO.findById(account.getCustomerId());
        if (customer == null) {
            return false;
        }
        if (customer.getAuditStatus() == null || customer.getAuditStatus() != 2) {
            return false;
        }
        if (account.getAccountNo() == null || account.getAccountNo().isEmpty()) {
            account.setAccountNo(generateAccountNo());
        }
        if (account.getAccountType() == null || account.getAccountType().isEmpty()) {
            account.setAccountType("NORMAL");
        }
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        if (account.getFrozenBalance() == null) {
            account.setFrozenBalance(BigDecimal.ZERO);
        }
        if (account.getAvailableBalance() == null) {
            account.setAvailableBalance(account.getBalance());
        }
        if (account.getCurrency() == null || account.getCurrency().isEmpty()) {
            account.setCurrency("CNY");
        }
        if (account.getStatus() == null) {
            account.setStatus(1);
        }
        if (account.getOpenWay() == null || account.getOpenWay().isEmpty()) {
            account.setOpenWay("ONLINE");
        }
        boolean result = accountDAO.save(account) > 0;
        if (result && auditBy != null) {
            AuditRecord record = new AuditRecord();
            record.setCustomerId(account.getCustomerId());
            record.setAuditType("OPEN_ACCOUNT");
            record.setAuditStatus(1);
            record.setAuditResult("开户成功，账户号：" + account.getAccountNo());
            record.setAuditBy(auditBy);
            auditRecordDAO.save(record);
        }
        return result;
    }

    public boolean update(Account account) throws SQLException {
        Account existAccount = accountDAO.findById(account.getId());
        if (existAccount == null) {
            return false;
        }
        if (account.getAccountNo() == null || account.getAccountNo().isEmpty()) {
            account.setAccountNo(existAccount.getAccountNo());
        }
        return accountDAO.update(account) > 0;
    }

    public boolean updateStatus(Long id, Integer status) throws SQLException {
        return accountDAO.updateStatus(id, status) > 0;
    }

    public boolean delete(Long id) throws SQLException {
        return accountDAO.delete(id) > 0;
    }
}
