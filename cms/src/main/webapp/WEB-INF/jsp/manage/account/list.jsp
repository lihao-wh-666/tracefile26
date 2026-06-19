<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.opening.utils.PageResult" %>
<%@ page import="com.opening.entity.Account" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    PageResult<Account> pageResult = (PageResult<Account>) request.getAttribute("pageResult");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>账户管理 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
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
                <div class="card-title">账户列表</div>

                <div class="toolbar">
                    <a href="${ctx}/manage/account?action=add" class="btn btn-success">+ 开立账户</a>
                    <form class="search-form" method="get" action="${ctx}/manage/account">
                        <div class="form-group">
                            <label>关键词</label>
                            <input type="text" name="keyword" class="form-control" value="${keyword}" placeholder="账户号/客户号/客户姓名/手机号" style="width: 260px;">
                        </div>
                        <div class="form-group">
                            <label>账户状态</label>
                            <select name="status" class="form-control">
                                <option value="">全部</option>
                                <option value="0" ${status != null && status == 0 ? 'selected' : ''}>待激活</option>
                                <option value="1" ${status != null && status == 1 ? 'selected' : ''}>正常</option>
                                <option value="2" ${status != null && status == 2 ? 'selected' : ''}>冻结</option>
                                <option value="3" ${status != null && status == 3 ? 'selected' : ''}>销户</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                        <a href="${ctx}/manage/account" class="btn btn-default btn-sm">重置</a>
                    </form>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>账户编号</th>
                        <th>客户编号</th>
                        <th>客户姓名</th>
                        <th>账户类型</th>
                        <th>币种</th>
                        <th>账户余额</th>
                        <th>可用余额</th>
                        <th>账户状态</th>
                        <th>开户方式</th>
                        <th>开户时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageResult.list}">
                            <c:forEach items="${pageResult.list}" var="account">
                                <tr>
                                    <td><c:out value="${account.accountNo}" /></td>
                                    <td><c:out value="${account.customerNo}" /></td>
                                    <td><c:out value="${account.customerName}" /></td>
                                    <td>
                                        <span class="tag ${account.accountType == 'VIP' ? 'tag-orange' : 'tag-blue'}">
                                            <c:out value="${account.accountTypeDesc}" />
                                        </span>
                                    </td>
                                    <td><c:out value="${account.currency}" /></td>
                                    <td style="text-align: right;"><c:out value="${account.balance}" /></td>
                                    <td style="text-align: right;"><c:out value="${account.availableBalance}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${account.status == 0}">
                                                <span class="tag tag-gray">待激活</span>
                                            </c:when>
                                            <c:when test="${account.status == 1}">
                                                <span class="tag tag-green">正常</span>
                                            </c:when>
                                            <c:when test="${account.status == 2}">
                                                <span class="tag tag-red">冻结</span>
                                            </c:when>
                                            <c:when test="${account.status == 3}">
                                                <span class="tag tag-red">销户</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${account.openWayDesc}" /></td>
                                    <td><c:out value="${account.openTime}" /></td>
                                    <td class="action-btns">
                                        <a href="${ctx}/manage/account?action=view&id=${account.id}" class="btn btn-default btn-sm">查看</a>
                                        <a href="${ctx}/manage/account?action=edit&id=${account.id}" class="btn btn-warning btn-sm">编辑</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="11" style="text-align: center; padding: 40px; color: #999;">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <c:if test="${not empty pageResult.list}">
                    <div class="pagination">
                        <span>共 ${pageResult.total} 条，第 ${pageResult.pageNum}/${pageResult.pages} 页</span>

                        <c:if test="${pageResult.hasPrevious}">
                            <a href="${ctx}/manage/account?pageNum=1&keyword=${keyword}&status=${status}">首页</a>
                            <a href="${ctx}/manage/account?pageNum=${pageResult.pageNum - 1}&keyword=${keyword}&status=${status}">上一页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasPrevious}">
                            <span class="disabled">首页</span>
                            <span class="disabled">上一页</span>
                        </c:if>

                        <span class="current">${pageResult.pageNum}</span>

                        <c:if test="${pageResult.hasNext}">
                            <a href="${ctx}/manage/account?pageNum=${pageResult.pageNum + 1}&keyword=${keyword}&status=${status}">下一页</a>
                            <a href="${ctx}/manage/account?pageNum=${pageResult.pages}&keyword=${keyword}&status=${status}">末页</a>
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
</body>
</html>
