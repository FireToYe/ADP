<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
 <!DOCTYPE html>
<html>
<head>
	<title>${fns:i18nMessage('user.manager')}</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				layer.confirm('${fns:i18nMessage('common.exportTip')}', {
					offset: 't' ,
					  btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}'] //按钮
					},function(){
						$("#searchForm").attr("action","${ctx}/sys/user/export");
						$("#searchForm").submit();
						layer.closeAll();
					});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
/* 			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			}); */
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/user/list");
			$("#searchForm").submit();
	    	return false;
	    }
		
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				  btn: ['${fns:i18nMessage('common.confirm')}'] //按钮
				}, function(){
					$.post(1)
					$("#searchForm").attr("action",href);
					$("#searchForm").submit();
				});
		}
		</script>
</head>
<body>
<%-- 	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/sys/user/import/template">下载模板</a>
		</form>
	</div> --%>
	<!-- 导入窗口 -->
	<div class="modal inmodal fade" id="importBox" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">${fns:i18nMessage('common.importData')}</h4>
            </div>
            <div class="modal-body modal-office-body">	
				<form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data"
					class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('${fns:i18nMessage('common.importLoading')}');"><br/>
					<div class="form-group">
						<input id="uploadFile" name="file" type="file"  class="form-control btn-primary" />　
					
					</div>
					<div class="form-group">
					<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.import')}   "/>
					<a href="${ctx}/sys/user/import/template">${fns:i18nMessage('common.dowload')}</a>	${fns:i18nMessage('common.importTip')}
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
		<li class="active"><a href="${ctx}/sys/user/list">${fns:i18nMessage('user.list')}</a></li>
		<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form?company.id=${user.company.id}&company.name=${user.company.name}&office.id=${user.office.id}&office.name=${user.office.name}">${fns:i18nMessage('user.add')}</a></li></shiro:hasPermission>
	</ul>
			<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/list" method="post" class="breadcrumb form-search form-horizontal">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		
		<div class="form-group">
			<label class="col-sm-1 control-label">${fns:i18nMessage('user.company')}：</label>
			<div class="col-sm-3">
			<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}" 
				title="${fns:i18nMessage('office.company')}" url="/sys/office/treeData?type=1" cssClass="input-small" allowClear="true"/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('user.loginName')}：</label>
			<div class="col-sm-3">
				<form:input class="form-control input-medium" path="loginName" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">${fns:i18nMessage('user.department')}：</label>
		
			<div class="col-sm-3">
			<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" 
				title="${fns:i18nMessage('office.department')}" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('user.name')}：</label>
			<div class="col-sm-3">
				<form:input class="form-control input-medium" path="name" htmlEscape="false" maxlength="50"/>
			</div>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" onclick="return page(false,false);"/>
			<input id="btnExport" class="btn btn-primary" type="button" value="${fns:i18nMessage('common.export')}"/>
			<button type="button" id="btnImport" data-target="#importBox" data-toggle="modal"  class="btn btn-primary">${fns:i18nMessage('common.import')}</button>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-hover table-striped">
		<thead>
			<tr>
				<th>${fns:i18nMessage('user.company')}</th>
				<th>${fns:i18nMessage('user.department')}</th>
				<th class="sort-column login_name">${fns:i18nMessage('user.loginName')}</th>
				<th class="sort-column name">${fns:i18nMessage('user.name')}</th>
				<th>${fns:i18nMessage('common.phone')}</th>
				<th>${fns:i18nMessage('user.mobilePhone')}</th>
				<%--<th>角色</th> --%>
				<shiro:hasPermission name="sys:user:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="user">
			<tr>
				<td>${user.company.name}</td>
				<td>${user.office.name}</td>
				<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
				<td>${user.name}</td>
				<td>${user.phone}</td>
				<td>${user.mobile}</td><%--
				<td>${user.roleNames}</td> --%>
				<shiro:hasPermission name="sys:user:edit"><td>
    				<a href="${ctx}/sys/user/form?id=${user.id}">${fns:i18nMessage('common.modify')}</a>
	<%-- 				<a href="javascript:void(0);" onclick="deleteAtId('确认要禁用该用户吗？', '${ctx}/sys/user/delete?id=${user.id}')">禁用</a> --%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</div>
	</div>
	</div>
	</div>
</body>

</html>