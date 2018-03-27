<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>负责人列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var dataArray = new Array();
		$(document).ready(function() {
			var preDataArray='${dataArray}';
			if(preDataArray.length>0){
				dataArray=JSON.parse(preDataArray);
			}
			if(dataArray){
				$("#dataArray").val(JSON.stringify(dataArray));
				for(var i=0;i<dataArray.length;i++){
					$("#selectOrgButton_"+dataArray[i].id).attr("checked","true");
				}
			}
		});
		
		function page(n,s){
			$("#searchForm").attr("action","${ctx}/sys/user/accepter");
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
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
			var bean = {};
			bean.name=$(element).attr("value_name");
			bean.id=id;
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
			var str = JSON.stringify(dataArray);
			$("#dataArray").val(str);
			console.log(str);
		}
		function contains(arr, obj) {
	        var i = arr.length;
	        while (i--) {
	            if (arr[i].id === obj.id) {
	                return true;
	            }
	        }
	        return false;
	    }
	    function removeByValue(dx)
	    {
	        for(var i=0,n=0;i<dataArray.length;i++)
	        {
	            if(this.dataArray[i].id!=dx.id)
	            {
	                dataArray[n++]=dataArray[i]
	            }
	        }
	        dataArray.length-=1
	    }
		
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body ">
	<form:form id="searchForm" modelAttribute="user" action="" class="breadcrumb form-search form-horizontal">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="col-sm-1 control-label">姓&nbsp;&nbsp;&nbsp;名：</label>
			<div class="col-sm-2">
				<form:input class="form-control input-medium" path="name" htmlEscape="false" maxlength="50"/>
			</div>
			<textarea style="display:none" id = "dataArray" name="dataArray"></textarea>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page(false,false);" />
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-hover table-striped">
		<thead>
			<tr>
				<th style="width:80;"><input id="allCheck" type="checkbox" onclick="allChoose(this)">全选</th>
				<th>归属公司</th>
				<th>归属部门</th>
				<th>姓名</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="choosePerson">
			<tr>
				<td><input type="checkbox" name="selectOrgButton" id="selectOrgButton_${choosePerson.id}"  value="${choosePerson.id}" value_name="${choosePerson.name}" onclick="choose(this,'${choosePerson.id}')" /></td>
				<td>${choosePerson.company.name}</td>
				<td>${choosePerson.office.name}</td>
				<td>${choosePerson.name}</td>
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