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
    <title>区域详情 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css?v=20260620">
</head>
<body>
<div class="layout">
    <jsp:include page="../common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="../common/header.jsp" />
        <div class="content">
            <div class="card">
                <div class="card-title">区域详情</div>

                <div class="detail-view">
                    <div class="detail-row">
                        <div class="detail-label">区域编码：</div>
                        <div class="detail-value"><c:out value="${region.regionCode}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">区域名称：</div>
                        <div class="detail-value"><c:out value="${region.regionName}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">层级：</div>
                        <div class="detail-value">
                            <c:choose>
                                <c:when test="${region.level == 1}">
                                    <span class="tag tag-blue">省份（一级）</span>
                                </c:when>
                                <c:when test="${region.level == 2}">
                                    <span class="tag tag-green">城市（二级）</span>
                                </c:when>
                                <c:when test="${region.level == 3}">
                                    <span class="tag tag-gray">区县（三级）</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="tag tag-gray">未知</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">上级区域：</div>
                        <div class="detail-value"><c:out value="${parentName}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">排序：</div>
                        <div class="detail-value"><c:out value="${region.sort}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">状态：</div>
                        <div class="detail-value">
                            <c:choose>
                                <c:when test="${region.status == 1}">
                                    <span class="tag tag-green">启用</span>
                                </c:when>
                                <c:when test="${region.status == 0}">
                                    <span class="tag tag-gray">禁用</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="tag tag-gray">未知</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">创建时间：</div>
                        <div class="detail-value"><c:out value="${region.createTime}" /></div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-label">更新时间：</div>
                        <div class="detail-value"><c:out value="${region.updateTime}" /></div>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${ctx}/manage/region?action=edit&id=${region.id}" class="btn btn-warning">编辑</a>
                    <a href="${ctx}/manage/region" class="btn btn-default">返回列表</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
