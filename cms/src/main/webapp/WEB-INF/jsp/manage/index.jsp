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
    <title>首页 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body>
<div class="layout">
    <jsp:include page="common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="common/header.jsp" />
        <div class="content">
            <% if (request.getAttribute("errorMsg") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("errorMsg") %>
            </div>
            <% } %>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">${customerStats.total}</div>
                    <div class="stat-label">客户总数</div>
                </div>
                <div class="stat-card warning">
                    <div class="stat-value">${customerStats.pending + customerStats.auditing}</div>
                    <div class="stat-label">待审核客户</div>
                </div>
                <div class="stat-card success">
                    <div class="stat-value">${customerStats.passed}</div>
                    <div class="stat-label">审核通过客户</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${accountStats.total}</div>
                    <div class="stat-label">账户总数</div>
                </div>
                <div class="stat-card success">
                    <div class="stat-value">${accountStats.active}</div>
                    <div class="stat-label">活跃账户</div>
                </div>
                <div class="stat-card danger">
                    <div class="stat-value">${accountStats.frozen}</div>
                    <div class="stat-label">冻结账户</div>
                </div>
            </div>

            <div class="card">
                <div class="card-title">快捷操作</div>
                <div class="toolbar">
                    <a href="${ctx}/manage/customer?action=add" class="btn btn-success">+ 新增客户</a>
                    <a href="${ctx}/manage/customer?auditStatus=0" class="btn btn-warning">审核客户</a>
                    <a href="${ctx}/manage/account?action=add" class="btn btn-primary">+ 开立账户</a>
                    <a href="${ctx}/manage/audit" class="btn btn-default">查看审核记录</a>
                </div>
            </div>

            <div class="card">
                <div class="card-title">客户状态说明</div>
                <div class="toolbar" style="gap: 20px;">
                    <span class="tag tag-gray">待审核</span>
                    <span class="tag tag-orange">审核中</span>
                    <span class="tag tag-green">审核通过</span>
                    <span class="tag tag-red">审核拒绝</span>
                    <span class="tag tag-red">已冻结</span>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
