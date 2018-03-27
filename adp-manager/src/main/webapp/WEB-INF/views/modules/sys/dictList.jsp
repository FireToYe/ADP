<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('dict.manager')}</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
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
				<li class="active"><a href="${ctx}/sys/dict/">${fns:i18nMessage('dict.list')}</a></li>
				<shiro:hasPermission name="sys:dict:edit">
					<li><a href="${ctx}/sys/dict/form?sort=10">字典添加</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="searchForm" modelAttribute="dict"
							action="${ctx}/sys/dict/" method="post"
							class="breadcrumb form-search form-horizontal">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<div class="form-group">
								<label  class="col-sm-1 control-label">${fns:i18nMessage('dict.type')}：</label>
								<div class="col-sm-2">
								<form:select id="type" path="type" class="form-control input-medium">
									<form:option value="" label="" />
									<form:options items="${typeList}" htmlEscape="false" />
								</form:select>
								</div>
								<label  class="col-sm-1 control-label">${fns:i18nMessage('dict.description')} ：</label>
								<div class="col-sm-2">
								<form:input path="description" htmlEscape="false" maxlength="50" class="form-control input-medium" />
									</div>
								<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
							</div>
						</form:form>
						<sys:message content="${message}" />
						<table id="contentTable"
							class="table table-hover table-striped">
							<thead>
								<tr>
									<th>${fns:i18nMessage('dict.key')}</th>
									<th>${fns:i18nMessage('dict.option')}</th>
									<th>${fns:i18nMessage('dict.type')}</th>
									<th>${fns:i18nMessage('dict.description')}</th>
									<th>${fns:i18nMessage('common.orderBy')}</th>
									<shiro:hasPermission name="sys:dict:edit">
										<th>${fns:i18nMessage('common.operation')}</th>
									</shiro:hasPermission>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="dict">
									<tr>
										<td>${dict.value}</td>
										<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
										<td><a href="javascript:"
											onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
										<td>${dict.description}</td>
										<td>${dict.sort}</td>
										<shiro:hasPermission name="sys:dict:edit">
											<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${fns:i18nMessage('common.modify')}</a>
												<a
												href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('dict.deleteTip')}？', '${ctx}/sys/dict/delete?id=${dict.id}&type=${dict.type}')">${fns:i18nMessage('common.delete')}</a> <a
												href="<c:url value='${fns:getAdminPath()}/sys/dict/form?type=${dict.type}&sort=${dict.sort+10}'><c:param name='description' value='${dict.description}'/></c:url>">${fns:i18nMessage('dict.addKey')}</a>
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