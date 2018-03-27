<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('office.manager')}</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");

			var data = ${fns:toJson(list)}, rootId = "${not empty office.id ? office.id : '0'}";
			if(rootId!='0'){
				if(data&&data.length>0){//第一行
					var row = data[0];
					$("#treeTableList").append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
						}, pid: (true?0:pid), row: row
					}));
				}
			}
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 5});
		});
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

	<!-- 导入窗口 -->
	<div class="modal inmodal fade" id="importBox" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">${fns:i18nMessage('common.importData')}</h4>
            </div>
            <div class="modal-body modal-office-body">	
				<form id="importForm" action="${ctx}/sys/office/import" method="post" enctype="multipart/form-data"
					class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('${fns:i18nMessage('common.importLoading')}');"><br/>
					<div class="form-group">
						<input id="uploadFile" name="file" type="file"  class="form-control btn-primary" />　
					
					</div>
					<div class="form-group">
					<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.import')}   "/>
					<a href="${ctx}/sys/office/import/template">${fns:i18nMessage('common.dowload')}</a>	${fns:i18nMessage('common.importTip')}
					</div>
				</form>
			</div> 
            <div class="modal-footer">
            </div>
        </div>
      </div>
 	</div>


<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/office/list?id=${office.id}&parentIds=${office.parentIds}">${fns:i18nMessage('office.list')}</a></li>
		<shiro:hasPermission name="sys:office:edit"><li><a href="${ctx}/sys/office/form?parent.id=${office.id}">${fns:i18nMessage('office.add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	 	<form class="breadcrumb form-search form-horizontal">
			<div class="form-group text-right" style="margin-right:0">
				<button type="button" id="btnImport" data-target="#importBox" data-toggle="modal"  class="btn btn-primary">${fns:i18nMessage('common.import')}</button>
			</div>
		</form>
	<sys:message content="${message}"/>
	<form id="listForm" method="post"></form>
	<table id="treeTable" class="table table-hover table-striped">
		<thead>
			<tr>
				<th>${fns:i18nMessage('office.name')}</th>
				<th>${fns:i18nMessage('office.area')}</th>
				<th>${fns:i18nMessage('office.code')}</th>
				<th>${fns:i18nMessage('office.type')}</th>
				<th>${fns:i18nMessage('common.note')}</th>
				<shiro:hasPermission name="sys:office:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/office/form?id={{row.id}}">{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="sys:office:edit"><td>
				<a href="${ctx}/sys/office/form?id={{row.id}}">${fns:i18nMessage('common.modify')}</a>
				<!-- <a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('office.deleteTip')}', '${ctx}/sys/office/delete?id={{row.id}}')">${fns:i18nMessage('common.delete')}</a> -->
				<a href="${ctx}/sys/office/form?parent.id={{row.id}}">${fns:i18nMessage('office.addChildOffice')}</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
	</div>
</body>
</html>