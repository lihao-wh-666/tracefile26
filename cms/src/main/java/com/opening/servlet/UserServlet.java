package com.opening.servlet;

import com.opening.entity.SysUser;
import com.opening.service.SysUserService;
import com.opening.utils.PageResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/manage/user")
public class UserServlet extends HttpServlet {

    private SysUserService sysUserService = new SysUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listUsers(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteUser(req, resp);
                    break;
                case "profile":
                    showProfile(req, resp);
                    break;
                case "password":
                    showPasswordForm(req, resp);
                    break;
                default:
                    listUsers(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listUsers(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    addUser(req, resp);
                    break;
                case "edit":
                    updateUser(req, resp);
                    break;
                case "password":
                    updatePassword(req, resp);
                    break;
                case "profile":
                    updateProfile(req, resp);
                    break;
                default:
                    listUsers(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：" + e.getMessage());
            try {
                listUsers(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");

        int pageNum = 1;
        int pageSize = 10;

        if (pageNumStr != null && !pageNumStr.isEmpty()) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        PageResult<SysUser> pageResult = sysUserService.findByPage(pageNum, pageSize);

        req.setAttribute("pageResult", pageResult);
        req.getRequestDispatcher("/WEB-INF/jsp/manage/user/list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher("/WEB-INF/jsp/manage/user/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/user");
            return;
        }

        Long id = Long.parseLong(idStr);
        SysUser user = sysUserService.findById(id);
        req.setAttribute("user", user);
        req.setAttribute("formAction", "edit");

        req.getRequestDispatcher("/WEB-INF/jsp/manage/user/form.jsp").forward(req, resp);
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        SysUser user = sysUserService.findById(loginUser.getId());
        req.setAttribute("user", user);

        req.getRequestDispatcher("/WEB-INF/jsp/manage/user/profile.jsp").forward(req, resp);
    }

    private void showPasswordForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher("/WEB-INF/jsp/manage/user/password.jsp").forward(req, resp);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        SysUser user = extractUserFromRequest(req);
        boolean result = sysUserService.save(user);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/user");
        } else {
            req.setAttribute("errorMsg", "添加用户失败，用户名可能已存在");
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/manage/user/form.jsp").forward(req, resp);
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/user");
            return;
        }

        SysUser user = extractUserFromRequest(req);
        user.setId(Long.parseLong(idStr));

        boolean result = sysUserService.update(user);
        if (result) {
            resp.sendRedirect(req.getContextPath() + "/manage/user");
        } else {
            req.setAttribute("errorMsg", "更新用户失败");
            req.setAttribute("user", user);
            req.setAttribute("formAction", "edit");
            req.getRequestDispatcher("/WEB-INF/jsp/manage/user/form.jsp").forward(req, resp);
        }
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        SysUser user = new SysUser();
        user.setId(loginUser.getId());
        user.setUsername(loginUser.getUsername());
        user.setPassword("");
        user.setRealName(req.getParameter("realName"));
        user.setRole(loginUser.getRole());
        user.setStatus(loginUser.getStatus());
        user.setPhone(req.getParameter("phone"));
        user.setEmail(req.getParameter("email"));

        boolean result = sysUserService.update(user);
        if (result) {
            SysUser updatedUser = sysUserService.findById(loginUser.getId());
            session.setAttribute("loginUser", updatedUser);
            req.setAttribute("successMsg", "个人信息更新成功");
            req.setAttribute("user", updatedUser);
        } else {
            req.setAttribute("errorMsg", "个人信息更新失败");
            req.setAttribute("user", user);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/manage/user/profile.jsp").forward(req, resp);
    }

    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || newPassword.length() < 6) {
            req.setAttribute("errorMsg", "新密码长度不能少于6位");
            req.getRequestDispatcher("/WEB-INF/jsp/manage/user/password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("errorMsg", "两次输入的新密码不一致");
            req.getRequestDispatcher("/WEB-INF/jsp/manage/user/password.jsp").forward(req, resp);
            return;
        }

        boolean result = sysUserService.updatePassword(loginUser.getId(), oldPassword, newPassword);
        if (result) {
            req.setAttribute("successMsg", "密码修改成功，请重新登录");
            session.invalidate();
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMsg", "原密码错误");
            req.getRequestDispatcher("/WEB-INF/jsp/manage/user/password.jsp").forward(req, resp);
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/manage/user");
            return;
        }

        Long id = Long.parseLong(idStr);
        sysUserService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/manage/user");
    }

    private SysUser extractUserFromRequest(HttpServletRequest req) {
        SysUser user = new SysUser();
        user.setUsername(req.getParameter("username"));
        user.setPassword(req.getParameter("password"));
        user.setRealName(req.getParameter("realName"));
        user.setRole(req.getParameter("role"));
        String statusStr = req.getParameter("status");
        if (statusStr != null && !statusStr.isEmpty()) {
            user.setStatus(Integer.parseInt(statusStr));
        }
        user.setPhone(req.getParameter("phone"));
        user.setEmail(req.getParameter("email"));
        return user;
    }
}
