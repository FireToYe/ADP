<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('appSql.management')}应用sql管理</title>
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/sysAppSql/">${fns:i18nMessage('appSql.managementList')}</a></li>
		<%-- <shiro:hasPermission name="sys:sysAppSql:edit"><li><a href="${ctx}/sys/sysAppSql/form">应用sql添加</a></li></shiro:hasPermission> --%>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysAppSql" action="${ctx}/sys/sysAppSql/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
			  <label class="col-sm-1 control-label">${fns:i18nMessage('appVersion.projectName')}</label>
			  <div class="col-sm-2">
				<form:input path="appname"  class="input-medium form-control" htmlEscape="false" maxlength="255" />
		     </div>
			  <label class="col-sm-1 control-label">${fns:i18nMessage('appVersion.currentVersion')}</label>
			  <div class="col-sm-2">
				<form:input path="version"  class="input-medium form-control" htmlEscape="false" maxlength="255" />
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
			    <th>${fns:i18nMessage('appSql.normalWork')}</th>
<!-- 			<th>sql脚本</th>
			    <th>反向sql脚本</th>
			    <th>出错sql脚本</th> -->
					<shiro:hasPermission name="sys:sysAppSql:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysAppSql">
				<tr>
				<td><a href="${ctx}/sys/sysAppSql/form?id=${sysAppSql.id}"/>
				${sysAppSql.appname}</td>
				<td>${sysAppSql.version}</td>
				<td>${empty sysAppSql.sqlerror?fns:i18nMessage('common.yes'):fns:i18nMessage('common.no')}</td>
<%-- 				<td>${sysAppSql.sqltext}</td>
				<td>${sysAppSql.rollbacktext}</td>
				<td>${sysAppSql.sqlerror}</td> --%>
					<shiro:hasPermission name="sys:sysAppSql:edit"><td>
	    				<a href="${ctx}/sys/sysAppSql/form?id=${sysAppSql.id}">${fns:i18nMessage('common.modify')}</a>
						<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('appSql.deleteTip')}', '${ctx}/sys/sysAppSql/delete?id=${sysAppSql.id}')">${fns:i18nMessage('common.delete')}</a>
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