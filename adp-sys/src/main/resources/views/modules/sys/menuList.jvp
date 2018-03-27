<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('menu.manager')}</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1}).show();
		});
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
	    	$("#listForm").submit();
    	}
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
	<ul class="nav nav-tabs ">
		<li class="active"><a href="${ctx}/sys/menu/">${fns:i18nMessage('menu.list')}</a></li>
<%-- 		<shiro:hasPermission name="sys:menu:edit"><li><a href="${ctx}/sys/menu/form">菜单添加</a></li></shiro:hasPermission> --%>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<sys:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-hover table-striped "><!-- hide -->
			<thead>
				<tr>
					<th>${fns:i18nMessage('menu.name')}</th>
					<th>${fns:i18nMessage('menu.link')}</th>
					<th style="text-align:center;">${fns:i18nMessage('common.orderBy')}</th>
					<th>${fns:i18nMessage('menu.visibility')}</th>
					<th>${fns:i18nMessage('menu.permission')}</th>
					<shiro:hasPermission name="sys:menu:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody><c:forEach items="${list}" var="menu">
				<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
					<td nowrap><i class="fa fa-${not empty menu.icon?menu.icon:' hide'}"></i><a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a></td>
					<td title="${menu.href}">${fns:abbr(menu.href,30)}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="sys:menu:edit">
							<input type="hidden" name="ids" value="${menu.id}"/>
							<input name="sorts" type="text" value="${menu.sort}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
							${menu.sort}
						</shiro:lacksPermission>
					</td>
					<td>${menu.isShow eq '1'?fns:i18nMessage('menu.show'):fns:i18nMessage('menu.hide')}</td>
					<td title="${menu.permission}">${fns:abbr(menu.permission,30)}</td>
					<shiro:hasPermission name="sys:menu:edit"><td nowrap>
						<a href="${ctx}/sys/menu/form?id=${menu.id}">${fns:i18nMessage('common.modify')}</a>
						<a onclick="deleteAtId('${fns:i18nMessage('menu.deleteTip')}', '${ctx}/sys/menu/delete?id=${menu.id}')">${fns:i18nMessage('common.delete')}</a>
						<a href="${ctx}/sys/menu/form?parent.id=${menu.id}&parent.name=${menu.name}">${fns:i18nMessage('menu.addChildMenu')}</a> 
					</td></shiro:hasPermission>
				</tr>
			</c:forEach></tbody>
		</table>
		<shiro:hasPermission name="sys:menu:edit"><div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="${fns:i18nMessage('menu.saveOrder')}" onclick="updateSort();"/>
		</div></shiro:hasPermission>
	 </form>
	 </div>
	 </div>
	 </div>
	 </div>
	 </div>
</body>
</html>