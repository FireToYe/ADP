<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="isAll" type="java.lang.Boolean" required="false" description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>

<div class="input-group">
	<input id="${id}Id"  name="${name}" class="${cssClass}" type="hidden" value="${value}"/>
	<input id="${id}Name"  ${allowInput?'':'readonly="readonly"'}  name="${labelName}" class="form-control ${cssClass}" type="text" value="${labelValue}"/>
	<%-- <input id="${id}Name"  name="${labelName}" ${allowInput?'':'readonly="readonly"'} type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}" class="form-control ${cssClass}" style="${cssStyle}"/> --%>
	<span class="input-group-btn">
		<button type="button" id="${id}Button" class="btn btn-primary ${disabled} ${hideBtn ? 'hide' : ''}">${fns:i18nMessage('common.search')}</button>
	</span>
	<span class="input-group-btn">
		<button type="button" id="${id}ButtonClear" style="color:#333" class="btn btn-default ${disabled} ${hideBtn ? 'hide' : ''}">${fns:i18nMessage('common.clear')}</button>
	</span>
</div>


<script type="text/javascript">

	//双击关闭弹框前赋值
	var tid="";//文本框的id
	function saveValue(nodeId,nodeText,id){
		$("#"+id+"Id").val(nodeId);
		$("#"+id+"Name").val(nodeText);
	}
	
	//清除所选内容
	$("#${id}ButtonClear").click(function() {
		$("#${id}Id").val('');
		$("#${id}Name").val('');
		$("#${id}Name").attr("value","");
	});
	//触发弹窗
	$("#${id}Button, #${id}Name").click(function() {
		tid = "${id}";
		var companyid = $("#companyId").val();
		var cid = "";
		if(typeof(companyid) != "undefined"){
			cid = "&companyid=" + companyid;
		}
	    layer.open({
	        type: 2,
	        title: '${fns:i18nMessage('common.select')} ${title}',
	        shadeClose: true,
	        shade: 0.5,
	        maxmin: true,
	        //开启最大化最小化按钮
	        area: ['350px', '600px'],
	        content: "${ctx}/tag/treeselect?url=" + encodeURIComponent("${url}"+cid) + "&module=${module}&checked=${checked}&extId=${extId}&isAll=${isAll}",
	        btn: ['${fns:i18nMessage('common.confirm')}', '${fns:i18nMessage('common.cancel')}'],
	        yes: function(index, layero) {
	            var ids = [];
	            var names = [];
	            var flag = true;
	            var body = layer.getChildFrame('body', index);
	            var iframeWin = layero.find('iframe')[0];
	            var selectedNodes = iframeWin.contentWindow.tree.treeview('getSelected');
	            $.each(selectedNodes,
	            function(index, node) { //<c:if test="${checked && notAllowSelectParent}">
	                if (node.isParent) {
	                    continue; // 如果为复选框选择，则过滤掉父节点
	                } //</c:if><c:if test="${notAllowSelectRoot}">
	                if (node.level == 0) {
	                    top.$.jBox.tip("不能选择根节点（" + node.text + "）请重新选择。");
	                    flag = false;
	                } //</c:if><c:if test="${notAllowSelectParent}">
	                if (node.isParent) {
	                    top.$.jBox.tip("不能选择父节点（" + node.text + "）请重新选择。");
	                    flag = false;
	                } //</c:if><c:if test="${not empty module && selectScopeModule}">
	                if (node.module == "") {
	                    top.$.jBox.tip("不能选择公共模型（" + nodes.text + "）请重新选择。");
	                    flag = false;
	                } else if (node.module != "${module}") {
	                    top.$.jBox.tip("不能选择当前栏目以外的栏目模型，请重新选择。");
	                    flag = false;
	                } //</c:if>
	                ids.push(node.id);
	                names.push(node.text); //<c:if test="${!checked}">
	                // </c:if>
	            });
	            if (flag && ids.length > 0 && names.length > 0) {
	                $("#${id}Id").val(ids.join(",").replace(/u_/ig, ""));
	                $("#${id}Name").val(names.join(","));
	            }
	            if(flag){
		            layer.close(index);
	            }else{
	            	return false;
	            }
	        }
	    });
	});
</script>