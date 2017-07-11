var authDataLayout, dataTopToolbar, dataBottomToolbar, authorityDataGrid, authorityDataGridData, authorityWins;
/**
 * 初始化数据权限列表
 */
function initAuthorityDataWin(win, objectId, objectType, menuId, componentVersionId) {
	win.setText("数据权限配置");
    win.center();
    win.detachToolbar();
    var tbar1 = win.attachToolbar();
    tbar1.setIconsPath(TOOLBAR_IMAGE_PATH);
    tbar1.addDiv("top$searchTextdiv2", 0, "left");
    var dataControlForm = initDataForm(objectId, objectType, menuId, componentVersionId);
    authDataLayout = win.attachLayout("1C");
    authDataLayout.cells("a").hideHeader();
    authorityDataGrid = authDataLayout.cells("a").attachGrid();
    if (useTreePower) {
        authorityDataGridData = {
            format : {
                headers : ["<center>名称</center>", "<center>控制表</center>", "<center>控制条件表</center>", "<center>状态</center>"],
                cols: ["name", "tableName", "controlTableNames", "status"],
                userdata: ["tableId"],
                colWidths : ["200", "200", "250", "100"],
                colTypes : ["ro", "ro", "ro", "ro"],
                colAligns : ["left", "left", "left", "left"]
            }
        };
    } else {
        authorityDataGridData = {
            format : {
                headers : ["<center>名称</center>", "<center>控制表</center>", "<center>控制条件表</center>"],
                cols: ["name", "tableName", "controlTableNames"],
                userdata: ["tableId"],
                colWidths : ["200", "200", "350"],
                colTypes : ["ro", "ro", "ro"],
                colAligns : ["left", "left", "left"]
            }
        };
    }
    initGridWithoutPageable(authorityDataGrid, authorityDataGridData, null);
    loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId);

    dataTopToolbar = authDataLayout.cells("a").attachToolbar();
    dataTopToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    dataTopToolbar.addButton("add", 1, "新增", "new.gif");
    dataTopToolbar.addButton("update", 2, "修改", "update.gif");
    dataTopToolbar.addButton("delete", 3, "删除", "delete.gif");
    dataTopToolbar.attachEvent("onClick", function(id) {
        if (authorityWins && (authorityWins.isWindow("newAuthorityData")||authorityWins.isWindow("newAuthorityData"))) {
            return;
        }
        if (id == "add") {
            var h = 500;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var newWin = createAuthorityWindow({
                id : "newAuthorityData",
                title : "新增数据权限",
                width : 900,
                height : h
            });
            initAuthorityDataDetailWin(newWin, objectId, objectType, menuId, componentVersionId, "", "", "");
        } else if (id == "update") {
            var selectIds = authorityDataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var name = authorityDataGrid.cellById(selectIds, 0).getValue();
            var tableId = authorityDataGrid.getUserData(selectIds, "tableId");
            var h = 500;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var updateWin = createAuthorityWindow({
                id : "updateAuthorityData",
                title : "修改数据权限",
                width : 800,
                height : h
            });
            initAuthorityDataDetailWin(updateWin, objectId, objectType, menuId, componentVersionId, selectIds, name, tableId);
        } else if (id == "delete") {
            var selectIds = authorityDataGrid.getSelectedRowId();
            if (!selectIds) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        var DELETE_URL = AUTHORITY_DATA_URL + "/" + selectIds + "?_method=delete";
                        dhtmlxAjax.get(DELETE_URL, function(loader) {
                            dhtmlx.message(getMessage("delete_success"));
                            loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId);
                        });
                    }
                }
            });
        }
    });
	
	var statusBar = win.attachStatusBar();
    dataBottomToolbar = new dhtmlXToolbarObject(statusBar);
    dataBottomToolbar.setAlign("right");
    dataBottomToolbar.addButton("save", 0, "保存");
    dataBottomToolbar.addButton("copyAuth", 1, "复制(其他角色)");
    dataBottomToolbar.addButton("close", 2, "关闭");
    dataBottomToolbar.attachEvent("onClick", function(id) {
        if (authorityWins && (authorityWins.isWindow("newAuthorityData")||authorityWins.isWindow("newAuthorityData"))) {
            return;
        }
        if (id == "save") {
            var allRowIds = authorityDataGrid.getAllRowIds();
            if (allRowIds) {
                dhtmlx.confirm({
                    type:"confirm",
                    text: "使用全部数据可访问的方式，将会删除已配置好的数据权限，确定吗？",
                    ok: "确定",
                    cancel: "取消",
                    callback: function(flag) {
                        if (flag) {
                            var url = AUTHORITY_DATA_URL + "!deleteAuthorityData.json?P_objectId=" + objectId
                                    + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                                    + componentVersionId;
                            dhtmlxAjax.get(url, function(loader) {
                                loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId);
                            });
                        }
                    }
                });
            }
            dhtmlx.message(getMessage("save_success"));
        } else if (id == "copyAuth") {
            var selectIds = authorityDataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            var h = 450;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var authwin = createAuthorityWindow({
                id : "copyAuthorityData",
                title : "",
                width : 500,
                height : h
            });
            initDataCopyAuth(authwin, objectId, objectType, menuId, componentVersionId, selectIds);
        } else if (id == "close") {
            win.close();
        }
    });
    if (authorityDataGrid.getRowsNum() > 0) {
        dataControlForm.setItemValue("control", "1");
        dataBottomToolbar.disableItem("save");
    } else {
        dataControlForm.setItemValue("control", "0");
        dataTopToolbar.disableItem("add");
        dataTopToolbar.disableItem("update");
        dataTopToolbar.disableItem("delete");
        dataBottomToolbar.disableItem("copyAuth");
    }
}
/**
 * 是否配置数据权限
 * @returns {dhtmlXForm}
 */
function initDataForm(objectId, objectType, menuId, componentVersionId) {
    var sformJson = [
        {type: "radio", name: "control", value: "0", className: "dhx_toolbar_form", label: "全部数据可访问", position: "label-right", labelAlign: "left", checked: false, labelWidth: 100},
        {type: "newcolumn"},
        {type: "radio", name: "control", value: "1", className: "dhx_toolbar_form", label: "控制", position: "label-right", labelAlign: "left", checked: false, labelWidth: 100}
    ];
    var form = new dhtmlXForm("top$searchTextdiv2", sformJson);
    form.attachEvent("onChange", function(id, value, state) {
        if (id == "control") {
            if (value == "0") {
                dataTopToolbar.disableItem("add");
                dataTopToolbar.disableItem("update");
                dataTopToolbar.disableItem("delete");
                dataBottomToolbar.disableItem("copyAuth");
                dataBottomToolbar.enableItem("save");
            } else {
                dataTopToolbar.enableItem("add");
                dataTopToolbar.enableItem("update");
                dataTopToolbar.enableItem("delete");
                dataBottomToolbar.enableItem("copyAuth");
                dataBottomToolbar.disableItem("save");
            }
        }
    });
    return form;
}
/**
 * 加载数据权限列表
 */
function loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId,
        componentVersionId) {
    if (!authorityDataGrid || !authorityDataGrid.clearAll)
        return;
    var authorityDataGridURL = AUTHORITY_DATA_URL + "!getAuthorityDataList.json?P_objectId="
            + objectId + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
            + componentVersionId;
    loadGridData(authorityDataGrid, authorityDataGridData, authorityDataGridURL);
}
/**
 * 初始化编码权限列表
 */
function initAuthorityCodeWin(win, objectId, objectType, menuId, componentVersionId) {
    win.center();
    authorityCodeTypeLayout = win.attachLayout("2U");
    authorityCodeTypeTreeCell = authorityCodeTypeLayout.cells("a");
    authorityCodeTypeTreeCell.hideHeader();
    authorityCodeTypeTreeCell.setWidth("240");
    //编码类型树
    initCodeTypeTree(authorityCodeTypeTreeCell, objectId, objectType, menuId, componentVersionId);
    
    authorityCodeGridCell = authorityCodeTypeLayout.cells("b");
    authorityCodeGridCell.hideHeader();
    //初始化类型列表
    initCodeGrid(authorityCodeGridCell);
    authorityCodeGridToolbar = authorityCodeGridCell.attachToolbar();
    authorityCodeGridToolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    authorityCodeGridToolbar.addButton("save", 1, "保存", "save.gif");
    authorityCodeGridToolbar.disableItem("save");
    authorityCodeGridToolbar.attachEvent("onClick", function(id) {
        if (id == "save") {
        	//当前树节点Id(code_type_code)
        	var pCode = codeTree.getSelectedItemId();
        	var rowIds = "";
        	codeGrid.forEachRow(function(rowId) {
    			var enabled = codeGrid.cells(rowId, 1).getValue();
    			if ("0" == enabled) {
    				rowIds += "," + rowId;
    			} 
    		});
    		if (rowIds.length > 0) {
    			rowIds = rowIds.substring(1);
    		}
    		var url = AUTHORITY_CODE_URL + "!save.json?codeTypeCode=" + pCode 
    			+ "&P_codeIds=" + rowIds + "&objectId=" + objectId + "&objectType=" + objectType
    			+ "&menuId=" + menuId + "&componentVersionId=" + componentVersionId;
    		dhtmlxAjax.get(url, function(loader) {
    			var msg = eval("(" + loader.xmlDoc.responseText + ")");
				dhtmlx.message(msg);
    		});
        }
    });
}
/**
 * 复制到其他角色或用户
 */
function initDataCopyAuth(authwin, objectId, objectType, menuId, componentVersionId, authorityDataIds) {
    authwin.setText("权限树");
    authwin.center();
    var statusBar = authwin.attachStatusBar();
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "保存");
    toolBar1.addButton("close", 1, "关闭");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var roleUserIds = roleUserTree.getAllChecked();
            if (!roleUserIds) {
                dhtmlx.message("请先选角色再保存!");
                return;
            }
            // 判断选择用户类型、
            var roleUserIdArray = roleUserIds.split(",");
            var userIds = "";
            var roleIds = "";
            for (var i = 0; i < roleUserIdArray.length; i++) {
                if (roleUserIdArray[i].startWith("R")) {
                    roleIds += roleUserIdArray[i] + ",";
                } else if (roleUserIdArray[i].startWith("U")) {
                    userIds += roleUserIdArray[i] + ",";
                }
            }
            var url = AUTHORITY_DATA_URL + "!copyAuthorityData.json?P_userIds=" + userIds + "&P_roleIds=" + roleIds
                    + "&P_objectId=" + objectId + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId + "&P_authorityDataIds=" + authorityDataIds;
            dhtmlxAjax.get(url, function(loader) {
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
            authwin.close();
        }
    });
    roleUserTree = authwin.attachTree();
    roleUserTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    roleUserTree.attachEvent("onMouseIn", function(id) {
        roleUserTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    roleUserTree.attachEvent("onMouseOut", function(id) {
        roleUserTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
	var treeJson = {id:0, item:[
        {id:"-A", text: "数据权限", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[
            {id:'-R',text:"角色", child:1, type:"role"}, 
        	{id:'-U',text:"用户", child:1, type:"user"}
        ]}
    ]};
	roleUserTree.setDataMode("json");
    roleUserTree.enableSmartXMLParsing(true);
    roleUserTree.enableCheckBoxes(true, true);
    roleUserTree.enableThreeStateCheckboxes(true);
    roleUserTree.setXMLAutoLoading(AUTHORITY_URL + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type&P_systemId="+currentSystemId+"&P_objectId="+objectId+"&P_objectType="+objectType);
    roleUserTree.loadJSONObject(treeJson);
}
/**
 * 创建弹出窗口
 * @param config 配置信息，格式：{id:"", parentId:"", title:"", width:"", height:""}
 */
function createAuthorityWindow(config) {
    if (!window.authorityWins) {
        authorityWins = new dhtmlXWindows();
    }
    if (config.parentId) {
        authorityWins.enableAutoViewport(false);
        authorityWins.attachViewportTo(config.parentId);
    }
    authorityWins.setImagePath(IMAGE_PATH);
    var w = config.width ? config.width : 500;
    var h = config.height ? config.height : 450;
    var win = authorityWins.createWindow(config.id, 0, 0, w, h);
    win.setModal(true);
    win.button("park").hide();
    win.button("minmax1").hide();
    win.button("minmax2").hide();
    win.setText(config.title);
    win.center();
    return win;
}