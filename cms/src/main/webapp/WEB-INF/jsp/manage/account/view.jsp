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
    <title>账户详情 - 非现场开户管理平台</title>
    <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body>
<div class="layout">
    <jsp:include page="../common/sidebar.jsp" />
    <div class="main">
        <jsp:include page="../common/header.jsp" />
        <div class="content">
            <div class="card">
                <div class="card-title">账户基本信息</div>

                <div class="detail-row">
                    <div class="detail-label">账户编号</div>
                    <div class="detail-value"><c:out value="${account.accountNo}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">客户编号</div>
                    <div class="detail-value"><c:out value="${account.customerNo}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">客户姓名</div>
                    <div class="detail-value"><c:out value="${account.customerName}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">账户类型</div>
                    <div class="detail-value">
                        <span class="tag ${account.accountType == 'VIP' ? 'tag-orange' : 'tag-blue'}">
                            <c:out value="${account.accountTypeDesc}" />
                        </span>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">币种</div>
                    <div class="detail-value"><c:out value="${account.currency}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">账户余额</div>
                    <div class="detail-value" style="font-size: 18px; font-weight: 600; color: #1890ff;">
                        <c:out value="${account.balance}" />
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">冻结余额</div>
                    <div class="detail-value"><c:out value="${account.frozenBalance}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">可用余额</div>
                    <div class="detail-value" style="font-weight: 600; color: #52c41a;">
                        <c:out value="${account.availableBalance}" />
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">账户状态</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${account.status == 0}">
                                <span class="tag tag-gray">待激活</span>
                            </c:when>
                            <c:when test="${account.status == 1}">
                                <span class="tag tag-green">正常</span>
                            </c:when>
                            <c:when test="${account.status == 2}">
                                <span class="tag tag-red">冻结</span>
                            </c:when>
                            <c:when test="${account.status == 3}">
                                <span class="tag tag-red">销户</span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">开户方式</div>
                    <div class="detail-value"><c:out value="${account.openWayDesc}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">开户渠道</div>
                    <div class="detail-value"><c:out value="${account.openChannel}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">开户时间</div>
                    <div class="detail-value"><c:out value="${account.openTime}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">最后交易时间</div>
                    <div class="detail-value"><c:out value="${account.lastTransTime}" /></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">备注</div>
                    <div class="detail-value"><c:out value="${account.remark}" /></div>
                </div>

                <div class="form-actions">
                    <a href="${ctx}/manage/account" class="btn btn-default">返回列表</a>
                    <a href="${ctx}/manage/account?action=edit&id=${account.id}" class="btn btn-warning">编辑</a>
                    <a href="${ctx}/manage/customer?action=view&id=${account.customerId}" class="btn btn-primary">查看客户</a>
                </div>
            </div>

            <c:if test="${not empty customerAccounts}">
                <div class="card">
                    <div class="card-title">该客户其他账户</div>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>账户编号</th>
                            <th>账户类型</th>
                            <th>币种</th>
                            <th>账户余额</th>
                            <th>可用余额</th>
                            <th>账户状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${customerAccounts}" var="acc">
                            <c:if test="${acc.id != account.id}">
                                <tr>
                                    <td><c:out value="${acc.accountNo}" /></td>
                                    <td><c:out value="${acc.accountTypeDesc}" /></td>
                                    <td><c:out value="${acc.currency}" /></td>
                                    <td style="text-align: right;"><c:out value="${acc.balance}" /></td>
                                    <td style="text-align: right;"><c:out value="${acc.availableBalance}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${acc.status == 1}">
                                                <span class="tag tag-green">正常</span>
                                            </c:when>
                                            <c:when test="${acc.status == 2}">
                                                <span class="tag tag-red">冻结</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="tag tag-gray">其他</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${ctx}/manage/account?action=view&id=${acc.id}" class="btn btn-default btn-sm">查看</a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
