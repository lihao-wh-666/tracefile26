<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    String uri = request.getRequestURI();
%>
<div class="sidebar">
    <div class="sidebar-logo">开户管理平台</div>
    <ul class="sidebar-menu">
        <li>
            <a href="${ctx}/manage/index" class="<%= uri.contains("/manage/index") ? "active" : ""%>">
                <span>📊 首页</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/customer" class="<%= uri.contains("/manage/customer") ? "active" : ""%>">
                <span>👥 客户管理</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/account" class="<%= uri.contains("/manage/account") ? "active" : ""%>">
                <span>💰 账户管理</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/audit" class="<%= uri.contains("/manage/audit") ? "active" : ""%>">
                <span>📋 审核记录</span>
            </a>
        </li>
        <c:if test="${loginUser.role == 'admin'}">
        <li>
            <a href="${ctx}/manage/user" class="<%= uri.contains("/manage/user") && !uri.contains("/profile") && !uri.contains("/password") ? "active" : ""%>">
                <span>👤 用户管理</span>
            </a>
        </li>
        </c:if>
    </ul>
</div>
