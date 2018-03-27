\<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('menu.manager')}</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#btnSubmit").click(function(){
				var valName = $("#name").val();
				var valHref = $("#href").val();
				var valShow = $("#isShow2").val();
				if(valName == '菜单管理' || valHref == '/sys/menu/'){
					var item = $(":radio:checked"); 
					var len=item.length; 
					if(len>0 && $(":radio:checked").val() == 0){ 
						layer.alert("${fns:i18nMessage('menu.notAllowHideMenu')}",{
							offset: 't'
						});
						return false;
					} 
				}
			});
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('${fns:i18nMessage('common.loading')}');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/menu/">${fns:i18nMessage('menu.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/menu/form?id=${menu.id}&parent.id=${menu.parent.id}">${fns:i18nMessage('menu')}<shiro:hasPermission name="sys:menu:edit">${not empty menu.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">查看</shiro:lacksPermission></a></li>
	</ul>
		<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.parentMenu')}:</label>
			<div class="col-sm-3">
                <sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}"
					title="${fns:i18nMessage('menu')}" url="/sys/menu/treeJSONData" extId="${menu.id}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><font color="red">* </font>${fns:i18nMessage('menu.name')}:</label>
			<div class="col-sm-3">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required input-xlarge form-control"/>
				<span class="help-inline"> </span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.link')}:</label>
			<div class="col-sm-3">
				<form:input path="href" htmlEscape="false" maxlength="2000" class="input-xxlarge form-control"/>
				<span class="help-inline">${fns:i18nMessage('menu.page')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.target')}:</label>
			<div class="col-sm-3">
				<form:input path="target" htmlEscape="false" maxlength="10" class="input-small form-control"/>
				<span class="help-inline">${fns:i18nMessage('area.defaultWindowByUrl')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.icon')}:</label>
			<div class="col-sm-3">
				<sys:iconselect id="icon" name="icon" value="${menu.icon}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.orderBy')}:</label>
			<div class="col-sm-3">
				<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits input-small form-control"/>
				<span class="help-inline">${fns:i18nMessage('menu.sort')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.visibility')}:</label>
			<div class="col-sm-6" style="padding-top:5px;">
				<form:radiobuttons path="isShow" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required radio i-checks"/>
				<span class="help-inline">${fns:i18nMessage('menu.display')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.permission')}:</label>
			<div class="col-sm-3">
				<form:input path="permission" htmlEscape="false" maxlength="100" class="input-xxlarge form-control"/>
				<span class="help-inline">${fns:i18nMessage('menu.permissionInController')}</span>
			</div>
		</div>
		 <div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.code')}:</label>
			<div class="col-sm-3">
				<form:input path="code" htmlEscape="false" maxlength="50" class="input-xxlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('menu.type')}:</label>
			<div class="col-sm-3">
				<form:select path="menuType" class="input-medium form-control">
					<form:options items="${fns:getDictList('menu_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.note')}:</label>
			<div class="col-sm-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xxlarge form-control"/>
			</div>
		</div>
				<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
             			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
             </div>
	</form:form>
		 </div>
	 </div>
	 </div>
	 </div>
	 </div>
</body>
</html>