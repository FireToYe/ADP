<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysExtend.management')}</title>
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
		<li class="active"><a href="${ctx}/sys/extendWord">${fns:i18nMessage('sysExtend.list')}</a></li>
		<li><a href="${ctx}/sys/extendWord/form?id=${sysExtendWord.id}">${fns:i18nMessage('sysExtend.add')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysExtendWord" action="${ctx}/sys/extendWord" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label  class="col-sm-1 control-label">${fns:i18nMessage('sysExtend.key')}：</label>
				<div class="col-sm-2">
					<form:select path="key" class="form-control input-medium">
						<form:option value="" label="" />
						<form:options items="${keyList}" itemLabel="keyAndComments" itemValue="key" htmlEscape="false" />
					</form:select>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th>${fns:i18nMessage('sysExtend.key')}</th>
					<th>${fns:i18nMessage('sysExtend.name')}</th>
					<th>${fns:i18nMessage('sysExtend.displayName')}</th>
					<th>${fns:i18nMessage('sysExtend.type')}</th>
					<th>${fns:i18nMessage('common.orderBy')}</th>
					<th>${fns:i18nMessage('common.orderBy')}</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysExtendWord">
				<tr>
					<td>${sysExtendWord.key}</td>
					<td><a href="">${sysExtendWord.name}</a></td>
					<td>${sysExtendWord.displayName}</td>
					<td>${sysExtendWord.type}</td>
					<td>${sysExtendWord.sort}</td>
					<td>
	    				<a href="${ctx}/sys/extendWord/form?id=${sysExtendWord.id}">${fns:i18nMessage('common.modify')}</a>
						<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('sysExtend.deleteTip')}', '${ctx}/sys/extendWord/delete?id=${sysExtendWord.id}')">${fns:i18nMessage('common.delete')}</a>
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