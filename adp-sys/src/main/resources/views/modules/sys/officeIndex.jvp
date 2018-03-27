<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
	    #tree {padding-left: 40px;}
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
	</style>
</head>
<body>
	<sys:message content="${message}"/>
	<div id="content" class="row">
		<div id="left" class="col-sm-2">
		<div class="ibox float-e-margins">
			<div class="ibox-content mailbox-content" onclick="refreshTree();">
		    	<a class="btn btn-block btn-info compose-mail" href="${ctx}/sys/office">${fns:i18nMessage('office.organization')}<i class="glyphicon glyphicon-refresh pull-right" ></i></a>
		    </div>
			<div id="tree"></div>
			</div>
		</div>
		<div id="openClose" class="close">&nbsp;</div>
		<div id="right" class="col-sm-10 animated fadeInRight">
		<div class="content-main" style="height:1000px" >
			<iframe id="officeContent" src="${ctx}/sys/office/list?id=&parentIds=" width="100%" height="91%" frameborder="0"></iframe>
		</div>
		</div>
	</div>
	<script type="text/javascript">
/* 		var setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
			callback:{onClick:function(event, treeId, treeNode){
					var id = treeNode.pId == '0' ? '' :treeNode.pId;
					$('#officeContent').attr("src","${ctx}/sys/office/list?id="+id+"&parentIds="+treeNode.pIds);
				}
			}
		};
		 */
		
		function refreshTree(){
			$.getJSON("${ctx}/sys/office/treeData",function(data){
			//	$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
		    $('#tree').treeview({
		          expandIcon: 'glyphicon glyphicon-plus',
		          collapseIcon: 'glyphicon glyphicon-minus',
		          nodeIcon: 'glyphicon',
		          showBorder: false,
		    	  selectedIcon: "glyphicon",
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
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize(){
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":"hidden", "overflow-y":"hidden"});
		}
		function loadpage(event,node){
			var pId = (node.pId == '0' ? node.id :node.pId);
			var pids=node.pIds;
			pids=pids+node.id+",";
			$('#officeContent').attr("src","${ctx}/sys/office/list?id="+node.id+"&parentIds="+pids);
		}
	</script>
	<%-- <script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script> --%>
</body>
</html>