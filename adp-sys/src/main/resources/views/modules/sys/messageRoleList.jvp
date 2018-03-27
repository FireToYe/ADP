<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>负责人列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var dataArray = new Array();
		$(document).ready(function() {
			$('#checkAll').click(function(){
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
		});
		function page(n,s){
			$("#searchForm").attr("action","${ctx}/sys/role/messageRoleList");
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
	<form:form id="searchForm" modelAttribute="role" action="" method="post" class="breadcrumb form-search form-horizontal">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<textarea style="display:none" id = "dataArray" name="dataArray"></textarea>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-hover table-striped">
		<thead>
			<tr>
				<th style="width:80;"><input id="allCheck" type="checkbox" onclick="allChoose(this)">全选</th>
				<th>角色名称</th>
				<th>英文名称</th>
				<th>归属机构</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="rolelist">
			<tr>
			<td><input type="checkbox" name="selectOrgButton" id="selectOrgButton_${rolelist.id}"  value="${rolelist.id}" value_name="${rolelist.name}" onclick="choose(this,'${rolelist.id}')" /></td>
			<%-- <td><input name="items" type="checkbox" class="roleListId" value="${rolelist.id}" /></td> --%>
			<td>${rolelist.name}</a></td>
			<td>${rolelist.enname}</a></td>
			<td>${rolelist.office.name}</td>
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
	<script type="text/javascript">
		$('.roleListId').click(function(){
			$('.roleListId').each(function(i,n){
	 			n.checked=false;
		 	});
		 	$(this)[0].checked=true;
		 	$("#contentTable tr").removeClass("info");
		 	$(this).parent().parent().addClass("info");
		});
	</script>
</body>
</html>