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
    <title>营业部详情 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css?v=20260620">
</head>
<body>
<div class="layout">
    <jsp:include page="../common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="../common/header.jsp" />
        <div class="content">
            <div class="card">
                <div class="card-title">营业部详情</div>

                <div class="detail-view">
                    <div class="detail-row">
                        <div class="detail-label">营业部编号：</div>
                        <div class="detail-value"><c:out value="${department.deptNo}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">营业部名称：</div>
                        <div class="detail-value"><c:out value="${department.deptName}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">联系电话：</div>
                        <div class="detail-value"><c:out value="${department.phone}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">所在省份：</div>
                        <div class="detail-value"><c:out value="${department.province}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">所在城市：</div>
                        <div class="detail-value"><c:out value="${department.city}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">详细地址：</div>
                        <div class="detail-value"><c:out value="${department.address}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">状态：</div>
                        <div class="detail-value">
                            <c:choose>
                                <c:when test="${department.status == 1}">
                                    <span class="tag tag-green">启用</span>
                                </c:when>
                                <c:when test="${department.status == 0}">
                                    <span class="tag tag-gray">禁用</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="tag tag-gray">未知</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">营业部介绍：</div>
                        <div class="detail-value"><c:out value="${department.introduction}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">创建时间：</div>
                        <div class="detail-value"><c:out value="${department.createTime}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">更新时间：</div>
                        <div class="detail-value"><c:out value="${department.updateTime}" /></div>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${ctx}/manage/department?action=edit&id=${department.id}" class="btn btn-warning">编辑</a>
                    <a href="${ctx}/manage/department" class="btn btn-default">返回列表</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
