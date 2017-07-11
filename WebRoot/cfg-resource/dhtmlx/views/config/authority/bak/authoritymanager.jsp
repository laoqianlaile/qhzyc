<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>系统配置平台1.0-权限定义</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authorityroleusertree.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritymenu.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritydatamanager.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritytree.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritydata.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritydatadetail.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritycomponentbutton.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authorityconstructbutton.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritycode.js"></script>
  </head>
  <body scroll="no" onload="init()">
		<script type="text/javascript">
			var objectNodeId = "-A";
			function init(){
				var dhxLayout = new dhtmlXLayoutObject("content", "2U");
				initLayout(dhxLayout, true);
				var layoutA = dhxLayout.cells("a");
				var layoutB = dhxLayout.cells("b");
				layoutA.setWidth(240);
				layoutB.hideHeader();
			    initAuthorityTree(layoutA, layoutB);
			}
			
			function initLayoutBContent(layoutB, objectId, objectType) {
				var _this = this;
				this.rosuAuth = this.dataAuth = this.buttonManager = false;
				layoutB.hideHeader();
				layoutB.detachToolbar();
				var tabbar = layoutB.attachTabbar();
				tabbar.setImagePath(IMAGE_PATH);
				tabbar.addTab("auth$01", "菜单权限", "130px");
				tabbar.addTab("auth$02", "数据权限", "130px");
				tabbar.addTab("auth$04", "开发的构件按钮权限", "130px");
				tabbar.addTab("auth$05", "组合构件按钮权限", "130px");
				tabbar.setTabActive("auth$01");
			    
				loadAuthorityResource(tabbar, objectId, objectType);
				
				tabbar.attachEvent("onSelect", function(id,last_id){
			   		if (id == "auth$01" && !_this.rosuAuth) {
			   			loadAuthorityResource(tabbar, objectId, objectType); //资源权限
				    } else if (id == "auth$02" && !_this.dataAuth) {
				    	if (objectNodeId =="-A" || objectNodeId == "-U" || objectNodeId =="-R") {
				    		dhtmlx.alert("请先角色或用户节点再进行其操作！");
				    		tabbar.setTabActive("auth$01");
				    		return false;
				    	} else {
				    		initAuthDataConfig(tabbar, objectId, objectType);//数据权限
						}
				    } else if (id == "auth$04" && !_this.buttonManager) {
				    	if (objectNodeId =="-A" || objectNodeId == "-U" || objectNodeId =="-R") {
				    		dhtmlx.alert("请先选择角色或用户树节点再进行其操作！");
				    		tabbar.setTabActive("auth$01");
				    		return false;
				    	} else {
				    		initAuthComponentButton(tabbar, objectId, objectType);
						}
				    } else if (id == "auth$05" && !_this.buttonManager) {
				    	if (objectNodeId =="-A" || objectNodeId == "-U" || objectNodeId =="-R") {
				    		dhtmlx.alert("请先选择角色或用户树节点再进行其操作！");
				    		tabbar.setTabActive("auth$01");
				    		return false;
				    	} else {
				    		initAuthConstructButton(tabbar, objectId, objectType);
						}
				    }
		   			return true;
			   	});
			}
			
			function initLayoutB_Auth_Help(layoutB) {
				layoutB.showHeader();
				layoutB.setText("操作说明");
				layoutB.attachObject(createAuhtHelpDiv());
			}
			
			function createAuhtHelpDiv() {
				var obj = document.getElementById("DIV-help");
				if (null == obj) {
					obj = document.createElement("DIV");
					obj.setAttribute("id", "DIV-help");
					obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
					obj.innerHTML = "<ul> \n\n\n\n"
						+ "<li type=\"square\">"
						+ "<p><b>\n\n 权限定义操作说明：</b><br></p> \n"
						+ "<p>1. 根节点数据权限下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.1 角色<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.2 用户<br></p> \n"
						+ "<p>2. 在分类节点下<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以点击角色下的叶子节点刷新右边操作区域页面<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.2 可以点击用户下的叶子节点刷新右边操作区域页面<br></p> \n"
						+ "</li> \n"
						+ "</ul> \n"
						+ "<ul> \n"
						+ "<li type=\"square\">"
						+ "<p><b>权限定义操作步骤：</b><br></p> \n"
						+ "<p>3. 点击分类节点时，右侧页面是操作说明<br></p> \n"
						+ "<p>4. 点击表节点时，右侧页面是操作页面<br></p> \n"
						+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;4.1 可以操作资源权限、数据权限</p> \n"
						+ "</li> \n"
						+ "</ul> \n";
				}
				return obj;
			}
		</script>		
	</body>
</html>
