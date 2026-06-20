<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/tailwind-output.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-box">
        <h2 class="login-title">非现场开户管理平台</h2>

        <% if (request.getAttribute("errorMsg") != null) { %>
        <div class="alert alert-danger">
            <%= request.getAttribute("errorMsg") %>
        </div>
        <% } %>

        <% if (request.getAttribute("successMsg") != null) { %>
        <div class="alert alert-success">
            <%= request.getAttribute("successMsg") %>
        </div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" class="form-control"
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                       placeholder="请输入用户名" required>
            </div>

            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" class="form-control"
                       placeholder="请输入密码" required>
            </div>

            <button type="submit" class="btn btn-primary btn-lg" style="width: 100%;">登 录</button>
        </form>

        <div style="margin-top: 24px; text-align: center; color: var(--color-text-disabled); font-size: 12px;">
            默认账号: admin / auditor01 / operator01 &nbsp;&nbsp; 密码: admin123
        </div>
    </div>
</div>
</body>
</html>
