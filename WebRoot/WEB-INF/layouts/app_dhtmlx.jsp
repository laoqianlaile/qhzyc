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
	var searchUrl = "${ctx}/menu/menu!loadMenu.json?E_model_name=menu&F_in=name,url,hasChild&P_UD=url&P_NOCHILD=true&releaseSystem=true";
	menu.enableDynamicLoading(searchUrl, "json");
	menu.attachEvent("onClick", function(id) {
		var url = this.getUserData(id, "url");
		if (!url || url == "" || url == "null") {
			return;
		}
		if (url.charAt(0) != '/') {
			url = "/" + url;
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
	function CFG_addTab(text, url) {
		if (tabbar) {
			var id = "TAB_" + getTimestamp();
			tabbar.addTab(id, text, "100px");
			tabbar.cells(id).attachURL(url);
			tabbar.setTabActive(id);
		}
	}
 </script>