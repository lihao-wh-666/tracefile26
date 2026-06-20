<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>审核记录详情 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css?v=20260620">
</head>
<body>
<div class="layout">
    <jsp:include page="../common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="../common/header.jsp" />
        <div class="content">
            <div class="card">
                <div class="card-title">审核记录详情</div>

                <div class="detail-row">
                    <div class="detail-label">客户编号</div>
                    <div class="detail-value"><c:out value="${record.customerNo}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">客户姓名</div>
                    <div class="detail-value"><c:out value="${record.customerName}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">审核类型</div>
                    <div class="detail-value">
                        <span class="tag tag-blue">
                            <c:out value="${record.auditTypeDesc}" />
                        </span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">审核状态</div>
                    <div class="detail-value">
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
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">审核意见</div>
                    <div class="detail-value"><c:out value="${record.auditResult}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">审核人</div>
                    <div class="detail-value"><c:out value="${record.auditorName}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">审核时间</div>
                    <div class="detail-value"><c:out value="${record.auditTime}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">创建时间</div>
                    <div class="detail-value"><c:out value="${record.createTime}" /></div>
                </div>

                <div class="form-actions">
                    <a href="${ctx}/manage/audit" class="btn btn-default">返回列表</a>
                    <a href="${ctx}/manage/customer?action=view&id=${record.customerId}" class="btn btn-primary">查看客户</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
