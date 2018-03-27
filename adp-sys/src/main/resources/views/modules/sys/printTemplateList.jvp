<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('printTemplate.manager')}</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
	    $('#datepicker').datepicker({
	        keyboardNavigation: false,
	        forceParse: false,
	        autoclose: true,
			todayBtn: "linked"
	    });
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
				<li class="active"><a href="${ctx}/sys/printTemplate/">${fns:i18nMessage('printTemplate.list')}</a></li>
				<shiro:hasPermission name="sys:printTemplate:edit">
				<li><a href="${ctx}/sys/printTemplate/form">${fns:i18nMessage('printTemplate.add')}</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="searchForm" action="${ctx}/sys/printTemplate/" method="post"
							class="breadcrumb form-search form-horizontal">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<div class="form-group">
								<label class="col-sm-1 control-label">${fns:i18nMessage('printTemplate.manager')}：</label>
									<div class="col-sm-2">
										<input id="templateCode" name="templateCode" type="text" maxlength="50" class="input-mini form-control" value="${printTemplate.templateCode}" />
									</div>
								<label class="col-sm-1 control-label">${fns:i18nMessage('printTemplate.name')}：</label>
									<div class="col-sm-2">
										<input id="name" name="name" type="text" maxlength="50" class="input-mini form-control" value="${printTemplate.name}" />
									</div>
								<div class="col-sm-1 control-label">
									<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
								</div>
							</div>
						</form:form>
						<sys:message content="${message}" />
						<table id="contentTable"
							class="table table-hover table-striped">
							<thead>
								<tr>
									<th>${fns:i18nMessage('printTemplate.manager')}</th>
									<th>${fns:i18nMessage('printTemplate.name')}</th>
									<th>${fns:i18nMessage('printTemplate.description')}</th>
									<th>${fns:i18nMessage('printTemplate.manager')}</th>
									<th>${fns:i18nMessage('common.operation')}</th>
							</thead>
							<tbody>
								<%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
								<c:forEach items="${page.list}" var="printTpl">
									<tr>
										<td>${printTpl.templateCode}</td>
										<td>${printTpl.name}</td>
										<td>${printTpl.description}</td>
										<td>
										<c:choose>
											<c:when  test="${printTpl.status == 1}">
												启用
											</c:when>
											<c:otherwise>
												未启用
											</c:otherwise>
										</c:choose>
										</td>
										<td>
										<shiro:hasPermission name="sys:printTemplate:edit">
											<a href="${ctx}/sys/printTemplate/form?id=${printTpl.id}">${fns:i18nMessage('common.modify')}</a>
											<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('printTemplate.deleteTip')}？', '${ctx}/sys/printTemplate/delete?id=${printTpl.id}')">${fns:i18nMessage('common.delete')}</a>
										</shiro:hasPermission>
										</td>

									</tr>
									<c:if test="${not empty log.exception}">
										<tr>
											<td colspan="8"
												style="word-wrap: break-word; word-break: break-all;">
												<%-- 					用户代理: ${log.userAgent}<br/> --%> <%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
												异常信息: <br />
												${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
											</td>
										</tr>
									</c:if>
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