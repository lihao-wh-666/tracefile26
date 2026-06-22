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
    <title><%= "add".equals(formAction) ? "新增区域" : "编辑区域" %> - 非现场开户管理平台</title>
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
                <div class="card-title"><%= "add".equals(formAction) ? "新增区域" : "编辑区域" %></div>

                <form method="post" action="${ctx}/manage/region">
                    <input type="hidden" name="action" value="<%= formAction %>">
                    <c:if test="${region != null}">
                        <input type="hidden" name="id" value="${region.id}">
                    </c:if>

                    <div class="form-row">
                        <div class="form-group">
                            <label>区域编码 <span class="required-star">*</span></label>
                            <input type="text" name="regionCode" class="form-control" required
                                   value="${region != null ? region.regionCode : ''}" placeholder="请输入区域编码，如110000">
                        </div>

                        <div class="form-group">
                            <label>区域名称 <span class="required-star">*</span></label>
                            <input type="text" name="regionName" class="form-control" required
                                   value="${region != null ? region.regionName : ''}" placeholder="请输入区域名称">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label>层级 <span class="required-star">*</span></label>
                            <select name="level" id="levelSelect" class="form-control" required onchange="onLevelChange()">
                                <option value="">请选择层级</option>
                                <option value="1" ${region != null && region.level == 1 ? 'selected' : ''}>省份（一级）</option>
                                <option value="2" ${region != null && region.level == 2 ? 'selected' : ''}>城市（二级）</option>
                                <option value="3" ${region != null && region.level == 3 ? 'selected' : ''}>区县（三级）</option>
                            </select>
                        </div>

                        <div class="form-group" id="provinceGroup" style="display:none;">
                            <label>所属省份</label>
                            <select name="parentId" id="provinceSelect" class="form-control" onchange="onProvinceChange()">
                                <option value="">请选择省份</option>
                                <c:forEach items="${provinceList}" var="p">
                                    <option value="${p.id}" ${region != null && region.parentId == p.id ? 'selected' : ''}>${p.regionName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" id="cityGroup" style="display:none;">
                            <label>所属城市</label>
                            <select name="parentId" id="citySelect" class="form-control">
                                <option value="">请选择城市</option>
                                <c:if test="${cityList != null}">
                                    <c:forEach items="${cityList}" var="c">
                                        <option value="${c.id}" ${region != null && region.parentId == c.id ? 'selected' : ''}>${c.regionName}</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>排序</label>
                            <input type="number" name="sort" class="form-control"
                                   value="${region != null && region.sort != null ? region.sort : 0}" placeholder="数字越小越靠前">
                        </div>

                        <div class="form-group">
                            <label>状态</label>
                            <select name="status" class="form-control">
                                <option value="1" ${region != null && region.status == 1 ? 'selected' : ''}>启用</option>
                                <option value="0" ${region != null && region.status == 0 ? 'selected' : ''}>禁用</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${ctx}/manage/region" class="btn btn-default">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}/static/js/app.js"></script>
<script>
function onLevelChange() {
    var level = document.getElementById('levelSelect').value;
    var provinceGroup = document.getElementById('provinceGroup');
    var cityGroup = document.getElementById('cityGroup');
    var provinceSelect = document.getElementById('provinceSelect');
    var citySelect = document.getElementById('citySelect');

    provinceGroup.style.display = 'none';
    cityGroup.style.display = 'none';
    provinceSelect.name = 'parentId_disabled';
    citySelect.name = 'parentId_disabled';

    if (level == '2') {
        provinceGroup.style.display = '';
        provinceSelect.name = 'parentId';
    } else if (level == '3') {
        provinceGroup.style.display = '';
        cityGroup.style.display = '';
        provinceSelect.name = 'provinceId_temp';
        citySelect.name = 'parentId';
    }
}

function onProvinceChange() {
    var provinceId = document.getElementById('provinceSelect').value;
    var citySelect = document.getElementById('citySelect');
    citySelect.innerHTML = '<option value="">请选择城市</option>';

    if (provinceId) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '${ctx}/manage/region?action=list&parentId=' + provinceId + '&level=2&pageSize=1000', true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var parser = new DOMParser();
                var doc = parser.parseFromString(xhr.responseText, 'text/html');
                var rows = doc.querySelectorAll('tbody tr');
                rows.forEach(function(row) {
                    var tds = row.querySelectorAll('td');
                    if (tds.length >= 3) {
                        var id = '';
                        var name = tds[2].textContent.trim();
                        var links = row.querySelectorAll('a');
                        links.forEach(function(a) {
                            var href = a.getAttribute('href');
                            if (href && href.indexOf('action=edit') > -1) {
                                var match = href.match(/id=(\d+)/);
                                if (match) id = match[1];
                            }
                        });
                        if (id) {
                            var option = document.createElement('option');
                            option.value = id;
                            option.textContent = name;
                            citySelect.appendChild(option);
                        }
                    }
                });
            }
        };
        xhr.send();
    }
}

window.onload = function() {
    onLevelChange();
    <c:if test="${region != null && region.level == 3}">
        var provinceSelect = document.getElementById('provinceSelect');
        if (provinceSelect.value) {
            onProvinceChange();
            setTimeout(function() {
                var citySelect = document.getElementById('citySelect');
                var regionParentId = '${region.parentId}';
                for (var i = 0; i < citySelect.options.length; i++) {
                    if (citySelect.options[i].value == regionParentId) {
                        citySelect.selectedIndex = i;
                        break;
                    }
                }
            }, 200);
        }
    </c:if>
};
</script>
</body>
</html>
