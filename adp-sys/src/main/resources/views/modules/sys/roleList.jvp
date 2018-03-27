]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('role.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}'] //按钮
				}, function(){
					$("#listForm").attr("action",href);
					$("#listForm").submit();
					//location.reload();
				});
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/role/">${fns:i18nMessage('role.list')}</a></li>
		<shiro:hasPermission name="sys:role:edit"><li><a href="${ctx}/sys/role/form">${fns:i18nMessage('role.add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active">
			<div class="panel-body ">
	<sys:message content="${message}"/>
	<form id="listForm" method="post"></form>
	<table id="contentTable" class="table table-hover table-striped">
		<tr>
			<th>${fns:i18nMessage('role.name')}</th>
			<th>${fns:i18nMessage('role.englishName')}</th>
			<th>${fns:i18nMessage('role.office')}</th>
			<th>${fns:i18nMessage('role.dataRange')}</th>
			<shiro:hasPermission name="sys:role:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
		</tr>
		<c:forEach items="${list}" var="role">
			<tr>
				<td><a href="form?id=${role.id}">${role.name}</a></td>
				<td><a href="form?id=${role.id}">${role.enname}</a></td>
				<td>${role.office.name}</td>
				<td>${fns:getDictLabel(role.dataScope, 'sys_data_scope', '无')}</td>
				<shiro:hasPermission name="sys:role:edit"><td>
					<a href="${ctx}/sys/role/assign?id=${role.id}">${fns:i18nMessage('role.distribute')}</a>
					<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
						<a href="${ctx}/sys/role/form?id=${role.id}">${fns:i18nMessage('common.modify')}</a>
					</c:if>
					<a onclick="deleteAtId('${fns:i18nMessage('role.deleteTip')}？', '${ctx}/sys/role/delete?id=${role.id}')">${fns:i18nMessage('common.delete')}</a>
				</td></shiro:hasPermission>	
			</tr>
		</c:forEach>
	</table>
	</div>
	</div>
	</div>
	</div>
	</div>
</body>
</html>