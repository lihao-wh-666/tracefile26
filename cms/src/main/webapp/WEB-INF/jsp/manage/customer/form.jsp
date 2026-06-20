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
    <title><%= "add".equals(formAction) ? "新增客户" : "编辑客户" %> - 非现场开户管理平台</title>
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
                <div class="card-title"><%= "add".equals(formAction) ? "新增客户" : "编辑客户" %></div>

                <form method="post" action="${ctx}/manage/customer">
                    <input type="hidden" name="action" value="<%= formAction %>">
                    <c:if test="${customer != null}">
                        <input type="hidden" name="id" value="${customer.id}">
                    </c:if>

                    <div class="form-row">
                        <div class="form-group">
                            <label>姓名 <span class="required-star">*</span></label>
                            <input type="text" name="realName" class="form-control" required
                                   value="${customer != null ? customer.realName : ''}" placeholder="请输入真实姓名">
                        </div>

                        <div class="form-group">
                            <label>性别</label>
                            <select name="gender" class="form-control">
                                <option value="M" ${customer != null && customer.gender == 'M' ? 'selected' : ''}>男</option>
                                <option value="F" ${customer != null && customer.gender == 'F' ? 'selected' : ''}>女</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>出生日期</label>
                            <input type="date" name="birthDate" class="form-control"
                                   value="${customer != null && customer.birthDate != null ? customer.birthDate : ''}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>证件类型 <span class="required-star">*</span></label>
                            <select name="idType" class="form-control" required>
                                <option value="ID_CARD" ${customer != null && customer.idType == 'ID_CARD' ? 'selected' : ''}>身份证</option>
                                <option value="PASSPORT" ${customer != null && customer.idType == 'PASSPORT' ? 'selected' : ''}>护照</option>
                                <option value="OTHER" ${customer != null && customer.idType == 'OTHER' ? 'selected' : ''}>其他</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>证件号码 <span class="required-star">*</span></label>
                            <input type="text" name="idNo" class="form-control" required
                                   value="${customer != null ? customer.idNo : ''}" placeholder="请输入证件号码">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>手机号码 <span class="required-star">*</span></label>
                            <input type="tel" name="phone" class="form-control" required
                                   value="${customer != null ? customer.phone : ''}" placeholder="请输入手机号码">
                        </div>

                        <div class="form-group">
                            <label>电子邮箱</label>
                            <input type="email" name="email" class="form-control"
                                   value="${customer != null ? customer.email : ''}" placeholder="请输入电子邮箱">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>联系地址</label>
                            <input type="text" name="address" class="form-control"
                                   value="${customer != null ? customer.address : ''}" placeholder="请输入联系地址">
                        </div>

                        <div class="form-group">
                            <label>职业</label>
                            <input type="text" name="occupation" class="form-control"
                                   value="${customer != null ? customer.occupation : ''}" placeholder="请输入职业">
                        </div>

                        <div class="form-group">
                            <label>国籍</label>
                            <input type="text" name="nationality" class="form-control"
                                   value="${customer != null && customer.nationality != null ? customer.nationality : '中国'}">
                        </div>
                    </div>

                    <c:if test="${formAction == 'edit'}">
                        <div class="form-row">
                            <div class="form-group">
                                <label>身份认证</label>
                                <select name="idVerified" class="form-control">
                                    <option value="0" ${customer != null && customer.idVerified == 0 ? 'selected' : ''}>未认证</option>
                                    <option value="1" ${customer != null && customer.idVerified == 1 ? 'selected' : ''}>已认证</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>人脸认证</label>
                                <select name="faceVerified" class="form-control">
                                    <option value="0" ${customer != null && customer.faceVerified == 0 ? 'selected' : ''}>未认证</option>
                                    <option value="1" ${customer != null && customer.faceVerified == 1 ? 'selected' : ''}>已认证</option>
                                </select>
                            </div>
                        </div>
                    </c:if>

                    <div class="form-row">
                        <div class="form-group" style="flex: 2;">
                            <label>备注</label>
                            <textarea name="remark" class="form-control" rows="3" placeholder="请输入备注信息">${customer != null ? customer.remark : ''}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${ctx}/manage/customer" class="btn btn-default">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
