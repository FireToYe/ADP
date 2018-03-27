<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('app.listManager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
		<li><a href="${ctx}/sys/sysAppList/">${fns:i18nMessage('app.checklist')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysAppList/form?id=${sysAppList.id}">${fns:i18nMessage('app.list')} <shiro:hasPermission name="sys:sysAppList:edit">${not empty sysAppList.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:sysAppList:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysAppList" action="${ctx}/sys/sysAppList/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('app.id')}：</label>
			<div class="col-sm-3">
				<form:input path="appId" htmlEscape="false" maxlength="32" class="input-xlarge required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('app.secret')}：</label>
			<div class="col-sm-3">
				<form:input path="appSecret" htmlEscape="false" maxlength="32" class="input-xlarge required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('app.token')}：</label>
			<div class="col-sm-3">
				<form:input path="token" htmlEscape="false" maxlength="32" class="input-xlarge required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('app.jumpLink')}：</label>
			<div class="col-sm-3">
				<form:input path="redirect" htmlEscape="false" maxlength="255" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('app.name')}：</label>
			<div class="col-sm-3">
				<form:input path="appName" htmlEscape="false" maxlength="255" class="input-xlarge required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('app.type')}：</label>
			<div class="col-sm-3">
				<form:select path="appType" class="input-xlarge required form-control">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('app_list_app_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('app.applicationContentAndFields(json)')}：</label>
			<div class="col-sm-3">
				<form:textarea path="infoKeys" rows="4" maxlength="255" class="input-xxlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('app.note')}：</label>
			<div class="col-sm-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-4 col-sm-offset-2">
				<shiro:hasPermission name="sys:sysAppList:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
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