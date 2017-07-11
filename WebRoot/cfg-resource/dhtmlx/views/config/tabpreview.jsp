<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String CFG_componentUrl = request.getParameter("CFG_componentUrl");
String folderPath = request.getRequestURI();
folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="<%=dhxResPath %>/common/js/base.js" type="text/javascript"></script>
	<script src="<%=dhxResPath %>/common/js/common.js" type="text/javascript"></script>
	<script src="<%=dhxResPath %>/common/js/dhtmlxform_item_container.js" type="text/javascript"></script>
	<script src="<%=dhxResPath %>/common/js/dhtmlxmenu_xarch.js" type="text/javascript"></script>
  </head>
  <body onload="init()">
    <div id="tabbarDiv" style="width:100%;height:100%;"></div>
    <script type="text/javascript">
    	var localUrl = window.location.href;
    	var CFG_componentUrl = localUrl.substring(localUrl.indexOf('CFG_componentUrl=') + 17);
    	if (CFG_componentUrl.charAt(0) != '/') {
    		CFG_componentUrl = "/" + CFG_componentUrl;
    	}
		var tabbar;
		function init(){
			if (!tabbar) {
				tabbar = new dhtmlXTabBar("tabbarDiv", "top");
				tabbar.setImagePath(IMAGE_PATH);
				tabbar.enableTabCloseButton(true);
			}
			CFG_addTab("构件", contextPath + CFG_componentUrl);
		}
		function CFG_addTab(text, url) {
			if (tabbar) {
				var id = "TAB_" + getTimestamp();
				tabbar.addTab(id, text, "100px");
				tabbar.cells(id).attachURL(url);
				tabbar.setTabActive(id);
			}
		}
    </script>
  </body>
</html>
