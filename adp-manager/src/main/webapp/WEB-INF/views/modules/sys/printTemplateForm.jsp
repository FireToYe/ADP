<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('printTemplate.manager')}</title>
	<meta name="decorator" content="default"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="assets/css/bootstrap-responsive.css" rel="stylesheet">
	<script type="text/javascript">
		$(document).ready(function() {
			$("#result").hide();
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
		});
		
		function printShow(params){
			var data = {};
			var templateParams = $("#templateParams").val();
			var templateContent = $("#templateContent").val();
			if(templateContent == null || templateContent ==''){
				layer.alert("模板内容不能为空");
				return;
			}
			if(templateParams == null || templateParams ==''){
				layer.alert("查询参数不能为空");
				return;
			}
			try{
				var c = JSON.parse(templateParams)
				var list=new Array()
				list[0]=c.list[0];
				var obj = new Object();
				obj.list = list;
				var length = c.list.length
				data.templateParams = JSON.stringify(obj);
			}catch(e){
				layer.alert("查询参数json数据错误");
				return;
			}
			data.templateContent = templateContent;
			data.number = params
			if(params=="html"){
				data.number = 1;
			}
			if(params=="pdf"){
				$.ajax({
					url:'${ctx}/sys/printTemplate/printShowPre',
					type:'POST',
					data:data,
					dataType:'json',
					success:function(result){
	 					if(result.head.status == 1){
							var key = result.body;
							window.open("${ctx}/sys/printTemplate/printShow?key="+key);   
						}
						if(result.head.status == 0){
							alert(result.head.errorMsg);
							return;
						}
					},
					error:function(){
						layer.alert("响应错误")
					}
				})	
			} else {
				$.ajax({
					url:'${ctx}/sys/printTemplate/printHtml',
					type:'POST',
					data:data,
					dataType:'json',
					success:function(result){
						var zh = RndNum(5);
						if(params=="html"){
							$("#resultIframe").attr("src","${ctxStatic}/printview/preview.html?zh="+zh);
							$("#result").show();
						}else {
							window.open("../../../static/printview/preview.html?zh="+zh);
						}
					},
					error:function(){
						layer.alert("响应错误")
					}
				})
		  }		
		}
		function preview(){
			var printNumber = $("#printNumber").val();
			if (printNumber<=0||printNumber=='') {
				return;
			}
			printShow(printNumber);
		}
		function RndNum(n){
		    var rnd="";
		    for(var i=0;i<n;i++)
		        rnd+=Math.floor(Math.random()*10);
		    return rnd;
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/printTemplate/">${fns:i18nMessage('printTemplate.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/printTemplate/form?id=${printTemplate.id}">${fns:i18nMessage('printTemplate')}<shiro:hasPermission name="sys:printTemplate:edit">${not empty printTemplate.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission></a></li>
	</ul>
		<div class="tab-content">
<div class="tab-pane active">
<div class="panel-body ">	
	<form:form id="inputForm" modelAttribute="printTemplate" action="${ctx}/sys/printTemplate/save" method="post" class="form-horizontal" >
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
				<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('printTemplate.code')}：</label>
			<div class="col-sm-3">
				<form:input path="templateCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
			<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('printTemplate.isItEnabled')}：</label>
			<div class="col-sm-3">
				<form:select path="status" class="form-control input-xlarge required">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('printTemplate.name')}：</label>
			<div class="col-sm-3">
				<form:input path="name" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
			<label class="col-sm-2 control-label">${fns:i18nMessage('printTemplate.description')}：</label>
			<div class="col-sm-3">
				<form:input path="description" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>${fns:i18nMessage('printTemplate.testParameters')}：</label>
			<div class="col-sm-5">
				<form:textarea id="templateParams" path="templateParams" rows="20" class="form-control input-xxlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>${fns:i18nMessage('printTemplate.content')}：</label>
			<div class="col-sm-5">
				<form:textarea id="templateContent" path="templateContent" rows="20" class="form-control input-xxlarge required"/>
			</div>
			<div id="result">
				<iframe id="resultIframe" frameborder="0" name="i" width="600" height="400"  src="${ctxStatic}/printview/start.html"></iframe>
			</div>
		</div>
		<div class="form-group">
		   <div class="col-sm-4 col-sm-offset-2">
			<%-- <shiro:hasPermission name="ica:iccnIcaService:edit"> --%>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>
			<%-- &nbsp;</shiro:hasPermission> --%>
			<input type="button" class="btn" value="${fns:i18nMessage('printTemplate.printPreview')}" onclick="printShow('pdf')">
			<input type="button" class="btn" value="Html预览" onclick="printShow('html')">
			<input type="number" id="printNumber" placeholder="请输入预览页数" value="">
			<input type="button" class="btn" value="预览" onclick="preview()">
			<span id="sendDate"></span>
		    </div>
        </div>
	</form:form>
	</div>
	</div>
	</div>
	<iframe id="id_iframe" name="nm_iframe" style="display:none;"></iframe>  
	</div></div>
</body>
</html>