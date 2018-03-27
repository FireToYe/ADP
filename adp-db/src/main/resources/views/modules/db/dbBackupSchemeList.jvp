<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%-- <c:set var="cxt" value="${pageContext.request.contextPath}${fns:getApiPath()}"/> --%>
<html>
<head>
	<title>${fns:i18nMessage('adp_db.dbBackUpScheme')}</title>
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
		function backupAll(){
				$.ajax({url:"${cxt}/sys/db/backupAll"});
 		}
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
				}, function(){
					location.href=href;
				});
		}
		function doBackUpTables(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
				}, function(){
					$.ajax({
						url:href,
						type:'get',
						success:function(msg){
							layer.msg(msg);
						}
					});
				});
		}
		
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/db/dbBackupScheme/">${fns:i18nMessage('adp_db.dbBackUpScheme_list')}</a></li>
		<shiro:hasPermission name="db:dbBackupScheme:edit"><li><a href="${ctx}/db/dbBackupScheme/form">${fns:i18nMessage('adp_db.dbBackUpScheme_add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="dbBackupScheme" action="${ctx}/db/dbBackupScheme/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('adb_db.schemeName')}</label>
			    <div class="col-sm-2">
					<form:input path="schemeName"  class="input-medium form-control" htmlEscape="false" maxlength="255" />
		        </div>
		        <input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
<!-- 				<input type="button" class="btn btn-primary"  onclick="backupAll()" value="备份全库" />
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="选库备份" /> -->
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th>${fns:i18nMessage('adb_db.schemeName')}</th>
			<!-- 		<th>备份表</th> -->
					<th>${fns:i18nMessage('adb_db.remarks')}</th>
					<th>${fns:i18nMessage('adb_db.updateDate')}</th>
					<shiro:hasPermission name="db:dbBackupScheme:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="dbBackupScheme">
				<tr>
					<td><a href="${ctx}/db/dbBackupScheme/form?id=${dbBackupScheme.id}">
						${dbBackupScheme.schemeName}
					</a></td>
	<%-- 				<td>${dbBackupScheme.backupTables}</td> --%>
					<td>${dbBackupScheme.remarks}</td>
					<td>
						<fmt:formatDate value="${dbBackupScheme.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<shiro:hasPermission name="db:dbBackupScheme:edit"><td>
	    				<a href="${ctx}/db/dbBackupScheme/form?id=${dbBackupScheme.id}">${fns:i18nMessage('common.modify')}</a>
						<a href="javascript:void(0);" onclick="doBackUpTables('${fns:i18nMessage('adb_db.do_back_up_tip')}', '${ctx}/db/dbBackupScheme/doBackUpTables?id=${dbBackupScheme.id}')">${fns:i18nMessage('adp_db.dbBackUp')}</a>
						<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('adb_db.delete_tip')}', '${ctx}/db/dbBackupScheme/delete?id=${dbBackupScheme.id}')">${fns:i18nMessage('common.delete')}</a>
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
</body>
</html>