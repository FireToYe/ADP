<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('app.authorizationApplicationManagement')}</title>
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
				<li class="active"><a href="${ctx}/sys/appApply/">${fns:i18nMessage('app.authorizationApplicationList')}</a></li>
				<%-- 	<shiro:hasPermission name="sys:appApply:edit"><li><a href="${ctx}/sys/appApply/form">app授权申请表添加</a></li></shiro:hasPermission> --%>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="searchForm" modelAttribute="appApply" action="${ctx}/sys/appApply/" method="post" class="breadcrumb form-search form-horizontal">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<div class="form-group">
								<label class="col-sm-1 control-label">${fns:i18nMessage('app.id')}：</label>
								<div class="col-sm-2">
									<form:input path="appId" htmlEscape="false" maxlength="32" class="input-medium form-control" />
								</div>
								<label class="col-sm-1 control-label">${fns:i18nMessage('common.status')}：</label>
								<div class="col-sm-2">
									<form:select path="status" class="input-medium form-control">
										<form:option value="" label="" />
										<form:options items="${fns:getDictList('app_apply_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
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
									<th>${fns:i18nMessage('app.openid')}</th>
									<th>${fns:i18nMessage('app.id')}</th>
									<th>${fns:i18nMessage('common.status')}</th>
									<th>${fns:i18nMessage('app.note')}</th>
									<th>${fns:i18nMessage('app.updateTime')}</th>
									<shiro:hasPermission name="sys:appApply:edit">
										<th>${fns:i18nMessage('common.operation')}</th>
									</shiro:hasPermission>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="appApply">
									<tr>
										<td><a
											href="${ctx}/sys/appApply/form?openId=${appApply.openId}">
												${appApply.openId} </a></td>
										<td>${appApply.appId}</td>
										<td>${fns:getDictLabel(appApply.status, 'app_apply_status', '未处理')}
										</td>
										<td>${appApply.remarks}</td>
										<td><fmt:formatDate value="${appApply.updateDate}"
												pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<shiro:hasPermission name="sys:appApply:edit">
											<td><c:if test="${'0' eq appApply.status }">
													<a href="${ctx}/sys/appApply/examine?openId=${appApply.openId}&status=1">授权</a>
													<a href="${ctx}/sys/appApply/examine?openId=${appApply.openId}&status=2">不允许</a>
												</c:if>
												<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('app.authorization.deleteTip')}？', '${ctx}/sys/appApply/delete?openId=${appApply.openId}')">${fns:i18nMessage('common.delete')}</a>
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