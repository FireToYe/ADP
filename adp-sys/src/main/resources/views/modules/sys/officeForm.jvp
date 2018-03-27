<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('office.manager')}</title>
	<meta name="decorator" content="default"/>
		<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
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
			
			jQuery.validator.addMethod("simpleCode", function(value, element) {
				var id = $('#id').val();
				var ok = this.optional(element);
				$.ajax({
					url : '${ctx}/sys/office/isExistCode',
					data : 'code=' + value + '&id=' + id,
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
			},"机构编码已经存在");
			
		});
		//选择负责人(flag=1,主负责人；flag=2,副负责人)
		function choosePerson(officeId,officeName,flag){
			layer.open({
		        type: 2,
		        title: '选择负责人',
 		        shadeClose: true, 
 		        shade: 0.5,
		        maxmin: true, //开启最大化最小化按钮
		        area: ['800px', '600px'],
		        content: "${ctx}/sys/user/personList?office.id=" + officeId + "&office.name=" + officeName,
		        btn: ['确定', '取消'],
		        yes: function(index, layero){
	        	 	var body = layer.getChildFrame('body', index);
	        	    var iframeWin = window[layero.find('iframe')[0]['name']];
	        	    var checkedItem=body.find("tbody input:checkbox[checked]").parent().next();
	        	    var $checkbox=body.find("table input:checkbox:checked");
	        	    
                    var person = $checkbox.parent().next().next().next().text();
                    
                    if(flag == 1){
                   		$('#primaryPerson').val(person);
                    }else if(flag == 2){
                    	$('#deputyPerson').val(person);
                    }
                   	layer.closeAll('iframe'); 
	          }
	      });
		}
	</script>
</head>
<body>
<!-- <div class="wrapper wrapper-content"> -->
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/office/list?id=${office.parent.id}&parentIds=${office.parentIds}">${fns:i18nMessage('office.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">${fns:i18nMessage('office')} <shiro:hasPermission name="sys:office:edit">${not empty office.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body ">
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.parent')}:</label>
			<div class="col-sm-3 controls">
                <sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
					title="${fns:i18nMessage('office')}" url="/sys/office/treeData" extId="${office.id}" cssClass="" allowClear="${office.currentUser.admin}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.area')}:</label>
			<div class="col-sm-3 controls">
                <sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
					title="${fns:i18nMessage('area')} " url="/sys/area/treeData" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('office.name')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('office.code')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="code" htmlEscape="false" maxlength="50" class="form-control required simpleCode"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.type')}:</label>
			<div class="col-sm-3 controls">
				<form:select path="type" class="input-medium form-control">
					<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.grade')}:</label>
			<div class="col-sm-3 controls">
				<form:select path="grade" class="input-medium form-control">
					<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.availability')}:</label>
			<div class="col-sm-3 controls">
				<form:select path="useable" class="form-control">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
<%-- 		<div class="form-group">
			<label class="col-sm-2 control-label">主负责人:</label>
			<div class="col-sm-3 controls" onclick="choosePerson('${not empty office.id?office.id:''}','${not empty office.name?office.name:''}','1');">
				<form:input id="primaryPerson" path="primaryPerson" htmlEscape="false" class="input-large form-control" />
				 <sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">副负责人:</label>
			<div class="col-sm-3 controls" onclick="choosePerson('${not empty office.id?office.id:''}','${not empty office.name?office.name:''}','2');">
				<form:input id="deputyPerson" path="deputyPerson" htmlEscape="false" class="input-large form-control" />
				 <sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div> --%>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.address')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="address" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.postcode')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="zipCode" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.personInCharge')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="master" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.phone')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('office.fax')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="fax" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.mail')}:</label>
			<div class="col-sm-3 controls">
				<form:input path="email" htmlEscape="false" maxlength="50" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('common.note')}:</label>
			<div class="col-sm-3 controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge form-control"/>
			</div>
		</div>
		<c:if test="${empty office.id}">
			<div class="form-group">
				<label class="col-sm-2 control-label">${fns:i18nMessage('office.addChildDeptList')}:</label>
				<div class="col-sm-3 controls">
					<form:checkboxes path="childDeptList" items="${fns:getDictList('sys_office_common')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</div>
			</div>
		</c:if>
		<div class="form-group">
		<div class="col-sm-4 col-sm-offset-2">
			<shiro:hasPermission name="sys:office:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
			</div>
		</div>
	</form:form>
	</div>
	</div>
	</div>
	</div>
	<!-- </div> -->
</body>
</html>