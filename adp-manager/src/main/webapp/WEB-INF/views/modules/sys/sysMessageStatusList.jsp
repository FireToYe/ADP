<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysMessage.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/sysMessage/statusList");
			$("#searchForm").submit();
        	return false;
        }
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
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
		<li class="active"><a href="">${fns:i18nMessage('sysMessageSend.status')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysMessage" action="${ctx}/sys/sysMessage/statusList" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<form:input path="sourceMessageId" type="hidden" value="${sysMessage.sourceMessageId}" class="form-control"/>
			<div class="form-group">
				<%-- <label class="col-sm-1 control-label">${fns:i18nMessage('sysMessage.title')}：</label>
				<div class="col-sm-2">
					<form:input path="title" htmlEscape="false" maxlength="100" class="form-control input-medium"/>
				</div> --%>
				<label class="col-sm-1 control-label">${fns:i18nMessage('office.company')}：</label>
				<div class="col-sm-2">
					<form:input path="companyName" htmlEscape="false" maxlength="100" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('office.department')}：</label>
				<div class="col-sm-2">
					<form:input path="officeName" htmlEscape="false" maxlength="100" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('sysMessageSend.receivingPerson')}：</label>
				<div class="col-sm-2">
					<form:input path="receiverName" htmlEscape="false" maxlength="100" class="form-control input-medium"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('sysMessage.delStatus')}：</label>
				<div class="col-sm-2">
					<form:select path="delStatus" class="input-medium form-control">
						<form:option value="" label="${fns:i18nMessage('common.pleaseChoose')}"/>
						<form:options items="${fns:getDictList('msg_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" onclick="return page(false,false);" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th>${fns:i18nMessage('sysMessage.sendPerson')}</th>
					<th>${fns:i18nMessage('sysMessageSend.receivingPerson')}</th>
					<th>${fns:i18nMessage('sysMessageSend.receiverCompany')}</th>
					<th>${fns:i18nMessage('sysMessageSend.receiverDepartment')}</th>
					<th>${fns:i18nMessage('sysMessage.delStatus')}</th>
					<th>${fns:i18nMessage('sysMessage.createTime')}</th>
					<th>${fns:i18nMessage('common.operation')}</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysMessage">
				<tr>
					<td>${sysMessage.createBy.name}</td>
					<td>${sysMessage.user.name}</td>
					<td>${sysMessage.company.name}</td>
					<td>${sysMessage.office.name}</td>
					<c:if test="${sysMessage.delStatus==1}"><td>${fns:i18nMessage('sysMessage.readed')}</td></c:if><c:if test="${sysMessage.delStatus==0}"><td style="color:red">${fns:i18nMessage('sysMessage.notReaded')}</td></c:if>
					<td><fmt:formatDate value="${sysMessage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('sysMessage.deleteTip')}？', '${ctx}/sys/sysMessage/StatusDelete?id=${sysMessage.id}')">${fns:i18nMessage('common.delete')}</a>
					</td>
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