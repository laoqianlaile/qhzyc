var filterDetailGrid, filterDetailGridToolbar, currentFilterComponentVersionId, currentTableId;
var filterDetailGridData = {
    format: {
        headers: ["<center>并/或</center>", "<center>左括号</center>", "<center>属性</center>", "<center>运算符</center>", "<center>属性值</center>", "<center>右括号</center>", "<center>操作</center>"],
        cols: ["relation", "leftParenthesis", "columnId", "operator", "value", "rightParenthesis", "operatesArea"],
        colWidths: ["50", "50", "100", "80", "100", "50", "100"],
        colTypes: ["coro", "ed", "coro", "coro", "ed", "ed", "link"],
        colAligns: ["left", "left", "left", "left", "left", "left", "left"]
    }
};
/**
 * 配置数据过滤
 * @param topComVersionId 上层组合构件版本ID
 */
function configFilter(topComVersionId) {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var filterWin = dhxWins.createWindow("filterWin", 0, 0, 800, 400);
    filterWin.setModal(true);
    filterWin.setText("配置过滤条件");
    filterWin.center();
    filterWin.button("park").hide();
    filterWin.button("minmax1").hide();
    filterWin.button("minmax2").hide();
    var filterLayout = filterWin.attachLayout("2U");
    var filterTreeCell = filterLayout.cells("a");
    filterTreeCell.hideHeader();
    filterTreeCell.setWidth("240");
    initFilterTree(filterTreeCell, topComVersionId);
    var filterDetailGridCell = filterLayout.cells("b");
    filterDetailGridCell.hideHeader();
    filterDetailGridToolbar = filterDetailGridCell.attachToolbar();
    filterDetailGridToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    filterDetailGridToolbar.addButton("add", 1, "新条件", "new.gif");
    filterDetailGridToolbar.disableItem("add");
    filterDetailGridToolbar.attachEvent("onClick", function(id) {
        if (id == "add") {
            var rowId = "ID" + new Date().getTime();
            filterDetailGrid.addRow(rowId, ["", "", "", "", "", "",
                    "删除^javascript:deleteFilterDetailRow(\"" + rowId + "\")^_self"]);
            filterDetailGrid.selectRowById(rowId);
        }
    });

    filterDetailGrid = filterDetailGridCell.attachGrid();
    var filterTypeCombo = filterDetailGrid.getCombo(3);
    filterTypeCombo.put("EQ", "等于");
    filterTypeCombo.put("GT", "大于");
    filterTypeCombo.put("GTE", "大于等于");
    filterTypeCombo.put("LT", "小于");
    filterTypeCombo.put("LTE", "小于等于");
    filterTypeCombo.put("NOT", "不等于");
    filterTypeCombo.put("LIKE", "包含");
    var colTypeCombo = filterDetailGrid.getCombo(0);
    colTypeCombo.put("AND", "并");
    colTypeCombo.put("OR", "或");
    colTypeCombo.put("", "");
    initGridWithoutColumnsAndPageable(filterDetailGrid, filterDetailGridData, null);
    var statusBar = filterDetailGridCell.attachStatusBar();
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "保存");
    toolBar1.addButton("close", 1, "关闭");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var rowsNum = filterDetailGrid.getRowsNum();
            if (null == rowsNum || 0 == rowsNum) {
                dhtmlx.alert("请先设置，再保存！");
                return;
            }
            var rowsValue = "";
            var leftParenthesisCount = 0;
            var rightParenthesisCount = 0;
            for (var i = 0; i < rowsNum; i++) {
                var rowId = filterDetailGrid.getRowId(i);
                if (filterDetailGrid.cells(rowId, 2).getValue() == "") {
                    dhtmlx.alert("【属性】未设值，请先设置值在保存!");
                    return;
                } else if (filterDetailGrid.cells(rowId, 3).getValue() == "") {
                    dhtmlx.alert("【运算符】未设值，请先设置值在保存!");
                    return;
                } else if (filterDetailGrid.cells(rowId, 4).getValue() == "") {
                    dhtmlx.alert("【属性值】未设值，请先设置值在保存!");
                    return;
                } else if (i != 0 && filterDetailGrid.cells(rowId, 0).getValue() == "") {
                    dhtmlx.alert("第" + (i + 1) + "行【条件（并/或）】未设值，请先设置值在保存!");
                    return;
                } else if (filterDetailGrid.cells(rowId, 1).getValue() != "" && filterDetailGrid.cells(rowId, 1).getValue() != "(") {
                    dhtmlx.alert("【左括号】值无效!");
                    return;
                } else if (filterDetailGrid.cells(rowId, 5).getValue() != "" && filterDetailGrid.cells(rowId, 5).getValue() != ")") {
                    dhtmlx.alert("【右括号】值无效!");
                    return;
                }
                if (filterDetailGrid.cells(rowId, 1).getValue() == "(") {
                    leftParenthesisCount++;
                }
                if (filterDetailGrid.cells(rowId, 5).getValue() == ")") {
                    rightParenthesisCount++;
                }
                rowsValue += ";" + filterDetailGrid.cells(rowId, 0).getValue() + ","
                        + filterDetailGrid.cells(rowId, 1).getValue() + ","
                        + filterDetailGrid.cells(rowId, 2).getValue() + ","
                        + filterDetailGrid.cells(rowId, 3).getValue() + ","
                        + filterDetailGrid.cells(rowId, 4).getValue() + ","
                        + filterDetailGrid.cells(rowId, 5).getValue();
            }
            if (leftParenthesisCount != rightParenthesisCount) {
                dhtmlx.alert("左右括号个数不对应!");
                return;
            }
            rowsValue = rowsValue.substring(1);
            var params = "&P_topComVersionId=" + topComVersionId
                    + "&P_componentVersionId=" + currentFilterComponentVersionId + "&P_tableId=" + currentTableId
                    + "&P_rowsValue=" + rowsValue;
            dhtmlxAjax.post(CONSTRUCT_FILTER_MODEL_URL + "!saveConstructFilter.json", params, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("save_success"));
                } else {
                    dhtmlx.message(getMessage("save_failure"));
                }
            });
        } else if (id == "close") {
            filterWin.close();
        }
    });
}
/**
 * 初始化自定义构件中的表树
 */
function initFilterTree(filterTreeCell, topComVersionId) {
    var TREE_URL = CONSTRUCT_FILTER_DETAIL_MODEL_URL + "!getFilterTree.json?E_model_name=tree&P_topComVersionId=" + topComVersionId;
    var filterTree = filterTreeCell.attachTree();
    filterTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    filterTree.attachEvent("onMouseIn", function(id) {
        filterTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    filterTree.attachEvent("onMouseOut", function(id) {
        filterTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var treeJson = {id:0, item:[
        {id:"-1", text: currentComponentVersionAlias, im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open: true, item : []}
    ]};
    filterTree.setDataMode("json");
    filterTree.enableSmartXMLParsing(true);
    filterTree.setXMLAutoLoading(TREE_URL);
    filterTree.loadJSONObject(treeJson);
    filterTree.refreshItem("-1");
    filterTree.attachEvent("onClick", function(nId) {
        var type = filterTree.getAttribute(nId, "prop1");
        currentFilterComponentVersionId = filterTree.getAttribute(nId, "prop0");
        if (type == "LogicTable" || type == "PhysicalTable") {
            currentTableId = filterTree.getAttribute(nId, "prop2");
            filterDetailGridToolbar.enableItem("add");
            var columnCombo = filterDetailGrid.getCombo(2);
            columnCombo.clear();
            var columnComboOpts = loadJson(AppActionURI.columnDefine + "!comboOfTableColumns.json?P_tableId=" + currentTableId);
            for (var i in columnComboOpts) {
                columnCombo.put(columnComboOpts[i]["value"], columnComboOpts[i]["text"]);
            }
            loadFilterDetailGrid(filterDetailGrid, filterDetailGridData, topComVersionId, currentFilterComponentVersionId, currentTableId);
        } else {
            filterDetailGridToolbar.disableItem("add");
            filterDetailGrid.clearAll();
        }
    });
}
/**
 * 加载数据过滤详情列表
 */
function loadFilterDetailGrid(filterDetailGrid, filterDetailGridData, topComVersionId, componentVersionId, tableId) {
    filterDetailGrid.clearAll();
    var filterDetailGridURL = CONSTRUCT_FILTER_DETAIL_MODEL_URL + "!getConstructFilterDetailList.json?topComVersionId=" + topComVersionId + "&componentVersionId=" + componentVersionId + "&tableId=" + tableId;
    loadGridData(filterDetailGrid, filterDetailGridData, filterDetailGridURL);
}
/**
 * 删除数据过滤详情行
 */
function deleteFilterDetailRow(id) {
    if (id.indexOf("ID") != -1) {
        filterDetailGrid.deleteRow(id);
    } else {
        dhtmlxAjax.get(CONSTRUCT_FILTER_DETAIL_MODEL_URL + "/" + id + "?_method=delete", function(loader) {
            filterDetailGrid.deleteRow(id);
        });
    }
}