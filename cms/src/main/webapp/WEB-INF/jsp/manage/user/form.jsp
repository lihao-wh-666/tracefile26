<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    String formAction = request.getAttribute("formAction") != null ? (String) request.getAttribute("formAction") : "add";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= "add".equals(formAction) ? "新增用户" : "编辑用户" %> - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css">
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

            <div class="card">
                <div class="card-title"><%= "add".equals(formAction) ? "新增用户" : "编辑用户" %></div>

                <form method="post" action="${ctx}/manage/user">
                    <input type="hidden" name="action" value="<%= formAction %>">
                    <c:if test="${user != null}">
                        <input type="hidden" name="id" value="${user.id}">
                    </c:if>

                    <div class="form-row">
                        <div class="form-group">
                            <label>用户名 <span class="required-star">*</span></label>
                            <input type="text" name="username" class="form-control" required
                                   value="${user != null ? user.username : ''}"
                                <%= "edit".equals(formAction) ? "readonly" : "" %>
                                   placeholder="请输入登录用户名">
                        </div>

                        <div class="form-group">
                            <label>真实姓名 <span class="required-star">*</span></label>
                            <input type="text" name="realName" class="form-control" required
                                   value="${user != null ? user.realName : ''}" placeholder="请输入真实姓名">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>密码 <span class="required-star">*</span></label>
                            <input type="password" name="password" class="form-control"
                                   placeholder="<%= "edit".equals(formAction) ? "不修改请留空" : "请输入密码，默认123456" %>">
                        </div>

                        <div class="form-group">
                            <label>角色 <span class="required-star">*</span></label>
                            <select name="role" class="form-control" required>
                                <option value="admin" ${user != null && user.role == 'admin' ? 'selected' : ''}>管理员</option>
                                <option value="auditor" ${user != null && user.role == 'auditor' ? 'selected' : ''}>审核员</option>
                                <option value="operator" ${user != null && user.role == 'operator' ? 'selected' : ''}>操作员</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>状态</label>
                            <select name="status" class="form-control">
                                <option value="1" ${user == null || user.status == 1 ? 'selected' : ''}>启用</option>
                                <option value="0" ${user != null && user.status == 0 ? 'selected' : ''}>禁用</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>手机号</label>
                            <input type="tel" name="phone" class="form-control"
                                   value="${user != null ? user.phone : ''}" placeholder="请输入手机号">
                        </div>

                        <div class="form-group">
                            <label>邮箱</label>
                            <input type="email" name="email" class="form-control"
                                   value="${user != null ? user.email : ''}" placeholder="请输入邮箱">
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${ctx}/manage/user" class="btn btn-default">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
