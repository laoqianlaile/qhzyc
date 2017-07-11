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
	<script type="text/javascript" src="<%=dhxResPath %>/common/js/CFG_embedded_page.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/common/js/CFG_navigation.js"></script>
  </head>
  <body onload="init()">
    <div id="CFG_navigationBarArea" style="width:100%;height:27px;"></div>
    <div id="CFG_navigationMainArea" style="width:100%;"></div>
    <script type="text/javascript">
    	var localUrl = window.location.href;
    	var CFG_componentUrl = localUrl.substring(localUrl.indexOf('CFG_componentUrl=') + 17);
    	if (CFG_componentUrl.charAt(0) != '/') {
    		CFG_componentUrl = "/" + CFG_componentUrl;
    	}
    	function CFG_navigationResize() {
			var barAreaDiv = document.getElementById("CFG_navigationBarArea");
			var mainAreaDiv = document.getElementById("CFG_navigationMainArea");
			barAreaDiv.style.width = (document.body.offsetWidth - 10) + "px";
			mainAreaDiv.style.height = (document.body.offsetHeight - 27) + "px";
			if (window.CFG_navigationMainLayout) {
				CFG_navigationMainLayout.cont.obj._offsetTop = 27;
				CFG_navigationMainLayout.setSizes();
			}
		};
		function init(){
			CFG_navigationResize();
			if (window.addEventListener) {
				window.addEventListener("resize", function(){
					CFG_navigationResize();
				});
			} else {
				window.attachEvent("onresize", function(){
					CFG_navigationResize();
				});
			}
			window.CFG_navigationBar = new dhtmlXToolbarObject("CFG_navigationBarArea");
			window.CFG_navigationMainLayout  = new dhtmlXLayoutObject("CFG_navigationMainArea", "1C");
			CFG_navigationMainLayout.cont.obj._offsetTop = 27;
			CFG_navigationMainLayout.setSizes();
			CFG_navigationMainLayout.cells("a").hideHeader();
			CFG_navigationMainLayout.cells("a").attachURL(contextPath + CFG_componentUrl);
			CFG_navigationBar.CFG_navigationItemPosition = -1;
			CFG_navigationBar.CFG_navigationWindows = [];
		}
    </script>
  </body>
</html>
