<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
	String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String folderPath = request.getRequestURI();
    folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
    String moduleId = request.getParameter("P_moduleId");
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
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		var page="MT_page_";
	</script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/common/js/CFG_component.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appsearch.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcolumn.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appsort.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_COMMON.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_TREE.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_NODE.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_SEARCH.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_TBAR.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_GRID.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_FORM.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/showbase/js/MT_BASE_USER.js"></script>
	<script type="text/javascript" src="<%=folderPath %>/js/MT_TREE.js"></script>
	<script type="text/javascript" src="<%=folderPath %>/js/MT_TBAR.js"></script>
	<script type="text/javascript" src="<%=folderPath %>/js/MT_GRID.js"></script>
	<script type="text/javascript" src="<%=folderPath %>/js/MT_FORM.js"></script>
	<script type="text/javascript" src="<%=folderPath %>/js/MT_USER.js"></script>
	<script type="text/javascript" src="<%=folderPath %>/js/SECONDARY_DEVELOPMENT.js"></script>
	<script type="text/javascript">
		function init() {
			var _this = this;
			this.moduleId = "<%=moduleId %>";
			this.nodeId = null;
			this.moduleUrl= contextPath + "/appmanage/module";
			var curLayout = new dhtmlXLayoutObject("content", "2U");
			initLayout(curLayout, true);
			var aLayout = curLayout.cells("a");
			aLayout.hideHeader();
			var bLayout = curLayout.cells("b");
			bLayout.hideHeader();
			var mObj = MT_GetModule(this.moduleId);
			document.title = mObj.name;
			this.componentVersionId = mObj.componentVersionId;
			this.mObj = mObj;
			this.actionName = mObj.componentClassName;
			// 初始化
			MT_TREE_init(_this, aLayout, bLayout);
			// 初始化帮助
			MT_HELP_init(bLayout);
			
			/** 构件组装方式为内嵌的代码，构件上内嵌构件显示的区域设置  start */
		    CFG_reserveZone = curLayout.cells("b");
		    curLayout.attachEvent("onResizeFinish", function(){
		    	if (window.CFG_componentZoneResize) {
		    		CFG_componentZoneResize();
		    	}
		    });
		    curLayout.attachEvent("onPanelResizeFinish", function(){
				if (window.CFG_componentZoneResize) {
					CFG_componentZoneResize();
		    	}
		    });
		    /** 构件组装方式为内嵌的代码 ，构件上内嵌构件显示的区域设置 end */
		}
	</script>
  </head>
  <body onload="init()">
  </body>
</html>