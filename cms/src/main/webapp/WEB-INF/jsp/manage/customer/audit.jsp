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
    <title>客户审核 - 非现场开户管理平台</title>
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
                <div class="card-title">客户信息审核</div>

                <div class="detail-row">
                    <div class="detail-label">客户编号</div>
                    <div class="detail-value"><c:out value="${customer.customerNo}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">姓名</div>
                    <div class="detail-value"><c:out value="${customer.realName}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">性别</div>
                    <div class="detail-value"><c:out value="${customer.genderDesc}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">证件类型</div>
                    <div class="detail-value"><c:out value="${customer.idTypeDesc}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">证件号码</div>
                    <div class="detail-value"><c:out value="${customer.idNo}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">手机号码</div>
                    <div class="detail-value"><c:out value="${customer.phone}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">身份认证</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${customer.idVerified == 1}">
                                <span class="tag tag-green">已认证</span>
                            </c:when>
                            <c:otherwise>
                                <span class="tag tag-gray">未认证</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">人脸认证</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${customer.faceVerified == 1}">
                                <span class="tag tag-green">已认证</span>
                            </c:when>
                            <c:otherwise>
                                <span class="tag tag-gray">未认证</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-title">审核操作</div>

                <form method="post" action="${ctx}/manage/customer">
                    <input type="hidden" name="action" value="audit">
                    <input type="hidden" name="id" value="${customer.id}">

                    <div class="form-row">
                        <div class="form-group">
                            <label>审核结果 <span class="required-star">*</span></label>
                            <select name="auditStatus" class="form-control" required>
                                <option value="">请选择审核结果</option>
                                <option value="1">审核通过</option>
                                <option value="2">审核拒绝</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group" style="flex: 2;">
                            <label>审核意见</label>
                            <textarea name="auditResult" class="form-control" rows="4" placeholder="请输入审核意见"></textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">提交审核</button>
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
