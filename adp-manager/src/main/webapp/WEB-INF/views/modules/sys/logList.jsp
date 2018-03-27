<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('log.manager')}</title>
<meta name="decorator" content="default" />
 <link href="${ctxStatic}/bootstrap3/css/plugins/datetimepicker/bootstrap-datetimepicker.css " rel="stylesheet">
 <script src="${ctxStatic}/bootstrap3/js/plugins/datetimepicker/bootstrap-datetimepicker.js "></script>
  <script src="${ctxStatic}/bootstrap3/js/plugins/datetimepicker/bootstrap-datetimepicker.i18n.js "></script>
<script type="text/javascript">
   	$(document).ready(function() {
   	    $("#beginDate").datetimepicker({
   	    	 language:'${lang}',
   	    	 keyboardNavigation: false,
   	    	 forceParse: false,
   	    	 format:'yyyy-mm-dd hh:ii:ss',
   	    	 autoclose: true,
   	    	 todayBtn: "linked",
   	    	 endDate:$("#endDate").val()
   	    
   	    }).on('changeDate', function(ev){
   	    	$("#endDate").datetimepicker('setStartDate',  $("#beginDate").val());
   	    });
   	    $("#endDate").datetimepicker({
   	     language:'${lang}',
   	     keyboardNavigation: false,
	     forceParse: false,
	     format:'yyyy-mm-dd hh:ii:ss',
	     autoclose: true,
	     todayBtn: "linked",
	     startDate:$("#beginDate").val()
   	   }).on('changeDate', function(ev){
   	   	$("#beginDate").datetimepicker('setEndDate',  $("#endDate").val());
   	   });
   	 document.getElementById("logLevel").value = "${log.logLevel}";
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function alertMsg(msg){
/* 		if("1"==type){
			layer.alert($("#params_"+msg).val());
		}else if("2"==type){
			layer.alert($("#result_"+msg).val());
		} */
		var data = "{"+"&quot;请求数据&quot;:"+$("#params_"+msg).val()+", \n &quot;返回数据&quot;:"+$("#result_"+msg).val()
		+", \n &quot;ip地址&quot;:&quot;"+$("#addr_"+msg).val()+"&quot;}";
/* 		data["请求数据"]=$("#params_"+msg).val();
		data["返回数据"]=$("#result_"+msg).val();
		data["ip地址"]=$("#addr_"+msg).val(); */
		layer.alert(data,{area: '60%'});
	}
	//$('.pagination').ul.addClass('pagination')
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a href="${ctx}/sys/log/">${fns:i18nMessage('log.list')}</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<form:form id="searchForm" action="${ctx}/sys/log/" method="post"
							class="breadcrumb form-search form-horizontal">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
								
							<div class="form-group">
								<label class="col-sm-1 control-label">${fns:i18nMessage('log.actionMenu')}：</label>
									<div class="col-sm-2">
										<input id="title" name="title" type="text" maxlength="50" class="input-mini form-control" value="${log.title}" />
									</div>
								<label class="col-sm-1 control-label">${fns:i18nMessage('log.actionUser')}：</label>
									<div class="col-sm-2">
										<input id="createBy.name" name="createBy.name" type="text" maxlength="50" class="input-mini form-control" value="${log.createBy.name}" />
									</div>
								<label class="col-sm-1 control-label">${fns:i18nMessage('log.URI')}：</label>
									<div class="col-sm-2">
										<input id="requestUri" name="requestUri" type="text" maxlength="50" class="input-mini form-control" value="${log.requestUri}" />
									</div>
				
							</div>
							<div class="form-group">
									 <label class="col-sm-1 control-label">${fns:i18nMessage('log.modules')}：</label>
					             <div class="col-sm-2">
										<input id="modules" name="modules" type="text" maxlength="50" class="input-mini form-control" value="${log.modules}" />
								</div>
								<label class="col-sm-1 control-label">${fns:i18nMessage('log.dateRange')}：</label>
								<div class="col-sm-4">
									<div class="input-daterange input-group" id="datepicker"> 
									<input id="beginDate" name="beginDate" type="text" class="input-medium required  form-control" value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly/>
    								<span class="input-group-addon">-</span>
									<input id="endDate" name="endDate" class="input-medium required  form-control" type="text" value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly/>
						               <%--   <input path="beginDate" type="text"  readonly="readonly" maxlength="20"  class="input-medium required  form-control" name="beginDate" value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
						                 <span class="input-group-addon">-</span>
						                 <input path="endDate" type="text"  readonly="readonly" maxlength="20"  class="input-medium required  form-control" name="endDate" value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd  HH:mm:ss"/>"/> --%>
					            	 </div>
					             </div> 
							</div>
							<div class="form-group">
									<label class="col-sm-1 control-label">${fns:i18nMessage('log.level')}：</label>
									<div class="col-sm-2">
										<select id="logLevel" name="logLevel" class="form-control input-xlarge required">
											<option value=""></option>
											<option value="1">${fns:i18nMessage('log.onlyManipulateLogs')}</option>
											<option value="2">${fns:i18nMessage('log.onlyBusinessLog')}</option>
										</select>
									</div>
								<label class="col-sm-1" >${fns:i18nMessage('log.parameters')}：</label>
									<div class="col-sm-2">
										<input id=params name="params" type="text" maxlength="50" class="input-mini form-control" value="${log.params}" />
								</div>
								<div class="col-sm-2 control-label">
									<label for="exception" >
									<input id="exception" name="exception" type="checkbox" ${log.exception eq '1'?' checked':''} value="1" />${fns:i18nMessage('log.queryException')}</label>
								</div>
								
								<div class="col-sm-2 control-label">
									<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
								</div> 	
							</div>
						</form:form>
						<sys:message content="${message}" />
						<table id="contentTable"
							class="table table-hover table-striped">
							<thead>
								<tr>
									<th>${fns:i18nMessage('log.actionMenu')}</th>
									<th>${fns:i18nMessage('log.actionUser')}</th>
									<th>${fns:i18nMessage('log.company')}</th>
									<th>${fns:i18nMessage('log.office')}</th>
									<th>${fns:i18nMessage('log.URI')}</th>
									<th>${fns:i18nMessage('log.modules')}</th>
							      <!--   <th>操作提交的数据</th>
							        <th>返回数据</th>
									<th>操作者IP</th> -->
									<th>${fns:i18nMessage('log.dataDetail')}</th>
									<th>${fns:i18nMessage('log.operatTime')}</th>
							</thead>
							<tbody>
								<%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
								<c:forEach items="${page.list}" var="log">
									<tr>
										<td>${log.title}</td>
										<td>${log.createBy.name}</td>
										<td>${log.createBy.company.name}</td>
										<td>${log.createBy.office.name}</td>
										<td><strong>${log.requestUri}</strong></td>
										<td>${log.modules}</td>
										<%-- <td>
										 
										 <c:if test="${not empty log.params}">
											<a onclick="alertMsg('${log.id}',1)">详情</a>
											<textarea style="display:none" id="params_${log.id }">${log.params }</textarea>
										</c:if>
										</td>
										<td>
										<c:if test="${not empty log.result}">
											<a onclick="alertMsg('${log.id}',2)">详情</a>
											<textarea style="display:none" id="result_${log.id}">${log.result }</textarea>
										</c:if>
										</td>
										<td>${log.remoteAddr}</td> --%>
										<td><a onclick="alertMsg('${log.id}')">${fns:i18nMessage('log.detail')}</a>
										<textarea style="display:none" id="params_${log.id }">${log.params }</textarea>
						 			 	<textarea style="display:none" rows="10" id="result_${log.id}">${fn:escapeXml(log.result)}</textarea>
										<input type="text" value="${log.remoteAddr}" id ="addr_${log.id }" style="display:none"/>
										</td>
										<td><fmt:formatDate value="${log.createDate}" type="both" /></td>
									</tr>
									<c:if test="${not empty log.exception}">
										<tr>
											<td colspan="8"
												style="word-wrap: break-word; word-break: break-all;">
												<%-- 					用户代理: ${log.userAgent}<br/> --%> <%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
												异常信息: <br />
												${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
											</td>
										</tr>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
						${page}
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
