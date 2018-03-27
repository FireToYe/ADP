<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
			$("#inputForm").validate({
				rules: {
					key: {remote: "${ctx}/sys/sysProperties/checkOnly?id=${sysProperties.id}"}
				},
				messages: {
					key: {remote: "${fns:i18nMessage('sys.properties.key.exist')}"}
				}, 
				submitHandler: function(form){
					loading("${fns:i18nMessage('common.loading')}");
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("${fns:i18nMessage('common.form.error.msg')}");
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
		<li><a href="${ctx}/sys/sysProperties/">${fns:i18nMessage('sys.properties.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysProperties/form?id=${sysProperties.id}"><shiro:hasPermission name="sys:sysProperties:edit"><c:if test="${not empty sysProperties.id}">${fns:i18nMessage('sys.properties.update')}</c:if><c:if test="${empty sysProperties.id}">${fns:i18nMessage('sys.properties.add')}</c:if></shiro:hasPermission><shiro:lacksPermission name="sys:sysProperties:edit">${fns:i18nMessage('sys.properties.view')}</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysProperties" action="${ctx}/sys/sysProperties/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			key：</label>
			<div class="col-sm-3">
				<form:input path="key" htmlEscape="false" maxlength="200" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			value：</label>
			<div class="col-sm-3">
				<form:input path="value" htmlEscape="false" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sys.properties.modify.by')}：</label>
			<div class="col-sm-3">
				<form:select path="modifyBy" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('srm_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>