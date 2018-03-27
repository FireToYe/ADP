<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('sysMessage.manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var dataArray = new Array();
		$(document).ready(function() {
			//刷新首页信息数量
			window.parent.refreshNum(0);
			if("${dataArray}"){
				console.log("${dataArray}");
				var jsonArr = '${dataArray}';
				dataArray = eval("("+jsonArr+")");
				for(var i=0;i<dataArray.length;i++){
					$("#selectOrgButton_"+dataArray[i]).attr("checked","true");
				}
			}
		});
		function page(n,s){
			var str = JSON.stringify(dataArray);
			$("#searchForm").attr("action","${ctx}/sys/sysMessage/");
			$("#dataArray").val(str);
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}'] //按钮
				}, function(){
					$("#searchForm").attr("action",href);
					$("#searchForm").submit();
				});
		}
		function allChoose(element){
			if(element.checked){
				$("input:checkbox[name=selectOrgButton]:not(:checked)").each(function () {
		            $(this).trigger('click');
		        })
			}else{
				$("input:checkbox[name=selectOrgButton]:checked").each(function () {
		            $(this).trigger('click');
		        })
			}
		}
		function choose(element,id){
			var bean = new Object();
			 bean = id;
			if(element.checked){
				 if(!contains(dataArray,bean)){
		                dataArray.push(bean);
		            }
			}else{
				$("#allCheck").attr("checked", false);
				 if(contains(dataArray,bean)){
		                removeByValue(bean);
		            }
			}
			console.log(dataArray);
		}
		function contains(arr, obj) {
	        var i = arr.length;
	        while (i--) {
	            if (arr[i] === obj) {
	                return true;
	            }
	        }
	        return false;
	    }
	    function removeByValue(dx)
	    {
	        for(var i=0,n=0;i<dataArray.length;i++)
	        {
	            if(this.dataArray[i]!=dx)
	            {
	                dataArray[n++]=dataArray[i]
	            }
	        }
	        dataArray.length-=1
	    }
	    
	    function deleteSelectedData(){
	    	layer.confirm('确认要删除所选数据吗？', {
				offset: 't' ,
				btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}'] //按钮
				},function(){
					if(!dataArray.length>0){
						layer.msg("请至少选择一条数据删除");
						return;
					}
					$("#searchForm").attr("action","${ctx}/sys/sysMessage/batchDelete");
					var str = JSON.stringify(dataArray);
					$("#dataArray").val(str);
					$("#searchForm").submit();
					layer.closeAll('dialog');
				});
	    }
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/sysMessage/list">${fns:i18nMessage('sysMessage.list')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="sysMessage" action="${ctx}/sys/sysMessage/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<label class="col-sm-1 control-label">${fns:i18nMessage('sysMessage.title')}：</label>
			<div class="form-group">
				<div class="col-sm-2">
					<form:input path="title" htmlEscape="false" maxlength="100" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('sysMessage.delStatus')}：</label>
				<div class="col-sm-2">
					<form:select path="delStatus" class="input-medium form-control">
						<form:option value="" label="${fns:i18nMessage('common.pleaseChoose')}"/>
						<form:options items="${fns:getDictList('msg_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<textarea style="display:none" id = "dataArray" name="dataArray"></textarea>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" onclick="return page(false,false);" />
				<button class="btn btn-primary" type="button"><a style="color:white" onclick="deleteSelectedData();">${fns:i18nMessage('sysMessage.deleteSelected')}</a></button>
				<button class="btn btn-primary" ><a style="color:white" href="${ctx}/sys/sysMessage/delStatus">${fns:i18nMessage('sysMessage.allMarkedAsRead')}</a></button>
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th style="width:80;"><input id="allCheck" type="checkbox" onclick="allChoose(this)"></th>
					<th>${fns:i18nMessage('sysMessage.title')}</th>
					<th>${fns:i18nMessage('sysMessage.content')}</th>
					<th>${fns:i18nMessage('sysMessage.createTime')}</th>
					<th>${fns:i18nMessage('sysMessage.sendPerson')}</th>
					<th>${fns:i18nMessage('sysMessage.delStatus')}</th>
					<th>${fns:i18nMessage('common.operation')}</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysMessage">
				<tr>
					<td><input type="checkbox" name="selectOrgButton" id="selectOrgButton_${sysMessage.id}" onclick="choose(this,${sysMessage.id})"></td>
					<td><a href="${ctx}/sys/sysMessage/form?id=${sysMessage.id}">${sysMessage.title}</a></td>
					<td>${sysMessage.content}</td>
					<td><fmt:formatDate value="${sysMessage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${sysMessage.createBy.name}</td>
					<c:if test="${sysMessage.delStatus==1}"><td>${fns:i18nMessage('sysMessage.readed')}</td></c:if><c:if test="${sysMessage.delStatus==0}"><td style="color:red">${fns:i18nMessage('sysMessage.notReaded')}</td></c:if>
					<td>
						<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('sysMessage.deleteTip')}？', '${ctx}/sys/sysMessage/delete?id=${sysMessage.id}&delStatus=${param.delStatus}&title=${param.title}')">${fns:i18nMessage('common.delete')}</a>
						&nbsp;<a href="${ctx}/sys/sysMessage/delStatus?id=${sysMessage.id}&delStatus=${param.delStatus}&title=${param.title}" ><c:if test="${sysMessage.delStatus==0}">${fns:i18nMessage('sysMessage.markedAsRead')}</c:if></a>
					</td>
					
				</tr>
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