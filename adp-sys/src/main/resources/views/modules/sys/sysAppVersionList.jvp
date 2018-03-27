<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('appVersion.manager')}</title>
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
					btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}'] //按钮
					}, function(){
						
						location.href=href;
					});
			}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
	
		<li class="active"><a href="${ctx}/sys/sysAppVersion/">${fns:i18nMessage('appVersion.managementList')}</a></li>
		<shiro:hasPermission name="sys:sysAppVersion:edit"><li><a href="${ctx}/sys/sysAppVersion/form">${fns:i18nMessage('appVersion.manager')}${fns:i18nMessage('common.add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysAppVersion" action="${ctx}/sys/sysAppVersion/" method="post" class="breadcrumb form-search form-horizontal">
			<form:hidden path="id"/>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
			  <label class="col-sm-1 control-label">${fns:i18nMessage('appVersion.projectName')}</label>
			  <div class="col-sm-2">
				<form:input path="appname"  class="input-medium form-control" htmlEscape="false" maxlength="255" />
		     </div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
				<th>${fns:i18nMessage('appVersion.projectName')}</th>
			    <th>${fns:i18nMessage('appVersion.currentVersion')}</th>
			    <th>${fns:i18nMessage('appVersion.updateTime')}</th>
					<shiro:hasPermission name="sys:sysAppVersion:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysAppVersion">
				<tr>
				<td><a href="${ctx}/sys/sysAppVersion/form?id=${sysAppVersion.appname}"/>
				${sysAppVersion.appname}</td>
				<td>${sysAppVersion.version}</td>
				<td><fmt:formatDate value="${sysAppVersion.uptime}" type="both" /></td>
					<shiro:hasPermission name="sys:sysAppVersion:edit"><td>
	    				<a href="${ctx}/sys/sysAppVersion/form?id=${sysAppVersion.appname}">${fns:i18nMessage('common.modify')}</a>
					    <a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('appVersion.deleteTip')}？', '${ctx}/sys/sysAppVersion/delete?appname=${sysAppVersion.appname}')">${fns:i18nMessage('common.delete')}</a>
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