/** 应用定义中的报表定义.*/
function initAppReportWin(win, componentVersionId, f_close) {
    APP_reportcfg(win, app, componentVersionId, f_close);
}
/**
 * 报表自定义统一入口
 * @param win
 * @param app
 * @param componentVersionId
 * @param f_close
 */
function APP_reportcfg(win, app/*{modified:boolean, tableId:string}*/, componentVersionId, f_close /*function*/) {
    var dhxLayout = win.attachLayout("3W");
    var tableId = app.tableId;
    // 工作流配置（tableId为工作流对应的视图ID）
    if (true === app.isWorkflow) {
        tableId = app.viewId;
    }

    dhxLayout.cont.obj._offsetTop = 1;
    dhxLayout.cont.obj._offsetLeft = 1;
    dhxLayout.cont.obj._offsetHeight = -2;
    dhxLayout.cont.obj._offsetWidth = -2;
    dhxLayout.setSizes();

    var sbar = dhxLayout.attachStatusBar();
    var btbar = new dhtmlXToolbarObject(sbar.id);
    btbar.setIconPath(TOOLBAR_IMAGE_PATH);
    btbar.addButton("bottom$save", 0, "保存", "save.gif");
    btbar.addSeparator("bottom$septr$01", 1);
    btbar.addButton("bottom$clear", 2, "清空配置", "delete.gif");
    btbar.addSeparator("bottom$septr$02", 3);
    btbar.addButton("bottom$close", 4, "关闭", "close.gif");
    btbar.setAlign("right");
    btbar.attachEvent("onClick", function(itemId) {
        if ("bottom$close" == itemId) {
            if (typeof f_close == "function")
                f_close();
        } else if ("bottom$save" == itemId) {
            var cnt = rGrid.getRowsNum();
            if (0 == cnt) {
                dhtmlx.alert("请先进行报表配置，再保存！");
                return;
            }
            var rowsValue = "";
            for (var i = 0; i < cnt; i++) {
                var rowId = rGrid.getRowId(i);
                rowsValue += ";" + rowId;
            }
            rowsValue = rowsValue.substring(1);
            var url = AppActionURI.appReport + "!save.json?P_tableId=" + tableId + "&P_menuId=" + app.menuId
                    + "&P_componentVersionId=" + componentVersionId + "&P_rowsValue=" + rowsValue;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("operate_success"));
                } else {
                    dhtmlx.message(getMessage("operate_failure"));
                }
                app.modified = false;
            });
        } else if ("bottom$clear" == itemId) {
            dhtmlx.confirm({
                type : "confirm",
                text : "确定要清空报表配置？",
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        var url = AppActionURI.appReport + "!clear.json?P_tableId=" + tableId
                                + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
                        dhtmlxAjax.get(url, function(loader) {
                            var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                            if (jsonObj.success) {
                                rGrid.forEachRow(function(rowId) {
                                    var name = rGrid.cells(rowId, 0).getValue();
                                    lGrid.addRow(rowId, name);
                                });
                                rGrid.clearAll();
                                dhtmlx.message(getMessage("operate_success"));
                            } else {
                                dhtmlx.message(getMessage("operate_failure"));
                            }
                            app.modified = false;
                        });
                    }
                }
            });
        }
    });

    var lLayout = dhxLayout.cells("a");
    var cLayout = dhxLayout.cells("b");
    var rLayout = dhxLayout.cells("c");
    lLayout.setWidth(240);
    lLayout.hideHeader();
    cLayout.hideHeader();
    rLayout.setWidth(245);
    rLayout.hideHeader();

    var cfg = {
        format : {
            headers : ["<center>报表名称</center>"],
            cols : ["showName"],
            id : ["reportId"],
            colWidths : ["*"],
            colTypes : ["ro"],
            colAligns : ["left"]
        }
    };

    var lGrid = dhxLayout.cells("a").attachGrid();
    var rGrid = dhxLayout.cells("c").attachGrid();
    rGrid.enableDragAndDrop(true);

    var params = "E_model_name=datagrid&E_frame_name=coral&P_tableId=" + tableId + "&P_componentVersionId="
            + componentVersionId + "&P_menuId=" + app.menuId;
    var lurl = AppActionURI.appReport + "!defaultReport.json?" + params;
    var rurl = AppActionURI.appReport + "!defineReport.json?" + params;
    initGridWithoutColumnsAndPageable(lGrid, cfg, lurl);
    initGridWithoutColumnsAndPageable(rGrid, cfg, rurl);

    lGrid.attachEvent("onRowDblClicked", function(rowId, cInd) {
        if (null == rowId)
            return;
        // add row in right grid
        rGrid.addRow(rowId, [lGrid.cells(rowId, 0).getValue()]);
        // delete current row
        lGrid.deleteRow(rowId);
        //
        app.modified = true;
    });
    // 
    rGrid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol) {
        app.modified = true;
    });
    rGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
        if (nValue != oValue)
            app.modified = true;
        return true;
    });
    // create div in document body
    createEmptyDiv("DIV-oparatorArea", 44);
    var buttonForm = new dhtmlXForm("DIV-oparatorArea", getMoveButton());
    buttonForm.attachEvent("onButtonClick", function(id) {
        if ("toRight" == id) {
            var rowIds = lGrid.getSelectedRowId();
            if (null == rowIds)
                return;
            var rowArray = rowIds.split(",");
            for (var i = 0; i < rowArray.length; i++) {
                var name = lGrid.cells(rowArray[i], 0).getValue();
                rGrid.addRow(rowArray[i], [name]);
            }
            lGrid.deleteSelectedRows();
        } else if ("allToRight" == id) {
            lGrid.forEachRow(function(rowId) {
                var name = lGrid.cells(rowId, 0).getValue();
                rGrid.addRow(rowId, [name]);
            });
            lGrid.clearAll();
        } else if ("toLeft" == id) {
            var rowIds = rGrid.getSelectedRowId();
            if (null == rowIds)
                return;
            var rowArray = rowIds.split(",");
            for (var i = 0; i < rowArray.length; i++) {
                var name = rGrid.cells(rowArray[i], 0).getValue();
                lGrid.addRow(rowArray[i], name);
            }
            rGrid.deleteSelectedRows();
        } else if ("allToLeft" == id) {
            rGrid.forEachRow(function(rowId) {
                var name = rGrid.cells(rowId, 0).getValue();
                lGrid.addRow(rowId, name);
            });
            rGrid.clearAll();
        }
        app.modified = true;
    });
    dhxLayout.cells("b").attachObject("DIV-oparatorArea");
}