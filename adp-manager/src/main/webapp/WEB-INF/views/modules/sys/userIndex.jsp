<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
	    #tree {padding-left: 40px;}
	</style>
</head>
<body>
<div class="wrapper wrapper-content">
	<sys:message content="${message}"/>
	<div id="content" class="row">
		<div id="left1" class="col-sm-2">
			<div class="ibox float-e-margins">
				<div class="ibox-content mailbox-content" >
		    		<a class="btn btn-block btn-info compose-mail" href="${ctx}/sys/user/index">${fns:i18nMessage('office.organization')}<i class="glyphicon glyphicon-refresh pull-right" ></i></a>
		    	</div>
    			<div id="tree">	</div>
		    </div>
	
			<div id="openClose" class="close">&nbsp;</div>
		</div>
		<div id="right1" class="col-sm-10 animated fadeInRight"  >
		<div class="content-main" style="height:1000px" >
			<iframe id="officeContent" src="${ctx}/sys/user/list" width="100%" height="100%" frameborder="0"></iframe></div>
		</div>
	</div>
</div>
	<script type="text/javascript">	
		function refreshTree(){
			$.getJSON("${ctx}/sys/office/treeData",function(data){
			//	$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
  	        var lastSelectedNodeId = null;
  	      	var lastSelectTime =null;
		    $('#tree').treeview({
		          expandIcon: 'glyphicon glyphicon-plus',
		          collapseIcon: 'glyphicon glyphicon-minus',
		          nodeIcon: 'glyphicon',
		          showBorder: false,
		    	  selectedIcon: "glyphicon ",
		    	  onNodeUnselected:function(event, node){
	    	        if (lastSelectedNodeId && lastSelectTime) {
	    	            var time = new Date().getTime();
	    	            var t = time - lastSelectTime;
	    	            //双击事件，频率高
	    	            if (lastSelectedNodeId == node.nodeId && t < 500) {
	    	    	        loadpage(event, node);
	    	    	        $('#tree').treeview('selectNode');
	    	            }
	    	        }
		    	  },
	            	onNodeSelected: function(event, node) {
          	        lastSelectedNodeId = node.nodeId;
	    	        lastSelectTime = new Date().getTime();
	    	        loadpage(event, node);
	              },
		        color: "#428bca",
		        data: data
		    });
			});
		} 
 		refreshTree();
 		var leftWidth = 180; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left1, #openClose, #right1, #right1 iframe");
		function wSize(){
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":"hidden", "overflow-y":"hidden"});

		} 
		function loadpage(event,node){
    		var id = node.id == '0' ? '' :node.id;
    		var paramQuery="";
    		//公司
    		if(node.type==1){
    			paramQuery+="&company.id="+id+"&company.name="+encodeURIComponent(node.text)+"&company.type="+node.type;
    		}else{//部门、小组、其他
    			paramQuery+="&office.id="+id+"&office.name="+encodeURIComponent(node.text)+"&office.type="+node.type+"&office.parent.id="+node.pId;
    		}
			$('#officeContent').attr("src","${ctx}/sys/user/list?"+paramQuery);
		}
	</script>
</body>
</html>