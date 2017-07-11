var logicGroupComLayout, logicGroupTree, logicGroupComGrid;
var logicGroupComGridData = {
    format : {
        headers : ["", "<center>构件</center>", ""],
        colWidths : ["30", "180", "*"],
        colTypes : ["ch", "ro", "ro"],
        colAligns : ["center", "left", "left"]
    }
};
var currentLogicGroupCode;
// 存储选中的逻辑表构件
var logicGroupComs = {};
/**
 * 获取组合构件中的逻辑表构件信息
 */
function loadLogicGroupComponent() {
    logicGroupComs = {};
    var logicGroupComUrl = CONSTRUCT_DETAIL_MODEL_URL + "!getCheckedLogicGroupComponentList.json?constructId="
            + currentConstructIdOfTree;
    var jsonObj = loadJson(logicGroupComUrl);
    if (jsonObj) {
        for (var m = 0; m < jsonObj.length; m++) {
            if (!logicGroupComs.logicGroupCodes) {
                logicGroupComs.logicGroupCodes = jsonObj[m].treeNodeProperty;
            } else if (logicGroupComs.logicGroupCodes.indexOf(jsonObj[m].treeNodeProperty) == -1) {
                logicGroupComs.logicGroupCodes += "," + jsonObj[m].treeNodeProperty;
            }
            logicGroupComs[jsonObj[m].treeNodeProperty] = jsonObj[m].componentVersionId;
        }
    }
}
/**
 * 初始化物理表组节点配置窗口
 */
function initLogicGroupComWin() {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var logicGroupComWin = dhxWins.createWindow("logicGroupComWin", 0, 0, 800, 400);
    logicGroupComWin.setModal(true);
    logicGroupComWin.setText("物理表组节点构件设置");
    logicGroupComWin.center();
    var logicGroupComStatusBar = logicGroupComWin.attachStatusBar();
    var logicGroupComToolBar = new dhtmlXToolbarObject(logicGroupComStatusBar);
    logicGroupComToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    logicGroupComToolBar.setAlign("right");
    logicGroupComToolBar.addButton("confirm", 1, "&nbsp;&nbsp;确定&nbsp;&nbsp;");
    logicGroupComToolBar.addButton("cancel", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
    logicGroupComToolBar.attachEvent("onClick", function(id) {
        if (id == "confirm") {
            var checkedLogicGroupComs = "";
            if (logicGroupComs.logicGroupCodes) {
                var logicGroupCodeArray = logicGroupComs.logicGroupCodes.split(",");
                for (var i in logicGroupCodeArray) {
                    if (logicGroupComs[logicGroupCodeArray[i]] && logicGroupComs[logicGroupCodeArray[i]] != "") {
                        checkedLogicGroupComs += logicGroupCodeArray[i] + "," + logicGroupComs[logicGroupCodeArray[i]]
                                + ";";
                    }
                }
            } else {
                dhtmlx.message("没有选择任何构件！");
                return;
            }
            var jsonObj = eval("("
                    + loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!saveLogicGroupComponents.json?constructId="
                            + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId
                            + "&checkedLogicGroupComs=" + checkedLogicGroupComs) + ")");
            if (typeof jsonObj == 'string') {
                jsonObj = eval("(" + jsonObj + ")");
            }
            if (jsonObj.success) {
                dhtmlx.message({
                    text : getMessage("operate_success")
                });
            } else {
                dhtmlx.message({
                    text : getMessage("operate_failure")
                });
            }
            logicGroupComWin.close();
            constructDetailGridLoadData();
        } else if (id == "cancel") {
            logicGroupComWin.close();
        }
    });
    logicGroupComLayout = logicGroupComWin.attachLayout("2U");
    var logicGroupTreeCell = logicGroupComLayout.cells("a");
    logicGroupTreeCell.hideHeader();
    logicGroupTreeCell.setWidth(200);
    var logicGroupComGridCell = logicGroupComLayout.cells("b");
    logicGroupComGridCell.hideHeader();
    initLogicGroupTree(logicGroupTreeCell);
    logicGroupComGrid = logicGroupComGridCell.attachGrid();
    initParamGrid(logicGroupComGrid, logicGroupComGridData);
    logicGroupComGrid.attachEvent("onCheck", checkLogicGroupCom);
    loadLogicGroupComponent();
}
/**
 * 初始化逻辑表组树
 */
function initLogicGroupTree(logicGroupTreeCell) {
    logicGroupTree = logicGroupTreeCell.attachTree();
    logicGroupTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    logicGroupTree.attachEvent("onMouseIn", function(id) {
        logicGroupTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    logicGroupTree.attachEvent("onMouseOut", function(id) {
        logicGroupTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var treeJson = {
        id : 0,
        item : [{
            id : 'Root',
            text : '逻辑表组树',
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1
        }]
    };
    logicGroupTree.setDataMode("json");
    logicGroupTree.enableSmartXMLParsing(true);
    logicGroupTree.setXMLAutoLoading(CONSTRUCT_DETAIL_MODEL_URL + "!getLogicGroupTree.json?E_model_name=tree");
    logicGroupTree.loadJSONObject(treeJson);
    logicGroupTree.refreshItem('Root');
    logicGroupTree.attachEvent("onClick", function(nId) {
        // 树节点ID
        if (nId != "Root") {
            currentLogicGroupCode = nId;
            loadLogicGroupComGrid();
        } else {
            currentLogicGroupCode = null;
            logicGroupComGrid.clearAll();
        }
    });
}
/**
 * 加载逻辑表构件列表
 */
function loadLogicGroupComGrid() {
    logicGroupComGrid.clearAll();
    loadParamGridData(logicGroupComGrid, CONSTRUCT_DETAIL_MODEL_URL
            + "!getLogicGroupComponentList.json?E_model_name=datagrid&logicTableGroupCode=" + currentLogicGroupCode);
    logicGroupComGrid.checkAll(false);
    if (logicGroupComs[currentLogicGroupCode]) {
        logicGroupComGrid.cells(logicGroupComs[currentLogicGroupCode], 0).setValue("1");
    }
}
/**
 * 点击逻辑表构件列表复选框时的事件
 */
function checkLogicGroupCom(rId, cInd, state) {
    if (!logicGroupComs.logicGroupCodes) {
        logicGroupComs.logicGroupCodes = currentLogicGroupCode;
    } else if (logicGroupComs.logicGroupCodes.indexOf(currentLogicGroupCode) == -1) {
        logicGroupComs.logicGroupCodes += "," + currentLogicGroupCode;
    }
    if (state) {
        if (logicGroupComs[currentLogicGroupCode]) {
            logicGroupComGrid.cells(logicGroupComs[currentLogicGroupCode], 0).setValue("0");
        }
        logicGroupComs[currentLogicGroupCode] = rId;
    } else {
        if (logicGroupComs[currentLogicGroupCode]) {
            logicGroupComs[currentLogicGroupCode] = "";
        }
    }
}