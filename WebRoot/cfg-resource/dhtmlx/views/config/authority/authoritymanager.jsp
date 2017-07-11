<%@page import="com.ces.config.utils.SystemParameterUtil"%>
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
	<script type="text/javascript">
		var TREE_DEFINE_URL = contextPath + "/appmanage/tree-define";
		var CODE_TYPE_URL = contextPath + "/code/code-type";
		var CODE_URL = contextPath + "/code/code";
		var AUTHORITY_URL = contextPath + "/authority/authority";
		var useTreePower = false;
		// 公用的权限URL（不区分是否三权分立的方法调用）
		var COMMON_AUTHORITY_TREE_URL = contextPath + "/authority/authority-tree";
		var COMMON_AUTHORITY_DATA_DETAIL_URL = contextPath + "/authority/authority-data-detail";
		<% if ("1".equals(SystemParameterUtil.getInstance().getSystemParamValue("三元分立"))) {%>
		var AUTHORITY_TREE_URL = contextPath + "/authority/authority-tree-copy";
		var AUTHORITY_TREE_DATA_URL = contextPath + "/authority/authority-tree-data-copy";
		var AUTHORITY_DATA_URL = contextPath + "/authority/authority-data-copy";
		var AUTHORITY_DATA_DETAIL_URL = contextPath + "/authority/authority-data-detail-copy";
		var AUTHORITY_CODE_URL = contextPath + "/authority/authority-code";
		useTreePower = true;
		<% } else {%>
		var AUTHORITY_TREE_URL = contextPath + "/authority/authority-tree";
		var AUTHORITY_TREE_DATA_URL = contextPath + "/authority/authority-tree-data";
		var AUTHORITY_DATA_URL = contextPath + "/authority/authority-data";
		var AUTHORITY_DATA_DETAIL_URL = contextPath + "/authority/authority-data-detail";
		var AUTHORITY_CODE_URL = contextPath + "/authority/authority-code";
		<% }%>
		var AUTHORITY_APPROVE_URL = contextPath + "/authority/authority-approve";
	</script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authorityroleusertree.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritydatamanager.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritytree.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritydata.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authoritydatadetail.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authorityrelatetabledata.js"></script>
	<script type="text/javascript" src="<%=dhxResPath %>/views/config/authority/js/authorityrelatetabledatadetail.js"></script>
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
				tabbar.addTab("auth$02", "数据权限", "130px");
				tabbar.setTabActive("auth$02");
				initAuthDataConfig(tabbar, objectId, objectType);
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
						+ "<p>1. 根节点权限定义树下是系统，系统下是角色<br></p> \n"
						+ "<p>2. 点击根节点或系统节点显示操作说明，点击角色显示权限操作区<br></p> \n"
						+ "</li> \n"
						+ "</ul> \n";
				}
				return obj;
			}
		</script>		
	</body>
</html>
