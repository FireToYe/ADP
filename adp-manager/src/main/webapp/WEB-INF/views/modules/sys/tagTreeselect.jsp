<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据选择</title>
	<meta name="decorator" content="blank"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		var tree;
		$(function(){
		  	$("#key").bind("change cut input propertychange", searchNode);
			  //初始化树
			$.getJSON("${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}&extId=${extId}&isAll=${isAll}&module=${module}&t="
					+ new Date().getTime(), function(data){
						var lastSelectedNodeId = null;
			  	      	var lastSelectTime =null;
					  tree= $('#tree').treeview({
			          expandIcon: 'glyphicon glyphicon-plus',
			          collapseIcon: 'glyphicon glyphicon-minus',
			          nodeIcon: 'glyphicon',
			          showBorder: false,
			          levels: 2,
			    	  selectedIcon: "glyphicon",
			    	  onNodeUnselected:function(event, node){
			    	        if (lastSelectedNodeId >=0 && lastSelectTime) {
			    	            var time = new Date().getTime();
			    	            var t = time - lastSelectTime;
			    	            if (lastSelectedNodeId == node.nodeId && t < 500) {
			    	            	var newId = parent.tid;
			    	            	parent.saveValue(node.id,node.text,newId);
			    	            	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			    	            	parent.layer.close(index);
			    	            }
			    	        }
				    	  },
			            	onNodeSelected: function(event, node) {
		          	        lastSelectedNodeId = node.nodeId;
			    	        lastSelectTime = new Date().getTime();
			              },
			        color: "#428bca",
			        data: data
			    });
			});
		});
		//查询
		var searchNode = function(e) {
			var pattern = $("#key").val().trim();
		    var options = {
		      ignoreCase:':checked',
		      revealResults:'checked'
		    };
		    if(tree){
			    var results =  tree.treeview('search', [ pattern, options]);
		    }
		}
	</script>
</head>
<body>
<div class="ibox float-e-margins">
<div class="ibox-content">
<div class="input-group">
    <input type="text" id="key" placeholder="${fns:i18nMessage('common.keyWords')}" class="input form-control">
    <span class="input-group-btn">
            <button type="button" class="btn btn btn-primary"> <i class="fa fa-search"></i> ${fns:i18nMessage('common.search')}</button>
   </span>
</div>
	<div id="tree" class="ztree" style="padding:15px 20px;min-height:400px;"></div>
	</div>
</div>
</body>