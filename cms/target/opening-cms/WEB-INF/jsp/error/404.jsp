<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>404 - 页面不存在</title>
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-box" style="text-align: center;">
        <h1 style="font-size: 72px; color: #1890ff; margin-bottom: 20px;">404</h1>
        <h2 style="margin-bottom: 16px;">页面不存在</h2>
        <p style="color: #999; margin-bottom: 30px;">您访问的页面可能已被删除或不存在</p>
        <a href="${ctx}/manage/index" class="btn btn-primary">返回首页</a>
    </div>
</div>
</body>
</html>
