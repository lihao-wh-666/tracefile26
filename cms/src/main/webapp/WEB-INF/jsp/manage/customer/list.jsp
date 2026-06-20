<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.opening.utils.PageResult" %>
<%@ page import="com.opening.entity.Customer" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    PageResult<Customer> pageResult = (PageResult<Customer>) request.getAttribute("pageResult");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>客户管理 - 非现场开户管理平台</title>
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
                <div class="card-title">客户列表</div>

                <div class="toolbar">
                    <a href="${ctx}/manage/customer?action=add" class="btn btn-success">+ 新增客户</a>
                    <form class="search-form" method="get" action="${ctx}/manage/customer">
                        <div class="form-group">
                            <label>关键词</label>
                            <input type="text" name="keyword" class="form-control" value="${keyword}" placeholder="姓名/客户号/手机号/证件号" style="width: 240px;">
                        </div>
                        <div class="form-group">
                            <label>审核状态</label>
                            <select name="auditStatus" class="form-control">
                                <option value="">全部</option>
                                <option value="0" ${auditStatus != null && auditStatus == 0 ? 'selected' : ''}>待审核</option>
                                <option value="1" ${auditStatus != null && auditStatus == 1 ? 'selected' : ''}>审核中</option>
                                <option value="2" ${auditStatus != null && auditStatus == 2 ? 'selected' : ''}>已通过</option>
                                <option value="3" ${auditStatus != null && auditStatus == 3 ? 'selected' : ''}>已拒绝</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                        <a href="${ctx}/manage/customer" class="btn btn-default btn-sm">重置</a>
                    </form>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>客户编号</th>
                        <th>姓名</th>
                        <th>证件类型</th>
                        <th>证件号码</th>
                        <th>手机号</th>
                        <th>身份认证</th>
                        <th>人脸认证</th>
                        <th>审核状态</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageResult.list}">
                            <c:forEach items="${pageResult.list}" var="customer">
                                <tr>
                                    <td><c:out value="${customer.customerNo}" /></td>
                                    <td><c:out value="${customer.realName}" /></td>
                                    <td><c:out value="${customer.idTypeDesc}" /></td>
                                    <td><c:out value="${customer.idNo}" /></td>
                                    <td><c:out value="${customer.phone}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${customer.idVerified == 1}">
                                                <span class="tag tag-green">已认证</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="tag tag-gray">未认证</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${customer.faceVerified == 1}">
                                                <span class="tag tag-green">已认证</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="tag tag-gray">未认证</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${customer.auditStatus == 0}">
                                                <span class="tag tag-gray">待审核</span>
                                            </c:when>
                                            <c:when test="${customer.auditStatus == 1}">
                                                <span class="tag tag-orange">审核中</span>
                                            </c:when>
                                            <c:when test="${customer.auditStatus == 2}">
                                                <span class="tag tag-green">已通过</span>
                                            </c:when>
                                            <c:when test="${customer.auditStatus == 3}">
                                                <span class="tag tag-red">已拒绝</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${customer.createTime}" /></td>
                                    <td class="action-btns">
                                        <a href="${ctx}/manage/customer?action=view&id=${customer.id}" class="btn btn-default btn-sm">查看</a>
                                        <c:if test="${customer.auditStatus != 2 && customer.auditStatus != 3}">
                                            <a href="${ctx}/manage/customer?action=edit&id=${customer.id}" class="btn btn-warning btn-sm">编辑</a>
                                        </c:if>
                                        <c:if test="${customer.auditStatus == 0 || customer.auditStatus == 1}">
                                            <a href="${ctx}/manage/customer?action=audit&id=${customer.id}" class="btn btn-success btn-sm">审核</a>
                                        </c:if>
                                        <a href="${ctx}/manage/account?action=add&customerId=${customer.id}" class="btn btn-primary btn-sm">开户</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="10" class="empty-row">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <c:if test="${not empty pageResult.list}">
                    <div class="pagination">
                        <span>共 ${pageResult.total} 条，第 ${pageResult.pageNum}/${pageResult.pages} 页</span>

                        <c:if test="${pageResult.hasPrevious}">
                            <a href="${ctx}/manage/customer?pageNum=1&keyword=${keyword}&auditStatus=${auditStatus}">首页</a>
                            <a href="${ctx}/manage/customer?pageNum=${pageResult.pageNum - 1}&keyword=${keyword}&auditStatus=${auditStatus}">上一页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasPrevious}">
                            <span class="disabled">首页</span>
                            <span class="disabled">上一页</span>
                        </c:if>

                        <span class="current">${pageResult.pageNum}</span>

                        <c:if test="${pageResult.hasNext}">
                            <a href="${ctx}/manage/customer?pageNum=${pageResult.pageNum + 1}&keyword=${keyword}&auditStatus=${auditStatus}">下一页</a>
                            <a href="${ctx}/manage/customer?pageNum=${pageResult.pages}&keyword=${keyword}&auditStatus=${auditStatus}">末页</a>
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
