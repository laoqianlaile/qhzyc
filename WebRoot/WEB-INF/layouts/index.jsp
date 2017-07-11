<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>系统配置平台V1.0</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script type="text/javascript">
	var contextPath = "${ctx}";
</script>
<script src="${ctx}/cfg-resource/dhtmlx/common/js/base.js" type="text/javascript"></script>
<script src="${ctx}/cfg-resource/dhtmlx/common/js/common.js" type="text/javascript"></script>
<script src="${ctx}/cfg-resource/dhtmlx/common/js/dhtmlxform_item_container.js" type="text/javascript"></script>
<script src="${ctx}/cfg-resource/dhtmlx/common/js/dhtmlxmenu_xarch.js" type="text/javascript"></script>
</head>
<body onresize="resizeMainLayout()">
	<div id="head">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
	</div>
	<div id="main">
		<div id="tabbarDiv" style="width:100%;height:100%;"></div>
	</div>
	<div id="foot">
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
</html>
<script type="text/javascript">
	var tabbar;
	var menu = new dhtmlXMenuObject('menuObj');
	var menuStr = '<menu>'
	
			+ '<item id="ying_yong_manager" text="自定义">'
			  + '<item id="dang_an_type" text="表定义"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/tablemanager.jsp]]></userdata></item>'
			  + '<item id="app_toolbar" text="工具条配置"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/apptoolbar.jsp]]></userdata></item>'
			  + '<item id="app_define" text="应用定义"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/appdefine.jsp]]></userdata></item>'
			  + '<item id="baobiaodingyi" text="报表定义"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/report.jsp]]></userdata></item>'
			  + '<item id="dang_an_tree" text="树定义"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/ArchiveTree.jsp]]></userdata></item>'
			  + '<item id="workflow" text="工作流自定义"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/workflow.jsp]]></userdata></item>'
			  + '<item id="menu_define" text="模块布局定义"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/module.jsp]]></userdata></item>'
		+ '</item>'
	
		+ '<item type="separator"/>'
		
		+ '<item id="system_config" text="系统组装">'
			  + '<item id="component_lib" text="构件管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/component/component.jsp]]></userdata></item>'
			  + '<item id="menu" text="菜单管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/menu/menu.jsp]]></userdata></item>'
			  + '<item id="construct" text="构件组装"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/construct/construct.jsp]]></userdata></item>'
			  + '<item id="release" text="系统发布"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/release/release.jsp]]></userdata></item>'
			 
		+ '</item>'
		
		+ '<item type="separator"/>'
		
		+ '<item id="system_manager" text="系统管理">'
			 + '<item id="parameter_define" text="系统参数管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/parameter/systemparameter.jsp]]></userdata></item>'
			 + '<item id="code_define" text="编码管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/code/code.jsp]]></userdata></item>'
			 + '<item id="authority_define" text="权限管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/AuthorityManager.jsp]]></userdata></item>'
			 + '<item id="timing" text="定时任务管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/Timing.jsp]]></userdata></item>'
			 + '<item id="database_manager" text="数据源管理"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/database/database.jsp]]></userdata></item>'
		+ '</item>'
		
		+ '<item type="separator"/>'
		
		+ '<item id="module_preview" text="模块预览">'
			//+ '<item id="MT_1C" text="模块(1C)预览"><userdata name="url"><![CDATA[${ctx}/views/config/appmanage/show/MT_1C.jsp]]></userdata></item>'
			+ '<item id="MT_2U_01" text="整理归档"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/show/MT_2U.jsp?P_moduleId=402884e940ebe3e40140ebf4a1b30014]]></userdata></item>'
			+ '<item id="MT_2U_02" text="部门审核"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/show/MT_2U.jsp?P_moduleId=402884e940ebe3e40140ebf4cc980017]]></userdata></item>'
			+ '<item id="MT_2E_01" text="借阅审批"><userdata name="url"><![CDATA[${ctx}/cfg-resource/views/config/appmanage/show/MT_2E.jsp?P_moduleId=402881e8412b38c601412b4be8ee00e3]]></userdata></item>'
		+ '</item>'
		+'</menu>';
	//menu.loadXMLString(menuStr);
	var searchUrl = "${ctx}/menu/menu!loadMenu.json?E_model_name=menu&F_in=name,url,hasChild&P_UD=url&P_NOCHILD=true";
	menu.enableDynamicLoading(searchUrl, "json");
	menu.attachEvent("onClick", function(id) {
		var url = this.getUserData(id, "url");
		if (!url || url == "" || url == "null") {
			return;
		}
		if (url.charAt(0) != '/') {
			url = "/" + url;
    	}
		if (id.indexOf("sys_") != -1) {
			if (url.indexOf("?") != -1) {
				url += "&isConfigPlatform=true";
			} else {
				url += "?isConfigPlatform=true";
			}
		}
		if (!tabbar) {
			tabbar = new dhtmlXTabBar("tabbarDiv", "top");
			tabbar.setImagePath(IMAGE_PATH);
			tabbar.enableTabCloseButton(true);
		}
		var allTabs = tabbar.getAllTabs();
		var exist = false;
		if (allTabs && allTabs.length > 0) {
			for (var key in allTabs) {
				if (allTabs[key] == id) {
					tabbar.setTabActive(id);
					exist = true;
					break;
				}
			}
		}
		if (!exist) {
			tabbar.addTab(id, this.getItemText(id), "100px");
			tabbar.cells(id).attachURL(contextPath + url);
			tabbar.setTabActive(id);
		}
	});
	function resizeMainLayout () {
		if (!tabbar) return;
		var allTabs = tabbar.getAllTabs();
		if (!allTabs || allTabs.length == 0) return;
		var mainDiv = document.getElementById("main");
		var w = mainDiv.offsetWidth;
		var h = mainDiv.offsetHeight;
		for (var key in allTabs) {
			tabbar.setSize(w,h);
		}		
	}
 </script>