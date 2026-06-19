<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>500 - 服务器错误</title>
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-box" style="text-align: center;">
        <h1 style="font-size: 72px; color: #ff4d4f; margin-bottom: 20px;">500</h1>
        <h2 style="margin-bottom: 16px;">服务器内部错误</h2>
        <p style="color: #999; margin-bottom: 20px;">服务器发生异常，请稍后重试</p>
        <% if (exception != null) { %>
        <div style="text-align: left; background: #fff2f0; padding: 12px; border-radius: 4px; margin-bottom: 20px; font-size: 12px; color: #ff4d4f; max-height: 150px; overflow-y: auto;">
            <%= exception.getMessage() %>
        </div>
        <% } %>
        <a href="${ctx}/manage/index" class="btn btn-primary">返回首页</a>
    </div>
</div>
</body>
</html>
