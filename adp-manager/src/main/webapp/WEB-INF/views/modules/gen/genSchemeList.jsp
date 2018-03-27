<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>生成方案管理</title>
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
				  btn: ['确定','取消'] //按钮
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
		<li class="active"><a href="${ctx}/gen/genScheme/">生成方案列表</a></li>
		<shiro:hasPermission name="gen:genScheme:edit"><li><a href="${ctx}/gen/genScheme/form">生成方案添加</a></li></shiro:hasPermission>
	</ul>
		<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="searchForm" modelAttribute="genScheme" action="${ctx}/gen/genScheme/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="col-sm-1 control-label">方案名称 ：</label>
				<div class="col-sm-2">
					<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium form-control"/>
				</div>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-hover table-striped">
		<thead><tr><th>方案名称</th><th>生成模块</th><th>模块名</th><th>功能名</th><th>功能作者</th><shiro:hasPermission name="gen:genScheme:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="genScheme">
			<tr>
				<td><a href="${ctx}/gen/genScheme/form?id=${genScheme.id}">${genScheme.name}</a></td>
				<td>${genScheme.packageName}</td>
				<td>${genScheme.moduleName}${not empty genScheme.subModuleName?'.':''}${genScheme.subModuleName}</td>
				<td>${genScheme.functionName}</td>
				<td>${genScheme.functionAuthor}</td>
				<shiro:hasPermission name="gen:genScheme:edit"><td>
    				<a href="${ctx}/gen/genScheme/form?id=${genScheme.id}">修改</a>
					<a href="javascript:void(0);" onclick="deleteAtId('确认要删除该生成方案吗？', '${ctx}/gen/genScheme/delete?id=${genScheme.id}')">删除</a>
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
