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
		<title>标签管理</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>
	<body scroll="no" onload="init()">
		<script type="text/javascript">
			var nodeId = null;
			function init(){
				nodeId = "1";
				var that = this;
				var dhxLayout = new dhtmlXLayoutObject("content", "2U");
				initLayout(dhxLayout, true);
				that.aLayout = dhxLayout.cells("a");
				that.bLayout = dhxLayout.cells("b");				
				that.createTableInited = false;				
			    initTree(that);
			    initTabbar(that, nodeId);			    
			}
			function initTree(that){
				var layoutA = that.aLayout;
				layoutA.setWidth(240);
				layoutA.hideHeader();

				var menuItem = loadJson(contextPath + "/menu/menu!getSysMenu.json");
				
			    tree = layoutA.attachTree();
			    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
			    tree.attachEvent("onMouseIn", function(id) {
			        tree.setItemStyle(id, "background-color:#D5E8FF;");
			    });
			    tree.attachEvent("onMouseOut", function(id) {
			        tree.setItemStyle(id, "background-color:#FFFFFF;");
			    });
			    tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
			    var treeJson = {
			        id : 0,
			        item : [{id : -1, text : "标签定义", im0 : "safe_close.gif", im1 : "safe_open.gif", im2 : "safe_close.gif", open : true, item : menuItem}]
			    };
			    tree.setDataMode("json");
			    tree.loadJSONObject(treeJson);
			    tree.selectItem("1", true);
			    tree.attachEvent("onClick", function(nId) {
			    	initTabbar(that, nId);
			    });
			}
			function initTabbar(that, nId){
				nodeId = nId;
				var layoutB = that.bLayout;
				layoutB.hideHeader();
				layoutB.detachToolbar();
				var tabbar = layoutB.attachTabbar();
				tabbar.setImagePath(IMAGE_PATH);
				tabbar.enableAutoReSize();
				tabbar.addTab("typeLabel", "分类标签", "150px");
				tabbar.addTab("columnLabel", "字段标签", "150px");
				tabbar.cells("typeLabel").attachURL("<%=path%>/cfg-resource/dhtmlx/views/config/label/typelabel.jsp?nodeId='" + nodeId + "'");
				tabbar.cells("columnLabel").attachURL("<%=path%>/cfg-resource/dhtmlx/views/config/label/columnlabel.jsp?nodeId='" + nodeId + "'");
				tabbar.setTabActive("typeLabel");
			}
		</script>
	</body>
</html>
