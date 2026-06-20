<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人中心 - 非现场开户管理平台</title>
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
                <div class="card-title">个人信息</div>

                <form method="post" action="${ctx}/manage/user">
                    <input type="hidden" name="action" value="profile">

                    <div class="form-row">
                        <div class="form-group">
                            <label>用户名</label>
                            <input type="text" class="form-control" value="${user.username}" readonly>
                        </div>

                        <div class="form-group">
                            <label>角色</label>
                            <input type="text" class="form-control" value="${user.roleDesc}" readonly>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>真实姓名 <span class="required-star">*</span></label>
                            <input type="text" name="realName" class="form-control" required value="${user.realName}">
                        </div>

                        <div class="form-group">
                            <label>状态</label>
                            <input type="text" class="form-control" value="${user.statusDesc}" readonly>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>手机号</label>
                            <input type="tel" name="phone" class="form-control" value="${user.phone}">
                        </div>

                        <div class="form-group">
                            <label>邮箱</label>
                            <input type="email" name="email" class="form-control" value="${user.email}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>最后登录时间</label>
                            <input type="text" class="form-control" value="${user.lastLoginTime}" readonly>
                        </div>

                        <div class="form-group">
                            <label>最后登录IP</label>
                            <input type="text" class="form-control" value="${user.lastLoginIp}" readonly>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存修改</button>
                        <a href="${ctx}/manage/user?action=password" class="btn btn-warning">修改密码</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
