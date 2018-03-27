<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysMessage.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
			window.parent.refreshNum(0);
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
		
		function back(obj){
			location.href=obj;
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/sysMessage/">${fns:i18nMessage('sysMessage.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysMessage/form?id=${sysMessage.id}"><shiro:lacksPermission name="sys:sysMessage:edit">${fns:i18nMessage('sysMessage.detail')}</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysMessage" action=""  class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.sendPerson')}：</label>
			<div class="col-sm-3">
				<form:input path="createBy" value="${sysMessage.createBy.name}" htmlEscape="false" readonly="true" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.type')}：</label>
			<div class="col-sm-3">
				<form:input path="type" htmlEscape="false" readonly="true" value="${fns:getDictLabels(sysMessage.type,'msg_type','0')}" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.title')}：</label>
			<div class="col-sm-3">
				<form:input path="title" htmlEscape="false" readonly="true" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.content')}：</label>
			<div class="col-sm-3">
				<form:textarea path="content" htmlEscape="false" rows="4" readonly="true" class="form-control input-xxlarge "/>
			</div>
		</div>
			<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"></span>
			${fns:i18nMessage('sysMessage.attachment')}：</label>
			<div class="col-sm-3">
		<form:hidden id="files" path="attachmentIds" htmlEscape="false" maxlength="255" class="form-control"/>
		<sys:ckfinder input="files" type="files" uploadPath="" selectMultiple="true" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.createTime')}：</label>
			<div class="col-sm-3">
				<input name="createDate" type="text" readonly="readonly" class="form-control input-xlarge" 
				value="<fmt:formatDate value="${sysMessage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
				<button class="btn btn-primary" type="button"><a style="color:white" href="javascript:back('${ctx}/sys/sysMessage/list');">${fns:i18nMessage('sysMessage.backToAllMessageList')}</a></button>
				&nbsp;&nbsp;<button class="btn btn-primary" type="button"><a style="color:white" href="javascript:back('${ctx}/sys/sysMessage/list?delStatus=0');">${fns:i18nMessage('sysMessage.backToNotReadMessageList')}</a></button>
             </div>
        </div>
	</form:form>
</body>
</html>