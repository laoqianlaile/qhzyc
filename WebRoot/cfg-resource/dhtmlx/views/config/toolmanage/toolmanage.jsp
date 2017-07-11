<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		<title>工具管理</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>
	<body scroll="no" onload="init()">
		<script type="text/javascript">
			function init(){
				var dhxLayout = new dhtmlXLayoutObject("content", "1C");
				dhxLayout.cells("a").hideHeader();
				dhxLayout.cells("a").detachToolbar();
				var tabbar = dhxLayout.cells("a").attachTabbar();
				tabbar.setImagePath(IMAGE_PATH);
				tabbar.enableAutoReSize();
				tabbar.addTab("componentPackage", "构件打包", "150px");
				tabbar.addTab("toolbarUpdate", "工具条更新", "150px");
				tabbar.cells("componentPackage").attachURL("<%=path%>/cfg-resource/dhtmlx/views/config/toolmanage/componentconfig.jsp");
				tabbar.cells("toolbarUpdate").attachURL("<%=path%>/cfg-resource/dhtmlx/views/config/toolmanage/toolbarupdate.jsp");
				tabbar.setTabActive("componentPackage");
			}
		</script>
	</body>
</html>
