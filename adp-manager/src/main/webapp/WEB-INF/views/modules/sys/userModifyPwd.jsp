<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('user.modifyPassWord')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "${fns:i18nMessage('user.enterSamePassWord')}"}
				},
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
		<li><a href="${ctx}/sys/user/info">${fns:i18nMessage('user.userInfo')}</a></li>
		<li class="active"><a href="${ctx}/sys/user/modifyPwd">${fns:i18nMessage('user.modifyPassWord')}</a></li>
	</ul>
		<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.oldPassWord')}:</label>
			<div class="col-sm-3">
				<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.newPassWord')}:</label>
			<div class="col-sm-3">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.confirmNewPassWord')}:</label>
			<div class="col-sm-3">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="required form-control" equalTo="#newPassword"/>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
                 <button id="btnSubmit" class="btn btn-primary" type="submit" value="保 存">${fns:i18nMessage('common.saves')}</button>
             </div>
         </div>
	</form:form>
	</div></div>
	 </div> 
	</div>
</body>
</html>