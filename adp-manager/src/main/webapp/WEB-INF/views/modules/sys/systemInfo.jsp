<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>单号规则内容管理</title>
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
</script>

</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
			<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/systemInfo/show">系统信息</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<sys:message content="${message}"/>
						<form:form class="breadcrumb form-search form-horizontal">
							<div class="form-group">
								<label class="col-sm-1 control-label" style="font-size:120%;">系统消息</label>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">ip地址</label>
								<div class="col-sm-5" >
									<div id="hostIp" class="left">${systemInfo.hostIp}</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">主机名</label>
								<div class="col-sm-5">
									<div id="hostName" class="left">${systemInfo.hostName}</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">操作系统的名称</label>
								<div class="col-sm-5">
									<td id="osName" class="left">${systemInfo.osName}</td>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">操作系统的构架</label>
								<div class="col-sm-5">
									<td id="arch" class="left">${systemInfo.arch}</td>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">操作系统的版本</label>
								<div class="col-sm-5">
									<td id="osVersion" class="left">${systemInfo.osVersion}</td>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">处理器个数</label>
								<div class="col-sm-5">
									<td id="processors" class="left">${systemInfo.processors}</td>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Java的运行环境版本</label>
								<div class="col-sm-5">
									<td id="javaVersion" class="left">${systemInfo.javaVersion}</td>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Java供应商的URL</label>
								<div class="col-sm-5">
									<td id="javaUrl" class="left">${systemInfo.javaUrl}</td>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">Java的安装路径</label>
								<div class="col-sm-5">
									<td id="javaHome" class="left">${systemInfo.javaHome}</td>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label">临时文件路径</label>
								<div class="col-sm-5">
									<td id="tmpdir" class="left">${systemInfo.tmpdir}</td>
								</div>
							</div>
                            <div class="form-group">
								<label class="col-sm-2 control-label">物理地址</label>
								<div class="col-sm-5">
									<td id="macs" class="left">${systemInfo.macs}</td>
								</div>
							</div>

						</form:form>
						<form:form class="breadcrumb form-search form-horizontal">
							
                            <div class="form-group">
								<label class="col-sm-1 control-label" style="font-size:120%;">证书信息</label>
							</div>
                            <div class="form-group">
								<label class="col-sm-2 control-label">一级组织架构数量</label>
								<div class="col-sm-5">
									<td id="macs" class="left">${licenseInfo.consumerAmount}</td>
								</div>
							</div>
                            <div class="form-group">
								<label class="col-sm-2 control-label">wms登录数量</label>
								<div class="col-sm-5">
									<td id="macs" class="left">${licenseInfo.wmsAppAmount}</td>
								</div>
							</div>
                            <div class="form-group">
								<label class="col-sm-2 control-label">证书到期时间</label>
								<div class="col-sm-5">
									<td id="macs" class="left">
										<fmt:formatDate value="${licenseInfo.notAfter}" type="both" pattern="yyyy-MM-dd HH:mm:ss" dateStyle="full"/>
									</td>
								</div>
							</div>
						</form:form>
						<div class="form-search form-horizontal" >
							<label class="col-sm-2 control-label">上传license</label>
							<div class="col-sm-6">
								<form id="uploadLicense" action="${ctx}/fileupload/upload"  method="post" enctype="multipart/form-data">  
								<div class="col-sm-5">
								  <input type="file" name="file" size="45">  
								</div>
								<div class="col-sm-2" sytyle="float:left;">
								  <input type="submit" name="submit" value="确认上传" class="btn btn-primary">  
								  </div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
