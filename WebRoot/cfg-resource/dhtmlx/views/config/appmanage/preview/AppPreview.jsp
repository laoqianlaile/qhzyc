<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String folderPath = request.getRequestURI();
folderPath = folderPath.substring(0, folderPath.lastIndexOf("/"));
String moduleId = request.getParameter("P_moduleId");
String tableId  = request.getParameter("P_tableId");
System.out.println("module id = " + moduleId);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>应用自定义预览</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		var page="MT_page_";
	</script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/preview/js/PV_COMMON.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/preview/js/PV_SEARCH.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/preview/js/PV_TBAR.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/preview/js/PV_GRID.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/preview/js/PV_FORM.js"></script>

  </head>
  
<body onload="init()">
<script type="text/javascript">

function init() {
  	var _this = this;
  	this.moduleId = "<%=moduleId %>";
  	var curLayout = new dhtmlXLayoutObject("content", "1C");
	initLayout(curLayout);
	//var mObj = MT_GetModule(_this.moduleId);
	var aLayout = curLayout.cells("a");
	aLayout.hideHeader();
	//if (null == mObj) {
	//	aLayout.setText("模块不存在");
	//	return;
	//}
	PV_SubLayoutInit(_this, aLayout, "<%=tableId%>", "1");
}

</script>
</body>
</html>
