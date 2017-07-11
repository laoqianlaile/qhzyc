var selfDefineTableTree1, selfDefineTableTreeCell1;
var currentDataDetailWin, winToolbar;
var dataDetailGridsLayoutCell;
var currentDataDetailTableId, currentAuthorityDataId;
var dataDetailGrids;
var authorityDataDetailGridData = {
	format: {
		headers: ["<center>并/或</center>", "<center>左括号</center>", "<center>字段</center>", "<center>运算符</center>", "<center>字段值</center>", "<center>右括号</center>", "<center>操作</center>"],
		cols: ["relation", "leftParenthesis", "columnId", "operator", "value", "rightParenthesis", "operatesArea"],
		colWidths: ["60", "60", "120", "80", "120", "60", "100"],
		colTypes: ["coro", "ed", "coro", "coro", "ed", "ed", "link"],
		colAligns: ["left", "left", "left", "left", "left", "left", "center"]
	}
};
var codeGridData = {
	format: {
		headers: ["<center>编码类型名称</center>", "<center>是否授权</center>", ""],
		cols: ["name", "id"],
		userdata: ["value"],
		colWidths: ["150", "100","*"],
		colTypes: ["ro", "ch", "ro"],
		colAligns: ["center","center", "center"],
		colTooltips: ["true", "true", "false"]
	}
};
/**
 * 初始化数据权限详情Win
 */
function initAuthorityDataDetailWin(win, objectId, objectType, menuId, componentVersionId, authorityDataId, name, tableId) {
	currentAuthorityDataId = authorityDataId;
	currentDataDetailWin = win;
    win.center();
    winToolbar = win.attachToolbar();
    winToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    winToolbar.addLabel("nameLabel", 1, "名称：");
    winToolbar.addInput("name", 2, "", "150");
    winToolbar.setValue("name", name);
    var authorityDataDetailLayout;
    if (authorityDataId) {
        authorityDataDetailLayout = win.attachLayout("1C");
        dataDetailGridsLayoutCell = authorityDataDetailLayout.cells("a");
        currentDataDetailTableId = tableId;
        initDetailGridsLayout(objectId, objectType, menuId, componentVersionId);
    } else {
        authorityDataDetailLayout = win.attachLayout("2U");
        selfDefineTableTreeCell1 = authorityDataDetailLayout.cells("a");
        selfDefineTableTreeCell1.hideHeader();
        selfDefineTableTreeCell1.setWidth("240");
        initSelfDefineTableTree1(objectId, objectType, menuId, componentVersionId);
        dataDetailGridsLayoutCell = authorityDataDetailLayout.cells("b");
    }
    dataDetailGridsLayoutCell.hideHeader();
}
/**
 * 初始化自定义构件中的表树
 */
function initSelfDefineTableTree1(objectId, objectType, menuId, componentVersionId) {
    var SELF_DEFINE_TABLE_TREE_URL = COMMON_AUTHORITY_DATA_DETAIL_URL + "!getTableTree.json?objectId=" + objectId + "&objectType=" + objectType + "&menuId=" + menuId + "&componentVersionId=" + componentVersionId;
    selfDefineTableTree1 = selfDefineTableTreeCell1.attachTree();
    selfDefineTableTree1.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    selfDefineTableTree1.attachEvent("onMouseIn", function(id) {
        selfDefineTableTree1.setItemStyle(id, "background-color:#D5E8FF;");
    });
    selfDefineTableTree1.attachEvent("onMouseOut", function(id) {
        selfDefineTableTree1.setItemStyle(id, "background-color:#FFFFFF;");
    });
    // 物理表分类节点
	var ptItem = loadJson(AppActionURI.tableClassification + "!treeNode.json");
	var vItem = loadJson(AppActionURI.tableClassification + "!treeNodeOfView.json");
	// 这几个分类是需求中固定的
    var treeJson = {id:0, item:[
		{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem},
		{id:"-V", text: "视图", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: vItem}
	]};
    selfDefineTableTree1.setDataMode("json");
    selfDefineTableTree1.enableSmartXMLParsing(true);
    selfDefineTableTree1.setXMLAutoLoading(SELF_DEFINE_TABLE_TREE_URL);
    selfDefineTableTree1.loadJSONObject(treeJson);
    selfDefineTableTree1.attachEvent("onClick", function(nId) {
        var type = selfDefineTableTree1.getUserData(nId, "type");
        if (type == "1") {
        	currentDataDetailTableId = nId;
        	dataDetailGridsLayoutCell.detachObject();
            dataDetailGridsLayoutCell.detachStatusBar();
        	initDetailGridsLayout(objectId, objectType, menuId, componentVersionId);
        } else {
        	currentDataDetailTableId = null;
        	dataDetailGridsLayoutCell.detachObject();
        	dataDetailGridsLayoutCell.detachStatusBar();
        }
    });
}
/**
 * 初始化数据权限详情列表（可能是多个列表）Layout
 */
function initDetailGridsLayout(objectId, objectType, menuId, componentVersionId) {
    var ctlTablesJson = loadJson(COMMON_AUTHORITY_DATA_DETAIL_URL + "!getControlTables.json?tableId=" + currentDataDetailTableId);
    if (typeof(ctlTablesJson) == "string") {
        ctlTablesJson = eval("(" + ctlTablesJson + ")" ); 
    }
    var detailGridCount = ctlTablesJson.length;
    var detailGridsLayout;
    if (detailGridCount > 0) {
        var detailGridCells = new Array(4);
        if (detailGridCount == 1) {
            detailGridsLayout = dataDetailGridsLayoutCell.attachLayout("1C");
            detailGridCells[0] = detailGridsLayout.cells("a");
        } else if (detailGridCount == 2) {
            detailGridsLayout = dataDetailGridsLayoutCell.attachLayout("2E");
            detailGridCells[0] = detailGridsLayout.cells("a");
            detailGridCells[1] = detailGridsLayout.cells("b");
        } else if (detailGridCount == 3) {
            detailGridsLayout = dataDetailGridsLayoutCell.attachLayout("3E");
            detailGridCells[0] = detailGridsLayout.cells("a");
            detailGridCells[1] = detailGridsLayout.cells("b");
            detailGridCells[2] = detailGridsLayout.cells("c");
        } else if (detailGridCount == 4) {
            detailGridsLayout = dataDetailGridsLayoutCell.attachLayout("4E");
            detailGridCells[0] = detailGridsLayout.cells("a");
            detailGridCells[1] = detailGridsLayout.cells("b");
            detailGridCells[2] = detailGridsLayout.cells("c");
            detailGridCells[3] = detailGridsLayout.cells("d");
        }
        dataDetailGrids = new Array(4);
        for (var i in ctlTablesJson) {
            if (detailGridCells[i]) {
                dataDetailGrids[i] = initAuthorityDataDetailGrid(detailGridCells[i], ctlTablesJson[i].id, ctlTablesJson[i].name);
            }
        }
    }
    var statusBar = dataDetailGridsLayoutCell.attachStatusBar();
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar1.addButton("close", 1, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var name = winToolbar.getValue("name");
            if (!name) {
                dhtmlx.alert("请输入名称!");
                return;
            }
            var result = eval("("
                    + loadJson(AUTHORITY_DATA_URL + "!validateFields.json?P_name=" + encodeURIComponent(name)
                            + "&P_authorityDataId=" + currentAuthorityDataId + "&P_objectId=" + objectId
                            + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                            + componentVersionId) + ")");
            if (result.nameExist) {
                dhtmlx.message(getMessage("form_field_exist", "名称"));
                return;
            }
            var rowsNum = 0;
            for (var i in dataDetailGrids) {
                var tempDataDetailGrid = dataDetailGrids[i];
                if (tempDataDetailGrid) {
                    rowsNum += tempDataDetailGrid.getRowsNum();
                }
            }
            if (0 == rowsNum) {
                dhtmlx.alert("请先设置，再保存！");
                return;
            }
            var controlTableIds = "";
            var allGridRows = "";
            for (var i in dataDetailGrids) {
                var tempDataDetailGrid = dataDetailGrids[i];
                if (tempDataDetailGrid && tempDataDetailGrid.getRowsNum() > 0) {
                    var tempRowsNum = tempDataDetailGrid.getRowsNum();
                    var rowsValue = "";
                    var leftParenthesisCount = 0;
                    var rightParenthesisCount = 0;
                    for (var i = 0; i < tempRowsNum; i++) {
                        var rowId = tempDataDetailGrid.getRowId(i);
                        if (tempDataDetailGrid.cells(rowId, 2).getValue() == "") {
                            dhtmlx.alert("【字段】未设值，请先设置值在保存!");
                            return;
                        } else if (tempDataDetailGrid.cells(rowId, 3).getValue() == "") {
                            dhtmlx.alert("【运算符】未设值，请先设置值在保存!");
                            return;
                        } else if (tempDataDetailGrid.cells(rowId, 4).getValue() == "") {
                            dhtmlx.alert("【字段值】未设值，请先设置值在保存!");
                            return;
                        } else if (i != 0 && tempDataDetailGrid.cells(rowId, 0).getValue() == "") {
                            dhtmlx.alert("第" + (i + 1) + "行【条件（并/或）】未设值，请先设置值在保存!");
                            return;
                        } else if (tempDataDetailGrid.cells(rowId, 1).getValue() != "" && tempDataDetailGrid.cells(rowId, 1).getValue() != "(") {
                            dhtmlx.alert("【左括号】值无效!");
                            return;
                        } else if (tempDataDetailGrid.cells(rowId, 5).getValue() != "" && tempDataDetailGrid.cells(rowId, 5).getValue() != ")") {
                            dhtmlx.alert("【右括号】值无效!");
                            return;
                        }
                        if (tempDataDetailGrid.cells(rowId, 1).getValue() == "(") {
                            leftParenthesisCount++;
                        }
                        if (tempDataDetailGrid.cells(rowId, 5).getValue() == ")") {
                            rightParenthesisCount++;
                        }
                        rowsValue += ";" + tempDataDetailGrid.cells(rowId, 0).getValue() + ","
                                + tempDataDetailGrid.cells(rowId, 1).getValue() + ","
                                + tempDataDetailGrid.cells(rowId, 2).getValue() + ","
                                + tempDataDetailGrid.cells(rowId, 3).getValue() + ","
                                + tempDataDetailGrid.cells(rowId, 4).getValue() + ","
                                + tempDataDetailGrid.cells(rowId, 5).getValue();
                    }
                    if (leftParenthesisCount != rightParenthesisCount) {
                        dhtmlx.alert("左右括号个数不对应!");
                        return;
                    }
                    rowsValue = rowsValue.substring(1);
                    if (controlTableIds) {
                        controlTableIds += ",";
                    }
                    controlTableIds += tempDataDetailGrid.tableId;
                    if (allGridRows) {
                        allGridRows += "≡";
                    }
                    allGridRows += tempDataDetailGrid.tableId + "#" + rowsValue;
                }
            }
            // 校验本表权限是否配置过，本表权限只能配置一次
            if (currentDataDetailTableId == controlTableIds) {
                var result = eval("("
                        + loadJson(AUTHORITY_DATA_URL + "!validateSelfTableDataAuth.json?P_authorityDataId="
                                + currentAuthorityDataId + "&P_objectId=" + objectId + "&P_objectType=" + objectType
                                + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId + "&P_tableId="
                                + currentDataDetailTableId) + ")");
                if (result.selfTableDataAuthExist) {
                    dhtmlx.message("本表权限只能配置一次!");
                    return;
                }
            }
            var params = "P_name=" + encodeURIComponent(name) + "&P_authorityDataId=" + currentAuthorityDataId
                    + "&P_objectId=" + objectId + "&P_objectType=" + objectType + "&P_menuId=" + menuId
                    + "&P_componentVersionId=" + componentVersionId + "&P_tableId=" + currentDataDetailTableId
                    + "&P_controlTableIds=" + controlTableIds + "&P_rowsValue=" + allGridRows;
            dhtmlxAjax.post(AUTHORITY_DATA_URL + "!saveAuthorityData.json", params, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("save_success"));
                } else {
                    dhtmlx.message(getMessage("save_failure"));
                }
                currentDataDetailWin.close();
                currentDataDetailWin = null;
                loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId);
            });
        } else if (id == "close") {
            currentDataDetailWin.close();
            currentDataDetailWin = null;
        }
    });
}
/**
 * 初始化数据权限详情列表
 */
function initAuthorityDataDetailGrid(authorityDataDetailGridCell, tableId, tableName) {
    authorityDataDetailGridCell.hideHeader();
    // 初始化列表
    var authorityDataDetailGrid = authorityDataDetailGridCell.attachGrid();
    var filterTypeCombo = authorityDataDetailGrid.getCombo(3);
    filterTypeCombo.put("EQ", "等于");
    filterTypeCombo.put("GT", "大于");
    filterTypeCombo.put("GTE", "大于等于");
    filterTypeCombo.put("LT", "小于");
    filterTypeCombo.put("LTE", "小于等于");
    filterTypeCombo.put("NOT", "不等于");
    filterTypeCombo.put("LIKE", "包含");
    var colTypeCombo = authorityDataDetailGrid.getCombo(0);
    colTypeCombo.put("AND", "并");
    colTypeCombo.put("OR", "或");
    colTypeCombo.put("", "");
    initGridWithoutColumnsAndPageable(authorityDataDetailGrid, authorityDataDetailGridData, null);
    var columnCombo = authorityDataDetailGrid.getCombo(2);
    columnCombo.clear();
    var columnComboOpts = loadJson(AppActionURI.columnDefine + "!comboOfTableColumns.json?P_tableId=" + tableId);
    for (var i in columnComboOpts) {
        columnCombo.put(columnComboOpts[i]["value"], columnComboOpts[i]["text"]);
    }
    authorityDataDetailGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
        if (stage=="2" && cInd=="2") {
            var columnDefine = loadJson(AppActionURI.columnDefine + "/" + nValue + ".json");
            if (typeof columnDefine == 'string') {
                columnDefine = eval("(" + columnDefine + ")");
            }
            if (columnDefine && isNotEmpty(columnDefine.codeTypeCode)) {
                authorityDataDetailGrid.cells(rId, "4").setValue("");
                authorityDataDetailGrid.setCellExcellType(rId, "4", "coro");
                var valueCombo = authorityDataDetailGrid.getCustomCombo(rId, "4");
                valueCombo.clear();
                var valueComboOpts = loadJson(CODE_URL + "!getCodeList.json?codeTypeCode=" + columnDefine.codeTypeCode);
                for (var i in valueComboOpts.data) {
                    valueCombo.put(valueComboOpts.data[i]["value"], valueComboOpts.data[i]["name"]);
                }
            } else {
                authorityDataDetailGrid.setCellExcellType(rId, "4", "ed");
            }
        }
        return true;
    });
    // 初始化工具条
    var authorityDataDetailToolbar = authorityDataDetailGridCell.attachToolbar();
    authorityDataDetailToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    authorityDataDetailToolbar.addText("title", 0, "<b>"+tableName+"</b><font color='#D2E6FF'>-</font>");
    var divId = "placeHolderDiv_" + new Date().getTime();
    authorityDataDetailToolbar.addDiv(divId, 1);
    if (currentAuthorityDataId) {
        document.getElementById(divId).style.width = (660-tableName.length*13) + "px";
    } else {
        document.getElementById(divId).style.width = (510-tableName.length*13) + "px";
    }
    var optionsJson = loadJson(COMMON_AUTHORITY_DATA_DETAIL_URL + "!getAuthorityFilterModelInitDataModel.json");
    if (typeof(optionsJson) == "string") {
        optionsJson = eval("(" + optionsJson + ")" ); 
    }
    if (optionsJson && optionsJson.length > 0) {
        var secondButtons = [];
        for (var i in optionsJson) {
            if (!optionsJson[i].value) {
                continue;
            }
            secondButtons[secondButtons.length] = [optionsJson[i].value, 'obj', optionsJson[i].text];
        }
        authorityDataDetailToolbar.addButtonSelect("add", 2, "新条件", secondButtons, "new.gif");
    } else {
        authorityDataDetailToolbar.addButton("add", 2, "新条件", "new.gif");
    }
    authorityDataDetailToolbar.attachEvent("onClick", function(id) {
        if (id == "add") {
            var rowId = "ID" + new Date().getTime();
            authorityDataDetailGrid.addRow(rowId, ["", "", "", "", "", "",
                    "删除^javascript:deleteAuthorityDataDetailRow(\"" + rowId + "\")^_self"]);
            authorityDataDetailGrid.selectRowById(rowId);
        } else {
            var rowId = "ID" + new Date().getTime();
            authorityDataDetailGrid.addRow(rowId, ["", "", "", "", id, "",
                    "删除^javascript:deleteAuthorityDataDetailRow(\"" + rowId + "\")^_self"]);
            authorityDataDetailGrid.selectRowById(rowId);
        }
    });
    // 加载列表数据
    if (currentAuthorityDataId) {
        loadAuthorityDataDetailGrid(authorityDataDetailGrid, authorityDataDetailGridData, currentAuthorityDataId, tableId);
    }
    authorityDataDetailGrid.tableId = tableId;
    authorityDataDetailGrid.tableName = tableName;
    authorityDataDetailGrid.toolbar = authorityDataDetailToolbar;
    return authorityDataDetailGrid;
}
/**
 * 加载数据权限详情列表
 */
function loadAuthorityDataDetailGrid(authorityDataDetailGrid, authorityDataDetailGridData, authorityDataId, tableId) {
    authorityDataDetailGrid.clearAll();
    if (authorityDataId) {
        var authorityDataDetailGridURL = AUTHORITY_DATA_DETAIL_URL
                + "!getDetailListOfTable.json?authorityDataId=" + authorityDataId + "&tableId=" + tableId;
        loadAuthorityDataGridData(authorityDataDetailGrid, authorityDataDetailGridData, authorityDataDetailGridURL);
    }
}
/**
 * 加载数据权限列表数据（特殊处理）
 */
function loadAuthorityDataGridData(grid, gridcfg, url) {
    if (!grid || !grid.clearAll) return;
    grid.clearAll();
    if (isEmpty(url)) return;
    var datas = null;
    if (typeof url === "string") {
        datas = loadJson(url);
    } else {
        datas = url;
    }
    if (undefined != datas.rows) {
        grid.parse(datas, "json");
        return;
    }
    var rows = datas.data ? datas.data : datas;
    for (var i in rows) {
        if (!rows[i].columnId) {
            continue;
        }
        var columnDefine = loadJson(AppActionURI.columnDefine + "/" + rows[i].columnId + ".json");
        if (typeof columnDefine == 'string') {
            columnDefine = eval("(" + columnDefine + ")");
        }
        grid.addRow(rows[i].id, rows[i].relation+","+rows[i].leftParenthesis+","+rows[i].columnId+","+rows[i].operator+","+rows[i].value+","+rows[i].rightParenthesis+","+rows[i].operatesArea, i);
        if (columnDefine && isNotEmpty(columnDefine.codeTypeCode)) {
            grid.setCellExcellType(rows[i].id, "4", "coro");
            var valueCombo = grid.getCustomCombo(rows[i].id, "4");
            valueCombo.clear();
            var valueComboOpts = loadJson(CODE_URL + "!getCodeList.json?codeTypeCode=" + columnDefine.codeTypeCode);
            for (var j in valueComboOpts.data) {
                valueCombo.put(valueComboOpts.data[j]["value"], valueComboOpts.data[j]["name"]);
            }
            grid.cells(rows[i].id, "4").setValue(rows[i].value);
        }
    }
}
/**
 * 删除数据权限详情行
 */
function deleteAuthorityDataDetailRow(id) {
    for (var i in dataDetailGrids) {
        var authorityDataDetailGrid = dataDetailGrids[i];
        if (authorityDataDetailGrid && authorityDataDetailGrid.getRowIndex(id) > -1) {
            if (id.indexOf("ID") != -1) {
                authorityDataDetailGrid.deleteRow(id);
            } else {
                authorityDataDetailGrid.deleteRow(id);
                //dhtmlxAjax.get(AUTHORITY_DATA_DETAIL_URL + "/" + id + "?_method=delete", function(loader) {});
            }
        }
    }
}
/**
 * 初始化编码类型树
 */
function initCodeTypeTree(dhxLayoutCell, objectId, objectType, menuId, componentVersionId) {
    codeTree = dhxLayoutCell.attachTree();
    codeTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    codeTree.attachEvent("onMouseIn", function(id) {
    	codeTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    codeTree.attachEvent("onMouseOut", function(id) {
    	codeTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    codeTree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
	var codeTreeJson = {id:0, item:[{id:-1,text:"编码类型定义",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
	codeTree.setDataMode("json");
	codeTree.enableSmartXMLParsing(true);
	codeTree.setXMLAutoLoading(CODE_TYPE_URL + "!getCodeTypeDataTree.json?E_model_name=tree&F_in=name,child&view=dhtmlx");
	codeTree.loadJSONObject(codeTreeJson);
	codeTree.refreshItem("-1");
	codeTree.attachEvent("onClick", function(id, prevId) {
		if (codeTree.getAttribute(id, "prop2") == "1") {
            authorityCodeGridToolbar.disableItem("save");
        }else {
        	authorityCodeGridToolbar.enableItem("save");
        }
		
    	var cUrl = CODE_URL + "!search.json?Q_EQ_codeTypeCode="+id+"&P_orders=showOrder&Q_NULL_parentId&E_model_name=jqgrid&F_in=id,name,value";
    	loadGridData(codeGrid,codeGridData,cUrl);
    	var url = AUTHORITY_CODE_URL + "!getAuthorityCode.json?codeTypeCode=" + id 
		+ "&objectId=" + objectId + "&objectType=" + objectType
		+ "&menuId=" + menuId + "&componentVersionId=" + componentVersionId;
    	var authorityCode = loadJson(url);
		var codeString = authorityCode.codeJson;
		var arrayString = codeString.split(",");
		codeGrid.forEachRow(function(rowId) {
			var code = codeGrid.getUserData(rowId, "value");
			for (var i=0; i < arrayString.length; i++ ) {
				if(code == arrayString[i] ) {
					codeGrid.cells(rowId, 1).setValue("0");
					break;
				}
			}
		});
    });
}
/**
 * 初始化编码类型列表
 */
function initCodeGrid(dhxLayoutCell) {
	codeGrid = dhxLayoutCell.attachGrid();
	codeGrid.setImagePath(IMAGE_PATH);
    codeGrid.setHeader(codeGridData.format.headers.toString());
    codeGrid.setInitWidths(codeGridData.format.colWidths.toString());
    codeGrid.setColTypes(codeGridData.format.colTypes.toString());
    codeGrid.setColAlign(codeGridData.format.colAligns.toString());
    if (codeGridData.format.colTooltips) {
        codeGrid.enableTooltips(codeGridData.format.colTooltips.toString());
    }
    codeGrid.setSkin(Skin);
    codeGrid.init();
    codeGrid.enableMultiselect(true);
}
