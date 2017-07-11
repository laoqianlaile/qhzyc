COMPONENT_AREA_MODEL_URL = contextPath + "/component/component-area";
COMPONENT_VERSION_MODEL_URL = contextPath + "/component/component-version";
var componentLayout, componentTree, componentGrid, componentToolBar, componentStatusBar;
var componentGridData = {
	format: {
		headers: ["<center>构件类名</center>", "<center>构件名称</center>", "<center>版本号</center>", "<center>导入日期</center>", "<center>类型</center>", "<center>前台</center>",""],
		cols: ["component.name", "component.alias", "version", "importDate", "component.type", "views"],
		colWidths: ["100", "100", "60", "100", "100", "100", "*"],
		colTypes: ["ro", "ro", "ro", "ro", "co", "ro", "ro"],
		colAligns: ["left", "left", "left", "left", "left", "left"],
		colTooltips: ["true", "true", "true", "true", "true", "true", "true"]
	}
};
/**
 * 选择添加构件弹出框
 */
function selectComponentVersion() {
	if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var selectComponentWin = dhxWins.createWindow("selectComponentWin", 0, 0, 1000, 400);
    selectComponentWin.setModal(true);
    selectComponentWin.setText("选择构件");
    selectComponentWin.center();
    componentLayout = selectComponentWin.attachLayout("2U");
    initComponentLayout();
    selectComponentWin.attachEvent("onClose", function() {
    	componentLayout = null;
    	componentTree = null;
    	componentGrid = null;
    	componentToolBar = null;
    	componentStatusBar = null;
        return true;
    });
}
/**
 * 初始化选择构件布局
 */
function initComponentLayout() {
	componentLayout.cells("a").hideHeader();
	componentLayout.cells("b").hideHeader();
	componentLayout.cells("a").setWidth(240);
	componentLayout.setAutoSize();
	componentTree = componentLayout.cells("a").attachTree();
	componentTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	componentTree.attachEvent("onMouseIn", function(id) {
		componentTree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	componentTree.attachEvent("onMouseOut", function(id) {
		componentTree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	componentTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
	componentTree.setDataMode("json");
	componentTree.enableSmartXMLParsing(true);
	componentTree.setXMLAutoLoading(COMPONENT_AREA_MODEL_URL+"!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
	var componentTreeJson = {id:0, item:[{id:-1,text:"构件生产库",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
	componentTree.loadJSONObject(componentTreeJson);
	componentTree.refreshItem("-1");
	componentTree.attachEvent("onClick", function(id) {
		if (id == "-1") {
			if (componentToolBar) {
				componentLayout.cells("b").detachToolbar();
				componentLayout.cells("b").detachStatusBar();
				componentLayout.cells("b").detachObject();
				componentToolBar = null;
				componentStatusBar = null;
				componentGrid = null;
			}
		} else {
			componentLayout.cells("b").hideHeader();
			if (!componentToolBar) {
				componentToolBar = componentLayout.cells("b").attachToolbar();
			    componentStatusBar = componentLayout.cells("b").attachStatusBar();
			    initComponentToolBar();
		    }
		    if (!componentGrid) {
			    componentGrid = componentLayout.cells("b").attachGrid();
			    var typeCombo = componentGrid.getCombo(4);
			    typeCombo.put("0", "公用构件");
                typeCombo.put("1", "页面构件");
                typeCombo.put("2", "逻辑构件");
                typeCombo.put("3", "树构件");
                typeCombo.put("4", "物理表构件");
                typeCombo.put("5", "逻辑表构件");
                typeCombo.put("6", "通用表构件");
                typeCombo.put("7", "标签页构件");
                typeCombo.put("8", "中转器构件");
                typeCombo.put("9", "组合构件");
			    pageable = true;
			    initGrid(componentGrid, componentGridData, componentStatusBar);
		    }
            refreshComponentVersionGrid();
    	}
	});
}
/**
 * 初始化构件列表工具条
 */
function initComponentToolBar() {
	componentToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
	componentToolBar.addButton("import", 0, "添加", "new.gif");
	componentToolBar.attachEvent('onClick', function(id) {
    	if (id == "import") {
    		var componentVersionIds = componentGrid.getSelectedRowId();
    		if (componentVersionIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
    		dhtmlxAjax.get(MODEL_URL + "!validateSystemComponentVersion.json?rootMenuId=" + currentTreeNodeId + "&componentVersionIds=" + componentVersionIds, function(loader) {
				var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
				if (!result.success) {
					var failureComponentVersionId = result.failureComponentVersionId;
					var failureComponentVersionIds = failureComponentVersionId.split(",");
					var failureComponentVersionNames = "";
					for (var i in failureComponentVersionIds) {
						failureComponentVersionNames += "、" + componentGrid.cells(failureComponentVersionIds[i], 1).getValue();
					}
					failureComponentVersionNames = failureComponentVersionNames.substring(1, failureComponentVersionNames.length);
					dhtmlx.message("\"" + failureComponentVersionNames + "\"构件冲突");
				} else {
					dhtmlxAjax.get(MODEL_URL + "!saveSystemComponentVersion.json?rootMenuId=" + currentTreeNodeId + "&componentVersionIds=" + componentVersionIds, function(loader) {
						dhtmlx.message(getMessage("save_success"));
						refreshSystemComponentGrid();
					});
				}
			});
    	}
    });
}
/**
 * 刷新构件列表
 */
function refreshComponentVersionGrid() {
    var url = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_areaId=" + componentTree.getSelectedItemId() + "&Q_LT_component.type=9&P_orders=componentName,version";
	search(componentGrid, componentGridData, url);
}