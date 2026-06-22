<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    String uri = request.getRequestURI();
%>
<div class="sidebar" id="sidebar">
    <div class="sidebar-logo">
        <span class="logo-icon">🏦</span>
        开户管理平台
    </div>
    <ul class="sidebar-menu">
        <li>
            <a href="${ctx}/manage/index" class="<%= uri.contains("/manage/index") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg></span>
                首页
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/customer" class="<%= uri.contains("/manage/customer") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg></span>
                客户管理
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/account" class="<%= uri.contains("/manage/account") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg></span>
                账户管理
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/audit" class="<%= uri.contains("/manage/audit") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg></span>
                审核记录
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/department" class="<%= uri.contains("/manage/department") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/><path d="M12 12h.01"/><path d="M12 16h.01"/></svg></span>
                营业部管理
            </a>
        </li>
        <li>
            <a href="${ctx}/manage/region" class="<%= uri.contains("/manage/region") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg></span>
                省市区管理
            </a>
        </li>
        <c:if test="${loginUser.role == 'admin'}">
        <li>
            <a href="${ctx}/manage/user" class="<%= uri.contains("/manage/user") && !uri.contains("/profile") && !uri.contains("/password") ? "active" : ""%>">
                <span class="menu-icon"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></span>
                用户管理
            </a>
        </li>
        </c:if>
    </ul>
</div>
<div class="sidebar-overlay" id="sidebarOverlay"></div>
