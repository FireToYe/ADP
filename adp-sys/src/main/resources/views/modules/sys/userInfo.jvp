<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('user.userInfo')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
		<li class="active"><a href="${ctx}/sys/user/info">${fns:i18nMessage('user.userInfo')}</a></li>
		<li><a href="${ctx}/sys/user/modifyPwd">${fns:i18nMessage('user.modifyPassWord')}</a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.nameImage')}:</label>
			<div class="col-sm-3">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.company')}:</label>
			<div class="col-sm-3">
				<label class="lbl">${user.company.name}</label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.department')}:</label>
			<div class="col-sm-3">
				<label class="lbl">${user.office.name}</label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.name')}:</label>
			<div class="col-sm-3">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.mail')}:</label>
			<div class="col-sm-3">
				<form:input path="email" class="form-control email" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.phone')}:</label>
			<div class="col-sm-3">
				<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.mobilePhone')}:</label>
			<div class="col-sm-3">
				<form:input path="mobile" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.note')}:</label>
			<div class="col-sm-3">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.type')}:</label>
			<div class="col-sm-3">
				<label class="lbl">${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.role')}:</label>
			<div class="col-sm-3">
				<label class="lbl">${user.roleNames}</label>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.lastLogin')}:</label>
			<div class="col-sm-3">
				<label class="lbl">IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;${fns:i18nMessage('user.time')}：<fmt:formatDate value="${user.oldLoginDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="full"/></label>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
                 <button class="btn btn-primary" type="submit" value="保 存">${fns:i18nMessage('common.saves')}</button>
             </div>
         </div>
<!-- 		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
		</div> -->
	</form:form>
</div></div>
	 </div> 
	</div>
</div>
</body>
</html>