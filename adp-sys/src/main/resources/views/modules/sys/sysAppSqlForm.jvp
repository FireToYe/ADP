<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>应用sql管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
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
		function sendSql(data) {
			var sql = $(data).val();
			if(isNull(sql)){
				return;
			}
			var path = $("#path").val();
			$.ajax({
				url : path + "/sys/sysAppSql/executeSql",
				type:'post',
				data:{
					sql:sql
				},
				dataType:'json',
				success:function(result){
					layer.msg(result.msg);
					if(result.msg=='执行sql脚本成功'&&data=='#errorSql'){
						$("#errorSql").val("");
						$.post('${ctx}/sys/sysAppSql/updateErrorSql',$('#id'));
					}
				},
				error:function(msg){
					layer.msg(result.msg);
				}
			})
		}
		function sendErrorSql() {
			
		}
		function goServerHome() {
			window.location.href =ctx+ "/sys/sysAppSql/"
		}
		function isNull( str ){
			if ( str == "" ) return true;
			var regu = "^[ ]+$";
			var re = new RegExp(regu);
			return re.test(str);
		}
/* 		function clearErrorSql() {
			$("#reverseError").val("");
		} */
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/sysAppSql/">${fns:i18nMessage('appSql.managementList')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysAppSql/form?id=${sysAppSql.id}">${fns:i18nMessage('appSql.modify')}<shiro:lacksPermission name="sys:sysAppSql:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysAppSql" action="${ctx}/sys/sysAppSql/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="path" value="${ctx}"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('appVersion.applicationProjectName')}：</label>
			<div class="col-sm-3">
				<form:input path="appname" disabled="${not empty sysAppSql.appname?true:false}" htmlEscape="false" maxlength="20" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('appVersion.currentVersion')}：</label>
			<div class="col-sm-3">
				<form:input path="version" disabled="${not empty sysAppSql.version?true:false}" htmlEscape="false" maxlength="20" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">

			<label class="col-sm-1 control-label">${fns:i18nMessage('appSql.errorSqlScript')}：</label>
			<div class="col-sm-5">
				<form:textarea id="errorSql" path="sqlerror" rows="20" class="form-control input-xxlarge " />
			</div>
			 <label class="col-sm-1 control-label">${fns:i18nMessage('appSql.reverseSqlScript')}：</label>
			<div class="col-sm-5">
				<form:textarea id = "reverseSql" path="rollbacktext" rows="20" class="form-control input-xxlarge " />
			</div>
		</div>
 		<div class="form-group">
 				<label class="col-sm-1 control-label">${fns:i18nMessage('appSql.sqlScript')}：</label>
			<div class="col-sm-5">
				<form:textarea path="sqltext" rows="20" class="form-control input-xxlarge " />
			</div>
			</div>
		<div class="form-group">
			<div class="col-sm-4 col-sm-offset-4">
				<shiro:hasPermission name="ica:iccnIcaService:edit"> <input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}" />&nbsp;</shiro:hasPermission>
		     <input id="btnCancel3" class="btn" type="button" value="${fns:i18nMessage('appSql.exeErrorSql')}" onclick="sendSql('#errorSql')" />
		     <input id="btnCancel2" class="btn" type="button" value="${fns:i18nMessage('appSql.exeReverseSql')}" onclick="sendSql('#reverseSql')" />
<!-- 		     <input id="btnCancel4" class="btn" type="button" value="清空出错sql" onclick="clearErrorSql()" /> -->
			 <input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="goServerHome()" /><span id="sendDate"></span>
			</div>
		</div>
	</form:form>
</body>
</html>