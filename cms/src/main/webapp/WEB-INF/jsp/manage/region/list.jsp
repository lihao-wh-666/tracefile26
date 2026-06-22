<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.opening.utils.PageResult" %>
<%@ page import="com.opening.entity.Region" %>
<%@ page import="com.opening.service.RegionService" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    PageResult<Region> pageResult = (PageResult<Region>) request.getAttribute("pageResult");
    RegionService regionService = (RegionService) request.getAttribute("regionService");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>省市区管理 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css?v=20260620">
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
                <div class="card-title">省市区列表</div>

                <div class="toolbar">
                    <a href="${ctx}/manage/region?action=add" class="btn btn-success">+ 新增区域</a>
                    <form class="search-form" method="get" action="${ctx}/manage/region">
                        <div class="form-group">
                            <label>区域名称</label>
                            <input type="text" name="regionName" class="form-control" value="${regionName}" placeholder="请输入区域名称" style="width: 200px;">
                        </div>
                        <div class="form-group">
                            <label>层级</label>
                            <select name="level" class="form-control">
                                <option value="">全部</option>
                                <option value="1" ${level != null && level == 1 ? 'selected' : ''}>省份</option>
                                <option value="2" ${level != null && level == 2 ? 'selected' : ''}>城市</option>
                                <option value="3" ${level != null && level == 3 ? 'selected' : ''}>区县</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>状态</label>
                            <select name="status" class="form-control">
                                <option value="">全部</option>
                                <option value="1" ${status != null && status == 1 ? 'selected' : ''}>启用</option>
                                <option value="0" ${status != null && status == 0 ? 'selected' : ''}>禁用</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                        <a href="${ctx}/manage/region" class="btn btn-default btn-sm">重置</a>
                    </form>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>区域编码</th>
                        <th>区域名称</th>
                        <th>层级</th>
                        <th>上级区域</th>
                        <th>排序</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageResult.list}">
                            <c:forEach items="${pageResult.list}" var="region" varStatus="status">
                                <tr>
                                    <td><c:out value="${(pageResult.pageNum - 1) * pageResult.pageSize + status.index + 1}" /></td>
                                    <td><c:out value="${region.regionCode}" /></td>
                                    <td><c:out value="${region.regionName}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${region.level == 1}">
                                                <span class="tag tag-blue">省份</span>
                                            </c:when>
                                            <c:when test="${region.level == 2}">
                                                <span class="tag tag-green">城市</span>
                                            </c:when>
                                            <c:when test="${region.level == 3}">
                                                <span class="tag tag-gray">区县</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="tag tag-gray">未知</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${region.level == 1}">
                                                <span>-</span>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${regionNameMap[region.parentId]}" default="未知" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${region.sort}" /></td>
                                    <td>
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
                                    </td>
                                    <td class="action-btns">
                                        <a href="${ctx}/manage/region?action=view&id=${region.id}" class="btn btn-default btn-sm">查看</a>
                                        <a href="${ctx}/manage/region?action=edit&id=${region.id}" class="btn btn-warning btn-sm">编辑</a>
                                        <a href="javascript:confirmDelete(${region.id})" class="btn btn-danger btn-sm">删除</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="8" class="empty-row">暂无数据</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>

                <c:if test="${not empty pageResult.list}">
                    <div class="pagination">
                        <span>共 ${pageResult.total} 条，第 ${pageResult.pageNum}/${pageResult.pages} 页</span>

                        <c:if test="${pageResult.hasPrevious}">
                            <a href="${ctx}/manage/region?pageNum=1&regionName=${regionName}&level=${level}&status=${status}">首页</a>
                            <a href="${ctx}/manage/region?pageNum=${pageResult.pageNum - 1}&regionName=${regionName}&level=${level}&status=${status}">上一页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasPrevious}">
                            <span class="disabled">首页</span>
                            <span class="disabled">上一页</span>
                        </c:if>

                        <span class="current">${pageResult.pageNum}</span>

                        <c:if test="${pageResult.hasNext}">
                            <a href="${ctx}/manage/region?pageNum=${pageResult.pageNum + 1}&regionName=${regionName}&level=${level}&status=${status}">下一页</a>
                            <a href="${ctx}/manage/region?pageNum=${pageResult.pages}&regionName=${regionName}&level=${level}&status=${status}">末页</a>
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
<script>
function confirmDelete(id) {
    if (confirm('确定要删除该区域吗？删除后无法恢复！')) {
        window.location.href = '${ctx}/manage/region?action=delete&id=' + id;
    }
}
</script>
</body>
</html>
