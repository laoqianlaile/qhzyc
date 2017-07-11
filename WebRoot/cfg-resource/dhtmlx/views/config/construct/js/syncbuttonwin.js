var syncButtonGrid;
var syncButtonGridData = {
    format : {
        headers: ["&nbsp;", "<center>构件</center>", "<center>按钮显示名称</center>", "<center>按钮类型</center>", "<center>所属按钮组</center>", ""],
        cols: ["id", "componentAliasAndVersion", "buttonDisplayName", "buttonType", "parentButtonCode"],
        userDatas: ["componentVersionId", "reserveZoneId", "buttonSource"],
        colWidths: ["30", "240", "120", "120", "120", "*"],
        colTypes : ["ch", "ro", "ro", "co", "ro", "ro"],
        colAligns: ["right", "left", "left", "left", "left"]
    }
};
var syncButtonIds;
/**
 * 初始化添加按钮窗口
 */
function initSyncButtonWin(newSyncButtonWin) {
    syncButtonIds = "";
    newSyncButtonWin.setModal(true);
    newSyncButtonWin.setText("预置按钮");
    newSyncButtonWin.center();
    var buttonWinStatusBar = newSyncButtonWin.attachStatusBar();
    var buttonWinToolBar = new dhtmlXToolbarObject(buttonWinStatusBar);
    buttonWinToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    buttonWinToolBar.setAlign("right");
    buttonWinToolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    buttonWinToolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    buttonWinToolBar.attachEvent("onClick", function(id) {
        if (id == "save") {
            if (isEmpty(syncButtonIds)) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            dhtmlxAjax.get(CONSTRUCT_DETAIL_MODEL_URL + "!syncConstructDetails.json?constructId="
                    + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId + "&constructDetailIds="
                    + syncButtonIds, function(loader) {
                dhtmlx.message("保存成功！");
                constructDetailGridLoadData();
                newSyncButtonWin.close();
            });
        } else if (id == "close") {
            newSyncButtonWin.close();
        }
    });
    syncButtonGrid = newSyncButtonWin.attachGrid();
    var buttonTypeCombo = syncButtonGrid.getCombo(3);
    buttonTypeCombo.put("0", "一级按钮");
    buttonTypeCombo.put("1", "按钮组");
    buttonTypeCombo.put("2", "二级按钮");
    initParamGrid(syncButtonGrid, syncButtonGridData);
    syncButtonGrid.attachEvent("onRowDblClicked", function(rId, cInd) {
    });
    syncButtonGrid.attachEvent("onCheck", checkSyncButton);
    syncButtonGrid.enableMultiselect(true);
    var url = CONSTRUCT_DETAIL_MODEL_URL + "!getSyncButtons.json?constructId=" + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId;
    searchNoPage(syncButtonGrid, syncButtonGridData, url);
    syncButtonGrid.checkAll(false);
}
/**
 * 点击按钮列表复选框时的事件
 */
function checkSyncButton(rId, cInd, state) {
    if (state) {
        if (!syncButtonIds) {
            syncButtonIds = rId;
        } else if (syncButtonIds.indexOf(rId) == -1) {
            syncButtonIds += "," + rId;
        }
    } else {
        if (syncButtonIds) {
            syncButtonIds = syncButtonIds.replace("," + rId, "");
            syncButtonIds = syncButtonIds.replace(rId + ",", "");
        }
    }
}