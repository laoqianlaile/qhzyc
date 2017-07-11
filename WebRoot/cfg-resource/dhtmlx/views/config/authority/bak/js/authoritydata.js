/**
 * 初始化数据权限列表
 */
function initAuthorityDataWin(win, objectId, objectType, menuId, componentVersionId) {
	win.setText("数据权限维护");
    win.center();
    win.detachToolbar();
    var authorityDataGrid = win.attachGrid();
    var authorityDataGridData = {
        format : {
            headers : ["", "<center>名称</center>"],
            cols: ["id", "name"],
            colWidths : ["20", "260"],
            colTypes : ["ch", "ro"],
            colAligns : ["left", "left"]
        }
    };
    initGridWithoutPageable(authorityDataGrid, authorityDataGridData, null);
    loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId);

    var toolbar = win.attachToolbar();
    toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolbar.addButton("add", 1, "新增", "new.gif");
    toolbar.addButton("update", 2, "修改", "update.gif");
    toolbar.addButton("delete", 3, "删除", "delete.gif");
    toolbar.attachEvent("onClick", function(id) {
        if (id == "add") {
            var h = 450;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var newWin = createAuthorityWindow({
                id : "newAuthorityData",
                title : "新增数据权限",
                width : 800,
                height : h
            });
            // 如果是菜单的话，使用菜单ID
            if (componentVersionId == "-1") {
                initAuthorityDataDetailWin(newWin, menuId, "M_" + menuId, "", "");
            } else {
                initAuthorityDataDetailWin(newWin, menuId, componentVersionId, "", "");
            }
            authorityWins.attachEvent("onClose", function(win) {
            	if (win.idd == "newAuthorityData") {
                    loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId);
            	}
                return true;
            });
        } else if (id == "update") {
            var rowIds = authorityDataGrid.getCheckedRows(0);
            if (rowIds == "") {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (rowIds.indexOf(",") > 0) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            var name = authorityDataGrid.cellById(rowIds, 1).getValue();
            var h = 450;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var updateWin = createAuthorityWindow({
                id : "updateAuthorityData",
                title : "修改数据权限",
                width : 800,
                height : h
            });
            // 如果是菜单的话，使用菜单ID
            if (componentVersionId == "-1") {
                initAuthorityDataDetailWin(updateWin, menuId, "M_" + menuId, rowIds, name);
            } else {
                initAuthorityDataDetailWin(updateWin, menuId, componentVersionId, rowIds, name);
            }
            
        } else if (id == "delete") {
            var rowIds = authorityDataGrid.getCheckedRows(0);
            if (rowIds == "") {
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
                        var DELETE_URL = contextPath + "/authority/authority-data/" + rowIds + "?_method=delete";
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
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "保存");
    toolBar1.addButton("clear", 1, "清除");
    toolBar1.addButton("copyAuth", 2, "复制(其他角色或用户)");
    toolBar1.addButton("close", 3, "关闭");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var checkedIds = authorityDataGrid.getCheckedRows(0);
            if (checkedIds == "") {
                dhtmlx.message(getMessage("select_record"));
            } else if (checkedIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
            } else {
                var url = contextPath + "/authority/authority-data-relation!saveAuthorityDataRelation.json?P_objectId=" + objectId + "&P_objectType=" + objectType
                        + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId + "&P_authorityDataId=" + checkedIds;
                dhtmlxAjax.get(url, function(loader) {
                    var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof jsonObj == "string") {
                        jsonObj = eval("(" + jsonObj + ")");
                    }
                    if (jsonObj.success) {
                        dhtmlx.message(getMessage("save_success"));
                    } else {
                        dhtmlx.message(getMessage("save_failure"));
                    }
                });
            }
        } else if (id == "clear") {
            var url = contextPath + "/authority/authority-data-relation!clearAuthorityDataRelation.json?P_objectId=" + objectId + "&P_objectType=" + objectType
                    + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == "string") {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("delete_success"));
                    authorityDataGrid.uncheckAll();
                } else {
                    dhtmlx.message(getMessage("delete_failure"));
                }
            });
        } else if (id == "copyAuth") {
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
            initDataCopyAuth(authwin, objectId, objectType, menuId, componentVersionId);
        } else if (id == "close") {
            win.close();
        }
    });
}
/**
 * 加载数据权限列表
 */
function loadAuthorityDataGrid(authorityDataGrid, authorityDataGridData, objectId, objectType, menuId, componentVersionId) {
    if (!authorityDataGrid || !authorityDataGrid.clearAll) return;
    var authorityDataGridURL;
    // 如果是菜单的话，使用菜单ID
    if (componentVersionId == "-1") {
        authorityDataGridURL = contextPath
            + "/authority/authority-data!getAuthorityDataList.json?P_componentVersionId=" + "M_" + menuId;
    } else {
        authorityDataGridURL = contextPath
            + "/authority/authority-data!getAuthorityDataList.json?P_componentVersionId=" + componentVersionId;
    }
    loadGridData(authorityDataGrid, authorityDataGridData, authorityDataGridURL);
    authorityDataGrid.setCheckedRows(0, 0);

    // 勾上当前角色或用户拥有的权限
    var checkedUrl = contextPath + "/authority/authority-data-relation!getAuthorityDataRelation.json?P_objectId="
            + objectId + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId;
    var authorityDataRelation = loadJson(checkedUrl);
    if (authorityDataRelation && authorityDataRelation.authorityDataId) {
        var rowIndex = authorityDataGrid.getRowIndex(authorityDataRelation.authorityDataId);
        if (rowIndex > -1) {
            authorityDataGrid.cells(authorityDataRelation.authorityDataId, 0).setValue("1");
        }
    }
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
    		var url = contextPath + "/authority/authority-code!save.json?codeTypeCode=" + pCode 
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
function initDataCopyAuth(authwin, objectId, objectType, menuId, componentVersionId) {
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
            // 判断选择用户类型、
            if (roleUserIds.indexOf(",") > 0) {
                var roleUserIdArray = roleUserIds.split(",");
                var userIds = "";
                var roleIds = "";
                for (var i = 0; i < roleUserIdArray.length; i++) {
                    var pId = roleUserTree.getParentId(roleUserIdArray[i]);
                    if (pId == "-R") {
                        roleIds += roleUserIdArray[i] + ",";
                    } else if (pId == "-U") {
                        userIds += roleUserIdArray[i] + ",";
                    }
                }
            } else {
                var pId = roleUserTree.getParentId(roleUserIds);
                if (pId == "-R") {
                    roleIds = roleUserIds;
                } else if (pId == "-U") {
                    userIds = roleUserIds;
                }
            }
            if (roleUserIds == "") {
                dhtmlx.message("请先选角色或用户再保存!");
            } else {
                var url = contextPath + "/authority/authority-data-relation!copyAuthorityDataRelation.json?P_userIds=" + userIds + "&P_roleIds=" + roleIds
                        + "&P_objectId=" + objectId + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId;
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
            }
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
    TREE_URL = contextPath + "/authority/authority";
    roleUserTree.setXMLAutoLoading(TREE_URL + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type");
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