<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<div class="header">
    <div class="header-title">非现场开户管理平台</div>
    <div class="header-user">
        <span>欢迎，<c:out value="${loginUser.realName}" /></span>
        <span class="tag tag-blue"><c:out value="${loginUser.roleDesc}" /></span>
        <a href="${ctx}/manage/user?action=profile">个人中心</a>
        <a href="${ctx}/manage/user?action=password">修改密码</a>
        <a href="${ctx}/logout">退出登录</a>
    </div>
</div>
