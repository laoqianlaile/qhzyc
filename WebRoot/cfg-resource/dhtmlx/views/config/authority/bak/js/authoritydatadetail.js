var authorityDataDetailGrid, selfDefineTableTree1, selfDefineTableTreeCell1, authorityDataDetailGridCell;
var authorityDataDetailGridToolbar;
var currentDataDetailTableId, currentAuthorityDataId;
var authorityDataDetailGridData = {
	format: {
		headers: ["<center>并/或</center>", "<center>左括号</center>", "<center>属性</center>", "<center>运算符</center>", "<center>属性值</center>", "<center>右括号</center>", "<center>操作</center>"],
		cols: ["relation", "leftParenthesis", "columnId", "operator", "value", "rightParenthesis", "operatesArea"],
		colWidths: ["50", "50", "100", "80", "100", "50", "100"],
		colTypes: ["coro", "ed", "coro", "coro", "ed", "ed", "link"],
		colAligns: ["left", "left", "left", "left", "left", "left", "left"]
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
function initAuthorityDataDetailWin(win, menuId, componentVersionId, authorityDataId, name) {
	currentAuthorityDataId = authorityDataId;
	win.setText("数据权限维护");
    win.center();
    var winToolbar = win.attachToolbar();
    winToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    winToolbar.addLabel("nameLabel", 1, "名称：");
    winToolbar.addInput("name", 2, "", "150");
    winToolbar.setValue("name", name);
    var authorityDataDetailLayout = win.attachLayout("2U");
    selfDefineTableTreeCell1 = authorityDataDetailLayout.cells("a");
    selfDefineTableTreeCell1.hideHeader();
    selfDefineTableTreeCell1.setWidth("240");
    initSelfDefineTableTree1(menuId, componentVersionId);
    authorityDataDetailGridCell = authorityDataDetailLayout.cells("b");
    authorityDataDetailGridCell.hideHeader();
    authorityDataDetailGridToolbar = authorityDataDetailGridCell.attachToolbar();
    authorityDataDetailGridToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    authorityDataDetailGridToolbar.addButton("add", 1, "新条件", "new.gif");
    authorityDataDetailGridToolbar.disableItem("add");
    authorityDataDetailGridToolbar.addDiv("form$combox", 0);
    authorityDataDetailGridToolbar.disableItem("form$combox");
    var optionsJson = loadJson(contextPath + "/authority/authority-data-detail!getAuthorityFilterModelInitDataModel.json");
    if( typeof(optionsJson) == "string") {
    	optionsJson = eval("(" + optionsJson + ")" ); 
    }
    var formJson = [
        {type: "combo", name: "userCombox", className: "dhx_toolbar_form", style:"font-size:11px;", width: 100, 
        	options:optionsJson}
    ];
 	var form = new dhtmlXForm("form$combox",formJson);
 	form.attachEvent("onChange", function(itemId, value) {
 		var type = selfDefineTableTree1.getUserData(currentDataDetailTableId, "type");
        if (type == "1") {
	 		var rowId = "ID" + new Date().getTime();
	 		var v = form.getItemValue("userCombox");
	        authorityDataDetailGrid.addRow(rowId, ["", "", "", "", v, "",
	                "删除^javascript:deleteAuthorityDataDetailRow(\"" + rowId + "\")^_self"]);
	        authorityDataDetailGrid.selectRowById(rowId);
        }
 	} );
    authorityDataDetailGridToolbar.attachEvent("onClick", function(id) {
        if (id == "add") {
            var rowId = "ID" + new Date().getTime();
            authorityDataDetailGrid.addRow(rowId, ["", "", "", "", "", "",
                    "删除^javascript:deleteAuthorityDataDetailRow(\"" + rowId + "\")^_self"]);
            authorityDataDetailGrid.selectRowById(rowId);
        }
    });

    authorityDataDetailGrid = authorityDataDetailGridCell.attachGrid();
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
	
	var rNum = authorityDataDetailGrid.getRowsNum();
	var updNum;
	
	var statusBar = authorityDataDetailGridCell.attachStatusBar();
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "保存");
    toolBar1.addButton("close", 1, "关闭");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var rowsNum = authorityDataDetailGrid.getRowsNum();
            if (null == rowsNum || 0 == rowsNum) {
                dhtmlx.alert("请先设置，再保存！");
                return;
            }
            var name = winToolbar.getValue("name");
            if (!name) {
                dhtmlx.alert("请输入名称!");
                return;
            } else {
                var rowsValue = "";
                var leftParenthesisCount = 0;
                var rightParenthesisCount = 0;
                for (var i = 0; i < rowsNum; i++) {
                    var rowId = authorityDataDetailGrid.getRowId(i);
                    if (authorityDataDetailGrid.cells(rowId, 2).getValue() == "") {
                        dhtmlx.alert("【属性】未设值，请先设置值在保存!");
                        return;
                    } else if (authorityDataDetailGrid.cells(rowId, 3).getValue() == "") {
                        dhtmlx.alert("【运算符】未设值，请先设置值在保存!");
                        return;
                    } else if (authorityDataDetailGrid.cells(rowId, 4).getValue() == "") {
                        dhtmlx.alert("【属性值】未设值，请先设置值在保存!");
                        return;
                    } else if (i != 0 && authorityDataDetailGrid.cells(rowId, 0).getValue() == "") {
                        dhtmlx.alert("第" + (i + 1) + "行【条件（并/或）】未设值，请先设置值在保存!");
                        return;
                    } else if (authorityDataDetailGrid.cells(rowId, 1).getValue() != "" && authorityDataDetailGrid.cells(rowId, 1).getValue() != "(") {
                    	dhtmlx.alert("【左括号】值无效!");
                        return;
                    } else if (authorityDataDetailGrid.cells(rowId, 5).getValue() != "" && authorityDataDetailGrid.cells(rowId, 5).getValue() != ")") {
                    	dhtmlx.alert("【右括号】值无效!");
                        return;
                    }
                    if (authorityDataDetailGrid.cells(rowId, 1).getValue() == "(") {
                    	leftParenthesisCount++;
                    }
                    if (authorityDataDetailGrid.cells(rowId, 5).getValue() == ")") {
                    	rightParenthesisCount++;
                    }
                    rowsValue += ";" + authorityDataDetailGrid.cells(rowId, 0).getValue() + ","
                    		+ authorityDataDetailGrid.cells(rowId, 1).getValue() + ","
                            + authorityDataDetailGrid.cells(rowId, 2).getValue() + ","
                            + authorityDataDetailGrid.cells(rowId, 3).getValue() + ","
                            + authorityDataDetailGrid.cells(rowId, 4).getValue() + ","
                            + authorityDataDetailGrid.cells(rowId, 5).getValue();
                }
                if (leftParenthesisCount != rightParenthesisCount) {
                    dhtmlx.alert("左右括号个数不对应!");
                    return;
                }
                rowsValue = rowsValue.substring(1);
                var result = eval("("
                        + loadJson(contextPath + "/authority/authority-data!validateFields.json?name="
                                + encodeURIComponent(name) + "&P_authorityDataId=" + currentAuthorityDataId
                                + "&P_componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "名称"));
                    return;
                }
                var url = encodeURI(contextPath
                        + "/authority/authority-data-detail!saveAuthorityDataDetails.json?P_componentVersionId="
                        + componentVersionId + "&P_name=" + name + "&P_tableId=" + currentDataDetailTableId
                        + "&P_authorityDataId=" + currentAuthorityDataId + "&P_rowsValue=" + rowsValue);
                dhtmlxAjax.get(url, function(loader) {
                    var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof jsonObj == 'string') {
                        jsonObj = eval("(" + jsonObj + ")");
                    }
                    if (jsonObj.success) {
                    	if (!currentAuthorityDataId) {
                    	    currentAuthorityDataId = jsonObj.authorityDataId;
                    	}
                        dhtmlx.message(getMessage("save_success"));
                    } else {
                        dhtmlx.message(getMessage("save_failure"));
                    }
                });
            }
        } else if (id == "close") {
            win.close();
        }
    });
}
/**
 * 初始化自定义构件中的表树
 */
function initSelfDefineTableTree1(menuId, componentVersionId) {
    var SELF_DEFINE_TABLE_TREE_URL = contextPath + "/authority/authority-data-detail!getTableTree.json?menuId=" + menuId + "&componentVersionId=" + componentVersionId;
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
        	authorityDataDetailGridToolbar.enableItem("add");
        	authorityDataDetailGridToolbar.enableItem("form$combox");
        	var columnCombo = authorityDataDetailGrid.getCombo(2);
        	columnCombo.clear();
		    var columnComboOpts = loadJson(AppActionURI.columnDefine + "!comboOfTableColumns.json?P_tableId=" + nId);
		    for (var i in columnComboOpts) {
		        columnCombo.put(columnComboOpts[i]["value"], columnComboOpts[i]["text"]);
		    }
            loadAuthorityDataDetailGrid(authorityDataDetailGrid, authorityDataDetailGridData, currentAuthorityDataId, nId);
        } else {
        	currentDataDetailTableId = null;
        	authorityDataDetailGridToolbar.disableItem("add");
        	authorityDataDetailGridToolbar.disableItem("form$combox");
            authorityDataDetailGrid.clearAll();
        }
    });
}
/**
 * 加载数据权限详情列表
 */
function loadAuthorityDataDetailGrid(authorityDataDetailGrid, authorityDataDetailGridData, authorityDataId, tableId) {
    authorityDataDetailGrid.clearAll();
    if (authorityDataId) {
        var authorityDataDetailGridURL = contextPath
                + "/authority/authority-data-detail!getDetailListOfTable.json?authorityDataId=" + authorityDataId + "&tableId=" + tableId;
        loadGridData(authorityDataDetailGrid, authorityDataDetailGridData, authorityDataDetailGridURL);
    }
}
/**
 * 删除数据权限详情行
 */
function deleteAuthorityDataDetailRow(id) {
    if (id.indexOf("ID") != -1) {
        authorityDataDetailGrid.deleteRow(id);
    } else {
        dhtmlxAjax.get(contextPath + "/authority/authority-data-detail" + "/" + id + "?_method=delete", function(loader) {
            authorityDataDetailGrid.deleteRow(id);
        });
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
	codeTree.setXMLAutoLoading(contextPath + "/code/code-type!getCodeTypeDataTree.json?E_model_name=tree&F_in=name,child&view=dhtmlx");
	codeTree.loadJSONObject(codeTreeJson);
	codeTree.refreshItem("-1");
	codeTree.attachEvent("onClick", function(id, prevId) {
		if (codeTree.getAttribute(id, "prop2") == "1") {
            authorityCodeGridToolbar.disableItem("save");
        }else {
        	authorityCodeGridToolbar.enableItem("save");
        }
		
    	var cUrl = contextPath + "/code/code!search.json?Q_EQ_codeTypeCode="+id+"&P_orders=showOrder&Q_NULL_parentId&E_model_name=jqgrid&F_in=id,name,value";
    	loadGridData(codeGrid,codeGridData,cUrl);
    	var url = contextPath + "/authority/authority-code!getAuthorityCode.json?codeTypeCode=" + id 
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
