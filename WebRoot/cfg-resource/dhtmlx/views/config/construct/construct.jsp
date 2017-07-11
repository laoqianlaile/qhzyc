<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ces.config.utils.CfgCommonUtil"%>
<%@page import="com.ces.config.utils.ComponentFileUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String folderPath = request.getRequestURI();
folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>构件组装</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/construct.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/constructlayout.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/constructdetaillayout.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/copywin.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/defaultbuttonwin.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/logicgroupcomponent.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/comboboxsearchwin.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/syncbuttonwin.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/syncbuttontowin.js"></script>
		<script type="text/javascript" src="<%=folderPath %>/js/filter.js"></script>
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
