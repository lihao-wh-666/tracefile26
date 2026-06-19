package com.opening.servlet;

import com.opening.entity.SysUser;
import com.opening.service.SysUserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private SysUserService sysUserService = new SysUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/manage/index");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String ip = req.getRemoteAddr();

        String errorMsg = null;
        if (username == null || username.trim().isEmpty()) {
            errorMsg = "请输入用户名";
        } else if (password == null || password.trim().isEmpty()) {
            errorMsg = "请输入密码";
        }

        if (errorMsg != null) {
            req.setAttribute("errorMsg", errorMsg);
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        try {
            SysUser user = sysUserService.login(username.trim(), password.trim(), ip);
            if (user != null) {
                HttpSession session = req.getSession(true);
                session.setAttribute("loginUser", user);
                session.setMaxInactiveInterval(30 * 60);
                resp.sendRedirect(req.getContextPath() + "/manage/index");
            } else {
                req.setAttribute("errorMsg", "用户名或密码错误");
                req.setAttribute("username", username);
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "系统异常：" + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
