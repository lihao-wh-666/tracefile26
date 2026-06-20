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
    <title>404 - 页面不存在</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css">
</head>
<body>
<div class="error-page">
    <div class="error-box">
        <div class="error-code">404</div>
        <div class="error-title">页面不存在</div>
        <div class="error-desc">您访问的页面可能已被删除或不存在</div>
        <a href="${ctx}/manage/index" class="btn btn-primary">返回首页</a>
    </div>
</div>
</body>
</html>
