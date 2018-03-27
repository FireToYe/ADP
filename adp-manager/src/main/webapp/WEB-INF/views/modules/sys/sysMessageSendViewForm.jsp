<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysMessageSend.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
			
			$("#target").change(function(){
				var v=$('#target option:selected').val();
				if(v == 0){
					alert("全部用户");
				}else if(v == 1){
					$("#accepters").hide();
					$("#role").hide();
					$("#office").show();
				}else if(v == 2){
					$("#accepters").hide();
					$("#office").hide();
					$("#role").show();
				}else if(v == 3){
					$("#office").hide();
					$("#role").hide();
					$("#accepters").show();
				}
			});
			
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/sysMessageSend/form?id=${sysMessageSend.id}"><shiro:hasPermission name="sys:sysMessageSend:edit">${not empty sysMessageSend.id?fns:i18nMessage('common.view'):fns:i18nMessage('common.send')}</shiro:hasPermission>${fns:i18nMessage('sysMessage')}<shiro:lacksPermission name="sys:sysMessageSend:edit"></shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysMessageSend" action="${ctx}/sys/sysMessageSend/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessageSend.title')}：</label>
			<div class="col-sm-3">
				<form:input path="title" htmlEscape="false" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessageSend.content')}：</label>
			<div class="col-sm-3">
				<form:textarea path="content" htmlEscape="false" rows="4" class="form-control input-xxlarge "/>
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
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.targetType')}：</label>
			<div class="col-sm-3">
				<form:input path="target" htmlEscape="false" value="${fns:getDictLabels(sysMessageSend.target,'accepter_type','0')}" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.targetObj')}：</label>
			<div class="col-sm-3">
				<form:textarea path="receivers" htmlEscape="false" rows="3" class="input-xlarge form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.nums')}：</label>
			<div class="col-sm-3">
				<form:input path="count" htmlEscape="false" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.sendTime')}：</label>
			<div class="col-sm-3">
				<input name="updateDate" type="text" readonly="readonly" maxlength="20" class="form-control input-xlarge" 
				value="<fmt:formatDate value="${sysMessageSend.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
			<input id="btnCancel" class="btn btn-primary" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>