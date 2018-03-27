<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%-- <c:set var="cxt" value="${pageContext.request.contextPath}${fns:getApiPath()}"/> --%>
<html>
<head>
	<title>备份方案</title>
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
		function deleteByFile(tip,href){
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
		function restore(tip,href){
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
		<li class="active"><a href="${ctx}/db/dbBackupScheme/history">${fns:i18nMessage('adp_db.dbBackUpScheme_record')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="dbBackupScheme" action="${ctx}/db/dbBackupScheme/history" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
<!-- 			<div class="form-group">
				<label class="col-sm-1 control-label">方案名称</label>
			    <div class="col-sm-2">
					<form:input path="schemeName"  class="input-medium form-control" htmlEscape="false" maxlength="255" />
		        </div>
		        <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
				<input type="button" class="btn btn-primary"  onclick="backupAll()" value="备份全库" />
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="选库备份" />
			</div> -->
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th>${fns:i18nMessage('adp_db.file_name')}</th>
			<!-- 		<th>备份表</th> -->
					<shiro:hasPermission name="db:dbBackupScheme:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="fileName">
				<tr>
	<%-- 				<td>${dbBackupScheme.backupTables}</td> --%>
					<td>${fileName}</td>
					<shiro:hasPermission name="db:dbBackupScheme:edit"><td>
						<a href="javascript:void(0);" onclick="restore('${fns:i18nMessage('adp_db.restore_tip')}', '${ctx}/db/dbBackupScheme/restore?fileName=${fileName}')">${fns:i18nMessage('adp_db.restore')}</a>
						<%-- <a href="javascript:void(0);" onclick="deleteAtId('确认要删除这条记录吗？', '${ctx}/db/dbBackupScheme/deleteByFile?fileName=${fileName}')">删除</a> --%>
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