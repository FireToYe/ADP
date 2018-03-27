<%
response.setStatus(404);

// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print("页面不存在.");
}

//输出异常信息页面
else {
%>
<%@page import="com.zhilink.manager.common.web.Servlets"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 页面</title>
    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctxStatic}/bootstrap3/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctxStatic}/bootstrap3/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="${ctxStatic}/bootstrap3/css/animate.css" rel="stylesheet">
    <link href="${ctxStatic}/bootstrap3/css/style.css?v=4.1.0" rel="stylesheet">

</head>

<body class="gray-bg">


    <div class="middle-box text-center animated fadeInDown">
        <h1>404</h1>
        <h3 class="font-bold">页面未找到！</h3>
<!-- 		<div><a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div> -->
        <div class="error-desc">
        </div>
    </div>

    <!-- 全局js -->
    <script src="${ctxStatic}/bootstrap3/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctxStatic}/bootstrap3/js/bootstrap.min.js?v=3.3.6"></script>

</body>

</html>
<%
out.print("<!--"+request.getAttribute("javax.servlet.forward.request_uri")+"-->");
} out = pageContext.pushBody();
%>