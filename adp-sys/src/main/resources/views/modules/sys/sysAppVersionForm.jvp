<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('appVersion.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			hideError("#modal1");
			
			 var appname = $("#appname").val();
			 var path = $("#path").val();
			 if( appname == "" && appname.trim() == ""){
				 hideError("#btnCancel2");
				 hideError("#btnCancel3");
				 hideError("#currentVersion");
			 }

			$("#btnCancel3").click(function(){
				 var appname = $("#appname").val();
				 var path = $("#path").val();
				if( appname != "" && appname.trim() != "")
					$.ajax({
						url : path + "/sys/sysAppVersion/executeAllSql?appname="+appname,
						type:'get',
						dataType:'json',
						success : function(result) {
							layer.msg(result.msg);							
						},
						error:function(msg){
							console.log(msg);
						},
					});
				 
			 });
			
			 $("#btnCancel2").click(function(){
				 var appname = $("#appname").val();
				 var path = $("#path").val();
				if( appname != "" && appname.trim() != "")
					$.ajax({
						url : path + "/sys/sysAppVersion/executeMenu?appname="+appname,
						type:'get',
						dataType:'json',
						success : function(result) {
							layer.msg(result.msg);							
						},
						error:function(msg){
							console.log(msg);
						},
					});
				 
			 });
			
			     
				 $("#newversion").click(function(){
		
					layer.open({
				        type: 1,
				        title: "选择升级版本的脚本",
		 		        shadeClose: true, 
		 		        shade: 0.5,
				        maxmin: true, //开启最大化最小化按钮
				        content:  $('#modal1'),
				        area: [400, 400],
				        btn: ['确定', '取消'],
				        success:function(layero,index){
				        	 console.log(layero, index);
				        },

				        yes: function(index, layero){
					        layer.close(index); 
						}
				    });
						
					});	
		                   	
			$("#appname").blur(function() {
				
				var appname = $("#appname").val();
				var path = $("#path").val();
				console.log(path);
				if(appname == "" || appname.trim() == ""){
					showError(appnameNull);
					$("#btnSubmit").attr({"disabled":"true"});
				}else{
					
					$.ajax({
						url : path + "/sys/sysAppVersion/find",
						data : {
							appname : appname,
							
						},
						success : function(result) {
							var data = result.body.result;
							console.log("data=",data);
							if (data == "true") {
								showError(appnameError);								
								hideError(appnameNull);
								$("#btnSubmit").attr({"disabled":"true"});
							}else if(data == "error"){
								hideError(appnameError);
								hideError(appnameNull);
								$("#btnSubmit").removeAttr("disabled");
							}
						}
					});
				}
			}).keyup(function(){
			    //triggerHandler 防止事件执行完后，浏览器自动为标签获得焦点
			    $(this).triggerHandler("blur"); 
			}).focus(function(){
			    $(this).triggerHandler("focus");
			});
			 
			 
			 
			 
			//获得焦点
	     $("#appname").focus(function() {
				hideError(appnameError);
				hideError(appnameNull);
			});
			
			var appname = $('#appname').val();
			//規則編碼已存在，失去光标事件失效
			if(appname != "" || appname.trim() != "" ){
				$("#appname").unbind("click");
			}else{
				$("#appname").bind("click");
			} 
			
			//显示与隐藏
	function showError(jqId) {
				$(jqId).show();
			}
			
			// 隐藏错误信息
	function hideError(jqId) {
				$(jqId).hide();
			}
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
    	return false;
    }

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
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/sysAppVersion/">${fns:i18nMessage('appVersion.managementList')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysAppVersion/form?id=${sysAppVersion.appname}">${fns:i18nMessage('appVersion.manager')}<shiro:hasPermission name="sys:sysAppVersion:edit">${not empty sysAppVersion.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:sysAppVersion:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysAppVersion" action="${ctx}/sys/sysAppVersion/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="path" value="${ctx}"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('appVersion.applicationProjectName')}：</label>
			<div class="col-sm-3">
			    <form:input path="appname" disabled="${not empty sysAppVersion.appname?true:false}"  htmlEscape="false" maxlength="20" class="input-xlarge required form-control"/>
				<span class="help-inline"><font id="appnameError" color="red" style="display:none">已经出现相同单号编码，请重新输入</font> <font id="appnameNull" color="red" style="display:none">请输入单号编码</font></span>
			</div>
		</div>
		<div class="form-group" >
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('appVersion.currentVersion')}：</label>
			<div class="col-sm-3" >
				<form:input path="version" disabled="${not empty sysAppVersion.version?true:false}"  htmlEscape="false" maxlength="20" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group" id="currentVersion">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('appVersion.modifyVersion')}：</label>
			<div class="col-sm-3">
				<form:input path="newversion"   htmlEscape="false" maxlength="20" class="form-control input-xlarge"/>
			</div>
		</div>
		<div class="form-group">
			 <div class="col-sm-4 col-sm-offset-2">
		     <input id="btnCancel2" class="btn" type="button" value="${fns:i18nMessage('appVersion.executeMenuScript')}" />
		     <input id="btnCancel3" class="btn" type="button" value="${fns:i18nMessage('appVersion.executeAllScript')}" />
		     </div>
		</div>
		<div class="form-group">
             <div class="col-sm-5 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
<script>
	function test(msg){
		/* layer.msg(msg); */
		$("#newversion").val(msg);
		layer.closeAll();
	}
</script>

<div style="hidden" id="modal1">
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>项目全部版本</th>
		</tr>
	</thead> 
	<tbody>
 <c:forEach items="${versionsList}" var="item">
 	<tr>
	<td><a onclick="test('${item}')">${item}</a></td>
	</tr>
 </c:forEach>
 	</tbody>
	</table>
	</div>
</body>
</html>