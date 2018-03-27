<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:i18nMessage('role.distributeRole')}</title>
<meta name="decorator" content="default" />
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="tabs-container">
			<ul class="nav nav-tabs">
				<li><a href="${ctx}/sys/role/">${fns:i18nMessage('role.list')}</a></li>
				<li class="active"><a
					href="${ctx}/sys/role/assign?id=${role.id}"><shiro:hasPermission
							name="sys:role:edit">角色分配</shiro:hasPermission>
						<shiro:lacksPermission name="sys:role:edit">人员列表</shiro:lacksPermission></a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane active">
					<div class="panel-body ">
						<div class="container-fluid breadcrumb">
							<div class="form-group form-control" style="border:none">
								<span class="col-sm-4 span4">${fns:i18nMessage('role.name')}: <b>${role.name}</b></span>
								<span class="col-sm-4 span4">${fns:i18nMessage('role.office')}: ${role.office.name}</span>
								<span class="col-sm-4 span4">${fns:i18nMessage('role.englishName')}: ${role.enname}</span>
							</div>
							<div class="form-group form-control" style="border:none">
								<span class="col-sm-4 span4">${fns:i18nMessage('role.type')}: ${role.roleType}</span>
								<c:set var="dictvalue" value="${role.dataScope}" scope="page" />
								<span class="col-sm-4 span4">${fns:i18nMessage('role.dataRange')}: ${fns:getDictLabel(dictvalue, 'sys_data_scope', '')}</span>
							</div>
						</div>
						<sys:message content="${message}" />
						<div class="form-group">
							<form id="assignRoleForm" action="${ctx}/sys/role/assignrole"
								method="post" class="hide">
								<input type="hidden" name="id" value="${role.id}" /> <input
									id="idsArr" type="hidden" name="idsArr" value="" />
							</form>
							<input id="assignButton" class="btn btn-primary" type="submit"
								value="${fns:i18nMessage('role.roleDistribute')}" />
							<script type="text/javascript">
							function deleteAtId(tip,href){
								layer.confirm(tip, {
									offset: 't' ,
									btn: ['${fns:i18nMessage('common.confirm')}','${fns:i18nMessage('common.cancel')}'] //按钮
									}, function(){
										$("#listForm").attr("action",href);
										$("#listForm").submit();
										//location.reload();
									});
							}
							$("#assignButton").click(function() {			
							    layer.open({
							        type: 2,
							        title: '分配角色',
 		 		        			shadeClose: true,  
 					 		        shade: 0.5,
							        maxmin: true, //开启最大化最小化按钮 
							        area: ['810px', ($(top.document).height() - 240)+'px'],
							        content: '${ctx}/sys/role/usertorole?id=${role.id}',
							        btn: ['确定分配', '清除已选','关闭'],
							        yes: function(index, layero){
						        	 	var body = layer.getChildFrame('body', index);
						        	    var iframeWin = layero.find('iframe')[0];
							            var pre_ids = iframeWin.contentWindow.pre_ids;
							            var ids = iframeWin.contentWindow.ids;
							            // 删除''的元素
						                if (ids[0] == '') {
						                    ids.shift();
						                    pre_ids.shift();
						                }            
							            if (pre_ids.sort().toString() == ids.sort().toString()) {
						                    top.$.jBox.tip("未给角色【${role.name}】分配新成员！", 'info');
						                    return false;
						                };                
						                loading('正在提交，请稍等...');
						                var idsArr = "";
						                for (var i = 0; i < ids.length; i++) {
						                    idsArr = (idsArr + ids[i]) + (((i + 1) == ids.length) ? '': ',');
						                }
						                $('#idsArr').val(idsArr);
						                $('#assignRoleForm').submit();
						                return true;
						          },btn2:function(index,layero){
						        	    var iframeWin = layero.find('iframe')[0];
						        	    iframeWin.contentWindow.clearAssign();
						         	    return false;
						          }
						      });
							});
								
/* 							    top.$.jBox.open("iframe:${ctx}/sys/role/usertorole?id=${role.id}", "分配角色", 810, $(top.document).height() - 240, {
							        buttons: {
							            "确定分配": "ok",
							            "清除已选": "clear",
							            "关闭": true
							        },
							        bottomText: "通过选择部门，然后为列出的人员分配角色。",
							        submit: function(v, h, f) {
							            var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
							            var ids = h.find("iframe")[0].contentWindow.ids;
							            //nodes = selectedTree.getSelectedNodes();
							            if (v == "ok") {
							                // 删除''的元素
							                if (ids[0] == '') {
							                    ids.shift();
							                    pre_ids.shift();
							                }
							                if (pre_ids.sort().toString() == ids.sort().toString()) {
							                    top.$.jBox.tip("未给角色【${role.name}】分配新成员！", 'info');
							                    return false;
							                };
							                // 执行保存
							                loading('正在提交，请稍等...');
							                var idsArr = "";
							                for (var i = 0; i < ids.length; i++) {
							                    idsArr = (idsArr + ids[i]) + (((i + 1) == ids.length) ? '': ',');
							                }
							                $('#idsArr').val(idsArr);
							                $('#assignRoleForm').submit();
							                return true;
							            } else if (v == "clear") {
							                h.find("iframe")[0].contentWindow.clearAssign();
							                return false;
							            }
							        },
							        loaded: function(h) {
							            $(".jbox-content", top.document).css("overflow-y", "hidden");
							        }
							    }); */
							</script>
						</div>
						<form id="listForm" method="post"></form>
						<table id="contentTable"
							class="table table-hover table-striped">
							<thead>
								<tr>
									<th>${fns:i18nMessage('user.company')}</th>
									<th>${fns:i18nMessage('user.department')}</th>
									<th>${fns:i18nMessage('user.loginName')}</th>
									<th>${fns:i18nMessage('user.name')}</th>
									<th>${fns:i18nMessage('common.phone')}</th>
									<th>${fns:i18nMessage('user.mobilePhone')}</th>
									<shiro:hasPermission name="sys:user:edit">
										<th>${fns:i18nMessage('common.operation')}</th>
									</shiro:hasPermission>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${userList}" var="user">
									<tr>
										<td>${user.company.name}</td>
										<td>${user.office.name}</td>
										<td>${user.loginName}</td>
										<td>${user.name}</td>
										<td>${user.phone}</td>
										<td>${user.mobile}</td>
										<shiro:hasPermission name="sys:role:edit">
											<td><a onclick="deleteAtId('${fns:i18nMessage('role.removeTipa')}<b>[${user.name}]</b>${fns:i18nMessage('role.removeTipb')}<b>[${role.name}]</b>${fns:i18nMessage('role.removeTipc')}', '${ctx}/sys/role/outrole?userId=${user.id}&roleId=${role.id}')">${fns:i18nMessage('common.remove')}</a>
											</td>
										</shiro:hasPermission>
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
