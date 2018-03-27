<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('dict.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
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
<body >
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/dict/">${fns:i18nMessage('dict.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${dict.id}">${fns:i18nMessage('dict')}<shiro:hasPermission name="sys:dict:edit">${not empty dict.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 	<div class="tab-pane active">
		 	<div class="panel-body ">
				<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('dict.key')}:</label>
						<div class="col-sm-3">
							<form:input path="value" htmlEscape="false" maxlength="50" class="required form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('dict.option')}:</label>
						<div class="col-sm-3">
							<form:input path="label" htmlEscape="false" maxlength="50" class="required form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('dict.type')}:</label>
						<div class="col-sm-3">
							<form:input path="type" htmlEscape="false" maxlength="50" class="required form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('dict.description')}:</label>
						<div class="col-sm-3">
							<form:input path="description" htmlEscape="false" maxlength="50" class="required form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('common.orderBy')}:</label>
						<div class="col-sm-3">
							<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('common.note')}:</label>
						<div class="col-sm-3">
							<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge form-control"/>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-4 col-sm-offset-2">
						<shiro:hasPermission name="sys:dict:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
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