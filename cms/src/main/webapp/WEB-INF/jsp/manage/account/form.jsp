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
    <title><%= "add".equals(formAction) ? "开立账户" : "编辑账户" %> - 非现场开户管理平台</title>
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
                <div class="card-title"><%= "add".equals(formAction) ? "开立账户" : "编辑账户" %></div>

                <form method="post" action="${ctx}/manage/account">
                    <input type="hidden" name="action" value="<%= formAction %>">
                    <c:if test="${account != null}">
                        <input type="hidden" name="id" value="${account.id}">
                    </c:if>

                    <div class="form-row">
                        <div class="form-group">
                            <label>选择客户 <span class="required-star">*</span></label>
                            <select name="customerId" class="form-control" required>
                                <option value="">请选择客户（仅显示已审核通过的客户）</option>
                                <c:forEach items="${customers}" var="customer">
                                    <option value="${customer.id}"
                                        ${(account != null && account.customerId == customer.id) || (selectedCustomerId != null && selectedCustomerId == customer.id) ? 'selected' : ''}>
                                        ${customer.customerNo} - ${customer.realName} - ${customer.phone}
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${empty customers}">
                                <div class="hint-text">暂无已审核通过的客户，请先审核客户信息</div>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label>账户类型 <span class="required-star">*</span></label>
                            <select name="accountType" class="form-control" required>
                                <option value="NORMAL" ${account != null && account.accountType == 'NORMAL' ? 'selected' : ''}>普通账户</option>
                                <option value="VIP" ${account != null && account.accountType == 'VIP' ? 'selected' : ''}>VIP账户</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>币种</label>
                            <select name="currency" class="form-control">
                                <option value="CNY" ${account != null && account.currency == 'CNY' ? 'selected' : ''}>人民币 (CNY)</option>
                                <option value="USD" ${account != null && account.currency == 'USD' ? 'selected' : ''}>美元 (USD)</option>
                                <option value="EUR" ${account != null && account.currency == 'EUR' ? 'selected' : ''}>欧元 (EUR)</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>初始余额</label>
                            <input type="number" name="balance" class="form-control" step="0.01" min="0"
                                   value="${account != null && account.balance != null ? account.balance : '0.00'}">
                        </div>

                        <div class="form-group">
                            <label>开户方式</label>
                            <select name="openWay" class="form-control">
                                <option value="ONLINE" ${account != null && account.openWay == 'ONLINE' ? 'selected' : ''}>线上</option>
                                <option value="OFFLINE" ${account != null && account.openWay == 'OFFLINE' ? 'selected' : ''}>线下</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>开户渠道</label>
                            <select name="openChannel" class="form-control">
                                <option value="WEB" ${account != null && account.openChannel == 'WEB' ? 'selected' : ''}>WEB端</option>
                                <option value="APP" ${account != null && account.openChannel == 'APP' ? 'selected' : ''}>APP</option>
                                <option value="H5" ${account != null && account.openChannel == 'H5' ? 'selected' : ''}>H5</option>
                            </select>
                        </div>
                    </div>

                    <c:if test="${formAction == 'edit'}">
                        <div class="form-row">
                            <div class="form-group">
                                <label>账户状态</label>
                                <select name="status" class="form-control">
                                    <option value="0" ${account != null && account.status == 0 ? 'selected' : ''}>待激活</option>
                                    <option value="1" ${account != null && account.status == 1 ? 'selected' : ''}>正常</option>
                                    <option value="2" ${account != null && account.status == 2 ? 'selected' : ''}>冻结</option>
                                    <option value="3" ${account != null && account.status == 3 ? 'selected' : ''}>销户</option>
                                </select>
                            </div>
                        </div>
                    </c:if>

                    <div class="form-row">
                        <div class="form-group" style="flex: 2;">
                            <label>备注</label>
                            <textarea name="remark" class="form-control" rows="3" placeholder="请输入备注信息">${account != null ? account.remark : ''}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${ctx}/manage/account" class="btn btn-default">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
