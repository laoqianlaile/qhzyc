var commonComponentGridData = {
	format: {
		headers: ["<center>构件类名</center>", "<center>构件名称</center>", "<center>版本号</center>", "<center>导入日期</center>", "<center>类型</center>", "<center>前台</center>", ""],
		cols: ["component.name", "component.alias", "version", "importDate", "component.type", "views"],
		colWidths: ["120", "120", "80", "120", "120", "120", "*"],
		colTypes: ["ro", "ro", "ro", "ro", "co", "ro", "co"],
		colAligns: ["left", "left", "left", "left", "left", "left"],
		colTooltips: ["true", "true", "true", "true", "true", "true", "true"]
	}
};
var commonComponentGrid;
/**
 * 配置关联的公用构件
 */
function configCommonComponentRelation() {
    var selectId = dataGrid.getSelectedRowId();
    if (selectId == undefined) {
        dhtmlx.message(getMessage("select_record"));
        return;
    }
    if (selectId.indexOf(",") != -1) {
        dhtmlx.message(getMessage("select_only_one_record"));
        return;
    }
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var configCommonComponentRelationWin = dhxWins.createWindow("configCommonComponentRelationWin", 0, 0, 800, 400);
    configCommonComponentRelationWin.setModal(true);
    configCommonComponentRelationWin.setText("配置公用构件");
    configCommonComponentRelationWin.center();
    commonComponentGrid = configCommonComponentRelationWin.attachGrid();
    var commonComponentToolBar = configCommonComponentRelationWin.attachToolbar();
	initConfigGrid(commonComponentGrid, commonComponentGridData);
	var typeCombo = commonComponentGrid.getCombo(4);
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
	initCommonComponentToolBar(commonComponentToolBar, selectId);
	refreshCommonComponentGrid(selectId);
}
/**
 * 初始化关联的公用构件的工具条的方法
 * @param {dhtmlxToolbar} toolBar
 * @param {string} componentVersionId 构件ID
 */
function initCommonComponentToolBar(toolBar, componentVersionId) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 1, "添加", "new.gif");
    toolBar.addSeparator("septr$1", 2);
    toolBar.addButton("delete", 3, "删除", "delete.gif");
    toolBar.attachEvent("onClick", function(id) {
        if (id == "add") {
            selectCommonComponentVersion();
        } else if (id == "delete") {
            var selectIds = commonComponentGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!deleteCommonComponent?componentVersionId="
                                + componentVersionId + "&commonComponentVersionIds=" + selectIds, function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            refreshCommonComponentGrid(componentVersionId);
                        });
                    }
                }
            });
        }
    });
}
/**
 * 刷新关联的公用构件列表
 */
function refreshCommonComponentGrid(componentVersionId) {
    var url = COMPONENT_VERSION_MODEL_URL + "!getCommonComponentList.json?E_model_name=datagrid&componentVersionId="
            + componentVersionId;
    search(commonComponentGrid, commonComponentGridData, url);
}
// 选择公用构件弹出框的相关信息
var selectCommonComponentLayout, selectCommonComponentTree, selectCommonComponentGrid, selectCommonComponentToolBar, commonComponentStatusBar;
/**
 * 选择添加公用构件的弹出框
 */
function selectCommonComponentVersion() {
	if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var selectComponentWin = dhxWins.createWindow("selectComponentWin", 0, 0, 1000, 400);
    selectComponentWin.setModal(true);
    selectComponentWin.setText("选择构件");
    selectComponentWin.center();
    selectCommonComponentLayout = selectComponentWin.attachLayout("2U");
    initSelectCommonComponentLayout();
    selectComponentWin.attachEvent("onClose", function() {
    	selectCommonComponentLayout = null;
    	selectCommonComponentTree = null;
    	selectCommonComponentGrid = null;
    	selectCommonComponentToolBar = null;
    	commonComponentStatusBar = null;
        return true;
    });
}
/**
 * 初始化选择公用构件弹出框布局
 */
function initSelectCommonComponentLayout() {
	selectCommonComponentLayout.cells("a").hideHeader();
	selectCommonComponentLayout.cells("b").hideHeader();
	selectCommonComponentLayout.cells("a").setWidth(240);
	selectCommonComponentLayout.setAutoSize();
	selectCommonComponentTree = selectCommonComponentLayout.cells("a").attachTree();
	selectCommonComponentTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	selectCommonComponentTree.attachEvent("onMouseIn", function(id) {
		selectCommonComponentTree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	selectCommonComponentTree.attachEvent("onMouseOut", function(id) {
		selectCommonComponentTree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	selectCommonComponentTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif")
	selectCommonComponentTree.setDataMode("json");
	selectCommonComponentTree.enableSmartXMLParsing(true);
	selectCommonComponentTree.setXMLAutoLoading(COMPONENT_AREA_MODEL_URL+"!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
	var selectCommonComponentTreeJson = {id:0, item:[{id:-1,text:"构件生产库",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
	selectCommonComponentTree.loadJSONObject(selectCommonComponentTreeJson);
	selectCommonComponentTree.refreshItem("-1");
	selectCommonComponentTree.attachEvent("onClick", function(id) {
		if (id == "-1") {
			if (selectCommonComponentToolBar) {
				selectCommonComponentLayout.cells("b").detachToolbar();
				selectCommonComponentLayout.cells("b").detachStatusBar();
				selectCommonComponentLayout.cells("b").detachObject();
				selectCommonComponentToolBar = null;
				commonComponentStatusBar = null;
				selectCommonComponentGrid = null;
			}
		} else {
			selectCommonComponentLayout.cells("b").hideHeader();
			if (!selectCommonComponentToolBar) {
				selectCommonComponentToolBar = selectCommonComponentLayout.cells("b").attachToolbar();
			    commonComponentStatusBar = selectCommonComponentLayout.cells("b").attachStatusBar();
			    initSelectCommonComponentToolBar();
		    }
		    if (!selectCommonComponentGrid) {
			    selectCommonComponentGrid = selectCommonComponentLayout.cells("b").attachGrid();
			    var typeCombo = selectCommonComponentGrid.getCombo(4);
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
			    initGrid(selectCommonComponentGrid, commonComponentGridData, commonComponentStatusBar);
		    }
            refreshSelectCommonComponentGrid();
    	}
	});
}
/**
 * 初始化选择公用构件的列表工具条
 */
function initSelectCommonComponentToolBar() {
    selectCommonComponentToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    selectCommonComponentToolBar.addButton("import", 0, "添加", "new.gif");
    selectCommonComponentToolBar.attachEvent('onClick', function(id) {
        if (id == "import") {
        	var componentVersionId = dataGrid.getSelectedRowId();
            var commonComponentVersionIds = selectCommonComponentGrid.getSelectedRowId();
            if (commonComponentVersionIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!validateCommonComponentVersion.json?componentVersionId="
                    + componentVersionId + "&commonComponentVersionIds=" + commonComponentVersionIds, function(loader) {
                var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
                if (!result.success) {
                    var failureComponentVersionId = result.failureComponentVersionId;
                    var failureComponentVersionIds = failureComponentVersionId.split(",");
                    var failureComponentVersionNames = "";
                    for (var i in failureComponentVersionIds) {
                        failureComponentVersionNames += "、"
                                + selectCommonComponentGrid.cells(failureComponentVersionIds[i], 1).getValue();
                    }
                    failureComponentVersionNames = failureComponentVersionNames.substring(1,
                            failureComponentVersionNames.length);
                    dhtmlx.message("\"" + failureComponentVersionNames + "\"构件冲突");
                } else {
                    dhtmlxAjax.get(COMPONENT_VERSION_MODEL_URL + "!saveCommonComponentVersion.json?componentVersionId="
                            + componentVersionId + "&commonComponentVersionIds=" + commonComponentVersionIds, function(
                            loader) {
                        dhtmlx.message(getMessage("save_success"));
                        refreshCommonComponentGrid(componentVersionId);
                    });
                }
            });
        }
    });
}
/**
 * 刷新选择公用构件列表
 */
function refreshSelectCommonComponentGrid() {
	var url = COMPONENT_VERSION_MODEL_URL + "!search.json?Q_EQ_areaId=" + selectCommonComponentTree.getSelectedItemId() + "&Q_EQ_component.type=0&P_orders=componentId,version";
	search(selectCommonComponentGrid, commonComponentGridData, url);
}