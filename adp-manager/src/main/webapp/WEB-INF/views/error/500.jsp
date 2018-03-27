<%
response.setStatus(500);

// 获取异常类
Throwable ex = Exceptions.getThrowable(request);
if (ex != null){
	LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
}

// 编译错误信息
StringBuilder sb = new StringBuilder("错误信息：\n");
if (ex != null) {
	sb.append(Exceptions.getStackTraceAsString(ex));
} else {
	sb.append("未知错误.\n\n");
}

// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print(sb);
}

// 输出异常信息页面
else {
%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@page import="com.zhilink.manager.common.web.Servlets"%>
<%@page import="com.zhilink.manager.framework.common.utils.Exceptions"%>
<%@page import="com.zhilink.manager.framework.common.utils.StringUtils"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>500错误</title>
    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctxStatic}/bootstrap3/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctxStatic}/bootstrap3/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctxStatic}/bootstrap3/css/animate.css" rel="stylesheet">
    <link href="${ctxStatic}/bootstrap3/css/style.css?v=4.1.0" rel="stylesheet">
</head>
<body class="gray-bg">


    <div class="middle-box text-center animated fadeInDown">
        <h1>500</h1>
        <h3 class="font-bold">服务器内部错误</h3>

        <div class="error-desc">
            	服务器出错了...
            <br/>您可以返回主页看看
 <!--            <br/><a href="index.html" class="btn btn-primary m-t">主页</a> -->
        </div>
    </div>
    <!-- 全局js -->
    <script src="${ctxStatic}/bootstrap3/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctxStatic}/bootstrap3/js/bootstrap.min.js?v=3.3.6"></script>

</body>

</html>
<%
} out = pageContext.pushBody();
%>