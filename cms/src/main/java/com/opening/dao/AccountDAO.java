package com.opening.dao;

import com.opening.entity.Account;
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

public class AccountDAO {

    private QueryRunner queryRunner = new QueryRunner();

    public Account findById(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT a.*, c.real_name AS customerName, c.customer_no AS customerNo FROM account a LEFT JOIN customer c ON a.customer_id = c.id WHERE a.id = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Account.class), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public Account findByAccountNo(String accountNo) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT a.*, c.real_name AS customerName, c.customer_no AS customerNo FROM account a LEFT JOIN customer c ON a.customer_id = c.id WHERE a.account_no = ?";
            return queryRunner.query(conn, sql, new BeanHandler<>(Account.class), accountNo);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Account> findByCustomerId(Long customerId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE customer_id = ? ORDER BY create_time DESC";
            return queryRunner.query(conn, sql, new BeanListHandler<>(Account.class), customerId);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public List<Account> findByPage(int pageNum, int pageSize, String keyword, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            int offset = (pageNum - 1) * pageSize;
            StringBuilder sql = new StringBuilder("SELECT a.*, c.real_name AS customerName, c.customer_no AS customerNo FROM account a LEFT JOIN customer c ON a.customer_id = c.id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (a.account_no LIKE ? OR c.customer_no LIKE ? OR c.real_name LIKE ? OR c.phone LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (status != null) {
                sql.append(" AND a.status = ?");
                params.add(status);
            }

            sql.append(" ORDER BY a.create_time DESC LIMIT ?, ?");
            params.add(offset);
            params.add(pageSize);

            return queryRunner.query(conn, sql.toString(), new BeanListHandler<>(Account.class), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public long count(String keyword, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM account a LEFT JOIN customer c ON a.customer_id = c.id WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (a.account_no LIKE ? OR c.customer_no LIKE ? OR c.real_name LIKE ? OR c.phone LIKE ?)");
                String likeKeyword = "%" + keyword.trim() + "%";
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
                params.add(likeKeyword);
            }

            if (status != null) {
                sql.append(" AND a.status = ?");
                params.add(status);
            }

            return queryRunner.query(conn, sql.toString(), new ScalarHandler<>(), params.toArray());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int save(Account account) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO account (account_no, customer_id, account_type, balance, frozen_balance, available_balance, currency, status, open_way, open_channel, open_time, remark, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            return queryRunner.update(conn, sql,
                    account.getAccountNo(),
                    account.getCustomerId(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getFrozenBalance(),
                    account.getAvailableBalance(),
                    account.getCurrency(),
                    account.getStatus(),
                    account.getOpenWay(),
                    account.getOpenChannel(),
                    now,
                    account.getRemark(),
                    now,
                    now);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int update(Account account) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE account SET account_no=?, customer_id=?, account_type=?, balance=?, frozen_balance=?, available_balance=?, currency=?, status=?, open_way=?, open_channel=?, remark=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql,
                    account.getAccountNo(),
                    account.getCustomerId(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getFrozenBalance(),
                    account.getAvailableBalance(),
                    account.getCurrency(),
                    account.getStatus(),
                    account.getOpenWay(),
                    account.getOpenChannel(),
                    account.getRemark(),
                    LocalDateTime.now(),
                    account.getId());
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int updateStatus(Long id, Integer status) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE account SET status=?, update_time=? WHERE id=?";
            return queryRunner.update(conn, sql, status, LocalDateTime.now(), id);
        } finally {
            DBUtil.close(conn, null);
        }
    }

    public int delete(Long id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "DELETE FROM account WHERE id = ?";
            return queryRunner.update(conn, sql, id);
        } finally {
            DBUtil.close(conn, null);
        }
    }
}
