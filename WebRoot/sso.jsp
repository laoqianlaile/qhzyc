<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.ces.utils.TokenUtils" %>
<%@ page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.context.SecurityContext" %>
<%
	/***注销已登录的用户***/
	Cookie cookie1 = new Cookie("_xarch_loginuser_name_", null);
	cookie1.setPath(request.getContextPath());
	response.addCookie(cookie1);
	Cookie cookie2 = new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE", null);
	cookie2.setPath(request.getContextPath());
	response.addCookie(cookie2);
	request.getSession(false).invalidate();
	SecurityContext context = SecurityContextHolder.getContext();
	context.setAuthentication(null);
	SecurityContextHolder.clearContext();
	/***注销已登录的用户***/
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String cesssotoken = request.getParameter("cesssotoken");
if (cesssotoken == null) {
	try {
		String q = request.getParameter("q");
        byte[] decode = Base64.decodeBase64(q);
        String[] p = new String(decode,"UTF-8").split("__");
		cesssotoken = TokenUtils.getToken(p[0], p[1]);
	} catch (Exception e){
		response.sendError(403, "用户名密码错误");
	}
}
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
			var targetUrl;
			var localUrl = window.location.href;
			if (localUrl.indexOf('targetUrl=') > 0) {
			    targetUrl = localUrl.substring(localUrl.indexOf('targetUrl=') + 10);
			}
			if (targetUrl) {
			    window.location.href = '<%=path%>/' + targetUrl;
			} else {
			    window.location.href = '<%=path%>/trace.jsp';
			}
		}
	</script>
  </head>
  <body style="width: 100%;height: 100%;margin: 0px;padding: 0px;overflow: hidden;" scroll="no" onload="init()">
    <div style="display: none">
    	<img src="<%=basePath %>/security/sso!sso.json?_token=<%=cesssotoken %>"/>
    </div>
  </body>
</html>
