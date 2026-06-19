package com.opening.service;

import com.opening.dao.SysUserDAO;
import com.opening.entity.SysUser;
import com.opening.utils.MD5Util;
import com.opening.utils.PageResult;

import java.sql.SQLException;
import java.util.List;

public class SysUserService {

    private SysUserDAO sysUserDAO = new SysUserDAO();

    public SysUser login(String username, String password, String ip) throws SQLException {
        SysUser user = sysUserDAO.findByUsername(username);
        if (user == null) {
            return null;
        }
        String md5Password = MD5Util.md5(password);
        if (!md5Password.equals(user.getPassword())) {
            return null;
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            return null;
        }
        sysUserDAO.updateLoginInfo(user.getId(), ip);
        return user;
    }

    public SysUser findById(Long id) throws SQLException {
        return sysUserDAO.findById(id);
    }

    public SysUser findByUsername(String username) throws SQLException {
        return sysUserDAO.findByUsername(username);
    }

    public List<SysUser> findAll() throws SQLException {
        return sysUserDAO.findAll();
    }

    public PageResult<SysUser> findByPage(int pageNum, int pageSize) throws SQLException {
        long total = sysUserDAO.count();
        List<SysUser> list = sysUserDAO.findByPage(pageNum, pageSize);
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public boolean save(SysUser user) throws SQLException {
        SysUser existUser = sysUserDAO.findByUsername(user.getUsername());
        if (existUser != null) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            user.setPassword("123456");
        }
        user.setPassword(MD5Util.md5(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        return sysUserDAO.save(user) > 0;
    }

    public boolean update(SysUser user) throws SQLException {
        SysUser existUser = sysUserDAO.findById(user.getId());
        if (existUser == null) {
            return false;
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(MD5Util.md5(user.getPassword()));
        } else {
            user.setPassword(existUser.getPassword());
        }
        return sysUserDAO.update(user) > 0;
    }

    public boolean updatePassword(Long id, String oldPassword, String newPassword) throws SQLException {
        SysUser user = sysUserDAO.findById(id);
        if (user == null) {
            return false;
        }
        if (!MD5Util.md5(oldPassword).equals(user.getPassword())) {
            return false;
        }
        user.setPassword(MD5Util.md5(newPassword));
        return sysUserDAO.update(user) > 0;
    }

    public boolean delete(Long id) throws SQLException {
        return sysUserDAO.delete(id) > 0;
    }
}
