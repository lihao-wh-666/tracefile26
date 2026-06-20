<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - 服务器错误</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css">
</head>
<body>
<div class="error-page">
    <div class="error-box">
        <div class="error-code danger">500</div>
        <div class="error-title">服务器内部错误</div>
        <div class="error-desc">服务器发生异常，请稍后重试</div>
        <% if (exception != null) { %>
        <div class="error-detail">
            <%= exception.getMessage() %>
        </div>
        <% } %>
        <a href="${ctx}/manage/index" class="btn btn-primary">返回首页</a>
    </div>
</div>
</body>
</html>
