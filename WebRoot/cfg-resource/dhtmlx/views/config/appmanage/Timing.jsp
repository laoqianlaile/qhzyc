<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.ces.xarch.core.security.entity.SysOrg"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>定时任务</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/TimingList.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/Timing.js"></script>

  </head>
  
 <body  scroll="no" onload="init()">
		<script type="text/javascript">
			var dataGrid;
			var timingTabbar = null;
			var layoutA = null;
			var selfDhxLayout = null;
			var timing01tabLayout = null;
			function init(){
				selfDhxLayout = new dhtmlXLayoutObject("content", "1C");
				initLayout(selfDhxLayout, true);
				layoutA = selfDhxLayout.cells("a");
				initLayoutContent(layoutA);
			}
			
			function initLayoutContent(layoutA) {
				layoutA.hideHeader();
				layoutA.detachToolbar();				
				timingTabbar = layoutA.attachTabbar();
				timingTabbar.setImagePath(IMAGE_PATH);
				timingTabbar.addTab("timing$01", "定时任务控制台", "150px");
				timingTabbar.addTab("timing$02", "定时任务维护", "150px");
				timingTabbar.setTabActive("timing$01");
				initTimeListDiv(timingTabbar);
			    dataGrid = initTimeDetil(timingTabbar);
			    timingTabbar.attachEvent("onSelect", function(id,last_id){
			   		if (id == "timing$01") {
			   			initTimeListDiv(timingTabbar);
				    } else if (id == "timing$02") {
				    	initTimeDetil(timingTabbar);
				    } 
		   			return true;
			   	});
			}
			
			function layoutResize() {
			    if (selfDhxLayout) {
			    	selfDhxLayout.setSizes();
			    }
			}
		</script>	
			
		<div id="tab01$from">		
		</div>	
	</body>
</html>
