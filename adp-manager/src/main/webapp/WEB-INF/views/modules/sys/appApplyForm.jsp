<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('app.authorizationApplicationManagement')}</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});

	function examine(openId, status) {
		if(status==2 && !$("#remarks").val()){
			$("#messageBox").text("请输入不通过备注!");
			return;
		}
		
		var relateUser=$("#relateUser").val();
		if($("#newuser").attr('checked')){
			relateUser='new';
		}
		
		window.location.href = ctx
				+ "/sys/appApply/examineInfo?openId=${appApply.openId}&status="
				+ status+"&remarks="+$("#remarks").val() + "&relateUser="+relateUser ;
	}

	function back() {
		window.location.href = ctx + "/sys/appApply";
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li><a href="${ctx}/sys/appApply/">${fns:i18nMessage('app.authorizationApplicationList')}</a></li>
				<li class="active"><a
					href="${ctx}/sys/appApply/form?id=${appApply.openId}">${fns:i18nMessage('app.authorizationApplication')}<%-- <shiro:hasPermission name="sys:appApply:edit">${not empty appApply.openId?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:appApply:edit"> --%>查看<%-- </shiro:lacksPermission> --%></a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="inputForm" modelAttribute="appApply"
							action="${ctx}/sys/appApply/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<sys:message content="${message}" />
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('app.openid')}：</label>
								<div class="col-sm-3">
									<form:input path="openId" htmlEscape="false" maxlength="32"
										class="input-xlarge form-control" readonly="true" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('app')}：</label>
								<div class="col-sm-3">
									<input type="text" value="${appInfo.appName}" maxlength="32" class="input-xlarge form-control" readonly="readonly" />

								</div>
							</div>
							<c:forEach var="entry" items="${bodyMap}" begin="0"
								varStatus="status">
								<div class="form-group">
									<label class="col-sm-2 control-label">${entry.key}：</label>
									<div class="col-sm-3">
										<c:choose>  
										    <c:when test="${'类型' eq entry.key}">
										    	<input type="text" value="${fns:getDictLabel(entry.value, 'app_apply_type', '')}" class="input-xlarge form-control" readonly="readonly" />
										    </c:when>
										    <c:otherwise>
										    	<input type="text" value="${entry.value}" maxlength="2000" class="input-xlarge form-control" readonly="readonly" />
										    </c:otherwise>  
										</c:choose>
									</div>
								</div>
							</c:forEach>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('common.status')}：</label>
								<div class="col-sm-3">
									<input type="text"
										value="${fns:getDictLabel(appApply.status, 'app_apply_status', '')}" maxlength="32" class="input-xlarge form-control" readonly="readonly" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('app.note')}：</label>
								<div class="col-sm-3">
									<form:textarea path="remarks" htmlEscape="false" rows="4"
										maxlength="255" class="input-xxlarge form-control" readonly="${('0' eq appApply.status)?false:true}" />
								</div>
							</div>
							<c:if test="${'0' eq appApply.status }">
							<div class="form-group">
								<label class="col-sm-2 control-label">：</label>
								<div class="col-sm-3">
									<form:input path="relateUser" htmlEscape="false" maxlength="64" class="input-xlarge form-control" />
									<%-- <sys:treeselect id="relate_user" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"/>
									<label for="newuser" ><input id="newuser" name="newuser" type="checkbox" />创建新用户</label> --%>
								</div>
							</div>
							</c:if>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-2">
								<%-- <shiro:hasPermission name="sys:appApply:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission> --%>
								<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="back()" />
								<c:if test="${'0' eq appApply.status }">
									<input id="btnTG" class="btn btn-primary" type="button" value="同意授权" onclick="examine('${appApply.openId}','1')" />
									<input id="btnBTG" class="btn btn-primary" type="button" value="禁止授权" onclick="examine('${appApply.openId}','2')" />
								</c:if>
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