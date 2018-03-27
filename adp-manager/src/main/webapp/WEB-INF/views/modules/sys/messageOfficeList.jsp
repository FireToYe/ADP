<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>组织列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");

			var data = ${fns:toJson(list)}, rootId = "${not empty office.id ? office.id : '0'}";
			if(data&&data.length>0){//第一行
				var row = data[0];
				$("#treeTableList").append(Mustache.render(tpl, {
					dict: {
						type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
					}, pid: (true?0:pid), row: row
				}));
			}
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
			
			$('#checkAll').click(function(){
				if(this.checked){
					$('[name=items]:checkbox').prop('checked',true);
				}
				else{
					$('[name=items]:checkbox').prop('checked',false);
				}
			});
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
		
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body ">
	<form:form id="searchForm" modelAttribute="office" action="" method="post" class="breadcrumb form-search form-horizontal">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="treeTable" class="table table-hover table-striped">
		<thead>
			<tr>
				<th style="width:80"><input id="checkAll" type="checkbox" name="checkAll" class="input-medium" style="width:15;height:15;"/>全选</th>
				<th hidden="true">pid</th>
				<th>机构名称</th>
				<th>归属区域</th>
				<th>机构编码</th>
				<th>机构类型</th>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><input name="items" type="checkbox" class="officeListId" value="{{row.id}}" /></td>
			<td hidden="true">{{pid}}</td>
			<td>{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
		</tr>
	</script>
	</div>
	</div>
	</div>
	</div>
	</div>
</body>
</html>