<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ces.config.utils.ComponentFileUtil"%>
<%@page import="com.ces.config.utils.CfgCommonUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String folderPath = request.getRequestURI();
folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>构件生产库</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="<%=folderPath %>/js/component.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/componentrelationconfig.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/commonrelationconfig.js"></script>
		<script type="text/javascript">
			var releasedSystem = false;
			<%
				if (CfgCommonUtil.isReleasedSystem()) {
			%>
			releasedSystem = true;
			<%
				}
			%>
			var previewSystemPath = "<%= ComponentFileUtil.getPreviewUrl()%>";
		</script>
	</head>
	<body onload="init()">
	</body>
</html>
