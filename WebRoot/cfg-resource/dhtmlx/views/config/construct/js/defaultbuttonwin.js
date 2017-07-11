var defaultButtonGrid;
var defaultButtonGridData = {
    format : {
        headers : ["", "<center>按钮名称</center>", "<center>显示名称</center>", ""],
        cols : ["id", "buttonName", "buttonDisplayName"],
        colWidths : ["30", "300", "300", "*"],
        colTypes : ["ch", "ro", "ro", "ro"],
        colAligns : ["left", "left", "left", "left"]
    }
};
var checkedButtonCodes;
/**
 * 初始化预置按钮窗口
 */
function initDefaultButtonWin(newDefaultButtonWin) {
    checkedButtonCodes = "";
    newDefaultButtonWin.setModal(true);
    newDefaultButtonWin.setText("预置按钮");
    newDefaultButtonWin.center();
    var buttonWinStatusBar = newDefaultButtonWin.attachStatusBar();
    var buttonWinToolBar = new dhtmlXToolbarObject(buttonWinStatusBar);
    buttonWinToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    buttonWinToolBar.setAlign("right");
    buttonWinToolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    buttonWinToolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    buttonWinToolBar.attachEvent("onClick", function(id) {
        if (id == "save") {
            if (isEmpty(checkedButtonCodes)) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            dhtmlxAjax.get(CONSTRUCT_DETAIL_MODEL_URL + "!saveCheckedDefaultButtons.json?constructId="
                    + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId + "&buttonCodes="
                    + checkedButtonCodes.replace(/#/g, ""), function(loader) {
                dhtmlx.message("保存成功！");
                constructDetailGridLoadData();
                newDefaultButtonWin.close();
            });
        } else if (id == "close") {
            newDefaultButtonWin.close();
        }
    });
    defaultButtonGrid = newDefaultButtonWin.attachGrid();
    initParamGrid(defaultButtonGrid, defaultButtonGridData);
    defaultButtonGrid.attachEvent("onCheck", checkButton);
    defaultButtonGrid.enableMultiselect(true);
    var url = CONSTRUCT_DETAIL_MODEL_URL + "!getDefaultButtons.json?reserveZoneId=" + currentReserveZoneId;
    searchNoPage(defaultButtonGrid, defaultButtonGridData, url);
    defaultButtonGrid.checkAll(false);
    var checkedUrl = CONSTRUCT_DETAIL_MODEL_URL + "!getCheckedDefaultButtons.json?constructId="
            + currentConstructIdOfTree + "&reserveZoneId=" + currentReserveZoneId;
    var checkedData = loadJson(checkedUrl);
    for (var i in checkedData) {
        defaultButtonGrid.cells(checkedData[i].buttonCode, 0).setValue("1");
        if (!checkedButtonCodes) {
            checkedButtonCodes = "#" + checkedData[i].buttonCode + "#";
        } else if (checkedButtonCodes.indexOf(checkedData[i].buttonCode) == -1) {
            // 防止按钮名称1为按钮名称2的一部分，取消选中时字符串replace会出错，按钮前后各加一个#
            checkedButtonCodes += ",#" + checkedData[i].buttonCode + "#";
        }
    }
}
/**
 * 点击按钮列表复选框时的事件
 */
function checkButton(rId, cInd, state) {
    if (state) {
        if (!checkedButtonCodes) {
            checkedButtonCodes = rId;
        } else if (checkedButtonCodes.indexOf("#" + rId + "#") == -1) {
            checkedButtonCodes += ",#" + rId + "#";
        }
    } else {
        if (checkedButtonCodes) {
            checkedButtonCodes = checkedButtonCodes.replace(",#" + rId + "#", "");
            checkedButtonCodes = checkedButtonCodes.replace("#" + rId + "#,", "");
            checkedButtonCodes = checkedButtonCodes.replace("#" + rId + "#", "");
        }
    }
}