<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('log.manager')}</title>
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
			 document.getElementById("logState").value = "${logState}";
		});
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/log/changeState">${fns:i18nMessage('log.logSwitchManager')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body ">
	<form id="inputForm" action="${ctx}/sys/log/changeStateSave" method="post" class="form-horizontal">
		<sys:message content="${message}"/>		
		<%-- <input type="text" style="display: none;" id="logState" name="logState" value="${'true'.equals(logState)?'false':'true'}"/>
	<div class="form-group">
			<label class="col-sm-1 col-sm-offset-1 control-label">当前状态：</label>
			<label class="col-sm-1 control-label">${logStateStr}</label>
		</div> --%>
		<div class="form-group">
			<label class="col-sm-1 col-sm-offset-1 control-label">${fns:i18nMessage('common.status')}：</label>
			<div class="col-sm-2">
				<select id="logState" name="logState" class="form-control input-xlarge required">
					<option value="1">${fns:i18nMessage('log.allLogs')}</option>
					<option value="2">${fns:i18nMessage('log.onlyBusinessLog')}</option>
					<option value="3">${fns:i18nMessage('log.onlyManipulateLogs')}</option>
					<option value="0">${fns:i18nMessage('log.closeLog')}</option>
				</select>
			</div>
		</div>
	<div class="form-group">
	<div class="col-sm-4 col-sm-offset-2">
			<shiro:hasPermission name="sys:log:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
		</div>
		</div>
	</form>
	</div>
	</div>
	</div>
	</div>
	</div>
</body>
</html>