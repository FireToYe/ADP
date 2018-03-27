<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysMessageSend.manager')}</title>
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
		<li class="active"><a href="${ctx}/sys/sysMessageSend/">${fns:i18nMessage('sysMessageSend.list')}</a></li>
		<shiro:hasPermission name="sys:sysMessageSend:edit"><li><a href="${ctx}/sys/sysMessageSend/form">${fns:i18nMessage('sysMessageSend.send')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysMessageSend" action="${ctx}/sys/sysMessageSend/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('sysMessageSend.title')}：</label>
				<div class="col-sm-2">
					<form:input path="title" htmlEscape="false" maxlength="100" class="form-control input-medium"/>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th>${fns:i18nMessage('sysMessageSend.title')}</th>
					<th>${fns:i18nMessage('sysMessage.sendTime')}</th>
					<th>${fns:i18nMessage('common.query')}</th>
					<shiro:hasPermission name="sys:sysMessageSend:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysMessageSend">
				<tr>
					<td id="xx"><a href="${ctx}/sys/sysMessageSend/form?id=${sysMessageSend.id}">
						${sysMessageSend.title}
					</a></td>
					<td>
						<fmt:formatDate value="${sysMessageSend.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<a href="${ctx}/sys/sysMessage/statusList?sourceMessageId=${sysMessageSend.id}">${fns:i18nMessage('common.view')}</a>
					</td>
					<shiro:hasPermission name="sys:sysMessageSend:edit"><td>
	    				<a href="${ctx}/sys/sysMessageSend/form?id=${sysMessageSend.id}"></a>
						<a href="javascript:void(0);" onclick="deleteAtId('确认要删除该消息发送吗？', '${ctx}/sys/sysMessageSend/delete?id=${sysMessageSend.id}')">${fns:i18nMessage('common.delete')}</a>
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