<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改密码 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css?v=20260620">
</head>
<body>
<div class="layout">
    <jsp:include page="../common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="../common/header.jsp" />
        <div class="content">
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

            <div class="card">
                <div class="card-title">修改密码</div>

                <form method="post" action="${ctx}/manage/user" style="max-width: 500px;">
                    <input type="hidden" name="action" value="password">

                    <div class="form-group">
                        <label>原密码 <span class="required-star">*</span></label>
                        <input type="password" name="oldPassword" class="form-control" required placeholder="请输入原密码">
                    </div>

                    <div class="form-group">
                        <label>新密码 <span class="required-star">*</span></label>
                        <input type="password" name="newPassword" class="form-control" required placeholder="请输入新密码（至少6位）" minlength="6">
                    </div>

                    <div class="form-group">
                        <label>确认新密码 <span class="required-star">*</span></label>
                        <input type="password" name="confirmPassword" class="form-control" required placeholder="请再次输入新密码" minlength="6">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">确认修改</button>
                        <a href="${ctx}/manage/index" class="btn btn-default">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
