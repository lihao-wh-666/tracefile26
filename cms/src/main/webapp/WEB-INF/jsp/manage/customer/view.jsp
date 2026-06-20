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
    <title>客户详情 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/tailwind-output.css">
</head>
<body>
<div class="layout">
    <jsp:include page="../common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="../common/header.jsp" />
        <div class="content">
            <div class="card">
                <div class="card-title">客户基本信息</div>

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
                    <div class="detail-label">出生日期</div>
                    <div class="detail-value"><c:out value="${customer.birthDate}" /></div>
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
                    <div class="detail-label">电子邮箱</div>
                    <div class="detail-value"><c:out value="${customer.email}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">联系地址</div>
                    <div class="detail-value"><c:out value="${customer.address}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">职业</div>
                    <div class="detail-value"><c:out value="${customer.occupation}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">国籍</div>
                    <div class="detail-value"><c:out value="${customer.nationality}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">客户状态</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${customer.status == 0}">
                                <span class="tag tag-gray">待审核</span>
                            </c:when>
                            <c:when test="${customer.status == 1}">
                                <span class="tag tag-green">审核通过</span>
                            </c:when>
                            <c:when test="${customer.status == 2}">
                                <span class="tag tag-red">审核拒绝</span>
                            </c:when>
                            <c:when test="${customer.status == 3}">
                                <span class="tag tag-red">已冻结</span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">审核状态</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${customer.auditStatus == 0}">
                                <span class="tag tag-gray">待审核</span>
                            </c:when>
                            <c:when test="${customer.auditStatus == 1}">
                                <span class="tag tag-orange">审核中</span>
                            </c:when>
                            <c:when test="${customer.auditStatus == 2}">
                                <span class="tag tag-green">已通过</span>
                            </c:when>
                            <c:when test="${customer.auditStatus == 3}">
                                <span class="tag tag-red">已拒绝</span>
                            </c:when>
                        </c:choose>
                    </div>
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
                <div class="detail-row">
                    <div class="detail-label">备注</div>
                    <div class="detail-value"><c:out value="${customer.remark}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">创建时间</div>
                    <div class="detail-value"><c:out value="${customer.createTime}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">更新时间</div>
                    <div class="detail-value"><c:out value="${customer.updateTime}" /></div>
                </div>

                <div class="form-actions">
                    <a href="${ctx}/manage/customer" class="btn btn-default">返回列表</a>
                    <c:if test="${customer.auditStatus != 2 && customer.auditStatus != 3}">
                        <a href="${ctx}/manage/customer?action=edit&id=${customer.id}" class="btn btn-warning">编辑</a>
                    </c:if>
                    <c:if test="${customer.auditStatus == 0 || customer.auditStatus == 1}">
                        <a href="${ctx}/manage/customer?action=audit&id=${customer.id}" class="btn btn-success">审核</a>
                    </c:if>
                    <c:if test="${customer.auditStatus == 2}">
                        <a href="${ctx}/manage/account?action=add&customerId=${customer.id}" class="btn btn-primary">开立账户</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
</body>
</html>
