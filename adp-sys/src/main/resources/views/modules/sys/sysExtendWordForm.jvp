<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysExtend.management')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading("${fns:i18nMessage('common.loading')}");
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
			
			jQuery.validator.addMethod("simpleData", function() {
				var ok = false;
				var key = $('#key').val();
				var name = $('#name').val();
				var id = $("#id").val();
				$.ajax({
					url : '${ctx}/sys/extendWord/existSameData',
					data : 'key=' + key +'&name=' + name + '&id=' + id,
					async :false,
					success : function(data){
						if(data > 0){
							ok = false;
						}else{
							ok = true;
						}
					}
				});
				return ok;
			},"${fns:i18nMessage('sysExtend.noSameKeyAndName')}");
			
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/extendWord">${fns:i18nMessage('sysExtend.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/extendWord/form?id=${sysExtendWord.id}"><shiro:hasPermission name="sys:sysExtendWord:edit">${fns:i18nMessage('sysExtend')}${not empty sysExtendWord.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:sysExtendWord:edit">${fns:i18nMessage('common.view')}</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysExtendWord" action="${ctx}/sys/extendWord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysExtend.key')}：</label>
			<div class="col-sm-3">
				<form:input path="key" htmlEscape="false" maxlength="255" readonly="readonly" class="form-control input-xlarge required simpleData"/>
				<span class="help-inline">${fns:i18nMessage('sysExtend.model')}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysExtend.comments')}对key的描述：</label>
			<div class="col-sm-3">
				<form:input path="comments" htmlEscape="false" maxlength="255" readonly="readonly" class="form-control input-xlarge required simpleData"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysExtend.name')}：</label>
			<div class="col-sm-3">
				<form:input path="name" htmlEscape="false" maxlength="64" class="form-control input-xlarge required simpleData"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysExtend.displayName')}：</label>
			<div class="col-sm-3">
				<form:input path="displayName" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysExtend.type')}：</label>
			<div class="col-sm-3">
				<form:select path="type" class="input-medium form-control required">
					<form:option value="" label="${fns:i18nMessage('common.pleaseChoose')}"/>
					<form:options items="${fns:getDictList('extend_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('common.orderBy')}：</label>
			<div class="col-sm-3">
				<form:input path="sort" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.note')}：</label>
			<div class="col-sm-3">
				<form:textarea path="remarks" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
            <div class="col-sm-4 col-sm-offset-2">
	         	<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
            </div>
        </div>
	</form:form>
</body>
</html>