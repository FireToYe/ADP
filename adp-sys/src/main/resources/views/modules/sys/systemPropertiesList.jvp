<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/sysProperties/">${fns:i18nMessage('sys.properties.list')}</a></li>
		<li class="active"><a href="${ctx}/sys/sysProperties/">${fns:i18nMessage('sys.properties.list.sys')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>key</th>
					<th>value</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${propertiesSet}" var="sysProperties">
				<tr>
					<td>
						${sysProperties.key}
					</td>
					<td>
						${sysProperties.value}
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>