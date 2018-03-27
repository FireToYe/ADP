<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('area.manager')}</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "0";
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_area_type'))}, row.type)
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/area/">${fns:i18nMessage('area.list')}</a></li>
		<shiro:hasPermission name="sys:area:edit"><li><a href="${ctx}/sys/area/form">${fns:i18nMessage('area.add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
		<div class="tab-pane active">
			<div class="panel-body ">
	<sys:message content="${message}"/>
	<form id="listForm" method="post"></form>
	<table id="treeTable" class="table table-hover table-striped">
		<thead>
			<tr>
				<th>${fns:i18nMessage('area.name')}</th>
				<th>${fns:i18nMessage('area.code')}</th>
				<th>${fns:i18nMessage('area.type')}</th>
				<th>${fns:i18nMessage('common.note')}</th>
				<shiro:hasPermission name="sys:area:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/area/form?id={{row.id}}">{{row.name}}</a></td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="sys:area:edit"><td>
				<a href="${ctx}/sys/area/form?id={{row.id}}">${fns:i18nMessage('common.modify')}</a>
				<a onclick="deleteAtId('${fns:i18nMessage('area.deleteTip')}', '${ctx}/sys/area/delete?id={{row.id}}')">${fns:i18nMessage('common.delete')}</a>
				<a href="${ctx}/sys/area/form?parent.id={{row.id}}">${fns:i18nMessage('area.addChildArea')}</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
	</div>
	</div>
	</div>
	</div>
	</div>
</body>
</html>