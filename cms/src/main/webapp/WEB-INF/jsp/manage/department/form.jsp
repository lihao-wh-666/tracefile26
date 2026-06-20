<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    request.setAttribute("ctx", path);
    String formAction = request.getAttribute("formAction") != null ? (String) request.getAttribute("formAction") : "add";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= "add".equals(formAction) ? "新增营业部" : "编辑营业部" %> - 非现场开户管理平台</title>
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
                <div class="card-title"><%= "add".equals(formAction) ? "新增营业部" : "编辑营业部" %></div>

                <form method="post" action="${ctx}/manage/department">
                    <input type="hidden" name="action" value="<%= formAction %>">
                    <c:if test="${department != null}">
                        <input type="hidden" name="id" value="${department.id}">
                    </c:if>

                    <div class="form-row">
                        <div class="form-group">
                            <label>营业部编号</label>
                            <input type="text" name="deptNo" class="form-control"
                                   value="${department != null ? department.deptNo : ''}" placeholder="自动生成，可手动输入">
                        </div>

                        <div class="form-group">
                            <label>营业部名称 <span class="required-star">*</span></label>
                            <input type="text" name="deptName" class="form-control" required
                                   value="${department != null ? department.deptName : ''}" placeholder="请输入营业部名称">
                        </div>

                        <div class="form-group">
                            <label>联系电话 <span class="required-star">*</span></label>
                            <input type="tel" name="phone" class="form-control" required
                                   value="${department != null ? department.phone : ''}" placeholder="请输入联系电话">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>所在省份 <span class="required-star">*</span></label>
                            <input type="text" name="province" class="form-control" required
                                   value="${department != null ? department.province : ''}" placeholder="请输入所在省份">
                        </div>

                        <div class="form-group">
                            <label>所在城市 <span class="required-star">*</span></label>
                            <input type="text" name="city" class="form-control" required
                                   value="${department != null ? department.city : ''}" placeholder="请输入所在城市">
                        </div>

                        <div class="form-group">
                            <label>状态</label>
                            <select name="status" class="form-control">
                                <option value="1" ${department != null && department.status == 1 ? 'selected' : ''}>启用</option>
                                <option value="0" ${department != null && department.status == 0 ? 'selected' : ''}>禁用</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group" style="flex: 2;">
                            <label>营业部地址 <span class="required-star">*</span></label>
                            <input type="text" name="address" class="form-control" required
                                   value="${department != null ? department.address : ''}" placeholder="请输入详细地址">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group" style="flex: 2;">
                            <label>营业部介绍</label>
                            <textarea name="introduction" class="form-control" rows="4" placeholder="请输入营业部介绍">${department != null ? department.introduction : ''}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${ctx}/manage/department" class="btn btn-default">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
