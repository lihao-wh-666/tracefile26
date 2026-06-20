<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.opening.utils.PageResult" %>
<%@ page import="com.opening.entity.SysUser" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    PageResult<SysUser> pageResult = (PageResult<SysUser>) request.getAttribute("pageResult");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户管理 - 非现场开户管理平台</title>
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
                <div class="card-title">用户列表</div>

                <div class="toolbar">
                    <a href="${ctx}/manage/user?action=add" class="btn btn-success">+ 新增用户</a>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>用户名</th>
                        <th>真实姓名</th>
                        <th>角色</th>
                        <th>状态</th>
                        <th>手机号</th>
                        <th>邮箱</th>
                        <th>最后登录时间</th>
                        <th>最后登录IP</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageResult.list}">
                            <c:forEach items="${pageResult.list}" var="user">
                                <tr>
                                    <td><c:out value="${user.username}" /></td>
                                    <td><c:out value="${user.realName}" /></td>
                                    <td>
                                        <span class="tag ${user.role == 'admin' ? 'tag-orange' : user.role == 'auditor' ? 'tag-blue' : 'tag-gray'}">
                                            <c:out value="${user.roleDesc}" />
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.status == 1}">
                                                <span class="tag tag-green">启用</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="tag tag-red">禁用</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${user.phone}" /></td>
                                    <td><c:out value="${user.email}" /></td>
                                    <td><c:out value="${user.lastLoginTime}" /></td>
                                    <td><c:out value="${user.lastLoginIp}" /></td>
                                    <td><c:out value="${user.createTime}" /></td>
                                    <td class="action-btns">
                                        <a href="${ctx}/manage/user?action=edit&id=${user.id}" class="btn btn-warning btn-sm">编辑</a>
                                        <a href="${ctx}/manage/user?action=delete&id=${user.id}" class="btn btn-danger btn-sm"
                                           onclick="return confirm('确定要删除该用户吗？')">删除</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="10" style="text-align: center; padding: 40px; color: #999;">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <c:if test="${not empty pageResult.list}">
                    <div class="pagination">
                        <span>共 ${pageResult.total} 条，第 ${pageResult.pageNum}/${pageResult.pages} 页</span>

                        <c:if test="${pageResult.hasPrevious}">
                            <a href="${ctx}/manage/user?pageNum=1">首页</a>
                            <a href="${ctx}/manage/user?pageNum=${pageResult.pageNum - 1}">上一页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasPrevious}">
                            <span class="disabled">首页</span>
                            <span class="disabled">上一页</span>
                        </c:if>

                        <span class="current">${pageResult.pageNum}</span>

                        <c:if test="${pageResult.hasNext}">
                            <a href="${ctx}/manage/user?pageNum=${pageResult.pageNum + 1}">下一页</a>
                            <a href="${ctx}/manage/user?pageNum=${pageResult.pages}">末页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasNext}">
                            <span class="disabled">下一页</span>
                            <span class="disabled">末页</span>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
