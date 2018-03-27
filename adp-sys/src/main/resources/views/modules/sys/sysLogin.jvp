<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title>
<!-- 	<meta name="decorator" content="blank"/> -->
	<style>
		html, body {
			height: 100%;
		}
		*, :after, :before {
			box-sizing: border-box;
		}
		body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, form, fieldset, input, textarea, p, blockquote, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas, footer, header, hgroup, nav, section, audio, video {
			margin: 0;
			padding: 0;
		}
		.hide{
			display: none;
		}
      	label.error{background:none;width:270px;font-weight:normal;color:inherit;margin:0;font-size:14px;}
		::-webkit-input-placeholder { /* WebKit browsers */
    		color:   #BDBDBD;
		}
		:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
			color:    #BDBDBD;;
		}
		::-moz-placeholder { /* Mozilla Firefox 19+ */
			color:    #BDBDBD;
		}
		:-ms-input-placeholder { /* Internet Explorer 10+ */
			color:    #BDBDBD;;
		}
		.app-root>h2{position: absolute;top: 40px;left: 90px;}
		.login-container .bg-box{float: left;position: relative;background: url("${ctxStatic}/images/icon-bg1.png") no-repeat 65px 65px;width: 648px;height: 400px;}
		.animate-bg2,.animate-bg4,.animate-bg3,.animate-bg5,.animate-bg6{width: 10px;height: 10px;background: url("${ctxStatic}/images/icon-bg4.png") no-repeat; position: absolute;background-size: 100% 100%;top: 70px;left: 198px;animation:animatebg4 1s; -webkit-animation:animatebg4 1s; /* Safari and Chrome */ animation-fill-mode:forwards;}

		.animate-bg2{background-image: url("${ctxStatic}/images/icon-bg2.png");top: 215px;left: 420px;animation-name:animatebg2;-webkit-animation-name:animatebg2; /* Safari and Chrome */}
		.animate-bg3{background-image: url("${ctxStatic}/images/icon-bg3.png");top: 77px;left: 345px;animation-name:animatebg3;-webkit-animation-name:animatebg3; /* Safari and Chrome */}
		.animate-bg5{background-image: url("${ctxStatic}/images/icon-bg5.png");top: 195px;left: 113px;animation-name:animatebg5;-webkit-animation-name:animatebg5; /* Safari and Chrome */}
		.animate-bg6{background-image: url("${ctxStatic}/images/icon-bg6.png");top: 77px;left: 345px;animation-name:animatebg6;-webkit-animation-name:animatebg6; /* Safari and Chrome */}
		@media screen and (min-width: 768px){
		.app-root{width: 100%;height: 100%;background: url("${ctxStatic}/images/1366bg.png") no-repeat center;background-size:100% 100%;position: relative; }			
			.login-container{ position: absolute; left: 50%; top: 49%; width: 1024px; height: 400px; margin-top: -200px; margin-left: -512px; overflow: hidden;}
			.reg-container{ padding-top: 113px; padding-left: 30px;}
			.complete-con{ float: left; clear: both;width: 60%;padding-left: 116px;}
		}
		@media screen and (min-device-width: 1920px){
			.app-root{width: 100%;height: 100%;background: url("${ctxStatic}/images/1920bg.png") no-repeat center;background-size:100% 100%;position: relative; }
			.login-container{ position: absolute; left: 48%; top: 50%; width: 1024px; height: 400px; margin-top: -200px; margin-left: -512px; overflow: hidden;}			
		}
		.login-box{
			float: right;
			width: 376px;
			padding: 20px 38px 0 38px;
			background-color: #fff;
			border: 1px solid #fff;
		}
		#login{
			position: relative;
			width:370px;
			/* height: 300px; */
			top: 15%;
			float: right;
			margin:0 auto;
			margin-top:8px;
			margin-bottom:2%;
			transition:opacity 1s;
			-webkit-transition:opacity 1s;
		  }
		  
		  #login h1{
			background:#fff;
			font-size:16px;
			color: #555;
			padding: 8% 10% 0 10%;
			font-family: MicrosoftYaHei;
		  }
		  #login .content {
			  /* width: 90%; */
			  margin: 0 auto;
		  }
		  #login p, #login a{
			  color: #707070;
			  font-size: 12px;
		  }
		  #login .fr {
			  float: right;
			  text-decoration: none;
		  }
		  #login .remember input{
			  margin-right: 5px;
		  }
		  #login form{
			background:#fff;
			padding:5% 10%;
		  }
		  
		  #login input[type="text"],input[type="password"]{
			margin: 0 auto;
			width:100%;
			background:#fff;
			margin-bottom:4%;
			border:1px solid #ccc;
			padding:3.2%;
			font-family:'Open Sans',sans-serif;
			font-size:95%;
			color:#555;
			border-radius: 3px;
		  }
		  #login input[type="text"],input[type="password"]:focus {
			  outline: none;
		  }
		  
		  #login input[type="submit"]{
			border-radius: 3px;
			width:100%;
			background: #49B7FA;;
			border:0;
			padding:3%;
			font-family:'Open Sans',sans-serif;
			font-size:100%;
			color:#fff;
			margin: 5% 0 6% 0;
			cursor:pointer;
			transition:background .3s;
			-webkit-transition:background .3s;
		  }
		  
		  #login input[type="submit"]:hover{
			background:#2288bb;
		  }

		@keyframes animatebg2
		{
			0%  {top: 215px;left: 420px;width: 10px;height: 10px;}
			100%  {top: 280px;left: 425px;width: 60px;height: 60px;}
		}
		
		@-webkit-keyframes animatebg2 /* Safari 与 Chrome */
		{
			0%  {top: 215px;left: 420px;width: 10px;height: 10px;}
			100%  {top: 280px;left: 425px;width: 60px;height: 60px;}
		}

		@keyframes animatebg3
		{
			0%  {top: 77px;left: 345px;width: 10px;height: 10px;}
			100%  {top: 50px;left: 348px;width: 50px;height: 50px;}
		}
		
		@-webkit-keyframes animatebg3 /* Safari 与 Chrome */
		{
			0%  {top: 77px;left: 345px;width: 10px;height: 10px;}
			100%  {top: 50px;left: 348px;width: 50px;height: 50px;}
		}

		@keyframes animatebg4
		{
			0%  {top: 70px;left: 198px;width: 10px;height: 10px;}
			100%  {top: 60px;left: 60px;width: 75px;height: 75px;}
		}
		
		@-webkit-keyframes animatebg4 /* Safari 与 Chrome */
		{
			0%  {top: 70px;left: 198px;width: 10px;height: 10px;}
			100%  {top: 60px;left: 60px;width: 75px;height: 75px;}
		}
		
		@keyframes animatebg5
		{
			0%  {top: 195px;left: 113px;width: 10px;height: 10px;}
			100%  {top: 260px;left: 40px;width: 65px;height: 65px;}
		}
		
		@-webkit-keyframes animatebg5 /* Safari 与 Chrome */
		{
			0%  {top: 195px;left: 113px;width: 10px;height: 10px;}
			100%  {top: 260px;left: 40px;width: 65px;height: 65px;}
		}

		@keyframes animatebg6
		{
			0%  {top: 77px;left: 345px;width: 10px;height: 10px;}
			100%  {top: 65px;left: 435px;width: 75px;height: 75px;}
		}
		
		@-webkit-keyframes animatebg6 /* Safari 与 Chrome */
		{
			0%  {top: 77px;left: 345px;width: 10px;height: 10px;}
			100%  {top: 65px;left: 435px;width: 75px;height: 75px;}
		}
	</style>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/zhilink.js" type="text/javascript"></script>
	<script>
	$(function () {
		$("#loginForm").validate({
			messages: {
				username: {required: "${fns:i18nMessage('base.writeAccount')}"},password: {required: "${fns:i18nMessage('base.writePassword')}"}
			},
			errorLabelContainer: "#messageBox",
			errorPlacement: function(error, element) {
				error.appendTo($("#loginError").parent());
			} 
		});
	    //页面初始化时，如果帐号密码cookie存在则填充
	    if (cookie('username') && cookie('password')) {
	        $("#username").val(cookie('username'));
	        $("#password").val(cookie('password'));
	        $("#rememberMe").attr("checked", true);
	    }
	    //复选框勾选状态发生改变时，如果未勾选则清除cookie
	    $("#rememberMe").change(function () {
	        if (!this.checked) {
	        	cookie('username',null);
	        	cookie('password',null);
	        }
	    });
	    //点击登录
	    $("#login_btn").click(function () {
	    	var checked=$("#rememberMe").attr("checked");
	    	if(checked){
	        	cookie('username',$("#username").val(),1);
	        	cookie('password',$("#password").val(),1);
	    	}
	    });
	});

	</script>
</head>

<body>
	<div class="app-root">
		<div class="login-container">
			<div class="bg-box">
				<div class="animate-bg2"></div>
				<div class="animate-bg3"></div>
				<!-- <div class="animate-bg0"></div>  -->
				<div class="animate-bg4"></div>
				<div class="animate-bg5"></div>
				<div class="animate-bg6"></div>
			</div>
			<div id="login">
				<div class="content">
					<h1>${fns:i18nMessage('base.login')}</h1>
					<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
					  <input type="text" name="username" id="username" placeholder="${fns:i18nMessage('base.account')}" class="required valid" />
					  <input type="password" name="password"  id="password" placeholder="${fns:i18nMessage('base.password')}"  class="required valid" />
						<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">
							<label id="loginError" class="error">${message}</label>
						</div>
					  <p>
						<!-- <a class="fr" href="">忘记密码</a> -->
						<label class="remember"><input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''}>${fns:i18nMessage('base.rememberMe')}</label>
					  </p>
					  <input type="submit" id="login_btn" value="${fns:i18nMessage('base.login')}" />
					</form>
				</div>
			  </div>
		</div>
		
	</div>
</body>
</html>