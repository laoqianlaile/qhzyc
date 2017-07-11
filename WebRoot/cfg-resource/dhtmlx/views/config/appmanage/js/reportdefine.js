/**
 * 报表配置
 */
function loadCellReportDefine(tabbar, that) {
	var _this = this;
	var moduleUrl = AppActionURI.reportDefine;
	
	var dhxLayout = tabbar.cells("tab$report$03").attachLayout("1C");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	var aLayout = dhxLayout.cells("a"); // 报表定义区
	if (_isIE) {
        aLayout.attachURL(DHX_RES_PATH + "/views/config/appmanage/report/reportdefine.jsp?P_reportId=" + nodeId);
    } else {
        aLayout.attachURL(contextPath + "/cfg-resource/cell/views/reportpreview.jsp?reportPrintUrl=" + DHX_RES_PATH
                + "/views/config/appmanage/report/reportdefine.jsp?P_reportId=" + nodeId);
    }
	
	aLayout.hideHeader();
	dhxLayout.attachEvent("onResizeFinish", function(){
		var tbarObj = aLayout.getFrame().contentDocument.getElementById("reporttoolbar$3$div");
		var cellWebObj = aLayout.getFrame().contentDocument.getElementById("cellweb$div");
		var accordionObj = aLayout.getFrame().contentDocument.getElementById("accordion$div");
		var CellWeb = aLayout.getFrame().contentWindow.CellWeb;
		if (!tbarObj || !cellWebObj || !accordionObj || !CellWeb) {
		    return;
		}
		var cw = aLayout.getFrame().contentDocument.body.clientWidth - 260 - 24 + 13;
		var ch = aLayout.getFrame().contentDocument.body.offsetHeight - 106 + 19;
		CellWeb.style.width = cw;
		cellWebObj.style.width = cw + "px";
		CellWeb.style.height = ch;
		cellWebObj.style.height = ch + "px";
		accordionObj.style.height = ch + "px";
		accordionObj.style.display = "";
	});
	// success loaded (loaded completed)
	that.defineLoaded = true;
}

