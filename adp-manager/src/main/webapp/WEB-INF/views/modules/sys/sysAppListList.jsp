<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('app.listManager')}</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
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
				$.post(1)
				$("#searchForm").attr("action",href);
				$("#searchForm").submit();
			});
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a href="${ctx}/sys/sysAppList/">${fns:i18nMessage('app.checklist')}</a></li>
				<shiro:hasPermission name="sys:sysAppList:edit">
					<li><a href="${ctx}/sys/sysAppList/form">${fns:i18nMessage('app.checklistAdd')}</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="searchForm" modelAttribute="sysAppList" action="${ctx}/sys/sysAppList/" method="post" class="breadcrumb form-search form-horizontal">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<div class="form-group">
								<label class="col-sm-1 control-label">${fns:i18nMessage('app.name')}：</label>
								<div class="col-sm-2">
									<form:input path="appName" htmlEscape="false" maxlength="255" class="input-medium form-control" />
								</div>
								<label class="col-sm-1 control-label">${fns:i18nMessage('app.type')}：</label>
								<div class="col-sm-2">
									<form:select path="appType" class="input-medium form-control">
										<form:option value="" label="" />
										<form:options items="${fns:getDictList('app_list_app_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
									</form:select>
								</div>
								<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
							</div>
						</form:form>
						<sys:message content="${message}" />
						<table id="contentTable"
							class="table table-hover table-striped">
							<thead>
								<tr>
									<th>${fns:i18nMessage('app.id')}</th>
									<th>${fns:i18nMessage('app.secret')}</th>
									<th>${fns:i18nMessage('app.token')}</th>
									<th>${fns:i18nMessage('app.jumpLink')}</th>
									<th>${fns:i18nMessage('app.name')}</th>
									<th>${fns:i18nMessage('app.type')}</th>
									<th>${fns:i18nMessage('app.note')}</th>
									<th>${fns:i18nMessage('app.updateTime')}</th>
									<shiro:hasPermission name="sys:sysAppList:edit">
										<th>${fns:i18nMessage('common.operation')}</th>
									</shiro:hasPermission>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="sysAppList">
									<tr>
										<td><a
											href="${ctx}/sys/sysAppList/form?id=${sysAppList.id}">
												${sysAppList.appId} </a></td>
										<td>${sysAppList.appSecret}</td>
										<td>${sysAppList.token}</td>
										<td>${sysAppList.redirect}</td>
										<td>${sysAppList.appName}</td>
										<td>${fns:getDictLabel(sysAppList.appType, 'app_list_app_type', '')}
										</td>
										<td>${sysAppList.remarks}</td>
										<td><fmt:formatDate value="${sysAppList.updateDate}"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<shiro:hasPermission name="sys:sysAppList:edit">
											<td>
												<a href="${ctx}/sys/sysAppList/form?id=${sysAppList.id}">${fns:i18nMessage('common.modify')}</a>
												<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('app.deleteTip')}？', '${ctx}/sys/sysAppList/delete?id=${sysAppList.id}')">${fns:i18nMessage('common.delete')}</a>
											</td>
										</shiro:hasPermission>
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