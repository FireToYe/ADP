<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>保存管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {$("#inputForm").validate(
								{
									rules:{schemeName: {remote: "${ctx}/db/dbBackupScheme/checkOnly?id=${dbBackupScheme.id}"}},
									messages:{schemeName: {remote: "${fns:i18nMessage('adb_db.schemeName_exist_tip')}"}},
									submitHandler : function(form) {
										btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										  $("#messageBox").text("${fns:i18nMessage('common.form.error.msg')}");
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

				 $("#backupTables").on("click", function() {
					 $("#spanmodal").empty();
					$("#myModal").modal()
					var oldValue = $("#backupTables").val();
					var oldValueArr = new Array();
					if(oldValue){
						oldValueArr = oldValue.split(",");
					}
					$.ajax({
						url:"${ctx}/db/dbBackupScheme/stTabeles",
						type:"post",
						success:function(res){
							var html = '<table>'
							res.forEach(function(item,index){
								if(index%5==0){
									html+='<tr>';
								}
								if(contains(oldValueArr,item)){
									html+='<td width="15%"><lable><input type="checkbox" checked="checked" name="tables" value='+item+'>'+item+'</lable></td>'
								}else{
									html+='<td width="15%"><lable><input type="checkbox" name="tables" value='+item+'>'+item+'</lable></td>'
								}
								if(index%5==4){
									html+='</tr>';
								}
							})
							if(html.endWith('</tr>')){
								html+='</tr>';
							}
							html+='</table>';
							$("#spanmodal").append(html)
						}
					})
					
				});
				 $("#select").on("click", function() {
			            var ckbValue = "";
			            $("input[name='tables']:checked").each(function () {
			                ckbValue = ckbValue + $(this).val() + ",";
			            });
			            $("#backupTables").val(ckbValue.substring(0, ckbValue.length - 1));
			            $("#myModal").modal("hide");
			        });
				 $("#checkAll").on("click", function() {
			            $("input[name='tables']").each(function () {
			                this.checked = true;
			            });
			        })
			         $("#checkNoAll").on("click", function() {
			            $("input[name='tables']").each(function () {
			                this.checked = false;
			            });
			        })
			});
	String.prototype.startWith=function(str){     
		  var reg=new RegExp("^"+str);     
		  return reg.test(this);        
		}  

		String.prototype.endWith=function(str){     
		  var reg=new RegExp(str+"$");     
		  return reg.test(this);        
		}
		function contains(arr, obj) {  
			if(arr==null){
				return false;
			}
		    var i = arr.length;  
		    if(i==0){
		    	return false;
		    }
		    while (i--) {  
		        if (arr[i] === obj) {  
		            return true;  
		        }  
		    }  
		    return false;  
		}
		function cronSelect(){
			layer.open({
				  type: 2,
				  title: 'cron选择页',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['90%', '90%'],
				  content: '${ctx}/job/job/cronSelect', //iframe的url
				  btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"], //按钮
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
				<li><a href="${ctx}/db/dbBackupScheme/">${fns:i18nMessage('adp_db.dbBackUpScheme_list')}</a></li>
				<li class="active"><a
					href="${ctx}/db/dbBackupScheme/form?id=${dbBackupScheme.id}">${fns:i18nMessage('adp_db.dbBackUpScheme')} <shiro:hasPermission
							name="db:dbBackupScheme:edit">
							<c:choose><c:when test="${not empty dbBackupScheme.id}">${fns:i18nMessage('common.modify')}</c:when><c:otherwise>${fns:i18nMessage('common.add')}</c:otherwise></c:choose>
							</shiro:hasPermission>
						<shiro:lacksPermission name="db:dbBackupScheme:edit">${fns:i18nMessage('common.view')}</shiro:lacksPermission></a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="inputForm" modelAttribute="dbBackupScheme"
							action="${ctx}/db/dbBackupScheme/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<sys:message content="${message}" />
							<div class="form-group">
								<label class="col-sm-2 control-label"><span
									class="help-inline"><font color="red">*</font></span> ${fns:i18nMessage('adb_db.schemeName')}：</label>
								<div class="col-sm-3">
									<form:input path="schemeName" htmlEscape="false" maxlength="32"
										class="form-control input-xlarge required" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><span
									class="help-inline"><font color="red">*</font></span>
									${fns:i18nMessage('adp_db.backupTables')}：</label>
								<div class="col-sm-3">
									<form:textarea rows="5" readOnly="true" path="backupTables" htmlEscape="false"
										 class="form-control input-xxlarge" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('adp_db.extend')}：</label>
								<div class="col-sm-3">
									<form:input path="extend" htmlEscape="false"
										class="form-control input-xlarge " />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('adb_db.remarks')}：</label>
								<div class="col-sm-3">
									<form:textarea path="remarks" htmlEscape="false" rows="4"
										maxlength="255" class="form-control input-xxlarge " />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('adp_db.job_state')}：</label>
								<div class="col-sm-3">
									<form:select path="jobStatus" class="form-control input-xlarge ">
								<%-- 		<form:option value="" label=""/> --%>
										<form:options items="${fns:getDictList('iccn_ica_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('adp_db.cron_expression')}：</label>
								<div class="col-sm-3">
									<form:input path="cronExpression" htmlEscape="false" maxlength="255" readOnly="true" onClick="cronSelect()" class="form-control input-xlarge "/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">${fns:i18nMessage('adp_db.save_count')}：</label>
								<div class="col-sm-3">
									<form:input path="saveCount" htmlEscape="false" maxlength="255" class="form-control input-xlarge digits"/>
									${fns:i18nMessage('adp_db.save_count_tip')}
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-4 col-sm-offset-2">
									<shiro:hasPermission name="sys:menu:edit">
										<input id="btnSubmit" class="btn btn-primary" type="submit"
											value="${fns:i18nMessage('common.saves')}" />&nbsp;</shiro:hasPermission>
									<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}"
										onclick="history.go(-1)" />
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!--  模态框 -->
	<div class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="myModal">
		<div class="modal-dialog" role="document" style="width:70%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					
					<h4 class="modal-title" id="myModalLabel">${fns:i18nMessage('adp_db.choose_table')}</h4>
					
				</div>
				<button class="btn btn-primary" id="checkAll">${fns:i18nMessage('adp_db.check_all')}</button>
				<button class="btn btn-primary" id="checkNoAll">${fns:i18nMessage('adp_db.not_check_all')}</button>
				<div class="modal-body">
					<p id="spanmodal"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" >${fns:i18nMessage('common.cancel')}</button>
					<button type="button" id="select" class="btn btn-primary" >${fns:i18nMessage('common.saves')}</button>
				</div>
			</div>
		</div>
	</div>
</body>

</html>