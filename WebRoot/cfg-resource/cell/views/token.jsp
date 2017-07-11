<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String token = request.getParameter("_token");
%>

<!DOCTYPE HTML>
<html style="width: 100%;height: 100%;">
  <head>
    <base href="<%=basePath%>">
    
    <title>单点登录跳转</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function init() {
			var localUrl = window.location.href;
	    	var reportPrintUrl = localUrl.substring(localUrl.indexOf('reportPrintUrl=') + 15);
			var ifrm = document.createElement("iframe");
			ifrm.setAttribute("id", "ifr");
			ifrm.setAttribute("name", "ifr");
			ifrm.setAttribute("src", reportPrintUrl);
			ifrm.setAttribute("width", "100%");
			ifrm.setAttribute("height", "100%");
			ifrm.setAttribute("border", "0");
			ifrm.setAttribute("marginwidth", "0");
			ifrm.setAttribute("marginheight", "0");
			ifrm.setAttribute("scrolling", "no");
			ifrm.setAttribute("frameborder", "0", 0);
			document.body.appendChild(ifrm);
		}
	</script>
  </head>
  
  <body style="width: 100%;height: 100%;margin: 0px;padding: 0px;overflow: hidden;" scroll="no" onload="init()">
    <div style="width: 0px;height: 0px;">
    	<img src="<%=basePath %>/security/sso!sso.json?_token=<%=token %>"/>
    </div>
  </body>
</html>
