<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>${fns:i18nMessage('application.name')}</title>
<!--<meta name="decorator" content="blank"/> -->
<meta name="keywords" content=""> 
<meta name="description" content="">
<link rel="shortcut icon" href="favicon.ico">
<script>var ctx='${ctx}';</script>
<link href="${ctxStatic}/bootstrap3/css/bootstrap.min.css?v=3.3.6"
	rel="stylesheet">
<link href="${ctxStatic}/bootstrap3/css/font-awesome.min.css?v=4.4.0"
	rel="stylesheet">
<link href="${ctxStatic}/bootstrap3/css/animate.css" rel="stylesheet">
<link href="${ctxStatic}/bootstrap3/css/style.css?v=4.1.0"
	rel="stylesheet">
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css"
	rel="stylesheet" />

</head>

<body class="fixed-sidebar full-height-layout gray-bg skin-1"
	style="overflow: hidden">
	<div id="wrapper">
<header class="main-header">
    <div class="logo">
      <span class="logo-mini"><b>SRM</b></span>
      <span class="logo-lg"><b>${fns:i18nMessage('application.title')}</b></span>
    </div>
    <nav class="navbar navbar-static-top">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>

      <div class="navbar-custom-menu ">    
			<ul id="menu" class="nav navbar-top-links ">
				<c:set var="firstMenu" value="true" />
				<c:forEach items="${fns:getMenuList()}" var="menu"
					varStatus="idxStatus">
					<c:if test="${menu.parent.id eq '1'&&menu.isShow eq '1' && menu.menuType eq '0'}">
						<li
							class="menu nav-item px-3 ${not empty firstMenu && firstMenu ? ' active' : ''}">
							<c:if test="${empty menu.href}">
								<a class="menu nav-link" href="javascript:"
									data-href="${ctx}/sys/menu/tree?parentId=${menu.id}"
									data-id="${menu.id}"><span>${menu.name}</span></a>
							</c:if> <c:if test="${not empty menu.href}">
								<a class="menu nav-link"
									href="${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}"
									data-id="${menu.id}" target="mainFrame"><span>${menu.name}</span></a>
							</c:if>
						</li>
						<c:if test="${firstMenu}">
							<c:set var="firstMenuId" value="${menu.id}" />
						</c:if>
						<c:set var="firstMenu" value="false" />
					</c:if>
				</c:forEach>
			</ul>
				<!-- 头部右侧菜单 -->
				<ul class="nav navbar-top-links navbar-right" id="head-navbar-right">
				<li id="notify" class="dropdown menu nav-item px-3">
						<a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
	                        <i class="fa fa-bell"></i> <span id="notifyNum" class="label label-primary"></span>
	                    </a>
	                    <ul class="dropdown-menu dropdown-alerts">
	                        <li><a class="notify-menu" href="/sys/sysMessage/list?delStatus=0">
	                                <div>
	                                    <span id="hasNotifyNum"></span><span id = "notifyTime" class="pull-right text-muted small"></span>
	                                </div>
	                        </a></li>
	                        <li>
	                            <div class="text-center link-block">
	                                <a class="notify-menu" href="/sys/sysMessage/list"><strong>${fns:i18nMessage('common.viewAll')} </strong><i class="fa fa-angle-right"></i></a>
	                            </div>
	                        </li>
	                    </ul>
					</li>
				<li id="languageInfo" class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="选择语言">${fns:i18nMessage('base.chooseLanguage')}<span id="notifyNum" class="label label-info hide"></span></a>
						<ul class="dropdown-menu">
						<li><a href="#" onclick="location='${ctx}/sys/locale/change?lang=zh_CN&url='+location.href">${fns:i18nMessage('base.simplifiedChinese')}</a></li>
					  <li><a href="#" onclick="location='${ctx}/sys/locale/change?lang=zh_TW&url='+location.href">${fns:i18nMessage('base.traditionalChinese')}</a></li>
					<li><a href="#" onclick="location='${ctx}/sys/locale/change?lang=en_US&url='+location.href">${fns:i18nMessage('base.english')}</a></li>
						</ul>
				</li>
				<li id="userInfo" class="dropdown menu nav-item px-3">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="个人信息">${fns:i18nMessage('base.hello')}, ${fns:getUser().name}&nbsp;</a>
					<ul class="dropdown-menu">
						<li><a href="/sys/user/info" class="person-menu" ><i class="icon-user"></i>${fns:i18nMessage('base.personalInfo')}</a></li>
						<li><a href="/sys/user/modifyPwd" class="person-menu" ><i class="icon-lock"></i>${fns:i18nMessage('base.modifyPassword')}</a></li>
					</ul>
				</li>
				<li><a href="${ctx}/logout" title="退出登录">${fns:i18nMessage('base.exitLogin')}</a></li>
				</ul>
      </div>
      
    </nav>
  </header>

		<!--左侧导航开始-->
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="nav-close">
				<i class="fa fa-times-circle"></i>
			</div>
			<div class="sidebar-collapse">
				<ul class="nav" id="side-menu">
					${menuTree}
				</ul>
			</div>
		</nav>
		<!--左侧导航结束-->
		<!--右侧部分开始-->
		<div id="page-wrapper" class="gray-bg dashbard-1">
<!--             <div class="content-tabs" id="tabs">
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                    </div>
                </nav>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="divider"></li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
            </div> -->
             <div class="J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" id="iframe0" width="100%" height="100%" src="${ctx}/welcome" frameborder="0" data-id="" seamless></iframe>
            </div>
       	   <div class="footer">
                Copyright &copy; 2012-${fns:getConfig('copyrightYear')} ${fns:getConfig('productName')} - Powered By <a href="http://zhilink.com" target="_blank">zhilink</a> ${fns:getConfig('version')}
            </div>
		</div>
		<!--右侧部分结束-->
	</div>
	<!-- 全局js -->
	<script src="${ctxStatic}/bootstrap3/js/jquery.min.js"></script>
	<script src="${ctxStatic}/bootstrap3/js/bootstrap.min.js"></script>
	<script
		src="${ctxStatic}/bootstrap3/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script
		src="${ctxStatic}/bootstrap3/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="${ctxStatic}/bootstrap3/js/plugins/layer/layer.min.js"></script>
	<!-- 自定义js -->
	<script src="${ctxStatic}/bootstrap3/js/home.js"></script>
	<!-- 第三方插件 -->
	<script src="${ctxStatic}/bootstrap3/js/plugins/pace/pace.min.js"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/bootstrap3/js/contabs.js"></script>
	
		<script text="text/javascript">
		var i18n={
			hasNotifyNumPre:"${fns:i18nMessage('base.hasNotifyNumPre')}",
			hasNotifyNumsuf:"${fns:i18nMessage('base.hasNotifyNumsuf')}",
			minuteBefore:"${fns:i18nMessage('base.minuteBefore')}",
			hourBefore:"${fns:i18nMessage('base.hourBefore')}",
			dayBefore:"${fns:i18nMessage('base.dayBefore')}",
			monthBefore:"${fns:i18nMessage('base.monthBefore')}"	
		};
		var oldNum =0;
		var notifyOldDate = new Date();
		var message = "";
		$(function(){
			refreshNum(0);
			  setInterval(function(){
				  refreshNum(0);
			  }, 60000);
			 });
		function refreshNum(delStatus){
			$.ajax({
				 url:'${ctx}/sys/sysMessage/notifyNum',
		         type:'GET',
		         data:{delStatus:delStatus},
		         async:true,    
		         success:function(result){
			         if(isNaN(result)){
			        		 return;
			         }
		        	 var newNum = result;
		        	 if(newNum != oldNum){
		        		 oldNum = newNum;
		        		 //弹框提醒
		        		 var notifyNewDate = new Date();
		        		 var mixTime = notifyNewDate - notifyOldDate;
		        		 timeDifference(mixTime);
		        	 }
		        	 $("#notifyNum").text(result);
		        	 $("#hasNotifyNum").text(i18n.hasNotifyNumPre+result+i18n.hasNotifyNumsuf);
		        	 $("#notifyTime").text(message);
		         }
			})
		}
		function timeDifference(time){
			var timed;
			if(0 < time/1000/60 < 60){
				timed = Math.ceil(time/1000/60);
				message = timed + i18n.minuteBefore;
			}else if(1 <= time/1000/60/60 < 24){
				timed = Math.ceil(time/1000/60/60);
				message = timed + i18n.hourBefore;
			}else if(1 <= time/1000/60/60/24 < 30){
				timed = Math.ceil(time/1000/60/60/24);
				message = timed + i18n.dayBefore;
			}else if(1 <= time/1000/60/60/24/30 < 12){
				timed = Math.ceil(time/1000/60/60/24/30);
				message = timed + i18n.monthBefore;
			}else if(time/1000/60/60/24/30 >= 12) {
				message = "很久之前";
			}
		}
	</script>
</body>

</html>
