<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.opening.utils.PageResult" %>
<%@ page import="com.opening.entity.Department" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    PageResult<Department> pageResult = (PageResult<Department>) request.getAttribute("pageResult");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>营业部管理 - 非现场开户管理平台</title>
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
                <div class="card-title">营业部列表</div>

                <div class="toolbar">
                    <a href="${ctx}/manage/department?action=add" class="btn btn-success">+ 新增营业部</a>
                    <form class="search-form" method="get" action="${ctx}/manage/department">
                        <div class="form-group">
                            <label>营业部名称</label>
                            <input type="text" name="deptName" class="form-control" value="${deptName}" placeholder="请输入营业部名称" style="width: 200px;">
                        </div>
                        <div class="form-group">
                            <label>省份</label>
                            <input type="text" name="province" class="form-control" value="${province}" placeholder="请输入省份" style="width: 120px;">
                        </div>
                        <div class="form-group">
                            <label>城市</label>
                            <input type="text" name="city" class="form-control" value="${city}" placeholder="请输入城市" style="width: 120px;">
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
                        <a href="${ctx}/manage/department" class="btn btn-default btn-sm">重置</a>
                    </form>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>营业部编号</th>
                        <th>营业部名称</th>
                        <th>营业部地址</th>
                        <th>营业部电话</th>
                        <th>营业部介绍</th>
                        <th>所在省</th>
                        <th>所在市</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty pageResult.list}">
                            <c:forEach items="${pageResult.list}" var="department" varStatus="status">
                                <tr>
                                    <td><c:out value="${(pageResult.pageNum - 1) * pageResult.pageSize + status.index + 1}" /></td>
                                    <td><c:out value="${department.deptNo}" /></td>
                                    <td><c:out value="${department.deptName}" /></td>
                                    <td><c:out value="${department.address}" /></td>
                                    <td><c:out value="${department.phone}" /></td>
                                    <td><c:out value="${department.introduction}" /></td>
                                    <td><c:out value="${department.province}" /></td>
                                    <td><c:out value="${department.city}" /></td>
                                    <td>
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
                                    </td>
                                    <td class="action-btns">
                                        <a href="${ctx}/manage/department?action=view&id=${department.id}" class="btn btn-default btn-sm">查看</a>
                                        <a href="${ctx}/manage/department?action=edit&id=${department.id}" class="btn btn-warning btn-sm">编辑</a>
                                        <a href="javascript:confirmDelete(${department.id})" class="btn btn-danger btn-sm">删除</a>
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
                            <a href="${ctx}/manage/department?pageNum=1&deptName=${deptName}&province=${province}&city=${city}&status=${status}">首页</a>
                            <a href="${ctx}/manage/department?pageNum=${pageResult.pageNum - 1}&deptName=${deptName}&province=${province}&city=${city}&status=${status}">上一页</a>
                        </c:if>
                        <c:if test="${!pageResult.hasPrevious}">
                            <span class="disabled">首页</span>
                            <span class="disabled">上一页</span>
                        </c:if>

                        <span class="current">${pageResult.pageNum}</span>

                        <c:if test="${pageResult.hasNext}">
                            <a href="${ctx}/manage/department?pageNum=${pageResult.pageNum + 1}&deptName=${deptName}&province=${province}&city=${city}&status=${status}">下一页</a>
                            <a href="${ctx}/manage/department?pageNum=${pageResult.pages}&deptName=${deptName}&province=${province}&city=${city}&status=${status}">末页</a>
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
    if (confirm('确定要删除该营业部吗？删除后无法恢复！')) {
        window.location.href = '${ctx}/manage/department?action=delete&id=' + id;
    }
}
</script>
</body>
</html>
