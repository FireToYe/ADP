<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('news.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ckfinder/ckfinder.js"></script>
	<script type="text/javascript">
	    window.onload = function(){
	        editor = CKEDITOR.replace('content'); //参数‘content’是textarea元素的name属性值，而非id属性值
	        CKFinder.setupCKEditor( editor, '${ctxStatic}/ckfinder/' );
	    }
		$(document).ready(function() {
		    var editor = null;
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/news/">${fns:i18nMessage('news.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/news/form?id=${news.id}">${fns:i18nMessage('news')}<shiro:hasPermission name="sys:news:edit">${not empty news.id?fns:i18nMessage('common.modify'):fns:i18nMessage('common.add')}</shiro:hasPermission><shiro:lacksPermission name="sys:news:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="news" action="${ctx}/sys/news/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('news.title')}：</label>
			<div class="col-sm-3">
				<form:input path="subject" htmlEscape="false" maxlength="200" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"></span>
			${fns:i18nMessage('news.content')}：</label>
			<div class="col-sm-9">
				<form:textarea path="content" htmlEscape="false" rows="4" class="form-control input-xxlarge"/>
			</div>
		</div>
<%-- 		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			新闻类型：</label>
			<div class="col-sm-3">
				<form:input path="typeId" htmlEscape="false" maxlength="200" class="form-control input-xlarge required"/>
			</div>
		</div> --%>
		<div class="form-group">
          <label class="col-sm-2 control-label">${fns:i18nMessage('common.status')}</label>
			<div class="col-sm-10" style="padding-top:5px;">
			  	<form:radiobuttons path="publish" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required radio i-checks"/>
      </div>
      </div>
<%-- 		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			新闻标题颜色：</label>
			<div class="col-sm-3">
				<form:input path="subjectColor" htmlEscape="false" maxlength="8" class="form-control input-xlarge required"/>
			</div>
		</div> --%>

		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"></span>
			${fns:i18nMessage('news.attachment')}：</label>
			<div class="col-sm-3">

		<form:hidden id="files" path="attachmentId" htmlEscape="false" maxlength="255" class="form-control"/>
		<sys:ckfinder input="files" type="files" uploadPath="" selectMultiple="true"/>
<%-- 				<form:input path="attachmentName" htmlEscape="false" class="form-control input-xlarge required"/> --%>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label">
			${fns:i18nMessage('news.contentKeyWords')}：</label>
			<div class="col-sm-3">
				<form:input path="keyword" htmlEscape="false" maxlength="100" class="form-control input-xlarge"/>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:news:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>