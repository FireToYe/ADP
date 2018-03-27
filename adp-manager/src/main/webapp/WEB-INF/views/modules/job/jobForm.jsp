<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('job.job_task_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
			$("#inputForm").validate({
				submitHandler: function(form){
					loading("${fns:i18nMessage('common.loading')}");
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("${fns:i18nMessage('common.form.error.msg')}");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			if("${job.executeType}"){
				typeChangerd("${job.executeType}");
			}else{
				typeChangerd("1");
			}
		});
		
		function typeChangerd(value){
			$("#serviceCodeDiv").css("display","none");
/* 			$("#parameter").css("display","none"); */
			$("#springIdDiv").css("display","none");
			$("#methodNameDiv").css("display","none");
			$("#beanClassNameDiv").css("display","none");
			$("#resServiceCodeDiv").css("display","none");
			if("1"==value){
				$("#serviceCodeDiv").css("display","block");
				$("#resServiceCodeDiv").css("display","block");
				/* $("#parameter").css("display","block"); */
			}else if("2"==value){
				$("#springIdDiv").css("display","block");
				$("#methodNameDiv").css("display","block");
			}else if("3"==value){
				$("#beanClassNameDiv").css("display","block");
				$("#methodNameDiv").css("display","block");
			}
		}
		
		function cronSelect(){
			layer.open({
				  type: 2,
				  title: 'cron选择页',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['90%', '90%'],
				  content: '${ctx}/job/job/cronSelect', //iframe的url
				btn: ['确定','关闭'],
	               yes: function(index){
	                        //当点击‘确定’按钮的时候，获取弹出层返回的值
	                        var res = window["layui-layer-iframe" + index].callbackdata();
	                        //打印返回的值，看是否有我们想返回的值。
	                        console.log(res);
	                        //最后关闭弹出层
	                        layer.close(index);
	                        $("#cronExpression").val(res);
	                    },
	                    cancel: function(){
	                        //右上角关闭回调
	                    }
				}); 
			
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/job/job/">${fns:i18nMessage('job.job_task_list')}</a></li>
		<li class="active"><a href="${ctx}/job/job/form?id=${job.id}">${fns:i18nMessage('job.job_task')}<shiro:hasPermission name="job:job:edit"><c:choose><c:when test="${not empty job.id}">${fns:i18nMessage('ica.db_modify')}</c:when><c:otherwise>${fns:i18nMessage('ica.db_add')}</c:otherwise></c:choose></shiro:hasPermission><shiro:lacksPermission name="job:job:edit">查看${fns:i18nMessage('common.loading')}</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="job" action="${ctx}/job/job/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>${fns:i18nMessage('job.job_name')}：</label>
			<div class="col-sm-3">
				<form:input path="jobName" htmlEscape="false" maxlength="255" class="form-control input-xlarge  required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>${fns:i18nMessage('job.job_group')}：</label>
			<div class="col-sm-3">
				<form:input path="jobGroup" htmlEscape="false" maxlength="255" class="form-control input-xlarge  required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.detail')}：</label>
			<div class="col-sm-3">
				<form:input path="detail" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.cron_expression')}：</label>
			<div class="col-sm-3">
				<form:input path="cronExpression" readOnly="true" onclick="cronSelect()" htmlEscape="false" maxlength="255" class="form-control input-xlarge  required"/>
			</div>
		</div>
<%-- 		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			状态：1:启动  0：停止：</label>
			<div class="col-sm-3">
				<form:select path="jobState" class="form-control input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div> --%>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.is_concurrent')}：</label>
			<div class="col-sm-3">
				<form:select path="isConcurrent" class="form-control input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.execute_type')}：</label>
			<div class="col-sm-3">
				<form:select path="executeType" class="form-control input-xlarge " onchange="typeChangerd(this.value)">
					<%-- <form:option value="" label=""/> --%>
					<form:options items="${fns:getDictList('dc_job_execute_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group" id="serviceCodeDiv">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group" id="resServiceCodeDiv">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.res_service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="resServiceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.parameter')}：</label>
			<div class="col-sm-3">
				<form:textarea path="parameter" rows="20" class="form-control input-xxlarge "/>
			</div>
		</div>
		<div class="form-group" id ="springIdDiv">
			<label class="col-sm-2 control-label">springBeanId：</label>
			<div class="col-sm-3">
				<form:input path="springId" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group" id="beanClassNameDiv">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.bean_class_name')}：</label>
			<div class="col-sm-3">
				<form:input path="beanClassName" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group" id="methodNameDiv">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.method_name')}：</label>
			<div class="col-sm-3">
				<form:input path="methodName" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
<%-- 		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			是否依赖上一周期：</label>
			<div class="col-sm-3">
				<form:select path="dependLast" class="form-control input-xlarge required">
					<form:options items="${fns:getDictList('srm_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div> --%>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('job.depend_last')}：</label>
			<div class="col-sm-3">
				<form:select path="dependLastTime" class="form-control input-xlarge required">
					<form:options items="${fns:getDictList('srm_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.date_format')}：</label>
			<div class="col-sm-3">
				<form:input path="dateFormat" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.data_parameter_name')}：</label>
			<div class="col-sm-3">
				<form:input path="dataParameterName" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
	<%-- 	<div class="form-group">
			<label class="col-sm-2 control-label">创建时间：</label>
			<div class="col-sm-3">
				<div class="input-group date" id="datepicker-createTime">  
					<input name="createTime" type="text" readonly="readonly" maxlength="20" class="form-control input-medium Wdate " 
					value="<fmt:formatDate value="${job.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 
					<span class="input-group-btn input-group-addon"> <i class="fa fa-calendar"></i></span>
				</div>
				<script type="text/javascript">
					$('#datepicker-createTime').datepicker({
					todayBtn: "linked",
					keyboardNavigation: false,
					forceParse: false,
					calendarWeeks: true,
					autoclose: true});
				</script>	
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">更新时间：</label>
			<div class="col-sm-3">
				<div class="input-group date" id="datepicker-updateTime">  
					<input name="updateTime" type="text" readonly="readonly" maxlength="20" class="form-control input-medium Wdate " 
					value="<fmt:formatDate value="${job.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 
					<span class="input-group-btn input-group-addon"> <i class="fa fa-calendar"></i></span>
				</div>
				<script type="text/javascript">
					$('#datepicker-updateTime').datepicker({
					todayBtn: "linked",
					keyboardNavigation: false,
					forceParse: false,
					calendarWeeks: true,
					autoclose: true});
				</script>	
			</div>
		</div> --%>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>