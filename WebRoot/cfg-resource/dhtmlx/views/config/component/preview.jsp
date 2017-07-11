<%@page import="java.net.URLDecoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>构件预览</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>
	<body>
		<script type="text/javascript">
			var localUrl = window.location.href;
			var componentVersionId = '<%=request.getParameter("componentVersionId")%>';
			var previewUrl = localUrl.substring(localUrl.indexOf('&previewUrl=')+12);
			var componentType = '<%=request.getParameter("componentType")%>';
			var componentAlias = '<%=URLDecoder.decode(request.getParameter("componentAlias"), "utf-8")%>';
			if (componentType == "1") {
				var tabbar = new dhtmlXTabBar("content", "top");
				tabbar.setImagePath(IMAGE_PATH);
				tabbar.enableAutoReSize();
				tabbar.addTab("preview", componentAlias + "预览", "150px");
				tabbar.cells("preview").attachURL(previewUrl);
				tabbar.addTab("config", "构件配置信息", "100px");
				tabbar.cells("config").attachURL("<%=path%>/component/component-version!previewConfigFile?componentVersionId=" + componentVersionId);
				tabbar.setTabActive("preview");
			} else if (componentType == "2") {
				dhxLayout = new dhtmlXLayoutObject("content", "1C");
				dhxLayout.cells("a").setText("构件配置信息");
				dhxLayout.setAutoSize();
				dhxLayout.cells("a").attachURL("<%=path%>/component/component-version!previewConfigFile?componentVersionId=" + componentVersionId);
			} else if (componentType == "3") {
				dhxLayout = new dhtmlXLayoutObject("content", "1C");
				dhxLayout.cells("a").setText(componentAlias + "预览");
				dhxLayout.setAutoSize();
				dhxLayout.cells("a").attachURL(previewUrl);
			}
		</script>
	</body>
</html>
