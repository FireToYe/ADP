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
			
			jQuery.validator.addMethod("maxNums", function() {
				var files=$("#files").val();
				var values = [];
				values = files.split("|");
				//最多上传5个
			    return (values.length < 8);
			}, "${fns:i18nMessage('sysMessageSend.maxFilesTip')}");
			
			jQuery.validator.addMethod("singleSize", function() {
				var files=$("#files").val();
				var values = [];
				values = files.split("|");
				for(var i=0; i < values.length; i++){
					var v = decodeURI(values[i]);
					if(getlength(v) > 80){
						return false;
					}
				}
				return true;
			}, "${fns:i18nMessage('sysMessageSend.attatchmentTip')}");
			
		    function getlength(string){  
		        //定义一个计数器  
		        var count = 0;  
		        for(var i=0;i<string.length;i++){  
		            //对每一位字符串进行判断，如果Unicode编码在0-127，计数器+1；否则+2  
		            if(string.charCodeAt(i)<128 && string.charCodeAt(i)>=0 ){  
		                count++; //一个英文在Unicode表中站一个字符位  
		            }else{  
		                count+=2; //一个中文在Unicode表中站二个字符位  
		            }  
		        }  
		        return count;  
		    }  
			
			$("#target").change(function(){
				var v=$('#target option:selected').val();
				if(v == 0){
					$("#accepters").hide();
					$("#role").hide();
					$("#office").hide();
					$("#accepters").val('');
					$("#role").val('');
					$("#office").val('');
				}else if(v == 1){
					$("#accepters").hide();
					$("#role").hide();
					$("#office").show();
					$("#accepters").val('');
					$("#role").val('');
				}else if(v == 2){
					$("#accepters").hide();
					$("#office").hide();
					$("#role").show();
					$("#accepters").val('');
					$("#office").val('');
				}else if(v == 3){
					$("#office").hide();
					$("#role").hide();
					$("#accepters").show();
					$("#role").val('');
					$("#office").val('');
				}
			});
			
			$("#receiversName").click(function(){
			    layer.open({
			        type: 2,
			        title: '选择消息接收人',
	 		        shadeClose: true, 
	 		        shade: 0.5,
			        maxmin: true, //开启最大化最小化按钮
			        area: ['800px', '500px'],
			        content: '${ctx}/sys/user/accepter',
			        btn: ['确定', '取消'],
			        yes: function(index, layero){
		        	 	var body = layer.getChildFrame('body', index);
		        	    var iframeWin = window[layero.find('iframe')[0]['name']];
		        	    var checkedItem=body.find("tbody input:checkbox[checked]").parent().next();
		        	    var $checkbox=body.find("table input:checkbox:checked");
		        	    var allSelectedId= body.find("#dataArray").val();

		        	    var receivers =[];
		        	    var receiversName = [];
		        	    
		        	    var allreceivers=JSON.parse(allSelectedId);
		        	    if(allreceivers){
		        	    	$.each(allreceivers,function(i,n){
		        	    		receivers.push(n.id);
		        	    		receiversName.push(n.name);
		        	    	});
		        	    }
		        	    
						$('#receivers').val(receivers);
						$('#receiversName').val(receiversName);
	                   	
	                   	layer.closeAll('iframe'); 
		          }
		      });
			});
			
			$("#roleName").click(function(){
			    layer.open({
			        type: 2,
			        title: '选择消息接收角色',
	 		        shadeClose: true, 
	 		        shade: 0.5,
			        maxmin: true, //开启最大化最小化按钮
			        area: ['800px', '500px'],
			        content: '${ctx}/sys/role/messageRoleList',
			        btn: ['确定', '取消'],
			        yes: function(index, layero){
		        	 	var body = layer.getChildFrame('body', index);
		        	    var iframeWin = window[layero.find('iframe')[0]['name']];
		        	    var checkedItem=body.find("tbody input:checkbox[checked]").parent().next();
		        	    var allSelectedId= body.find("#dataArray").val();
						
		        	    var receivers =[];
		        	    var receiversName = [];
		        	    
		        	    var allreceivers=JSON.parse(allSelectedId);
		        	    if(allreceivers){
		        	    	$.each(allreceivers,function(i,n){
		        	    		receivers.push(n.id);
		        	    		receiversName.push(n.name);
		        	    	});
		        	    }
		        	    
						$('#receivers').val(receivers);
						$('#roleName').val(receiversName);
	                   	
	                   	layer.closeAll('iframe'); 
		          }
		      });
			});
			
			$("#officeName").click(function(){
			    layer.open({
			        type: 2,
			        title: '选择消息接收组织',
	 		        shadeClose: true, 
	 		        shade: 0.5,
			        maxmin: true, //开启最大化最小化按钮
			        area: ['800px', '500px'],
			        content: '${ctx}/sys/office/messageOfficeList?id=&parentIds=',
			        btn: ['确定', '取消'],
			        yes: function(index, layero){
		        	 	var body = layer.getChildFrame('body', index);
		        	    var iframeWin = window[layero.find('iframe')[0]['name']];
		        	    var checkedItem=body.find("tbody input:checkbox[checked]").parent().next();
		        	    var $checkbox=body.find("table input:checkbox:checked");
						
		        	    var receivers =[];
		        	    var officeName = [];
		        	    var pids = [];
		        	    $checkbox.each(function(i, o){
		        	    	receivers[i] = $(o).val();
		        	    	officeName[i] = $(o).parent().next().next().text().trim();
		        	    	pids[i] = $(o).parent().next().text().trim();
		        	    });
						$('#receivers').val(receivers);
						$('#officeName').val(officeName);
						$('#pids').val(pids);
	                   	
	                   	layer.closeAll('iframe'); 
		          }
		      });
			});
		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/sysMessageSend/">${fns:i18nMessage('sysMessageSend.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysMessageSend/form?id=${sysMessageSend.id}"><shiro:hasPermission name="sys:sysMessageSend:edit">${not empty sysMessageSend.id?fns:i18nMessage('common.view'):fns:i18nMessage('common.send')}</shiro:hasPermission>${fns:i18nMessage('sysMessage')}</a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="sysMessageSend" action="${ctx}/sys/sysMessageSend/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysMessageSend.title')}：</label>
			<div class="col-sm-3">
				<form:input path="title" htmlEscape="false" maxlength="100" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysMessageSend.content')}：</label>
			<div class="col-sm-3">
				<form:textarea path="content" htmlEscape="false" rows="4" maxlength="256" class="form-control input-xxlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"></span>
			${fns:i18nMessage('sysMessage.attachment')}：</label>
			<div class="col-sm-3">
				<sys:ckfinder input="files" type="files" uploadPath="" selectMultiple="true"/>
				<input htmlEscape="false" value="" class="maxNums singleSize" style="border:none"/>
				<form:textarea id="files" path="attachmentIds" htmlEscape="false" maxlength="255" class="form-control hide"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('sysMessage.targetObj')}：</label>
			<div class="col-sm-3">
				<form:select path="target" class="input-medium form-control">
					<form:options items="${fns:getDictList('accepter_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="form-group" id="role" hidden="true">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysMessageSend.receivingRole')}：</label>
			<div class="col-sm-3">
				<input id="roleName" htmlEscape="false" value="" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group" id="office" hidden="true">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysMessageSend.receivingOrganization')}：</label>
			<div class="col-sm-3">
				<form:hidden path="pids" htmlEscape="false" value="" class="form-control input-xlarge required" hidden = "true"/>
				<input id="officeName" htmlEscape="false" value="" class="form-control input-xlarge required"/>
			</div>
		</div>
		
		<div class="form-group" id="accepters" hidden="true">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font> </span>${fns:i18nMessage('sysMessageSend.receivingPerson')}：</label>
			<div class="col-sm-3">
				<input id="receiversName" htmlEscape="false" value="" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
       			<c:if test="${empty sysMessageSend.id}">
       				<shiro:hasPermission name="sys:sysMessageSend:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/></shiro:hasPermission>
       			</c:if>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
		<form:hidden path="receivers" htmlEscape="false" value="" class="form-control input-xlarge required" hidden = "true"/>
	</form:form>
</body>
</html>