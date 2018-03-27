<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				  btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
				}, function(){
					location.href=href;
				});
		}
		
		function toSystemList(){
			location.href="${ctx}/sys/sysProperties/systemList"
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/sysProperties/">${fns:i18nMessage('sys.properties.list')}</a></li>
		<shiro:hasPermission name="sys:sysProperties:edit"><li><a href="${ctx}/sys/sysProperties/form">${fns:i18nMessage('sys.properties.add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysProperties" action="${ctx}/sys/sysProperties/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">key：</label>
				<div class="col-sm-2">
					<form:input path="key" htmlEscape="false" maxlength="200" class="form-control input-medium"/>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
				<input id="btnViewSys" class="btn btn-primary" type="button" value="${fns:i18nMessage('sys.properties.view.sys')}" onclick="toSystemList()"/>
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>key</th>
					<th>value</th>
					<th>${fns:i18nMessage('app.updateTime')}</th>
					<shiro:hasPermission name="sys:sysProperties:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysProperties">
				<tr>
					<td>
						${sysProperties.key}
					</td>
					<td>
						${sysProperties.value}
					</td>
					<td>
						<fmt:formatDate value="${sysProperties.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<shiro:hasPermission name="sys:sysProperties:edit"><td>
					<c:if test="${'Y' eq sysProperties.modifyBy}">
	    				<a href="${ctx}/sys/sysProperties/form?id=${sysProperties.id}">${fns:i18nMessage('common.modify')}</a>
						<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('sys.properties.delete.tip')}', '${ctx}/sys/sysProperties/delete?id=${sysProperties.id}')">${fns:i18nMessage('common.delete')}</a>
						</c:if>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		${page}
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>