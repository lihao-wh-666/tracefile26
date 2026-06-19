<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.opening.utils.PageResult" %>
<%@ page import="com.opening.entity.AuditRecord" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    PageResult<AuditRecord> pageResult = (PageResult<AuditRecord>) request.getAttribute("pageResult");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>审核记录 - 非现场开户管理平台</title>
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
                <div class="card-title">审核记录列表</div>

                <div class="toolbar">
                    <form class="search-form" method="get" action="${ctx}/manage/audit">
                        <div class="form-group">
                            <label>关键词</label>
                            <input type="text" name="keyword" class="form-control" value="${keyword}" placeholder="客户姓名/客户号" style="width: 220px;">
                        </div>
                        <div class="form-group">
                            <label>审核状态</label>
                            <select name="auditStatus" class="form-control">
                                <option value="">全部</option>
                                <option value="0" ${auditStatus != null && auditStatus == 0 ? 'selected' : ''}>待审核</option>
                                <option value="1" ${auditStatus != null && auditStatus == 1 ? 'selected' : ''}>审核通过</option>
                                <option value="2" ${auditStatus != null && auditStatus == 2 ? 'selected' : ''}>审核拒绝</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                        <a href="${ctx}/manage/audit" class="btn btn-default btn-sm">重置</a>
                    </form>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>客户编号</th>
                        <th>客户姓名</th>
                        <th>审核类型</th>
                        <th>审核状态</th>
                        <th>审核意见</th>
                        <th>审核人</th>
                        <th>审核时间</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageResult.list}">
                            <c:forEach items="${pageResult.list}" var="record">
                                <tr>
                                    <td><c:out value="${record.customerNo}" /></td>
                                    <td><c:out value="${record.customerName}" /></td>
                                    <td>
                                        <span class="tag tag-blue">
                                            <c:out value="${record.auditTypeDesc}" />
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${record.auditStatus == 0}">
                                                <span class="tag tag-gray">待审核</span>
                                            </c:when>
                                            <c:when test="${record.auditStatus == 1}">
                                                <span class="tag tag-green">审核通过</span>
                                            </c:when>
                                            <c:when test="${record.auditStatus == 2}">
                                                <span class="tag tag-red">审核拒绝</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td style="max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                        <c:out value="${record.auditResult}" />
                                    </td>
                                    <td><c:out value="${record.auditorName}" /></td>
                                    <td><c:out value="${record.auditTime}" /></td>
                                    <td><c:out value="${record.createTime}" /></td>
                                    <td>
                                        <a href="${ctx}/manage/audit?action=view&id=${record.id}" class="btn btn-default btn-sm">查看</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" style="text-align: center; padding: 40px; color: #999;">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <c:if test="${not empty pageResult.list}">
                    <div class="pagination">
                        <span>共 ${pageResult.total} 条，第 ${pageResult.pageNum}/${pageResult.pages} 页</span>

                        <c:if test="${pageResult.hasPrevious}">
                            <a href="${ctx}/manage/audit?pageNum=1&keyword=${keyword}&auditStatus=${auditStatus}">首页</a>
                            <a href="${ctx}/manage/audit?pageNum=${pageResult.pageNum - 1}&keyword=${keyword}&auditStatus=${auditStatus}">上一页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasPrevious}">
                            <span class="disabled">首页</span>
                            <span class="disabled">上一页</span>
                        </c:if>

                        <span class="current">${pageResult.pageNum}</span>

                        <c:if test="${pageResult.hasNext}">
                            <a href="${ctx}/manage/audit?pageNum=${pageResult.pageNum + 1}&keyword=${keyword}&auditStatus=${auditStatus}">下一页</a>
                            <a href="${ctx}/manage/audit?pageNum=${pageResult.pages}&keyword=${keyword}&auditStatus=${auditStatus}">末页</a>
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
