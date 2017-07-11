MODEL_URL = contextPath + "/page-demo";
gridData = {
	format: {
		headers: ["<center>名称</center>", "<center>值</center>", "<center>说明</center>", ""],
		cols: ["name", "value", "remark"],
		colWidths: ["120", "120", "120", "*"],
		colTypes: ["ro", "ro", "ro", "ro"],
		colAligns: ["left", "left", "left"]
	}
};
// 初始化grid
function initGrid1() {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    dataGrid.setSkin(Skin);
    dataGrid.init();
}
function load() {
	dhxLayout = new dhtmlXLayoutObject("content", "1C");
	dhxLayout.cells("a").hideHeader();
    dataGrid = dhxLayout.cells("a").attachGrid();
    initGrid1();
    statusBar = dhxLayout.cells("a").attachStatusBar();
    toolBar = new dhtmlXToolbarObject(statusBar);
    toolBar.setIconsPath(IMAGE_PATH);
	toolBar.addButton("submit", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
	toolBar.addSeparator("septr", 2);
	CFG_setReturnButton(toolBar);
	CFG_setCloseButton(toolBar);
	toolBar.setAlign("right");
	toolBar.attachEvent("onClick", function(id) {
		if (id == "submit") {
			alert("保存");
			return;
		}
		if (window.CFG_clickReturnButton) {
			CFG_clickReturnButton(id);
		}
		if (window.CFG_clickCloseButton) {
			CFG_clickCloseButton(id);
		}
	});
	QUERY_URL = MODEL_URL + "!search.json?Q_EQ_moduleId=" + moduleId + "&P_orders=showOrder";
   	search();
}