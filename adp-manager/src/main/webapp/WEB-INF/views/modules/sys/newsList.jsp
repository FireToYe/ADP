<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('news.manager')}</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function deleteAtId(tip, href) {
		layer.confirm(tip, {
			offset : 't',
			btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}']//按钮
		}, function() {
			location.href = href;
		});
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li class="active"><a href="${ctx}/sys/news/">${fns:i18nMessage('news.list')}</a></li>
				<shiro:hasPermission name="sys:news:edit">
					<li><a href="${ctx}/sys/news/form">${fns:i18nMessage('news.add')}</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body">
						<form:form id="searchForm" modelAttribute="news"
							action="${ctx}/sys/news/" method="post"
							class="breadcrumb form-search form-horizontal">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<div class="form-group">
								<label class="col-sm-1 control-label">${fns:i18nMessage('news.title')}：</label>
								<div class="col-sm-2">
									<form:input path="subject" htmlEscape="false" maxlength="50"
										class="form-control input-medium" />
								</div>
								<input id="btnSubmit" class="btn btn-primary" type="submit"
									value="${fns:i18nMessage('common.query')}" />
							</div>
						</form:form>
						<sys:message content="${message}" />
						<table id="contentTable" class="table table-hover table-striped">
							<thead>
								<tr>
									<th>${fns:i18nMessage('news.title')}</th>
									<th>${fns:i18nMessage('news.releaseIdentification')}</th>
									<th>${fns:i18nMessage('news.type')}</th>
									<th>${fns:i18nMessage('news.updateTime')}</th>
									<th>${fns:i18nMessage('news.createTime')}</th>
									<shiro:hasPermission name="sys:news:edit">
										<th>${fns:i18nMessage('common.operation')}</th>
									</shiro:hasPermission>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="news">
									<tr>
										<td>${news.subject}</td>
										<td><c:if test="${news.publish == '0'}">隐藏</c:if> <c:if
												test="${news.publish == '1'}">显示</c:if> <c:if
												test="${news.publish == '2'}">已终止</c:if></td>
										<td><c:if test="${news.typeId == '1'}">通告通知</c:if></td>
											<td><a href="${ctx}/sys/news/form?id=${news.id}"> <fmt:formatDate
													value="${news.updateDate}" pattern="yyyy-MM-dd HH:mm:ss" />
										</a></td>
										<td><a href="${ctx}/sys/news/form?id=${news.id}"> <fmt:formatDate
													value="${news.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
										</a></td>

										<shiro:hasPermission name="sys:news:edit">
											<td><a href="${ctx}/sys/news/form?id=${news.id}">${fns:i18nMessage('common.modify')}</a>
												<a
												onclick="deleteAtId('${fns:i18nMessage('news.deleteTip')}？', '${ctx}/sys/news/delete?id=${news.id}')">${fns:i18nMessage('common.delete')}</a>
											</td>
										</shiro:hasPermission>
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