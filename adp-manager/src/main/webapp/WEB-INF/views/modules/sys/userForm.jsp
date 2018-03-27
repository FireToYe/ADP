<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head> 
	<title>${fns:i18nMessage('user.manager')}</title>
	<meta name="decorator" content="default"/>
		<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#no").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "${fns:i18nMessage('user.notAllowSameLoginName')}"},
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
<!-- <div class="wrapper wrapper-content"> -->
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">${fns:i18nMessage('user.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}&company.id=${user.company.id}&company.name=${user.company.name}&office.id=${user.office.id}&office.name=${user.office.name}">${fns:i18nMessage('user')}<shiro:hasPermission name="sys:user:edit">${not empty user.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 	<div class="tab-pane active">
		 	<div class="panel-body ">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.nameImage')}:</label>
			<div class="col-sm-3 controls">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge form-control"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.company')}:</label>
			<div class="col-sm-3 controls">
                <sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="${fns:i18nMessage('office.company')}" url="/sys/office/treeData?type=1" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.department')}:</label>
			<div class="col-sm-3 controls">
                <sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="${fns:i18nMessage('office.department')}" url="/sys/office/treeData?type=2" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.idCode')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="no" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.name')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.loginName')}:</label>
			<div class="col-sm-3 controls">
				<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="required userName form-control"/>
				<c:if test="${empty user.id}"></c:if>
				<c:if test="${not empty user.id}"><span class="help-inline">${fns:i18nMessage('user.modifyLoginNameTip')}</span></c:if>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('common.password')}:</label>
			<div class="col-sm-3 controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="${empty user.id?'required':''} form-control"/>
				<c:if test="${empty user.id}"></c:if>
				<c:if test="${not empty user.id}"><span class="help-inline">${fns:i18nMessage('user.modifyPasswordTip')}</span></c:if>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('common.confirmPassword')}:</label>
			<div class="col-sm-3 controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newPassword" class="form-control"/>
				<c:if test="${empty user.id}"></c:if>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.mail')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="email" htmlEscape="false" maxlength="100" class="email form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.phone')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="phone" htmlEscape="false" maxlength="100" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.mobilePhone')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.isAllowToLogin')}:</label>
			<div class="col-sm-3 controls">
				<form:select path="loginFlag" class="form-control">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline">${fns:i18nMessage('user.login')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.type')}:</label>
			<div class="col-sm-3 controls">
				<form:select path="userType" class="input-xlarge form-control">
					<form:option value="" label="${fns:i18nMessage('common.pleaseChoose')}"/>
					<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		 <div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('user.role')}:</label>
			<div class="col-sm-6 controls">
				<form:checkboxes path="roleIdList" element="div" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" class="required "/>
				</br>
				<c:if test="${empty user.id}"></c:if>
				<c:if test="${not empty user.id}"><span class="help-inline">${fns:i18nMessage('user.modifyRoleTip')}</span></c:if>
			</div>
		</div>
		<!--erp columes  -->
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('user.defaultEnterpriseCode')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="ent" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"></span>${fns:i18nMessage('user.defaultSite')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="site" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"> ${fns:i18nMessage('user.erpUserNo')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="employeeNo" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"> ${fns:i18nMessage('user.erpEmployeeName')} :</label>
			<div class="col-sm-3 controls">
				<form:input path="employeeName" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"></span>${fns:i18nMessage('user.departmentNo')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="departmentNo" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"> ${fns:i18nMessage('user.erpDepartmentName')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="departmentName" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"> ${fns:i18nMessage('user.defaultWarehouse')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="ware" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
			</div>
		</div>
		 
		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.note')}:</label>
			<div class="col-sm-3 controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge form-control"/>
			</div>
		</div>
		<c:if test="${not empty user.id}">
			<div class="form-group">
				<label class="col-sm-2 control-label">${fns:i18nMessage('user.createTime')}:</label>
				<div class="col-sm-3 controls">
					<label class="lbl"><fmt:formatDate value="${user.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="full"/></label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">${fns:i18nMessage('user.lastLogin')}:</label>
				<div class="col-sm-3 controls">
					<label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;${fns:i18nMessage('common.time')}：<fmt:formatDate value="${user.loginDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="full"/></label>
				</div>
			</div>
		</c:if>
		<div class="form-actions">
			<div class="col-sm-4 col-sm-offset-2">
				<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
	</div>
	</div>
<!-- 	</div> -->
	</div>
	</div>
</body>
</html>